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

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.BuyerWriteDao;

@Repository("buyerDao")
public class JdbcBuyerDao extends JdbcPartyDao implements BuyerReadDao, BuyerWriteDao {

    private static final String CHINESE_NAME_COLUMN_NAME = "company_name_chinese";
    private static final String TITLE_COLUMN_NAME = "title";
    private static final String BUSINESS_PHONE_COLUMN_NAME = "business_phone";
    private static final String IMPORTS_COLUMN_NAME = "total_imports_us_dollars";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert partyAuditInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        partyAuditInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("party_audit")
                .usingGeneratedKeyColumns(PARTY_AUDIT_ID_COLUMN_NAME)
                .usingColumns(PARTY_TYPE_COLUMN_NAME, ENGLISH_NAME_COLUMN_NAME, CHINESE_NAME_COLUMN_NAME,
                        DESCRIPTION_COLUMN_NAME, EMPLOYEES_COLUMN_NAME, WEBSITE_COLUMN_NAME,
                        YEAR_ESTABLISHED_COLUMN_NAME, ANNUAL_SALES_COLUMN_NAME, IMPORTS_COLUMN_NAME,
                        FIRST_NAME_COLUMN_NAME, LAST_NAME_COLUMN_NAME, BUSINESS_PHONE_COLUMN_NAME, SKYPE_COLUMN_NAME,
                        MSN_COLUMN_NAME, ICQ_COLUMN_NAME, EMAIL_COLUMN_NAME, PROVINCE_COLUMN_NAME, COUNTRY_COLUMN_NAME,
                        LICENSE_LINK_COLUMN_NAME, LOGO_LINK_COLUMN_NAME, TITLE_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Buyer loadLatest(Long id) {
        LOGGER.debug("Looking up buyer with ID " + id);
        String sql = getSql("buyer/loadLatest.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("partyId", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new BuyerRowMapper());
    }

    @Override
    public Buyer loadApproved(Long id) {
        LOGGER.debug("Looking up approved supplier with ID " + id);
        String sql = getSql("buyer/loadApproved.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new BuyerRowMapper());
    }

    @Override
    public Buyer insert(Buyer buyer) {
        buyer.setPartyType(PartyType.BUYER);

        createParty(buyer);
        createPartyAuditForInsert(buyer);
        createPartyAuditStatus(buyer);
        return buyer;
    }

    @Override
    public Buyer update(Buyer buyer) {
        buyer.setPartyType(PartyType.BUYER);

        createPartyAuditForUpdate(buyer);
        createPartyAuditStatus(buyer);
        return buyer;
    }

    @Override
    public Buyer recordEvent(Buyer buyer) {
        createPartyAuditStatus(buyer);
        return buyer;
    }

    @Override
    public Buyer switchToBuyer(Buyer buyer) {
        buyer.getContact().setLanguage(Language.CHINESE);

        // Update All Existing Approved Party Audit Status
        updateApprovedPartyAuditStatus(buyer);

        // Switch to a Buyer
        updateParty(buyer);
        update(buyer);

        return buyer;
    }

    private void createPartyAuditForInsert(Buyer buyer) {
        LOGGER.info("Creating party audit for creation of " + buyer);
        SqlParameterSource partyAuditUpdateParameterSource = new PartyAuditSqlParameterSource(buyer);

        executePartyAuditInsert(buyer, partyAuditUpdateParameterSource);
    }

    private void createPartyAuditForUpdate(Buyer buyer) {
        LOGGER.info("Creating party audit for update of " + buyer);
        SqlParameterSource partyAuditUpdateParameterSource = new PartyAuditSqlParameterSource(buyer).addValue(
                LICENSE_LINK_COLUMN_NAME, buyer.getImportLicenseRefId());

        executePartyAuditInsert(buyer, partyAuditUpdateParameterSource);
    }

    private void executePartyAuditInsert(Party party, SqlParameterSource partyAuditUpdateParameterSource) {
        Number partyAuditKey = partyAuditInsert.executeAndReturnKey(partyAuditUpdateParameterSource);
        party.setSnapshotId(partyAuditKey.longValue());
    }

    private class BuyerRowMapper implements RowMapper<Buyer> {
        @Override
        public Buyer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Buyer(rs.getLong(PARTY_ID_COLUMN_NAME), rs.getString(PUBLIC_PARTY_ID_COLUMN_NAME), mapContact(rs), mapCompany(rs),
                    ApprovalStatus.findByName(rs.getString(EVENT_TYPE_COLUMN_NAME)), rs.getInt(VERSION_COLUMN_NAME),
                    rs.getLong(PARTY_AUDIT_ID_COLUMN_NAME), rs.getString(LICENSE_LINK_COLUMN_NAME));
        }

        private Contact mapContact(ResultSet rs) throws SQLException {
            return new Contact(rs.getString(TITLE_COLUMN_NAME), null, rs.getString(FIRST_NAME_COLUMN_NAME),
                    rs.getString(LAST_NAME_COLUMN_NAME), rs.getString(COUNTRY_COLUMN_NAME),
                    rs.getString(PROVINCE_COLUMN_NAME), rs.getString(EMAIL_COLUMN_NAME),
                    rs.getString(SKYPE_COLUMN_NAME), rs.getString(MSN_COLUMN_NAME), rs.getString(ICQ_COLUMN_NAME),
                    rs.getString(BUSINESS_PHONE_COLUMN_NAME), rs.getString(IP_ADDRESS_COLUMN_NAME), Language.CHINESE,
                    rs.getTimestamp(REGISTRATION_DATE_COLUMN_NAME));
        }

        private Company mapCompany(ResultSet rs) throws SQLException {
            return new Company(rs.getString(ENGLISH_NAME_COLUMN_NAME), rs.getString(CHINESE_NAME_COLUMN_NAME),
                    rs.getString(DESCRIPTION_COLUMN_NAME), new ArrayList<BusinessType>(),
                    rs.getString(EMPLOYEES_COLUMN_NAME), rs.getString(WEBSITE_COLUMN_NAME), getNullSafeInteger(rs,
                            YEAR_ESTABLISHED_COLUMN_NAME), rs.getString(ANNUAL_SALES_COLUMN_NAME),
                    rs.getString(IMPORTS_COLUMN_NAME), rs.getString(LOGO_LINK_COLUMN_NAME), null);
        }
    }
}
