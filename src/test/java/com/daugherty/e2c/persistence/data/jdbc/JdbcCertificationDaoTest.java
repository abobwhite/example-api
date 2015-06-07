package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;

public class JdbcCertificationDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcCertificationDao certificationDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return null;
    }

    @Test
    public void validateInsertOfSupplierCertification() {
        Certification testCertification = new Certification(null, // id
                "standard", "5678X", // certificate number
                "www.whocares.???", 42L, // supplierId
                "bob", // issuedBy
                1234567890000L, // issued date
                "scopey", // scope range
                ApprovalStatus.DRAFT, 12, // version
                null // snapshot id
        );
        certificationDao.insertSupplierCertification(testCertification);
        assertThat(testCertification.getId(), notNullValue());
        assertThat(testCertification.getSnapshotId(), notNullValue());

        Map<String, Object> certificationRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM certification WHERE certification_id = ? ", testCertification.getId());

        assertThat((Long) certificationRowMap.get("certification_id"), is(testCertification.getId()));

        Map<String, Object> partyCertificationRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_certification WHERE certification_id = ? ", testCertification.getId());

        assertThat((Long) partyCertificationRowMap.get("certification_id"), is(testCertification.getId()));
        assertThat((Long) partyCertificationRowMap.get("party_id"), is(testCertification.getSupplierOrProductId()));

        Map<String, Object> certificationAuditRowMap = jdbcTemplate
                .queryForMap("SELECT * FROM certification_audit WHERE certification_audit_id <> 779 AND certification_audit_id <> 668 ");

        assertThat((String) certificationAuditRowMap.get("certification_standard"), is(testCertification.getStandard()));
        assertThat((String) certificationAuditRowMap.get("certification_number"),
                is(testCertification.getCertificateNumber()));
        assertThat((String) certificationAuditRowMap.get("certification_link"), is(testCertification.getLink()));
        assertThat(((Date) certificationAuditRowMap.get("issued_date")).getTime(),
                is(testCertification.getIssuedDate()));
        assertThat((String) certificationAuditRowMap.get("issued_by"), is(testCertification.getIssuedBy()));
        assertThat((String) certificationAuditRowMap.get("scope_range"), is(testCertification.getScopeRange()));

        Map<String, Object> certificationAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM certification_audit_status WHERE certification_id = ? AND certification_audit_id = ? ",
                testCertification.getId(), testCertification.getSnapshotId());

        assertThat((String) certificationAuditStatusRowMap.get("event_type"), is(testCertification.getApprovalStatus()
                .getName()));
        assertThat((Integer) certificationAuditStatusRowMap.get("version_number"), is(testCertification.getVersion()));
    }

    @Test
    public void validateInsertOfProductCertification() {
        Certification testCertification = new Certification(null, // id
                "standard", "L7347", // certificate number
                "www.somesite.???", 42L, // productId
                "some-governing-body", // issuedBy
                1234567890000L, // issued date
                "scopey", // scope range
                ApprovalStatus.DRAFT, 12, // version
                null // snapshot id
        );
        certificationDao.insertProductCertification(testCertification);
        assertThat(testCertification.getId(), notNullValue());
        assertThat(testCertification.getSnapshotId(), notNullValue());

        Map<String, Object> certificationRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM certification WHERE certification_id = ? ", testCertification.getId());

        assertThat((Long) certificationRowMap.get("certification_id"), is(testCertification.getId()));

        Map<String, Object> partyCertificationRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_certification WHERE certification_id = ? ", testCertification.getId());

        assertThat((Long) partyCertificationRowMap.get("certification_id"), is(testCertification.getId()));
        assertThat((Long) partyCertificationRowMap.get("product_id"), is(testCertification.getSupplierOrProductId()));

        Map<String, Object> certificationAuditRowMap = jdbcTemplate
                .queryForMap("SELECT * FROM certification_audit WHERE certification_audit_id <> 779 AND certification_audit_id <> 668 ");

        assertThat((String) certificationAuditRowMap.get("certification_standard"), is(testCertification.getStandard()));
        assertThat((String) certificationAuditRowMap.get("certification_number"),
                is(testCertification.getCertificateNumber()));
        assertThat((String) certificationAuditRowMap.get("certification_link"), is(testCertification.getLink()));
        assertThat(((Date) certificationAuditRowMap.get("issued_date")).getTime(),
                is(testCertification.getIssuedDate()));
        assertThat((String) certificationAuditRowMap.get("issued_by"), is(testCertification.getIssuedBy()));
        assertThat((String) certificationAuditRowMap.get("scope_range"), is(testCertification.getScopeRange()));

        Map<String, Object> certificationAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM certification_audit_status WHERE certification_id = ? AND certification_audit_id = ? ",
                testCertification.getId(), testCertification.getSnapshotId());

        assertThat((String) certificationAuditStatusRowMap.get("event_type"), is(testCertification.getApprovalStatus()
                .getName()));
        assertThat((Integer) certificationAuditStatusRowMap.get("version_number"), is(testCertification.getVersion()));
    }

    @Test
    public void loadByIdThatExistsReturnsApprovedSupplierCertification() {

        Long certificationId = 777L;
        Long supplierId = 21L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 17, 0, 0);

        Certification persistedCertification = certificationDao.loadApproved(777L, Locale.ENGLISH);

        Certification testCertification = new Certification(certificationId, "Standard", "cert-number", null,
                supplierId, "issued-by", calendar.getTimeInMillis(), "scope-range!", ApprovalStatus.APPROVED, 13, 779L);

        assertThat(persistedCertification.getId(), is(testCertification.getId()));
        assertThat(persistedCertification.getCertificateNumber(), is(testCertification.getCertificateNumber()));
        assertThat(persistedCertification.getStandard(), is(testCertification.getStandard()));
        assertThat(persistedCertification.getLink(), is(testCertification.getLink()));
        assertThat(persistedCertification.getIssuedBy(), is(testCertification.getIssuedBy()));
        assertThat(persistedCertification.getIssuedDate(), is(testCertification.getIssuedDate()));
        assertThat(persistedCertification.getScopeRange(), is(testCertification.getScopeRange()));
        assertThat(persistedCertification.getApprovalStatus(), is(testCertification.getApprovalStatus()));
        assertThat(persistedCertification.getVersion(), is(testCertification.getVersion()));
        assertThat(persistedCertification.getSnapshotId(), is(persistedCertification.getSnapshotId()));
    }

    @Test
    public void verifyDeletionOfExistingSupplierCertificationSetsFlag() {
        certificationDao.delete(777L);
        jdbcTemplate
                .queryForMap("SELECT * FROM party_certification WHERE certification_id = 777 AND is_active = FALSE");
    }

    @Test
    public void loadByIdThatExistsReturnsLatestProductCertification() {

        Long certificationId = 666L;
        Long productId = 51L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 17, 0, 0);

        Certification persistedCertification = certificationDao.loadLatest(666L, Locale.ENGLISH);

        Certification testCertification = new Certification(certificationId, "Standard", "cert-number",
                "www.cert-link.com", productId, "issued-by", calendar.getTimeInMillis(), "scope-range!",
                ApprovalStatus.DRAFT, 13, 779L);

        assertThat(persistedCertification.getId(), is(testCertification.getId()));
        assertThat(persistedCertification.getCertificateNumber(), is(testCertification.getCertificateNumber()));
        assertThat(persistedCertification.getStandard(), is(testCertification.getStandard()));
        assertThat(persistedCertification.getLink(), is(testCertification.getLink()));
        assertThat(persistedCertification.getIssuedBy(), is(testCertification.getIssuedBy()));
        assertThat(persistedCertification.getIssuedDate(), is(testCertification.getIssuedDate()));
        assertThat(persistedCertification.getScopeRange(), is(testCertification.getScopeRange()));
        assertThat(persistedCertification.getApprovalStatus(), is(testCertification.getApprovalStatus()));
        assertThat(persistedCertification.getVersion(), is(testCertification.getVersion()));
        assertThat(persistedCertification.getSnapshotId(), is(persistedCertification.getSnapshotId()));
    }

    @Test
    public void verifyDeletionOfExistingProductCertificationSetsFlag() {
        certificationDao.delete(666L);
        jdbcTemplate
                .queryForMap("SELECT * FROM product_certification WHERE certification_id = 666 AND is_active = FALSE");
    }

    @Test
    public void verifyDeletionOfExistingProductCertificationByPartyId() {
        assertThat(1, is(certificationDao.deleteByPartyId(21L)));
    }

    @Test
    public void verifyUpdateInsertsIntoAuditAndAuditStatus() {

        Long certificationId = 666L;
        Long productId = 51L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 18, 0, 0);

        Certification testCertification = new Certification(certificationId, "StandardUPD", "cert-numberUPD",
                "www.cert-linkUPD.com", productId, "issued-byUPD", calendar.getTimeInMillis(), "scope-range!UPD",
                ApprovalStatus.DRAFT, 13, 779L);

        certificationDao.update(testCertification);

        Map<String, Object> certificationAuditRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM certification_audit ca, certification_audit_status cas "
                                + " WHERE ca.certification_audit_id = cas.certification_audit_id and cas.certification_id = ? "
                                + "       and cas.event_datetime = (select max(event_datetime) from certification_audit_status where certification_id = ?)",
                        666L, 666L);

        assertThat((String) certificationAuditRowMap.get("certification_standard"), is(testCertification.getStandard()));
        assertThat((String) certificationAuditRowMap.get("certification_number"),
                is(testCertification.getCertificateNumber()));
        assertThat((String) certificationAuditRowMap.get("certification_link"), is(testCertification.getLink()));
        assertThat(((Date) certificationAuditRowMap.get("issued_date")).getTime(),
                is(testCertification.getIssuedDate()));
        assertThat((String) certificationAuditRowMap.get("issued_by"), is(testCertification.getIssuedBy()));
        assertThat((String) certificationAuditRowMap.get("scope_range"), is(testCertification.getScopeRange()));

        assertThat((String) certificationAuditRowMap.get("event_type"), is(testCertification.getApprovalStatus()
                .getName()));
        assertThat((Integer) certificationAuditRowMap.get("version_number"), is(testCertification.getVersion()));

        // verify that 'update' is really inserting another row into audit/audit_status for certification id.
        Map<String, Object> certificationCountAuditRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT COUNT(*) AS nbrRecords FROM certification_audit ca, certification_audit_status cas "
                                + " WHERE ca.certification_audit_id = cas.certification_audit_id and cas.certification_id = ? ",
                        666L);
        assertThat((Long) certificationCountAuditRowMap.get("nbrRecords"), is(2L));
    }

}
