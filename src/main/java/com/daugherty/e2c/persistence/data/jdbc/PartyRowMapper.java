package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;

/**
 * Map Result Sets to Party Domain Objects
 */
public class PartyRowMapper implements RowMapper<Party> {
    boolean includeUsername = false;

    public PartyRowMapper() {
        this.includeUsername = false;
    }

    public PartyRowMapper(boolean includeUsername) {
        this.includeUsername = includeUsername;
    }

    @Override
    public Party mapRow(ResultSet rs, int rowNum) throws SQLException {
        return this.mapRow(rs, "");
    }

    public Party mapRow(ResultSet rs, String prefix) throws SQLException {
        Party party = new Party(rs.getLong(prefix + JdbcPartyDao.PARTY_ID_COLUMN_NAME), rs.getString(prefix
                + JdbcPartyDao.PUBLIC_PARTY_ID_COLUMN_NAME), mapContact(rs, prefix), mapCompany(rs, prefix),
                ApprovalStatus.findByName(rs.getString(prefix + JdbcPartyDao.EVENT_TYPE_COLUMN_NAME)),
                PartyType.findByType(rs.getString(prefix + JdbcPartyDao.PARTY_TYPE_COLUMN_NAME)), rs.getInt(prefix
                        + JdbcPartyDao.VERSION_COLUMN_NAME), rs.getLong(prefix
                        + JdbcPartyDao.PARTY_AUDIT_ID_COLUMN_NAME));
        if (includeUsername) {
            party.setUsername(rs.getString(prefix + JdbcPartyDao.USERNAME_COLUMN_NAME));
        }

        return party;
    }

    private Contact mapContact(ResultSet rs, String prefix) throws SQLException {
        return new Contact(null, null, rs.getString(prefix + JdbcPartyDao.FIRST_NAME_COLUMN_NAME), rs.getString(prefix
                + JdbcPartyDao.LAST_NAME_COLUMN_NAME), rs.getString(prefix + JdbcPartyDao.COUNTRY_COLUMN_NAME),
                rs.getString(prefix + JdbcPartyDao.PROVINCE_COLUMN_NAME), rs.getString(prefix
                        + JdbcPartyDao.EMAIL_COLUMN_NAME), rs.getString(prefix + JdbcPartyDao.SKYPE_COLUMN_NAME),
                rs.getString(prefix + JdbcPartyDao.MSN_COLUMN_NAME),
                rs.getString(prefix + JdbcPartyDao.ICQ_COLUMN_NAME), rs.getString(prefix
                        + JdbcPartyDao.BUSINESS_PHONE_COLUMN_NAME), rs.getString(prefix
                        + JdbcPartyDao.IP_ADDRESS_COLUMN_NAME), Language.findById(rs.getLong(prefix
                        + JdbcPartyDao.LANGUAGE_ID_COLUMN_NAME)), rs.getTimestamp(prefix
                        + JdbcPartyDao.REGISTRATION_DATE_COLUMN_NAME));
    }

    private Company mapCompany(ResultSet rs, String prefix) throws SQLException {
        return new Company(rs.getString(prefix + JdbcPartyDao.ENGLISH_NAME_COLUMN_NAME), null, rs.getString(prefix
                + JdbcPartyDao.DESCRIPTION_COLUMN_NAME), new ArrayList<BusinessType>(), rs.getString(prefix
                + JdbcPartyDao.EMPLOYEES_COLUMN_NAME), rs.getString(prefix + JdbcPartyDao.WEBSITE_COLUMN_NAME),
                getNullSafeInteger(rs, prefix + JdbcPartyDao.YEAR_ESTABLISHED_COLUMN_NAME), rs.getString(prefix
                        + JdbcPartyDao.ANNUAL_SALES_COLUMN_NAME), null, rs.getString(prefix
                        + JdbcPartyDao.LOGO_LINK_COLUMN_NAME), null);
    }

    private Integer getNullSafeInteger(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }
}
