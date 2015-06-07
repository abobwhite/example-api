package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.SupplierWriteDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Spring-JDBC implementation of the Supplier database access operations.
 */
@Repository("supplierDao")
public class JdbcSupplierDao extends JdbcPartyDao implements SupplierReadDao, SupplierWriteDao {

    private static final String GENDER_COLUMN_NAME = "gender";
    private static final String TRANSLATED_DESCRIPTION_COLUMN_NAME = "translated_company_description";
    private static final String CERTIFICATION_ID_COLUMN_NAME = "certification_id";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert partyAuditInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        partyAuditInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("party_audit")
                .usingGeneratedKeyColumns(PARTY_AUDIT_ID_COLUMN_NAME)
                .usingColumns(PARTY_TYPE_COLUMN_NAME, ENGLISH_NAME_COLUMN_NAME, DESCRIPTION_COLUMN_NAME,
                        EMPLOYEES_COLUMN_NAME, WEBSITE_COLUMN_NAME, YEAR_ESTABLISHED_COLUMN_NAME,
                        ANNUAL_SALES_COLUMN_NAME, FIRST_NAME_COLUMN_NAME, LAST_NAME_COLUMN_NAME, SKYPE_COLUMN_NAME,
                        MSN_COLUMN_NAME, ICQ_COLUMN_NAME, EMAIL_COLUMN_NAME, COUNTRY_COLUMN_NAME,
                        LICENSE_LINK_COLUMN_NAME, LOGO_LINK_COLUMN_NAME, GENDER_COLUMN_NAME, VIDEO_LINK_COLUMN_NAME,
                        BUSINESS_PHONE_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Supplier loadLatest(Long id, Locale locale) {
        LOGGER.debug("Looking up latest supplier with ID " + id);
        String sql = getSql("supplier/loadLatest.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", id).addValue(
                SqlQueryCriteria.LANGUAGE_PARAMETER_NAME, locale.getLanguage());
        Supplier supplier = null;
        try {
            supplier = jdbcTemplate.query(sql, parameterSource, new SupplierResultSetExtractor()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new EmptyResultDataAccessException(1);
        }

        return supplier;
    }

    @Override
    public Supplier loadApproved(Long id, Locale locale) {
        LOGGER.debug("Looking up approved supplier with ID " + id);
        String sql = getSql("supplier/loadApproved.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", id).addValue(
                SqlQueryCriteria.LANGUAGE_PARAMETER_NAME, locale.getLanguage());
        Supplier supplier = null;
        try {
            supplier = jdbcTemplate.query(sql, parameterSource, new SupplierResultSetExtractor()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new EmptyResultDataAccessException(1);
        }

        return supplier;
    }

    @Override
    public List<Supplier> loadApprovedBySupplierIds(List<Long> ids, Locale locale) {
        List<Supplier> suppliers = Lists.newArrayList();
        HashSet<Long> uniqueIds = Sets.newHashSet(ids);
        LOGGER.debug("Getting Supplier from the database by supplier ids " + uniqueIds);
        for (List<Long> partitionedIds : Lists.partition(Lists.newArrayList(uniqueIds), 1000)) {
            String sql = getSql("/supplier/getApprovedBySupplierIds.sql");
            SqlParameterSource parameterSource = new MapSqlParameterSource("supplierIds", partitionedIds).addValue(
                    "language", locale.getLanguage());
            suppliers.addAll(jdbcTemplate.query(sql, parameterSource, new SupplierResultSetExtractor()));
        }

        return suppliers;
    }

    @Override
    public List<Supplier> loadLatestBySupplierIds(List<Long> ids, Locale locale) {
        List<Supplier> suppliers = Lists.newArrayList();

        HashSet<Long> uniqueIds = Sets.newHashSet(ids);
        LOGGER.debug("Getting Supplier from the database by supplier ids " + uniqueIds);
        for (List<Long> partitionedIds : Lists.partition(Lists.newArrayList(uniqueIds), 1000)) {
            String sql = getSql("/supplier/getLatestBySupplierIds.sql");
            SqlParameterSource parameterSource = new MapSqlParameterSource("supplierIds", partitionedIds).addValue(
                    "language", locale.getLanguage());
            suppliers.addAll(jdbcTemplate.query(sql, parameterSource, new SupplierResultSetExtractor()));
        }

        return suppliers;
    }

    @Override
    public String loadSupplierIdMatchingLegacyId(Long legacyId) {
        LOGGER.debug("Looking up product ID matching legacy id " + legacyId);
        String sql = getSql("supplier/load-id-matching-legacy-id.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("legacyId", legacyId);
        return jdbcTemplate.queryForObject(sql, parameterSource, String.class);
    };

    @Override
    public Supplier insert(Supplier supplier) {
        supplier.setPartyType(PartyType.SUPPLIER);

        createParty(supplier);
        createPartyAudit(supplier);
        createPartyAuditStatus(supplier);
        return supplier;
    }

    @Override
    public Supplier update(Supplier supplier) {
        supplier.setPartyType(PartyType.SUPPLIER);

        createPartyAuditForUpdate(supplier);
        createPartyAuditStatus(supplier);
        return supplier;
    }

    @Override
    public Supplier recordEvent(Supplier supplier) {
        createPartyAuditStatus(supplier);
        return supplier;
    }

    @Override
    public Supplier switchToSupplier(Supplier supplier) {
        supplier.getContact().setLanguage(Language.ENGLISH);

        // Update All Existing Approved Party Audit Status
        updateApprovedPartyAuditStatus(supplier);

        // Switch to a Supplier
        updateParty(supplier);
        update(supplier);

        return supplier;
    }

    private void createPartyAuditForUpdate(Supplier supplier) {
        LOGGER.info("Creating party audit for update of " + supplier);
        SqlParameterSource partyAuditUpdateParameterSource = new PartyAuditStatusSqlParameterSource(supplier).addValue(
                LICENSE_LINK_COLUMN_NAME, supplier.getExportLicenseRefId());

        executePartyAuditInsert(supplier, partyAuditUpdateParameterSource);
    }

    private void createPartyAudit(Supplier supplier) {
        LOGGER.info("Creating party audit for creation of " + supplier);
        SqlParameterSource partyAuditUpdateParameterSource = new PartyAuditStatusSqlParameterSource(supplier);

        executePartyAuditInsert(supplier, partyAuditUpdateParameterSource);
    }

    private void executePartyAuditInsert(Party party, SqlParameterSource partyAuditUpdateParameterSource) {
        Number partyAuditKey = partyAuditInsert.executeAndReturnKey(partyAuditUpdateParameterSource);
        party.setSnapshotId(partyAuditKey.longValue());
    }

    private final class SupplierResultSetExtractor implements ResultSetExtractor<List<Supplier>> {
        private SupplierRowMapper supplierRowMapper = new SupplierRowMapper();
        private MembershipRowMapper membershiprRowMapper = new MembershipRowMapper();

        @Override
        public List<Supplier> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Supplier> suppliers = new LinkedHashMap<Long, Supplier>();
            while (rs.next()) {
                Long supplierId = rs.getLong(PARTY_ID_COLUMN_NAME);
                Supplier supplier = suppliers.get(supplierId);
                if (supplier == null) {
                    supplier = supplierRowMapper.mapRow(rs, 0);
                    suppliers.put(supplierId, supplier);
                }

                Membership membership = membershiprRowMapper.mapRow(rs, 0, "membership_");
                if (membership != null) {
                    supplier.getMemberships().add(membership);
                }
            }
            return Lists.newArrayList(suppliers.values());
        }
    }

    private final class SupplierRowMapper implements RowMapper<Supplier> {
        private MembershipRowMapper membershiprRowMapper = new MembershipRowMapper();

        @Override
        public Supplier mapRow(ResultSet rs, int rowNum) throws SQLException {
            Membership provisionalMembership = null;

            provisionalMembership = membershiprRowMapper.mapRow(rs, rowNum, "provisional_");

            return new Supplier(rs.getLong(PARTY_ID_COLUMN_NAME), rs.getString(PUBLIC_PARTY_ID_COLUMN_NAME), mapContact(rs), mapCompany(rs),
                    ApprovalStatus.findByName(rs.getString(EVENT_TYPE_COLUMN_NAME)), rs.getInt(VERSION_COLUMN_NAME),
                    rs.getLong(PARTY_AUDIT_ID_COLUMN_NAME), rs.getString(LICENSE_LINK_COLUMN_NAME),
                    provisionalMembership, rs.getLong(CERTIFICATION_ID_COLUMN_NAME));
        }

        private Contact mapContact(ResultSet rs) throws SQLException {
            Gender gender = StringUtils.isBlank(rs.getString(GENDER_COLUMN_NAME)) ? null : Gender.findByReadableName(rs
                    .getString(GENDER_COLUMN_NAME));
            Language language = Language.findById(rs.getLong(LANGUAGE_ID_COLUMN_NAME));
            return new Contact(null, gender, rs.getString(FIRST_NAME_COLUMN_NAME), rs.getString(LAST_NAME_COLUMN_NAME),
                    rs.getString(COUNTRY_COLUMN_NAME), null, rs.getString(EMAIL_COLUMN_NAME),
                    rs.getString(SKYPE_COLUMN_NAME), rs.getString(MSN_COLUMN_NAME), rs.getString(ICQ_COLUMN_NAME),
                    rs.getString(BUSINESS_PHONE_COLUMN_NAME), rs.getString(IP_ADDRESS_COLUMN_NAME), language,
                    rs.getTimestamp(REGISTRATION_DATE_COLUMN_NAME));
        }

        private Company mapCompany(ResultSet rs) throws SQLException {
            return new Company(rs.getString(ENGLISH_NAME_COLUMN_NAME), null, getPossiblyTranslatedString(rs,
                    DESCRIPTION_COLUMN_NAME, TRANSLATED_DESCRIPTION_COLUMN_NAME), new ArrayList<BusinessType>(),
                    rs.getString(EMPLOYEES_COLUMN_NAME), rs.getString(WEBSITE_COLUMN_NAME), getNullSafeInteger(rs,
                            YEAR_ESTABLISHED_COLUMN_NAME), rs.getString(ANNUAL_SALES_COLUMN_NAME), null,
                    rs.getString(LOGO_LINK_COLUMN_NAME), rs.getString(VIDEO_LINK_COLUMN_NAME));
        }
    }

}
