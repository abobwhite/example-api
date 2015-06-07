package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;

/**
 * Maps Membership Domain Objects
 */
public final class MembershipRowMapper implements RowMapper<Membership> {
    @Override
    public Membership mapRow(ResultSet rs, int rowNum) throws SQLException {
        return mapRow(rs, rowNum, "");
    }

    public Membership mapRow(ResultSet rs, int rowNum, String prefix) throws SQLException {

        if (rs.getLong(prefix + JdbcMembershipDao.MEMBERSHIP_ID_COLUMN_NAME) > 0) {
            MembershipLevel level = new MembershipLevel(rs.getLong(prefix
                    + JdbcMembershipDao.MEMBERSHIP_LEVEL_ID_COLUMN_NAME), rs.getInt(prefix
                    + JdbcMembershipDao.MEMBERSHIP_LEVEL_COLUMN_NAME), rs.getBigDecimal(prefix
                    + JdbcMembershipDao.BASE_PRICE_COLUMN_NAME), rs.getInt(prefix
                    + JdbcMembershipDao.MONTHS_OF_TERM_COLUMN_NAME), rs.getInt(prefix
                    + JdbcMembershipDao.PRODUCT_COUNT_COLUMN_NAME), rs.getInt(prefix
                    + JdbcMembershipDao.MESSAGE_COUNT_COLUMN_NAME), rs.getInt(prefix
                    + JdbcMembershipDao.TRANSLATION_COUNT_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.PROFILE_PUBLIC_COLUMN_NAME), rs.getInt(prefix
                    + JdbcMembershipDao.HOT_PRODUCT_COUNT_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.PRODUCT_ALERT_INCLUDED_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.SUPPLIER_MESSAGING_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.EXPORT_TUTORIAL_COLUMN_NAME), rs.getInt(prefix
                    + JdbcMembershipDao.ADDITIONAL_IMAGES_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.THIRD_PARTY_VERIFIABLE_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.ADVANCED_WEB_EMAIL_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.VIDEO_UPLOADABLE_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.CONTACT_CHINA_DIRECT_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.MARKET_ANALYSIS_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.BUYER_SEARCH_COLUMN_NAME), rs.getBoolean(prefix
                    + JdbcMembershipDao.LOGISTICS_ASSISTANCE_COLUMN_NAME));

            Membership membership = new Membership(rs.getLong(prefix + JdbcMembershipDao.MEMBERSHIP_ID_COLUMN_NAME),
                    rs.getLong(prefix + JdbcMembershipDao.PARTY_ID_COLUMN_NAME), level, ApprovalStatus.findByName(rs
                            .getString(prefix + JdbcMembershipDao.EVENT_TYPE_COLUMN_NAME)), rs.getInt(prefix
                            + JdbcMembershipDao.VERSION_COLUMN_NAME), rs.getTimestamp(prefix
                            + JdbcMembershipDao.PURCHASE_DATE_COLUMN_NAME), rs.getTimestamp(prefix
                            + JdbcMembershipDao.EFFECTIVE_DATE_COLUMN_NAME), rs.getTimestamp(prefix
                            + JdbcMembershipDao.EXPIRATION_DATE_COLUMN_NAME), rs.getBigDecimal(prefix
                            + JdbcMembershipDao.PURCHASE_PRICE_COLUMN_NAME), rs.getBigDecimal(prefix
                            + JdbcMembershipDao.PAYMENT_AMOUNT_COLUMN_NAME), rs.getLong(prefix
                            + JdbcMembershipDao.MEMBERSHIP_AUDIT_ID_COLUMN_NAME), PaymentType.findByType(rs
                            .getString(prefix + JdbcMembershipDao.PAYMENT_TYPE_COLUMN_NAME)), rs.getString(prefix
                            + JdbcMembershipDao.PAYMENT_INVOICE_COLUMN_NAME), rs.getBigDecimal(prefix
                            + JdbcMembershipDao.RENEWAL_DISCOUNT_AMOUNT_COLUMN_NAME), rs.getBigDecimal(prefix
                            + JdbcMembershipDao.UPGRADE_CREDIT_AMOUNT_COLUMN_NAME));

            return membership;
        }

        return null;

    }
}
