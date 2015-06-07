package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.PartyReadDao;
import com.daugherty.e2c.persistence.data.PartyWriteDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Base class for DAOs dealing with the "party" database tables.
 */
@Repository("partyDao")
public class JdbcPartyDao extends SortAndPaginationJdbcDao implements PartyReadDao, PartyWriteDao {

    public static final String PARTY_TYPE_COLUMN_NAME = "party_type";
    public static final String REGISTRATION_DATE_COLUMN_NAME = "registration_date";
    public static final String EMAIL_COLUMN_NAME = "email_address";
    public static final String FIRST_NAME_COLUMN_NAME = "first_name";
    public static final String LAST_NAME_COLUMN_NAME = "last_name";
    public static final String COUNTRY_COLUMN_NAME = "country";
    public static final String DESCRIPTION_COLUMN_NAME = "company_description";
    public static final String EMPLOYEES_COLUMN_NAME = "number_of_employees";
    public static final String WEBSITE_COLUMN_NAME = "company_website";
    public static final String YEAR_ESTABLISHED_COLUMN_NAME = "year_established";
    public static final String ANNUAL_SALES_COLUMN_NAME = "total_annual_sales";
    public static final String SKYPE_COLUMN_NAME = "skype_id";
    public static final String MSN_COLUMN_NAME = "msn_id";
    public static final String ICQ_COLUMN_NAME = "icq_id";
    public static final String LOGO_LINK_COLUMN_NAME = "company_logo_link";
    public static final String VIDEO_LINK_COLUMN_NAME = "video_link";
    public static final String LICENSE_LINK_COLUMN_NAME = "license_link";
    public static final String BUSINESS_PHONE_COLUMN_NAME = "business_phone";
    public static final String IP_ADDRESS_COLUMN_NAME = "ip_address";
    public static final String LANGUAGE_ID_COLUMN_NAME = "language_id";
    public static final String USERNAME_COLUMN_NAME = "username";
    public static final String PROVINCE_COLUMN_NAME = "province";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected SimpleJdbcInsert partyInsert;
    protected SimpleJdbcInsert partyAuditStatusInsert;

