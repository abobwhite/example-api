package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.persistence.data.ProductMembershipReadDao;
import com.daugherty.e2c.persistence.data.ProductMembershipWriteDao;

@Repository
public class JdbcProductMembershipDao extends JdbcDao implements ProductMembershipReadDao, ProductMembershipWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String MEMBERSHIP_ID_COLUMN_NAME = "membership_id";
    private static final String MEMBERSHIP_LEVEL_ID_COLUMN_NAME = "membership_level_id";
    private static final String PURCHASE_DATE_COLUMN_NAME = "date_purchased";
    private static final String PURCHASE_PRICE_COLUMN_NAME = "price_at_purchase";
    private static final String EFFECTIVE_DATE_COLUMN_NAME = "effective_date";
    private static final String BASE_PRICE_COLUMN_NAME = "base_price";
    private static final String MESSAGE_COUNT_COLUMN_NAME = "number_of_messages";
    private static final String PROFILE_PUBLIC_COLUMN_NAME = "company_showroom";
    private static final String HOT_PRODUCT_COUNT_COLUMN_NAME = "hot_product_listing";
    private static final String PRODUCT_ALERT_INCLUDED_COLUMN_NAME = "product_alert";
    private static final String SUPPLIER_MESSAGING_COLUMN_NAME = "supplier_to_supplier_inquiry";
    private static final String EXPORT_TUTORIAL_COLUMN_NAME = "export_to_china_tutorial";
    private static final String ADDITIONAL_IMAGES_COLUMN_NAME = "enhance_profile";
    private static final String THIRD_PARTY_VERIFIABLE_COLUMN_NAME = "third_party_verification";
    private static final String ADVANCED_WEB_EMAIL_COLUMN_NAME = "website_and_advanced_email";
    private static final String VIDEO_UPLOADABLE_COLUMN_NAME = "video";
    private static final String CONTACT_CHINA_DIRECT_COLUMN_NAME = "contact_china_direct";
    private static final String MARKET_ANALYSIS_COLUMN_NAME = "market_analysis";
    private static final String BUYER_SEARCH_COLUMN_NAME = "buyer_search";
    private static final String LOGISTICS_ASSISTANCE_COLUMN_NAME = "logistics_assistance";
    private static final String PAYMENT_TYPE_COLUMN_NAME = "payment_type";
    private static final String PAYMENT_INVOICE_COLUMN_NAME = "payment_invoice";
    private static final String RENEWAL_DISCOUNT_AMOUNT_COLUMN_NAME = "renewal_discount_amount";
    private static final String UPGRADE_CREDIT_AMOUNT_COLUMN_NAME = "upgrade_credit_amount";

    private static final String MEMBERSHIP_AUDIT_ID_COLUMN_NAME = "membership_audit_id";
    private static final String PRODUCT_COUNT_COLUMN_NAME = "number_of_products";
    private static final String TRANSLATION_COUNT_COLUMN_NAME = "number_of_translations";
    private static final String EXPIRATION_DATE_COLUMN_NAME = "expiration_date";
    private static final String PAYMENT_AMOUNT_COLUMN_NAME = "price_paid";
    private static final String PRODUCT_ID_COLUMN_NAME = "product_id";
    private static final String PRODUCT_MEMBERSHIP_ID_COLUMN_NAME = "product_membership_id";

    private SimpleJdbcInsert productMembershipInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        productMembershipInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("product_membership")
                .usingGeneratedKeyColumns(PRODUCT_MEMBERSHIP_ID_COLUMN_NAME)
                .usingColumns(PRODUCT_ID_COLUMN_NAME, MEMBERSHIP_ID_COLUMN_NAME, EVENT_TIME_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Membership findMembershipByProductId(Long productId) {
        LOGGER.debug("Getting all Membership for Product");
        String sql = getSql("productmembership/loadByProductId.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("productId", productId);
        return jdbcTemplate.queryForObject(sql, parameterSource, new MembershipRowMapper());
    }

    @Override
    public void create(Long productId, Long membershipId) {
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("product_id", productId)
                .addValue("membership_id", membershipId).addValue("event_datetime", new Date());

        productMembershipInsert.execute(parameterSource);
    }

    private final class MembershipRowMapper implements RowMapper<Membership> {
        @Override
        public Membership mapRow(ResultSet rs, int rowNum) throws SQLException {
            MembershipLevel level = new MembershipLevel(rs.getLong(MEMBERSHIP_LEVEL_ID_COLUMN_NAME),
                    rs.getInt("level"), rs.getBigDecimal(BASE_PRICE_COLUMN_NAME), null,
                    rs.getInt(PRODUCT_COUNT_COLUMN_NAME), rs.getInt(MESSAGE_COUNT_COLUMN_NAME),
                    rs.getInt(TRANSLATION_COUNT_COLUMN_NAME), rs.getBoolean(PROFILE_PUBLIC_COLUMN_NAME),
                    rs.getInt(HOT_PRODUCT_COUNT_COLUMN_NAME), rs.getBoolean(PRODUCT_ALERT_INCLUDED_COLUMN_NAME),
                    rs.getBoolean(SUPPLIER_MESSAGING_COLUMN_NAME), rs.getBoolean(EXPORT_TUTORIAL_COLUMN_NAME),
                    rs.getInt(ADDITIONAL_IMAGES_COLUMN_NAME), rs.getBoolean(THIRD_PARTY_VERIFIABLE_COLUMN_NAME),
                    rs.getBoolean(ADVANCED_WEB_EMAIL_COLUMN_NAME), rs.getBoolean(VIDEO_UPLOADABLE_COLUMN_NAME),
                    rs.getBoolean(CONTACT_CHINA_DIRECT_COLUMN_NAME), rs.getBoolean(MARKET_ANALYSIS_COLUMN_NAME),
                    rs.getBoolean(BUYER_SEARCH_COLUMN_NAME),
                    rs.getBoolean(LOGISTICS_ASSISTANCE_COLUMN_NAME));

            Membership membership = new Membership(rs.getLong(MEMBERSHIP_ID_COLUMN_NAME),
                    rs.getLong(PARTY_ID_COLUMN_NAME), level, ApprovalStatus.findByName(rs
                            .getString(EVENT_TYPE_COLUMN_NAME)), rs.getInt(VERSION_COLUMN_NAME),
                    rs.getTimestamp(PURCHASE_DATE_COLUMN_NAME), rs.getTimestamp(EFFECTIVE_DATE_COLUMN_NAME),
                    rs.getTimestamp(EXPIRATION_DATE_COLUMN_NAME), rs.getBigDecimal(PURCHASE_PRICE_COLUMN_NAME),
                    rs.getBigDecimal(PAYMENT_AMOUNT_COLUMN_NAME), rs.getLong(MEMBERSHIP_AUDIT_ID_COLUMN_NAME),
                    PaymentType.findByType(rs.getString(PAYMENT_TYPE_COLUMN_NAME)),
                    rs.getString(PAYMENT_INVOICE_COLUMN_NAME), rs.getBigDecimal(RENEWAL_DISCOUNT_AMOUNT_COLUMN_NAME),
                    rs.getBigDecimal(UPGRADE_CREDIT_AMOUNT_COLUMN_NAME));

            return membership;
        }
    }
}
