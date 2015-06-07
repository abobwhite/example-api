package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.persistence.data.AnonymousReadDao;
import com.daugherty.e2c.persistence.data.AnonymousWriteDao;

@Repository("anonymousDao")
public class JdbcAnonymousDao extends JdbcPartyDao implements AnonymousReadDao, AnonymousWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert partyAuditInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        partyAuditInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("party_audit")
                .usingGeneratedKeyColumns(PARTY_AUDIT_ID_COLUMN_NAME)
                .usingColumns(PARTY_TYPE_COLUMN_NAME, ENGLISH_NAME_COLUMN_NAME, WEBSITE_COLUMN_NAME,
                        FIRST_NAME_COLUMN_NAME, LAST_NAME_COLUMN_NAME, BUSINESS_PHONE_COLUMN_NAME, EMAIL_COLUMN_NAME,
                        COUNTRY_COLUMN_NAME, PROVINCE_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Anonymous load(Long id) {
        LOGGER.debug("Looking up anonymous with ID " + id);
        String sql = getSql("anonymous/loadLatest.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("partyId", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new AnonymousRowMapper());
    }

    @Override
    public Anonymous insert(Anonymous anonymous) {
        anonymous.setPartyType(PartyType.ANONYMOUS);

        createParty(anonymous);
        createPartyAuditForInsert(anonymous);
        createPartyAuditStatus(anonymous);
        return anonymous;
    }

    @Override
    public Anonymous update(Anonymous anonymous) {
        anonymous.setPartyType(PartyType.ANONYMOUS);

        createPartyAuditForUpdate(anonymous);
        createPartyAuditStatus(anonymous);
        return anonymous;
    }

    private void createPartyAuditForInsert(Anonymous anonymous) {
        LOGGER.info("Creating party audit for creation of " + anonymous);
        SqlParameterSource partyAuditUpdateParameterSource = new PartyAuditSqlParameterSource(anonymous);

        executePartyAuditInsert(anonymous, partyAuditUpdateParameterSource);
    }

    private void createPartyAuditForUpdate(Anonymous anonymous) {
        LOGGER.info("Creating party audit for update of " + anonymous);
        SqlParameterSource partyAuditUpdateParameterSource = new PartyAuditSqlParameterSource(anonymous);
        executePartyAuditInsert(anonymous, partyAuditUpdateParameterSource);
    }

    private void executePartyAuditInsert(Party party, SqlParameterSource partyAuditUpdateParameterSource) {
        Number partyAuditKey = partyAuditInsert.executeAndReturnKey(partyAuditUpdateParameterSource);
        party.setSnapshotId(partyAuditKey.longValue());
    }

    private class AnonymousRowMapper implements RowMapper<Anonymous> {
        @Override
        public Anonymous mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Anonymous(rs.getLong(PARTY_ID_COLUMN_NAME), rs.getString(PUBLIC_PARTY_ID_COLUMN_NAME), mapContact(rs), mapCompany(rs),
                    ApprovalStatus.findByName(rs.getString(EVENT_TYPE_COLUMN_NAME)), rs.getInt(VERSION_COLUMN_NAME),
                    rs.getLong(PARTY_AUDIT_ID_COLUMN_NAME));
        }

        private Contact mapContact(ResultSet rs) throws SQLException {
            Language language = Language.findById(rs.getLong(LANGUAGE_ID_COLUMN_NAME));
            return new Contact(null, null, rs.getString(FIRST_NAME_COLUMN_NAME), rs.getString(LAST_NAME_COLUMN_NAME),
                    rs.getString(COUNTRY_COLUMN_NAME), rs.getString(PROVINCE_COLUMN_NAME), rs.getString(EMAIL_COLUMN_NAME), null, null, null,
                    rs.getString(BUSINESS_PHONE_COLUMN_NAME), rs.getString(IP_ADDRESS_COLUMN_NAME), language,
                    rs.getTimestamp(REGISTRATION_DATE_COLUMN_NAME));
        }

        private Company mapCompany(ResultSet rs) throws SQLException {
            return new Company(rs.getString(ENGLISH_NAME_COLUMN_NAME), null, null, new ArrayList<BusinessType>(), null,
                    rs.getString(WEBSITE_COLUMN_NAME), null, null, null, null, null);
        }
    }
}
