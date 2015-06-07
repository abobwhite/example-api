package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.RowMapper;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.domain.Language;

/**
 * Map Result Sets to Employee Domain Objects
 */
public class EmployeeRowMapper implements RowMapper<Employee> {
    static final String CONFIRMATION_TOKEN_REGEX = "\\-?\\d{10}";

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(rs.getLong(JdbcPartyDao.PARTY_ID_COLUMN_NAME),
                rs.getString(JdbcPartyDao.PUBLIC_PARTY_ID_COLUMN_NAME), mapContact(rs), ApprovalStatus.findByName(rs
                        .getString(JdbcPartyDao.EVENT_TYPE_COLUMN_NAME)),
                rs.getString(JdbcUserDao.USERNAME_COLUMN_NAME), rs.getBoolean(JdbcUserDao.LOCKED_COLUMN_NAME),
                isBlocked(rs.getBoolean(JdbcUserDao.ENABLED_COLUMN_NAME),
                        rs.getString(JdbcUserDao.PASSWORD_COLUMN_NAME)), rs.getLong(JdbcUserDao.USER_ID_COLUMN_NAME),
                new ArrayList<String>(), rs.getInt(JdbcPartyDao.VERSION_COLUMN_NAME),
                rs.getLong(JdbcPartyDao.PARTY_AUDIT_ID_COLUMN_NAME));
    }

    private Contact mapContact(ResultSet rs) throws SQLException {
        return new Contact(null, null, rs.getString(JdbcPartyDao.FIRST_NAME_COLUMN_NAME),
                rs.getString(JdbcPartyDao.LAST_NAME_COLUMN_NAME), rs.getString(JdbcPartyDao.COUNTRY_COLUMN_NAME),
                rs.getString(JdbcPartyDao.PROVINCE_COLUMN_NAME), rs.getString(JdbcPartyDao.EMAIL_COLUMN_NAME),
                rs.getString(JdbcPartyDao.SKYPE_COLUMN_NAME), rs.getString(JdbcPartyDao.MSN_COLUMN_NAME),
                rs.getString(JdbcPartyDao.ICQ_COLUMN_NAME), rs.getString(JdbcPartyDao.BUSINESS_PHONE_COLUMN_NAME),
                rs.getString(JdbcPartyDao.IP_ADDRESS_COLUMN_NAME), Language.findById(rs
                        .getLong(JdbcPartyDao.LANGUAGE_ID_COLUMN_NAME)),
                rs.getTimestamp(JdbcPartyDao.REGISTRATION_DATE_COLUMN_NAME));
    }

    private boolean isBlocked(boolean enabled, String password) {
        return !enabled && !Pattern.matches(CONFIRMATION_TOKEN_REGEX, password);
    }
}
