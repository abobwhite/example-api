package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcSupplierTranslationDaoTest extends BaseJdbcDaoTest {

    private static final int SUPPLIER_TRANSLATION_SQL_PT_PARTY_COUNT = 1;
    private static final int SUPPLIER_TRANSLATION_SQL_PT_PARTY_AUDIT_COUNT = 1;

    @Inject
    private JdbcSupplierTranslationDao supplierTranslationDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("supplier-translation.sql");
    }

    @Test
    public void getAllReturnsAllProfileInPendingTranslationState() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = supplierTranslationDao.createSortingAndPaginationCriteria(null,
                false, 1, 25, Locale.ENGLISH);
        List<SupplierTranslation> translations = supplierTranslationDao.getAll(sortingAndPaginationCriteria);

        assertThat(translations.size(), is(2));

        SupplierTranslation wyrdFatesTranslation = translations.get(0);
        assertThat(wyrdFatesTranslation.getId(), is(26L));
        assertThat(wyrdFatesTranslation.getApprovalStatus(), is(equalTo(ApprovalStatus.PENDING_TRANSLATION)));
        assertThat(wyrdFatesTranslation.getVersion(), is(1));
        assertThat(wyrdFatesTranslation.getSnapshotId(), is(2502L));
        assertThat(wyrdFatesTranslation.getTitle(), is("Wyrd Fates"));
        assertThat(wyrdFatesTranslation.getLastUpdatedBy(), is("FatesUnitTestLoad"));
        assertThat(wyrdFatesTranslation.getType(), is("Profile"));
        assertThat(wyrdFatesTranslation.isTranslated(), is(true));

        SupplierTranslation productTranslation = translations.get(1);
        assertThat(productTranslation.getId(), is(9991L));
        assertThat(productTranslation.getApprovalStatus(), is(equalTo(ApprovalStatus.PENDING_TRANSLATION)));
        assertThat(productTranslation.getVersion(), is(1));
        assertThat(productTranslation.getSnapshotId(), is(999124L));
        assertThat(productTranslation.getTitle(), is("product name"));
        assertThat(productTranslation.getLastUpdatedBy(), is("UnitTestLoad"));
        assertThat(productTranslation.getType(), is("Product"));
        assertThat(productTranslation.isTranslated(), is(true));
    }

    @Test
    public void findFiltersByType() {
        QueryCriteria profileCriteria = supplierTranslationDao
                .createQueryCriteria("", "Profile", null, "", true, 0, 10);
        QueryCriteria productCriteria = supplierTranslationDao
                .createQueryCriteria("", "Product", null, "", true, 0, 10);

        List<SupplierTranslation> profileTranslations = supplierTranslationDao.find(profileCriteria);
        List<SupplierTranslation> productTranslations = supplierTranslationDao.find(productCriteria);

        assertThat(profileTranslations.size(), is(1));
        assertThat(productTranslations.size(), is(1));
    }

    // TODO Other filters?

    @Test
    public void loadLoadsCorrectSupplier() {
        SupplierTranslation translation = supplierTranslationDao.load(26L);

        assertThat(translation.getId(), is(26L));
        assertThat(translation.getApprovalStatus(), is(equalTo(ApprovalStatus.PENDING_TRANSLATION)));
        assertThat(translation.getVersion(), is(1));
        assertThat(translation.getSnapshotId(), is(2502L));
        assertThat(translation.getTitle(), is("Wyrd Fates"));
        assertThat(translation.getLastUpdatedBy(), is("FatesUnitTestLoad"));
        assertThat(translation.getType(), is("Profile"));
        assertThat(translation.isTranslated(), is(true));
    }

    @Test
    public void updateInsertsProfileApprovalIntoEventTableOnly() throws Exception {
        SupplierTranslation translation = new SupplierTranslation(26L, "xo0wK0qL", "Pending Translation Supplier 999999",
                ApprovalStatus.APPROVED, 3, 999123L, SupplierTranslation.PROFILE_TYPE, true, "babelf", new Date());

        supplierTranslationDao.update(translation);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_status WHERE party_id = ? AND party_audit_status_id NOT IN (25012)",
                translation.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(translation.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(translation.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(translation.getVersion()));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(translation.getApprovalStatus().getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT count(*) party_count FROM party");
        assertThat((Long) partyRowMap.get("party_count"), is(new Long(DATA_SQL_PARTY_COUNT
                + SUPPLIER_TRANSLATION_SQL_PT_PARTY_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) party_audit_count FROM party_audit");
        assertThat((Long) partyAuditRowMap.get("party_audit_count"), is(new Long(DATA_SQL_PARTY_AUDIT_COUNT
                + SUPPLIER_TRANSLATION_SQL_PT_PARTY_AUDIT_COUNT)));
    }

    @Test
    public void updateInsertsProductApprovalIntoEventTableOnly() throws Exception {
        SupplierTranslation translation = new SupplierTranslation(9991L, "9991", "Pending Translation Supplier 999999",
                ApprovalStatus.APPROVED, 3, 999123L, SupplierTranslation.PRODUCT_TYPE, true, "babelf", new Date());

        supplierTranslationDao.update(translation);

        Map<String, Object> productAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM product_audit_status WHERE product_id = ? AND product_audit_status_id NOT IN (80,81,91,9993241,99999)",
                        translation.getId());
        assertThat((Long) productAuditStatusRowMap.get("product_id"), is(translation.getId()));
        assertThat((Long) productAuditStatusRowMap.get("product_audit_id"), is(translation.getSnapshotId()));
        assertThat((Integer) productAuditStatusRowMap.get("version_number"), is(translation.getVersion()));
        assertThat(productAuditStatusRowMap.get("event_type").toString(), is(translation.getApprovalStatus().getName()));
        assertThat((Date) productAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT count(*) party_count FROM party");
        assertThat((Long) partyRowMap.get("party_count"), is(new Long(DATA_SQL_PARTY_COUNT
                + SUPPLIER_TRANSLATION_SQL_PT_PARTY_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) party_audit_count FROM party_audit");
        assertThat((Long) partyAuditRowMap.get("party_audit_count"), is(new Long(DATA_SQL_PARTY_AUDIT_COUNT
                + SUPPLIER_TRANSLATION_SQL_PT_PARTY_AUDIT_COUNT)));
    }
}
