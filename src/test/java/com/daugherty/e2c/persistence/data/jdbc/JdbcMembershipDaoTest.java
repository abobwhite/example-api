package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcMembershipDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcMembershipDao membershipDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("supplierMembership.sql");
    }

    @Test
    public void createLevelOneMembershipInsertsIntoBaseSnapshotAndEventTables() {
        MembershipLevel level = new MembershipLevel(101L, 1, new BigDecimal("0.000"), 6, 5, 2147483647, 600, true, 0,
                false, false, false, 0, false, false, false, false, false, false, false);
        Membership membership = new Membership(42L, level);

        Membership createdMembership = membershipDao.insert(membership);
        assertThat(createdMembership.getId(), notNullValue());
        assertThat(createdMembership.getSnapshotId(), notNullValue());

        Map<String, Object> membershipAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_audit_status WHERE membership_id = ?", createdMembership.getId());
        assertThat((Long) membershipAuditStatusRowMap.get("membership_id"), is(createdMembership.getId()));
        assertThat((Long) membershipAuditStatusRowMap.get("membership_audit_id"), is(createdMembership.getSnapshotId()));
        assertThat((Integer) membershipAuditStatusRowMap.get("version_number"), is(1));
        assertThat(membershipAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.PROVISIONAL.getName()));
        assertThat((Date) membershipAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> membershipRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership WHERE membership_id = ?", createdMembership.getId());
        assertThat((Long) membershipRowMap.get("party_id"), is(membership.getSupplierId()));

        Map<String, Object> membershipAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_audit WHERE membership_audit_id = ?", createdMembership.getSnapshotId());
        assertThat((Integer) membershipAuditRowMap.get("number_of_products"), is(membership.getLevel()
                .getProductCount()));
        assertThat((Integer) membershipAuditRowMap.get("number_of_translations"), is(membership.getLevel()
                .getTranslationCount()));
        assertThat((Date) membershipAuditRowMap.get("expiration_date"),
                is(sixMonthsMinusOneDayFromEffectiveDate(membership)));
        assertThat(membershipAuditRowMap.get("price_paid").toString(), is(membership.getLevel().getPrice().toString()));
        assertThat((Long) membershipAuditRowMap.get("membership_level_id"), is(membership.getLevel().getId()));
        assertThat((Date) membershipAuditRowMap.get("date_purchased"), is(today()));
        assertThat(membershipAuditRowMap.get("price_at_purchase").toString(), is(membership.getLevel().getPrice()
                .toString()));
        assertThat((Date) membershipAuditRowMap.get("effective_date"), is(today()));
        assertThat(membershipAuditRowMap.get("base_price").toString(), is(membership.getLevel().getPrice().toString()));
        assertThat((Long) membershipAuditRowMap.get("number_of_messages"), is(membership.getLevel().getMessageCount()
                .longValue()));
        assertThat((Boolean) membershipAuditRowMap.get("company_showroom"), is(membership.getLevel().isProfilePublic()));
        assertThat((Integer) membershipAuditRowMap.get("hot_product_listing"), is(membership.getLevel()
                .getHotProductCount()));
        assertThat((Boolean) membershipAuditRowMap.get("product_alert"), is(membership.getLevel()
                .isIncludedInProductAlerts()));
        assertThat((Boolean) membershipAuditRowMap.get("supplier_to_supplier_inquiry"), is(membership.getLevel()
                .isSupplierMessagingEnabled()));
        assertThat((Boolean) membershipAuditRowMap.get("export_to_china_tutorial"), is(membership.getLevel()
                .isExportTutorialAccessible()));
        assertThat((Integer) membershipAuditRowMap.get("enhance_profile"), is(membership.getLevel()
                .getAdditionalProductImageCount()));
        assertThat((Boolean) membershipAuditRowMap.get("third_party_verification"), is(membership.getLevel()
                .isVerifiableByThirdParty()));
        assertThat((Boolean) membershipAuditRowMap.get("website_and_advanced_email"), is(membership.getLevel()
                .isAdvancedWebAndMailCapabilityEnabled()));
        assertThat((Boolean) membershipAuditRowMap.get("video"), is(membership.getLevel().isVideoUploadable()));
    }

    @Test
    public void loadActiveMembership() {
        Membership createdMembership = membershipDao.loadBySupplierId(99911L);

        assertThat(createdMembership.getId(), notNullValue());
        assertThat(createdMembership.getSnapshotId(), notNullValue());

        Map<String, Object> membershipAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_audit_status WHERE membership_id = ?", createdMembership.getId());
        assertThat((Long) membershipAuditStatusRowMap.get("membership_id"), is(createdMembership.getId()));
        assertThat((Long) membershipAuditStatusRowMap.get("membership_audit_id"), is(createdMembership.getSnapshotId()));
        assertThat((Integer) membershipAuditStatusRowMap.get("version_number"), is(1));
        assertThat(membershipAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.PAID.getName()));
        assertThat((Date) membershipAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> membershipRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership WHERE membership_id = ?", createdMembership.getId());
        assertThat((Long) membershipRowMap.get("party_id"), is(99911L));

        Map<String, Object> membershipAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_audit WHERE membership_audit_id = ?", createdMembership.getSnapshotId());
        assertThat((Integer) membershipAuditRowMap.get("number_of_products"), is(25));
        assertThat((Integer) membershipAuditRowMap.get("number_of_translations"), is(1200));
        assertThat(membershipAuditRowMap.get("price_paid").toString(), is("800.000"));
        assertThat((Long) membershipAuditRowMap.get("membership_level_id"), is(1L));
        assertThat((Date) membershipAuditRowMap.get("date_purchased"), is(today()));
        assertThat(membershipAuditRowMap.get("price_at_purchase").toString(), is("1000.000"));
        assertThat(membershipAuditRowMap.get("base_price").toString(), is("100.000"));
        assertThat((Long) membershipAuditRowMap.get("number_of_messages"), is(20L));
        assertThat((Boolean) membershipAuditRowMap.get("company_showroom"), is(false));
        assertThat((Integer) membershipAuditRowMap.get("hot_product_listing"), is(1));
        assertThat((Boolean) membershipAuditRowMap.get("product_alert"), is(false));
        assertThat((Boolean) membershipAuditRowMap.get("supplier_to_supplier_inquiry"), is(true));
        assertThat((Boolean) membershipAuditRowMap.get("export_to_china_tutorial"), is(false));
        assertThat((Integer) membershipAuditRowMap.get("enhance_profile"), is(1));
        assertThat((Boolean) membershipAuditRowMap.get("third_party_verification"), is(false));
        assertThat((Boolean) membershipAuditRowMap.get("website_and_advanced_email"), is(true));
        assertThat((Boolean) membershipAuditRowMap.get("video"), is(false));
    }

    @Test
    public void loadProvisonalMemebership() {
        Membership provisionalMembership = membershipDao.loadProvisionalBySupplierId(99911L);

        assertThat(provisionalMembership.getId(), notNullValue());
        assertThat(provisionalMembership.getSnapshotId(), notNullValue());

        Map<String, Object> membershipAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_audit_status WHERE membership_id = ?", provisionalMembership.getId());
        assertThat((Long) membershipAuditStatusRowMap.get("membership_id"), is(provisionalMembership.getId()));
        assertThat((Long) membershipAuditStatusRowMap.get("membership_audit_id"),
                is(provisionalMembership.getSnapshotId()));
        assertThat((Integer) membershipAuditStatusRowMap.get("version_number"), is(1));
        assertThat(membershipAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.PROVISIONAL.getName()));
        assertThat((Date) membershipAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> membershipRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership WHERE membership_id = ?", provisionalMembership.getId());
        assertThat((Long) membershipRowMap.get("party_id"), is(99911L));

        Map<String, Object> membershipAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_audit WHERE membership_audit_id = ?", provisionalMembership.getSnapshotId());
        assertThat((Integer) membershipAuditRowMap.get("number_of_products"), is(25));
        assertThat((Integer) membershipAuditRowMap.get("number_of_translations"), is(1200));
        assertThat(membershipAuditRowMap.get("price_paid").toString(), is("800.000"));
        assertThat((Long) membershipAuditRowMap.get("membership_level_id"), is(1L));
        assertThat((Date) membershipAuditRowMap.get("date_purchased"), is(today()));
        assertThat(membershipAuditRowMap.get("price_at_purchase").toString(), is("1000.000"));
        assertThat(membershipAuditRowMap.get("base_price").toString(), is("100.000"));
        assertThat((Long) membershipAuditRowMap.get("number_of_messages"), is(20L));
        assertThat((Boolean) membershipAuditRowMap.get("company_showroom"), is(false));
        assertThat((Integer) membershipAuditRowMap.get("hot_product_listing"), is(1));
        assertThat((Boolean) membershipAuditRowMap.get("product_alert"), is(false));
        assertThat((Boolean) membershipAuditRowMap.get("supplier_to_supplier_inquiry"), is(true));
        assertThat((Boolean) membershipAuditRowMap.get("export_to_china_tutorial"), is(false));
        assertThat((Integer) membershipAuditRowMap.get("enhance_profile"), is(1));
        assertThat((Boolean) membershipAuditRowMap.get("third_party_verification"), is(false));
        assertThat((Boolean) membershipAuditRowMap.get("website_and_advanced_email"), is(true));
        assertThat((Boolean) membershipAuditRowMap.get("video"), is(false));
    }

    @Test
    public void loadTotalProducts() {
        Integer totalProducts = membershipDao.loadTotalProductsByMembershipId(77777L);

        assertThat(totalProducts, is(25));
    }

    @Test
    public void deleteMembershipsByProductIdRemovesMembership() {
        QueryCriteria queryCriteria = membershipDao.createQueryCriteria(null, 99911L, null, true, 1, 250,
                Locale.ENGLISH);
        List<Membership> memberships = membershipDao.getMemberships(queryCriteria);

        assertThat(memberships.size(), is(membershipDao.deleteMembershipsByPartyId(99911L)));
    }

    private Date sixMonthsMinusOneDayFromEffectiveDate(Membership membership) {
        return DateUtils.addDays(DateUtils.addMonths(today(), membership.getLevel().getMonthsOfTerm()), -1);
    }

}
