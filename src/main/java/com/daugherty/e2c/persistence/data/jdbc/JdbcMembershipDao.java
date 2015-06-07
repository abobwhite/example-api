package com.daugherty.e2c.persistence.data.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.persistence.data.MembershipReadDao;
import com.daugherty.e2c.persistence.data.MembershipWriteDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Spring JDBC implementation of Membership database operations.
 */
@Repository
public class JdbcMembershipDao extends SortAndPaginationJdbcDao implements MembershipReadDao, MembershipWriteDao {
    public static final String MEMBERSHIP_ID_COLUMN_NAME = "membership_id";
    public static final String MEMBERSHIP_LEVEL_ID_COLUMN_NAME = "membership_level_id";
    public static final String MEMBERSHIP_LEVEL_COLUMN_NAME = "level";
    public static final String PURCHASE_DATE_COLUMN_NAME = "date_purchased";
    public static final String PURCHASE_PRICE_COLUMN_NAME = "price_at_purchase";
    public static final String EFFECTIVE_DATE_COLUMN_NAME = "effective_date";
    public static final String BASE_PRICE_COLUMN_NAME = "base_price";
    public static final String MESSAGE_COUNT_COLUMN_NAME = "number_of_messages";
    public static final String PROFILE_PUBLIC_COLUMN_NAME = "company_showroom";
    public static final String HOT_PRODUCT_COUNT_COLUMN_NAME = "hot_product_listing";
    public static final String PRODUCT_ALERT_INCLUDED_COLUMN_NAME = "product_alert";
    public static final String SUPPLIER_MESSAGING_COLUMN_NAME = "supplier_to_supplier_inquiry";
    public static final String EXPORT_TUTORIAL_COLUMN_NAME = "export_to_china_tutorial";
    public static final String ADDITIONAL_IMAGES_COLUMN_NAME = "enhance_profile";
    public static final String THIRD_PARTY_VERIFIABLE_COLUMN_NAME = "third_party_verification";
    public static final String ADVANCED_WEB_EMAIL_COLUMN_NAME = "website_and_advanced_email";
    public static final String VIDEO_UPLOADABLE_COLUMN_NAME = "video";
    public static final String CONTACT_CHINA_DIRECT_COLUMN_NAME = "contact_china_direct";
    public static final String MARKET_ANALYSIS_COLUMN_NAME = "market_analysis";
    public static final String BUYER_SEARCH_COLUMN_NAME = "buyer_search";
    public static final String LOGISTICS_ASSISTANCE_COLUMN_NAME = "logistics_assistance";
    public static final String PAYMENT_TYPE_COLUMN_NAME = "payment_type";
    public static final String PAYMENT_INVOICE_COLUMN_NAME = "payment_invoice";
    public static final String RENEWAL_DISCOUNT_AMOUNT_COLUMN_NAME = "renewal_discount_amount";
    public static final String UPGRADE_CREDIT_AMOUNT_COLUMN_NAME = "upgrade_credit_amount";
    public static final String MEMBERSHIP_AUDIT_ID_COLUMN_NAME = "membership_audit_id";
    public static final String PRODUCT_COUNT_COLUMN_NAME = "number_of_products";
    public static final String TRANSLATION_COUNT_COLUMN_NAME = "number_of_translations";
    public static final String EXPIRATION_DATE_COLUMN_NAME = "expiration_date";
    public static final String PAYMENT_AMOUNT_COLUMN_NAME = "price_paid";
    public static final String MONTHS_OF_TERM_COLUMN_NAME = "term";