    @Inject
    private Hashids hashids;

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(Party.PARTY_TYPE_SERIAL_PROPERTY, PARTY_TYPE_COLUMN_NAME);
        columnsByProperty.put("username", USERNAME_COLUMN_NAME);
        columnsByProperty.put(Party.EMAIL_SERIAL_PROPERTY, EMAIL_COLUMN_NAME);
        columnsByProperty.put(Party.FIRST_NAME_SERIAL_PROPERTY, FIRST_NAME_COLUMN_NAME);
        columnsByProperty.put(Party.LAST_NAME_SERIAL_PROPERTY, LAST_NAME_COLUMN_NAME);
    }

    @Override
    public QueryCriteria createQueryCriteria(String username, String partyType, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count, Locale locale) {
        return createSqlQueryCriteria(propertyName, sortDescending, startItem, count, locale).append(
                username == null ? null : "(ul.username like '" + username + "%' OR pu.username like '" + username
                        + "%')").appendEqualsSubClause(PARTY_TYPE_COLUMN_NAME, "partyType", partyType);
    }

    @Override
    public List<Party> getAll(QueryCriteria queryCriteria) {
        LOGGER.debug("Getting all Parties from the database");
        String sql = getSql("/party/get-all.sql") + queryCriteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, queryCriteria.getParameterMap(), new PartyRowMapper(true));
    }

    @Override
    public Party loadById(Long partyId) {
        LOGGER.debug("Looking up party by Id " + partyId);
        String sql = getSql("/party/get-all.sql") + " WHERE p.party_id = :partyId";
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", partyId);
        return jdbcTemplate.queryForObject(sql, parameterSource, new PartyRowMapper(true));
    }

    @Override
    public boolean hasBeenApprovedOnce(Long partyId) {
        LOGGER.debug("Looking up if party has been approved for party Id " + partyId);
        String sql = getSql("/party/hasBeenApprovedOnce.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", partyId);
        return jdbcTemplate.queryForObject(sql, parameterSource, new RowMapper<Boolean>() {

            @Override
            public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getBoolean(PARTY_WAS_APPROVED_ONCE_COLUMN_NAME);
            }

        });
    }

    @Override
    public Party loadByEmailAddress(String emailAddress) {
        LOGGER.debug("Looking up party with email address " + emailAddress);
        String sql = getSql("party/loadByEmailAddress.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("emailAddress", emailAddress);
        return jdbcTemplate.queryForObject(sql, parameterSource, new PartyRowMapper());
    }

    @Override
    public int deletePartyByPartyId(Long partyId) {
        LOGGER.info("Deleting party" + partyId);

        SqlParameterSource partyParameterSource = new MapSqlParameterSource().addValue("partyId", partyId);

        List<Long> partyAuditIds = jdbcTemplate.queryForList(getSql("/party/loadPartyAuditIdsByPartyId.sql"),
                partyParameterSource, Long.class);

        SqlParameterSource partyAuditParameterSource = new MapSqlParameterSource().addValue("partyAuditIds",
                partyAuditIds);
        int numberOfDeletes = 0;

        if (!partyAuditIds.isEmpty()) {
            jdbcTemplate.update(getSql("/party/deletePartyAuditStatusByPartyId.sql"), partyParameterSource);
            jdbcTemplate.update(getSql("/party/deletePartyAuditBusinessTypeByPartyAuditId.sql"),
                    partyAuditParameterSource);
            jdbcTemplate.update(getSql("/party/deletePartyAuditTranslationByPartyAuditId.sql"),
                    partyAuditParameterSource);
            jdbcTemplate.update(getSql("/party/deletePartyAuditByPartyAuditId.sql"), partyAuditParameterSource);

            numberOfDeletes = jdbcTemplate.update(getSql("/party/deleteByPartyId.sql"), partyParameterSource);
        }

        LOGGER.info("Deleted party" + partyId);

        return numberOfDeletes;
    }

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        partyInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("party")
                .usingGeneratedKeyColumns(PARTY_ID_COLUMN_NAME)
                .usingColumns(REGISTRATION_DATE_COLUMN_NAME, IP_ADDRESS_COLUMN_NAME, LANGUAGE_ID_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);

        partyAuditStatusInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARTY_AUDIT_STATUS_TABLE_NAME)
                .usingGeneratedKeyColumns(PARTY_AUDIT_STATUS_ID_COLUMN_NAME)
                .usingColumns(PARTY_ID_COLUMN_NAME, PARTY_AUDIT_ID_COLUMN_NAME, VERSION_COLUMN_NAME,
                        EVENT_TYPE_COLUMN_NAME, EVENT_TIME_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    protected void createParty(Party party) {
        LOGGER.info("Creating party for " + party);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(REGISTRATION_DATE_COLUMN_NAME, today())
                .addValue(IP_ADDRESS_COLUMN_NAME, party.getContact().getIpAddress())
                .addValue(LANGUAGE_ID_COLUMN_NAME, getLanguageId(party.getContact()));
        Number partyKey = partyInsert.executeAndReturnKey(parameterSource);
        party.setId(partyKey.longValue());
        party.setPublicId(hashids.encode(party.getId()));
        updateParty(party);
    }

    /**
     * Only Language can ever be changed on Party
     */
    protected Party updateParty(Party party) {
        LOGGER.info("Updating language for " + party);
        String sql = getSql("party/update.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(LANGUAGE_ID_COLUMN_NAME, getLanguageId(party.getContact()))
                .addValue(PUBLIC_PARTY_ID_COLUMN_NAME, party.getPublicId())
                .addValue(PARTY_ID_COLUMN_NAME, party.getId());

        jdbcTemplate.update(sql, parameterSource);

        return party;
    }

    private Long getLanguageId(Contact contact) {
        return contact.getLanguage() == null ? null : contact.getLanguage().getId();
    }

    protected void createPartyAuditStatus(Party party) {
        LOGGER.info("Creating party audit status for " + party);
        SqlParameterSource partyAuditStatusParameterSource = new AuditSqlParameterSource()
                .addValue(PARTY_ID_COLUMN_NAME, party.getId())
                .addValue(PARTY_AUDIT_ID_COLUMN_NAME, party.getSnapshotId())
                .addValue(VERSION_COLUMN_NAME, party.getVersion())
                .addValue(EVENT_TYPE_COLUMN_NAME, party.getApprovalStatus())
                .addValue(EVENT_TIME_COLUMN_NAME, new Date());
        partyAuditStatusInsert.execute(partyAuditStatusParameterSource);
    }

    /**
     * Only Language can ever be changed on Party
     */
    protected Party updateApprovedPartyAuditStatus(Party party) {
        LOGGER.info("Updating language for " + party);
        String sql = getSql("party/updateApprovedPartyAuditStatus.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PARTY_ID_COLUMN_NAME, party.getId());

        jdbcTemplate.update(sql, parameterSource);

        return party;
    }
}
