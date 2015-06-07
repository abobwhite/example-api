package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.UserMembership;
import com.daugherty.e2c.persistence.data.UserMembershipReadDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Created with IntelliJ IDEA. User: SHK0723 Date: 1/2/14 Time: 1:53 PM To change this template use File | Settings |
 * File Templates.
 */
@Repository
public class JdbcUserMembershipDao extends SortAndPaginationJdbcDao implements UserMembershipReadDao {
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

    private static final String USER_NAME = "username";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL_ADDRESS = "email_address";
    private static final String COMPANY_NAME_ENGLISH = "company_name_english";
    private static final String USER_EVENT_TYPE = "user_event_type";
    private static final String MEMBERSHIP_EVENT_TYPE = "membership_event_type";
    private static final String MEMBERSHIP_LEVEL = "level";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public UserMembership load(Long id) {
        LOGGER.debug("Looking up membership with ID " + id);
        String sql = getSql("/userMembership/get-all.sql") + " AND p.membership_id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new UserMembershipRowMapper());
    }

    @Override
    public List<UserMembership> find(QueryCriteria criteria) {
        LOGGER.debug("Getting all provisional memberships ");
        String sql = getSql("/userMembership/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new UserMembershipRowMapper());
    }

    @Override
    public QueryCriteria createQueryCriteria(String usernamePrefix, String emailPrefix, String companyNamePrefix,
            Integer membershipLevel, String sortByPropertyName, Boolean sortDescending, Integer startItem, Integer count) {
        SqlQueryCriteria criteria = createSqlQueryCriteria(sortByPropertyName, sortDescending, startItem, count, null)
                .appendLikeSubClause(USER_NAME, "usernamePrefix", usernamePrefix)
                .appendLikeSubClause(EMAIL_ADDRESS, "emailPrefix", emailPrefix)
                .appendLikeSubClause(COMPANY_NAME_ENGLISH, "companyNamePrefix", companyNamePrefix)
                .appendEqualsSubClause(MEMBERSHIP_LEVEL, "membershipLevel", membershipLevel);
        return criteria;
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(UserMembership.USERNAME_SERIAL_PROPERTY, USER_NAME);
        columnsByProperty.put(UserMembership.FIRST_NAME_SERIAL_PROPERTY, FIRST_NAME);
        columnsByProperty.put(UserMembership.LAST_NAME_SERIAL_PROPERTY, LAST_NAME);
        columnsByProperty.put(UserMembership.EMAIL_SERIAL_PROPERTY, EMAIL_ADDRESS);
        columnsByProperty.put(UserMembership.COMPANY_NAME_SERIAL_PROPERTY, COMPANY_NAME_ENGLISH);
        columnsByProperty.put(UserMembership.MEMBERSHIP_LEVEL_SERIAL_PROPERTY, MEMBERSHIP_LEVEL);
        columnsByProperty.put(UserMembership.EFFECTIVE_DATE_SERIAL_PROPERTY, EFFECTIVE_DATE_COLUMN_NAME);
        columnsByProperty.put(UserMembership.EXPIRATION_DATE_SERIAL_PROPERTY, EXPIRATION_DATE_COLUMN_NAME);
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, false);
    }

    private final class UserMembershipRowMapper implements RowMapper<UserMembership> {
        @Override
        public UserMembership mapRow(ResultSet rs, int rowNum) throws SQLException {
            MembershipLevel level = new MembershipLevel(rs.getLong(MEMBERSHIP_LEVEL_ID_COLUMN_NAME),
                    rs.getInt(MEMBERSHIP_LEVEL), rs.getBigDecimal(BASE_PRICE_COLUMN_NAME), null,
                    rs.getInt(PRODUCT_COUNT_COLUMN_NAME), rs.getInt(MESSAGE_COUNT_COLUMN_NAME),
                    rs.getInt(TRANSLATION_COUNT_COLUMN_NAME), rs.getBoolean(PROFILE_PUBLIC_COLUMN_NAME),
                    rs.getInt(HOT_PRODUCT_COUNT_COLUMN_NAME), rs.getBoolean(PRODUCT_ALERT_INCLUDED_COLUMN_NAME),
                    rs.getBoolean(SUPPLIER_MESSAGING_COLUMN_NAME), rs.getBoolean(EXPORT_TUTORIAL_COLUMN_NAME),
                    rs.getInt(ADDITIONAL_IMAGES_COLUMN_NAME), rs.getBoolean(THIRD_PARTY_VERIFIABLE_COLUMN_NAME),
                    rs.getBoolean(ADVANCED_WEB_EMAIL_COLUMN_NAME), rs.getBoolean(VIDEO_UPLOADABLE_COLUMN_NAME),
                    rs.getBoolean(CONTACT_CHINA_DIRECT_COLUMN_NAME), rs.getBoolean(MARKET_ANALYSIS_COLUMN_NAME),
                    rs.getBoolean(BUYER_SEARCH_COLUMN_NAME), 
                    rs.getBoolean(LOGISTICS_ASSISTANCE_COLUMN_NAME));

            UserMembership userMembership = new UserMembership(rs.getLong(MEMBERSHIP_ID_COLUMN_NAME),
                    rs.getLong(PARTY_ID_COLUMN_NAME), level, getStatus(rs.getString(MEMBERSHIP_EVENT_TYPE)),
                    rs.getInt(VERSION_COLUMN_NAME), rs.getTimestamp(PURCHASE_DATE_COLUMN_NAME),
                    rs.getTimestamp(EFFECTIVE_DATE_COLUMN_NAME), rs.getTimestamp(EXPIRATION_DATE_COLUMN_NAME),
                    rs.getBigDecimal(PURCHASE_PRICE_COLUMN_NAME), rs.getBigDecimal(PAYMENT_AMOUNT_COLUMN_NAME),
                    rs.getLong(MEMBERSHIP_AUDIT_ID_COLUMN_NAME), PaymentType.findByType(rs
                            .getString(PAYMENT_TYPE_COLUMN_NAME)), rs.getString(PAYMENT_INVOICE_COLUMN_NAME),
                    rs.getBigDecimal(RENEWAL_DISCOUNT_AMOUNT_COLUMN_NAME),
                    rs.getBigDecimal(UPGRADE_CREDIT_AMOUNT_COLUMN_NAME), rs.getString(USER_NAME),
                    rs.getString(FIRST_NAME), rs.getString(LAST_NAME), rs.getString(EMAIL_ADDRESS),
                    rs.getString(COMPANY_NAME_ENGLISH), getStatus(rs.getString(USER_EVENT_TYPE)));

            return userMembership;
        }

        private ApprovalStatus getStatus(String status) {
            return StringUtils.isNotEmpty(status) ? ApprovalStatus.findByName(status) : null;
        }
    }

}