    private static final String MEMBERSHIP_AUDIT_STATUS_ID_COLUMN_NAME = "membership_audit_status_id";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert membershipInsert;
    private SimpleJdbcInsert membershipAuditInsert;
    private SimpleJdbcInsert membershipAuditStatusInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        membershipInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("membership")
                .usingGeneratedKeyColumns(MEMBERSHIP_ID_COLUMN_NAME)
                .usingColumns(PARTY_ID_COLUMN_NAME, AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        membershipAuditInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("membership_audit")
                .usingGeneratedKeyColumns(MEMBERSHIP_AUDIT_ID_COLUMN_NAME)
                .usingColumns(MEMBERSHIP_LEVEL_ID_COLUMN_NAME, PURCHASE_DATE_COLUMN_NAME, PURCHASE_PRICE_COLUMN_NAME,
                        EFFECTIVE_DATE_COLUMN_NAME, BASE_PRICE_COLUMN_NAME, MESSAGE_COUNT_COLUMN_NAME,
                        PROFILE_PUBLIC_COLUMN_NAME, HOT_PRODUCT_COUNT_COLUMN_NAME, PRODUCT_ALERT_INCLUDED_COLUMN_NAME,
                        SUPPLIER_MESSAGING_COLUMN_NAME, EXPORT_TUTORIAL_COLUMN_NAME, ADDITIONAL_IMAGES_COLUMN_NAME,
                        THIRD_PARTY_VERIFIABLE_COLUMN_NAME, ADVANCED_WEB_EMAIL_COLUMN_NAME,
                        VIDEO_UPLOADABLE_COLUMN_NAME, PRODUCT_COUNT_COLUMN_NAME, TRANSLATION_COUNT_COLUMN_NAME,
                        EXPIRATION_DATE_COLUMN_NAME, PAYMENT_AMOUNT_COLUMN_NAME, PAYMENT_TYPE_COLUMN_NAME,
                        PAYMENT_INVOICE_COLUMN_NAME, RENEWAL_DISCOUNT_AMOUNT_COLUMN_NAME,
                        UPGRADE_CREDIT_AMOUNT_COLUMN_NAME, MONTHS_OF_TERM_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        membershipAuditStatusInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("membership_audit_status")
                .usingGeneratedKeyColumns(MEMBERSHIP_AUDIT_STATUS_ID_COLUMN_NAME)
                .usingColumns(MEMBERSHIP_ID_COLUMN_NAME, MEMBERSHIP_AUDIT_ID_COLUMN_NAME, VERSION_COLUMN_NAME,
                        EVENT_TYPE_COLUMN_NAME, EVENT_TIME_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public List<Membership> getMemberships(QueryCriteria sortingAndPaginationCriteria) {
        LOGGER.debug("Getting Memberships from the database " + sortingAndPaginationCriteria.toString());
        String sql = getSql("membership/get-all.sql") + sortingAndPaginationCriteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, sortingAndPaginationCriteria.getParameterMap(), new MembershipRowMapper());
    }

    @Override
    public QueryCriteria createQueryCriteria(List<Long> membershipIds, Long partyId, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count, Locale locale) {

        return createSqlQueryCriteria(propertyName, sortDescending, startItem, count, locale).appendInClause(
                MEMBERSHIP_ID_COLUMN_NAME, "membershipIds", membershipIds).appendEqualsSubClause(PARTY_ID_COLUMN_NAME,
                "partyId", partyId);
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put("id", MEMBERSHIP_ID_COLUMN_NAME);
        columnsByProperty.put("effectiveDate", EFFECTIVE_DATE_COLUMN_NAME);
    }

    @Override
    public Membership loadByMembershipId(Long membershipId) {
        LOGGER.debug("Looking up membership with ID " + membershipId);
        String sql = getSql("membership/get-all.sql") + " WHERE mv.membership_id = :membershipId";
        SqlParameterSource parameterSource = new MapSqlParameterSource("membershipId", membershipId);

        return jdbcTemplate.queryForObject(sql, parameterSource, new MembershipRowMapper());
    }

    @Override
    public Membership loadBySupplierId(Long supplierId) {
        LOGGER.debug("Looking up supplier with ID " + supplierId);
        String sql = getSql("membership/loadBySupplierId.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("partyId", supplierId);

        return jdbcTemplate.queryForObject(sql, parameterSource, new MembershipRowMapper());
    }

    @Override
    public Membership loadProvisionalBySupplierId(Long supplierId) {
        LOGGER.debug("Looking up provisonal supplier membership with ID " + supplierId);
        String sql = getSql("membership/loadProvisionalBySupplierId.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("partyId", supplierId);

        return jdbcTemplate.queryForObject(sql, parameterSource, new MembershipRowMapper());
    }

    @Override
    public Integer loadTotalProductsByMembershipId(Long membershipId) {
        String sql = getSql("membership/loadTotalProductsByMembershipId.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("membershipId", membershipId);

        return jdbcTemplate.queryForInt(sql, parameterSource);
    }

    @Override
    public Membership insert(Membership membership) {
        createMembership(membership);
        createMembershipAudit(membership);
        createMembershipAuditStatus(membership);
        return membership;
    }

    @Override
    public Membership update(Membership membership) {
        createMembershipAudit(membership);
        createMembershipAuditStatus(membership);
        return membership;
    }

    @Override
    public int deleteMembershipsByPartyId(Long partyId) {
        SqlParameterSource partyParameterSource = new MapSqlParameterSource().addValue("partyId", partyId);

        List<Long> membershipAuditIds = jdbcTemplate.queryForList(
                getSql("/membership/loadMembershipAuditIdsByPartyId.sql"), partyParameterSource, Long.class);

        SqlParameterSource membershipAuditParameterSource = new MapSqlParameterSource().addValue("membershipAuditIds",
                membershipAuditIds);

        int numberOfDeletes = 0;

        LOGGER.info("Deleting memberships for party" + partyId);

        if (!membershipAuditIds.isEmpty()) {
            jdbcTemplate.update(getSql("/membership/deleteMembershipAuditStatusByPartyId.sql"), partyParameterSource);
            jdbcTemplate.update(getSql("/membership/deleteMembershipAuditDiscountsByMembershipAuditIds.sql"),
                    membershipAuditParameterSource);
            jdbcTemplate.update(getSql("/membership/deleteMembershipAuditByMembershipAuditIds.sql"),
                    membershipAuditParameterSource);
            jdbcTemplate.update(getSql("/membership/deleteProductMembershipByPartyId.sql"), partyParameterSource);
            numberOfDeletes = jdbcTemplate.update(getSql("/membership/deleteMembershipByPartyId.sql"),
                    partyParameterSource);
        }

        LOGGER.info("Deleted " + numberOfDeletes + " memberships for party" + partyId);

        return numberOfDeletes;
    }

    private void createMembership(Membership membership) {
        LOGGER.info("Creating membership for " + membership);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PARTY_ID_COLUMN_NAME,
                membership.getSupplierId());
        Number partyKey = membershipInsert.executeAndReturnKey(parameterSource);
        membership.setId(partyKey.longValue());
    }

    private void createMembershipAudit(Membership membership) {
        LOGGER.info("Creating membership audit for " + membership);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(MEMBERSHIP_LEVEL_ID_COLUMN_NAME, membership.getLevel().getId())
                .addValue(PURCHASE_DATE_COLUMN_NAME, membership.getPurchaseDate())
                .addValue(PURCHASE_PRICE_COLUMN_NAME, membership.getPurchasePrice())
                .addValue(EFFECTIVE_DATE_COLUMN_NAME, membership.getEffectiveDate())
                .addValue(BASE_PRICE_COLUMN_NAME, membership.getLevel().getPrice())
                .addValue(MESSAGE_COUNT_COLUMN_NAME, membership.getLevel().getMessageCount())
                .addValue(PROFILE_PUBLIC_COLUMN_NAME, membership.getLevel().isProfilePublic())
                .addValue(HOT_PRODUCT_COUNT_COLUMN_NAME, membership.getLevel().getHotProductCount())
                .addValue(PRODUCT_ALERT_INCLUDED_COLUMN_NAME, membership.getLevel().isIncludedInProductAlerts())
                .addValue(SUPPLIER_MESSAGING_COLUMN_NAME, membership.getLevel().isSupplierMessagingEnabled())
                .addValue(EXPORT_TUTORIAL_COLUMN_NAME, membership.getLevel().isExportTutorialAccessible())
                .addValue(ADDITIONAL_IMAGES_COLUMN_NAME, membership.getLevel().getAdditionalProductImageCount())
                .addValue(THIRD_PARTY_VERIFIABLE_COLUMN_NAME, membership.getLevel().isVerifiableByThirdParty())
                .addValue(ADVANCED_WEB_EMAIL_COLUMN_NAME, membership.getLevel().isAdvancedWebAndMailCapabilityEnabled())
                .addValue(VIDEO_UPLOADABLE_COLUMN_NAME, membership.getLevel().isVideoUploadable())
                .addValue(CONTACT_CHINA_DIRECT_COLUMN_NAME, membership.getLevel().isContactChinaDirect())
                .addValue(MARKET_ANALYSIS_COLUMN_NAME, membership.getLevel().isMarketAnalysis())
                .addValue(BUYER_SEARCH_COLUMN_NAME, membership.getLevel().isBuyerSearch())
                .addValue(LOGISTICS_ASSISTANCE_COLUMN_NAME, membership.getLevel().isLogisticsAssistance())
                .addValue(PRODUCT_COUNT_COLUMN_NAME, membership.getLevel().getProductCount())
                .addValue(TRANSLATION_COUNT_COLUMN_NAME, membership.getLevel().getTranslationCount())
                .addValue(EXPIRATION_DATE_COLUMN_NAME, membership.getExpirationDate())
                .addValue(PAYMENT_AMOUNT_COLUMN_NAME, membership.getPaymentAmount())
                .addValue(PAYMENT_TYPE_COLUMN_NAME, membership.getPaymentType())
                .addValue(PAYMENT_INVOICE_COLUMN_NAME, membership.getPaymentInvoice())
                .addValue(RENEWAL_DISCOUNT_AMOUNT_COLUMN_NAME, membership.getEarlyRenewalDiscount())
                .addValue(UPGRADE_CREDIT_AMOUNT_COLUMN_NAME, membership.getUpgradeCredit())
                .addValue(MONTHS_OF_TERM_COLUMN_NAME, membership.getLevel().getMonthsOfTerm());
        Number membershipAuditKey = membershipAuditInsert.executeAndReturnKey(parameterSource);
        membership.setSnapshotId(membershipAuditKey.longValue());
    }

    private void createMembershipAuditStatus(Membership membership) {
        LOGGER.info("Creating membership audit status for " + membership);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(MEMBERSHIP_ID_COLUMN_NAME, membership.getId())
                .addValue(MEMBERSHIP_AUDIT_ID_COLUMN_NAME, membership.getSnapshotId()).addValue(VERSION_COLUMN_NAME, 1)
                // We don't care about version on memberships
                .addValue(EVENT_TYPE_COLUMN_NAME, membership.getApprovalStatus())
                .addValue(EVENT_TIME_COLUMN_NAME, new Date());
        membershipAuditStatusInsert.execute(parameterSource);
    }
}
