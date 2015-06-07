package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;
import com.daugherty.e2c.persistence.data.MembershipLevelWriteDao;

/**
 * Spring JDBC implementation of MembershipLevel database operations.
 */
@Repository
public class JdbcMembershipLevelDao extends JdbcDao implements MembershipLevelReadDao, MembershipLevelWriteDao {

    private static final String MEMBERSHIP_LEVEL_ID_COLUMN_NAME = "membership_level_id";
    private static final String MEMBERSHIP_TIER_ID_COLUMN_NAME = "membership_tier_id";
    private static final String LEVEL_COLUMN_NAME = "level";
    private static final String BASE_PRICE_COLUMN_NAME = "base_price";
    private static final String TERM_COLUMN_NAME = "term";
    private static final String PRODUCT_COUNT_COLUMN_NAME = "number_of_product";
    private static final String TRANSLATION_COUNT_COLUMN_NAME = "number_of_translations";
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
    private static final String EFFECTIVE_DATE_COLUMN_NAME = "effective_date";
    private static final String EXPIRATION_DATE_COLUMN_NAME = "expiration_date";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert membershipLevelInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        membershipLevelInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("membership_level")
                .usingGeneratedKeyColumns(MEMBERSHIP_LEVEL_ID_COLUMN_NAME)
                .usingColumns(MEMBERSHIP_TIER_ID_COLUMN_NAME, TERM_COLUMN_NAME, BASE_PRICE_COLUMN_NAME,
                        PRODUCT_COUNT_COLUMN_NAME, MESSAGE_COUNT_COLUMN_NAME, TRANSLATION_COUNT_COLUMN_NAME,
                        PROFILE_PUBLIC_COLUMN_NAME, HOT_PRODUCT_COUNT_COLUMN_NAME, PRODUCT_ALERT_INCLUDED_COLUMN_NAME,
                        SUPPLIER_MESSAGING_COLUMN_NAME, EXPORT_TUTORIAL_COLUMN_NAME, ADDITIONAL_IMAGES_COLUMN_NAME,
                        THIRD_PARTY_VERIFIABLE_COLUMN_NAME, ADVANCED_WEB_EMAIL_COLUMN_NAME,
                        VIDEO_UPLOADABLE_COLUMN_NAME, CONTACT_CHINA_DIRECT_COLUMN_NAME, MARKET_ANALYSIS_COLUMN_NAME,
                        BUYER_SEARCH_COLUMN_NAME, LOGISTICS_ASSISTANCE_COLUMN_NAME,
                        EFFECTIVE_DATE_COLUMN_NAME, EXPIRATION_DATE_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public MembershipLevel loadByValue(long value) {
        LOGGER.debug("Looking up Membership Level with level " + value);
        String sql = getSql("membershiplevel/load-by-value.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource(LEVEL_COLUMN_NAME, value);

        return jdbcTemplate.queryForObject(sql, parameterSource, new MembershipLevelRowMapper());
    }

    @Override
    public List<MembershipLevel> loadAll() {
        LOGGER.debug("Looking up all Membership Levels");
        String sql = getSql("membershiplevel/loadAll.sql");

        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new MembershipLevelRowMapper());
    }

    @Override
    public void expireMembershipLevel(Integer value) {
        LOGGER.debug("Expiring with Membership with level " + value);

        String sql = getSql("membershiplevel/expire-by-value.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue(LEVEL_COLUMN_NAME, value);

        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public MembershipLevel create(MembershipLevel membershipLevel) {
        LOGGER.info("Creating Membership Level " + membershipLevel);

        Calendar cal = Calendar.getInstance();
        cal.set(9999, Calendar.DECEMBER, 31);
        Date expirationDate = cal.getTime();

        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(MEMBERSHIP_TIER_ID_COLUMN_NAME, membershipLevel.getValue())
                .addValue(TERM_COLUMN_NAME, membershipLevel.getMonthsOfTerm())
                .addValue(BASE_PRICE_COLUMN_NAME, membershipLevel.getPrice())
                .addValue(PRODUCT_COUNT_COLUMN_NAME, membershipLevel.getProductCount())
                .addValue(MESSAGE_COUNT_COLUMN_NAME, membershipLevel.getMessageCount())
                .addValue(TRANSLATION_COUNT_COLUMN_NAME, membershipLevel.getTranslationCount())
                .addValue(PROFILE_PUBLIC_COLUMN_NAME, membershipLevel.isProfilePublic())
                .addValue(HOT_PRODUCT_COUNT_COLUMN_NAME, membershipLevel.getHotProductCount())
                .addValue(PRODUCT_ALERT_INCLUDED_COLUMN_NAME, membershipLevel.isIncludedInProductAlerts())
                .addValue(SUPPLIER_MESSAGING_COLUMN_NAME, membershipLevel.isSupplierMessagingEnabled())
                .addValue(EXPORT_TUTORIAL_COLUMN_NAME, membershipLevel.isExportTutorialAccessible())
                .addValue(ADDITIONAL_IMAGES_COLUMN_NAME, membershipLevel.getAdditionalProductImageCount())
                .addValue(THIRD_PARTY_VERIFIABLE_COLUMN_NAME, membershipLevel.isVerifiableByThirdParty())
                .addValue(ADVANCED_WEB_EMAIL_COLUMN_NAME, membershipLevel.isAdvancedWebAndMailCapabilityEnabled())
                .addValue(VIDEO_UPLOADABLE_COLUMN_NAME, membershipLevel.isVideoUploadable())
                .addValue(CONTACT_CHINA_DIRECT_COLUMN_NAME, membershipLevel.isContactChinaDirect())
                .addValue(MARKET_ANALYSIS_COLUMN_NAME, membershipLevel.isMarketAnalysis())
                .addValue(BUYER_SEARCH_COLUMN_NAME, membershipLevel.isBuyerSearch())
                .addValue(LOGISTICS_ASSISTANCE_COLUMN_NAME, membershipLevel.isLogisticsAssistance())
                .addValue(EFFECTIVE_DATE_COLUMN_NAME, new Date()).addValue(EXPIRATION_DATE_COLUMN_NAME, expirationDate);

        Number key = membershipLevelInsert.executeAndReturnKey(parameterSource);
        membershipLevel.setId(key.longValue());

        return membershipLevel;
    }

    private final class MembershipLevelRowMapper implements RowMapper<MembershipLevel> {
        @Override
        public MembershipLevel mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MembershipLevel(rs.getLong(MEMBERSHIP_LEVEL_ID_COLUMN_NAME), rs.getInt(LEVEL_COLUMN_NAME),
                    rs.getBigDecimal(BASE_PRICE_COLUMN_NAME), rs.getInt(TERM_COLUMN_NAME),
                    rs.getInt(PRODUCT_COUNT_COLUMN_NAME), rs.getInt(MESSAGE_COUNT_COLUMN_NAME),
                    rs.getInt(TRANSLATION_COUNT_COLUMN_NAME), rs.getBoolean(PROFILE_PUBLIC_COLUMN_NAME),
                    rs.getInt(HOT_PRODUCT_COUNT_COLUMN_NAME), rs.getBoolean(PRODUCT_ALERT_INCLUDED_COLUMN_NAME),
                    rs.getBoolean(SUPPLIER_MESSAGING_COLUMN_NAME), rs.getBoolean(EXPORT_TUTORIAL_COLUMN_NAME),
                    rs.getInt(ADDITIONAL_IMAGES_COLUMN_NAME), rs.getBoolean(THIRD_PARTY_VERIFIABLE_COLUMN_NAME),
                    rs.getBoolean(ADVANCED_WEB_EMAIL_COLUMN_NAME), rs.getBoolean(VIDEO_UPLOADABLE_COLUMN_NAME),
                    rs.getBoolean(CONTACT_CHINA_DIRECT_COLUMN_NAME), rs.getBoolean(MARKET_ANALYSIS_COLUMN_NAME),
                    rs.getBoolean(BUYER_SEARCH_COLUMN_NAME),
                    rs.getBoolean(LOGISTICS_ASSISTANCE_COLUMN_NAME));
        }
    }
}
