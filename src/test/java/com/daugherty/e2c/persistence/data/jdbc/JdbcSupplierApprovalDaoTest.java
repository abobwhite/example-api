package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcSupplierApprovalDaoTest extends BaseJdbcDaoTest {

    private static final int SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT = 2;
    private static final int SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PRODUCT_COUNT = 2;
    private static final int SUPPLIER_APPROVAL_SQL_PA_OR_WFI_CERTIFICATION_COUNT = 2;
    private static final int TOTAL_PA_OR_WFI_COUNT = DATA_SQL_PA_OR_WFI_COUNT
            + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PRODUCT_COUNT
            + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_CERTIFICATION_COUNT;
    private static final int SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_AUDIT_COUNT = 2;
    private static final int SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PRODUCT_AUDIT_COUNT = 3;
    private static final int SUPPLIER_APPROVAL_SQL_PA_OR_WFI_CERTIFICATION_AUDIT_COUNT = 2;

    @Inject
    private JdbcSupplierApprovalDao supplierApprovalDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("supplier-approval.sql");
    }

    @Test
    public void getAllReturnsAllProfileProductAndCertificationsInPendingApprovalOrWaitingForInformationState()
            throws Exception {
        QueryCriteria sortingAndPaginationCriteria = supplierApprovalDao.createSortingAndPaginationCriteria(null,
                false, 1, 25, Locale.ENGLISH);
        List<SupplierApproval> approvals = supplierApprovalDao.find(sortingAndPaginationCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(TOTAL_PA_OR_WFI_COUNT));
        assertThatApprovalMatchesExpectedValuesForSupplier24(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForSupplier99912(approvals.get(1));
        assertThatApprovalMatchesExpectedValuesForSupplier25(approvals.get(2));
        assertThatApprovalMatchesExpectedValuesForSupplier99913(approvals.get(3));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(4));
        assertThatApprovalMatchesExpectedValuesForProduct88822(approvals.get(5));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(6));
        assertThatApprovalMatchesExpectedValuesForCertification77712(approvals.get(7));
    }

    @Test
    public void getAllSortsByTitle() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = supplierApprovalDao.createSortingAndPaginationCriteria(
                SupplierApproval.TITLE_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<SupplierApproval> approvals = supplierApprovalDao.find(sortingAndPaginationCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(TOTAL_PA_OR_WFI_COUNT));
        assertThatApprovalMatchesExpectedValuesForSupplier25(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(1));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(2));
        assertThatApprovalMatchesExpectedValuesForSupplier99912(approvals.get(3));
        assertThatApprovalMatchesExpectedValuesForSupplier24(approvals.get(4));
        assertThatApprovalMatchesExpectedValuesForCertification77712(approvals.get(5));
        assertThatApprovalMatchesExpectedValuesForProduct88822(approvals.get(6));
    }

    @Test
    public void getAllSortsByType() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = supplierApprovalDao.createSortingAndPaginationCriteria(
                SupplierApproval.TYPE_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<SupplierApproval> approvals = supplierApprovalDao.find(sortingAndPaginationCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(TOTAL_PA_OR_WFI_COUNT));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForCertification77712(approvals.get(1));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(2));
        assertThatApprovalMatchesExpectedValuesForProduct88822(approvals.get(3));
        assertThatApprovalMatchesExpectedValuesForSupplier24(approvals.get(4));
        assertThatApprovalMatchesExpectedValuesForSupplier99912(approvals.get(5));
        assertThatApprovalMatchesExpectedValuesForSupplier25(approvals.get(6));
        assertThatApprovalMatchesExpectedValuesForSupplier99913(approvals.get(7));
    }

    @Test
    public void getAllSortsByApprovalStatus() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = supplierApprovalDao.createSortingAndPaginationCriteria(
                SupplierApproval.STATUS_SERIAL_PROPERTY, true, 1, 25, Locale.ENGLISH);
        List<SupplierApproval> approvals = supplierApprovalDao.find(sortingAndPaginationCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(TOTAL_PA_OR_WFI_COUNT));
        assertThatApprovalMatchesExpectedValuesForSupplier25(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForSupplier99913(approvals.get(1));
        assertThatApprovalMatchesExpectedValuesForProduct88822(approvals.get(2));
        assertThatApprovalMatchesExpectedValuesForCertification77712(approvals.get(3));
        assertThatApprovalMatchesExpectedValuesForSupplier24(approvals.get(4));
        assertThatApprovalMatchesExpectedValuesForSupplier99912(approvals.get(5));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(6));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(7));
    }

    @Test
    public void getAllSortsByLastUpdatedBy() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = supplierApprovalDao.createSortingAndPaginationCriteria(
                SupplierApproval.LAST_UPDATED_BY_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<SupplierApproval> approvals = supplierApprovalDao.find(sortingAndPaginationCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(TOTAL_PA_OR_WFI_COUNT));
        assertThatApprovalMatchesExpectedValuesForSupplier99913(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForProduct88822(approvals.get(1));
        assertThatApprovalMatchesExpectedValuesForCertification77712(approvals.get(2));
        assertThatApprovalMatchesExpectedValuesForSupplier99912(approvals.get(3));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(4));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(5));
        assertThatApprovalMatchesExpectedValuesForSupplier24(approvals.get(6));
        assertThatApprovalMatchesExpectedValuesForSupplier25(approvals.get(7));
    }

    @Test
    public void getAllSortsByLastUpdatedDate() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = supplierApprovalDao.createSortingAndPaginationCriteria(
                SupplierApproval.LAST_UPDATED_AT_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<SupplierApproval> approvals = supplierApprovalDao.find(sortingAndPaginationCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(TOTAL_PA_OR_WFI_COUNT));
        assertThatApprovalMatchesExpectedValuesForSupplier99912(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForSupplier99913(approvals.get(1));
        assertThatApprovalMatchesExpectedValuesForSupplier24(approvals.get(2));
        assertThatApprovalMatchesExpectedValuesForSupplier25(approvals.get(3));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(4));
        assertThatApprovalMatchesExpectedValuesForProduct88822(approvals.get(5));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(6));
        assertThatApprovalMatchesExpectedValuesForCertification77712(approvals.get(7));
    }

    @Test
    public void getAllPaginates() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = supplierApprovalDao.createSortingAndPaginationCriteria(null,
                false, 6, 25, Locale.ENGLISH);
        List<SupplierApproval> approvals = supplierApprovalDao.find(sortingAndPaginationCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(3));
        assertThatApprovalMatchesExpectedValuesForProduct88822(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(1));
        assertThatApprovalMatchesExpectedValuesForCertification77712(approvals.get(2));

    }

    @Test
    public void findWithTitlePrefixCriterionOnlyReturnsMatchingApprovals() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria("Waiting for Info Supplier", null, null,
                null, null, null, null, false, 1, 25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(1));
        assertThatApprovalMatchesExpectedValuesForSupplier99913(approvals.get(0));
    }

    @Test
    public void findWithApprovalTypeCriterionOnlyReturnsMatchingApprovals() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria(null, null,
                SupplierApproval.PRODUCT_TYPE, null, null, null, null, false, 1, 25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(2));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForProduct88822(approvals.get(1));
    }

    @Test
    public void findWithApprovalStatusCriterionOnlyReturnsMatchingApprovals() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria(null, null, null,
                ApprovalStatus.PENDING_APPROVAL.getName(), null, null, null, false, 1, 25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(4));
        assertThatApprovalMatchesExpectedValuesForSupplier24(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForSupplier99912(approvals.get(1));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(2));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(3));
    }

    @Test
    public void findWithAllCriteriaOnlyReturnsMatchingApprovals() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria("Waiting", null,
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.WAITING_FOR_INFORMATION.getName(), null, null, null,
                false, 1, 25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(1));
        assertThatApprovalMatchesExpectedValuesForSupplier99913(approvals.get(0));
    }

    @Test
    public void findWithCriteriaReturnsEmptyListIfThereAreNoMatchingApprovals() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria("Waiting", null,
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL.getName(), null, null, null, false, 1,
                25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(0));
    }

    @Test
    public void findWithNewSupplierCriteriaReturnsOnlyNewSuppliers() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria(null, null,
                SupplierApproval.PROFILE_TYPE, null, Boolean.TRUE, null, null, false, 1, 25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(3));
    }

    @Test
    public void findWithNewSupplierCriteriaReturnsOnlyPaidSuppliers() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria(null, null,
                SupplierApproval.PROFILE_TYPE, null, null, Boolean.TRUE, null, false, 1, 25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(3));
    }

    @Test
    public void findWithNewSupplierCriteriaReturnsOnlyExistingSuppliers() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria(null, null,
                SupplierApproval.PROFILE_TYPE, null, Boolean.FALSE, null, null, false, 1, 25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(4));
    }

    @Test
    public void findWithSortingAndPaginationReturnsPageOfMatchingApprovalsInCorrectOrder() throws Exception {
        QueryCriteria queryCriteria = supplierApprovalDao.createQueryCriteria(null, null, null,
                ApprovalStatus.PENDING_APPROVAL.getName(), null, null, SupplierApproval.TITLE_SERIAL_PROPERTY, true, 3,
                25);
        List<SupplierApproval> approvals = supplierApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(2));
        assertThatApprovalMatchesExpectedValuesForProduct88821(approvals.get(0));
        assertThatApprovalMatchesExpectedValuesForCertification77711(approvals.get(1));
    }

    @Test
    public void loadBySupplierIdThatExistsReturnsSupplierApproval() {
        SupplierApproval approval = supplierApprovalDao.load(99912L);

        assertThatApprovalMatchesExpectedValuesForSupplier99912(approval);
    }

    @Test
    public void loadByProductIdThatExistsReturnsSupplierApproval() {
        SupplierApproval approval = supplierApprovalDao.load(88821L);

        assertThatApprovalMatchesExpectedValuesForProduct88821(approval);
    }

    @Test
    public void loadByCertificationIdThatExistsReturnsSupplierApproval() {
        SupplierApproval approval = supplierApprovalDao.load(77711L);

        assertThatApprovalMatchesExpectedValuesForCertification77711(approval);
    }

    @Test
    public void loadByIdThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        supplierApprovalDao.load(123456789L);
    }

    @Test
    public void insertInsertsProfileApprovalIntoEventTableOnly() throws Exception {
        SupplierApproval approval = new SupplierApproval(99913L, "RPekAL6N", "Waiting for Info Supplier 99913", null,
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 3, 999131L, "zaphodb", new Date());

        supplierApprovalDao.insert(approval);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_status WHERE party_id = ? AND party_audit_status_id NOT IN (9991311)",
                approval.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(approval.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(approval.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(approval.getVersion()));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(approval.getApprovalStatus().getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT count(*) party_count FROM party");
        assertThat((Long) partyRowMap.get("party_count"), is(new Long(DATA_SQL_PARTY_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) party_audit_count FROM party_audit");
        assertThat((Long) partyAuditRowMap.get("party_audit_count"), is(new Long(DATA_SQL_PARTY_AUDIT_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_AUDIT_COUNT)));
    }

    @Test
    public void insertInsertsProductApprovalIntoEventTableOnly() throws Exception {
        SupplierApproval approval = new SupplierApproval(88822L, "88822", "Waiting for Info Product 88822", null,
                SupplierApproval.PRODUCT_TYPE, ApprovalStatus.PENDING_APPROVAL, 5, 88852L, "zaphodb", new Date());

        supplierApprovalDao.insert(approval);

        Map<String, Object> productAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit_status WHERE product_id = ? AND product_audit_status_id NOT IN (88862)",
                approval.getId());
        assertThat((Long) productAuditStatusRowMap.get("product_id"), is(approval.getId()));
        assertThat((Long) productAuditStatusRowMap.get("product_audit_id"), is(approval.getSnapshotId()));
        assertThat((Integer) productAuditStatusRowMap.get("version_number"), is(approval.getVersion()));
        assertThat(productAuditStatusRowMap.get("event_type").toString(), is(approval.getApprovalStatus().getName()));
        assertThat((Date) productAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> productRowMap = jdbcTemplate.queryForMap("SELECT count(*) product_count FROM product");
        assertThat((Long) productRowMap.get("product_count"), is(new Long(DATA_SQL_PRODUCT_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PRODUCT_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) product_audit_count FROM product_audit");
        assertThat((Long) partyAuditRowMap.get("product_audit_count"), is(new Long(DATA_SQL_PRODUCT_AUDIT_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PRODUCT_AUDIT_COUNT)));
    }

    @Test
    public void insertInsertsCertificationApprovalIntoEventTableOnly() throws Exception {
        SupplierApproval approval = new SupplierApproval(77712L, "77712", "Waiting for Info Certification 77712", null,
                SupplierApproval.CERTIFICATION_TYPE, ApprovalStatus.PENDING_APPROVAL, 5, 777121L, "zaphodb", new Date());

        supplierApprovalDao.insert(approval);

        Map<String, Object> productAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM certification_audit_status WHERE certification_id = ? AND certification_audit_status_id NOT IN (7771211)",
                        approval.getId());
        assertThat((Long) productAuditStatusRowMap.get("certification_id"), is(approval.getId()));
        assertThat((Long) productAuditStatusRowMap.get("certification_audit_id"), is(approval.getSnapshotId()));
        assertThat((Integer) productAuditStatusRowMap.get("version_number"), is(approval.getVersion()));
        assertThat(productAuditStatusRowMap.get("event_type").toString(), is(approval.getApprovalStatus().getName()));
        assertThat((Date) productAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> productRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) certification_count FROM certification WHERE certification_id NOT IN (666,777)");
        assertThat((Long) productRowMap.get("certification_count"), is(new Long(
                SUPPLIER_APPROVAL_SQL_PA_OR_WFI_CERTIFICATION_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) certification_audit_count FROM certification_audit WHERE certification_audit_id NOT IN (668,779)");
        assertThat((Long) partyAuditRowMap.get("certification_audit_count"), is(new Long(
                SUPPLIER_APPROVAL_SQL_PA_OR_WFI_CERTIFICATION_AUDIT_COUNT)));
    }

    @Test
    public void updateInsertsProfileApprovalIntoEventTableOnly() throws Exception {
        SupplierApproval approval = new SupplierApproval(99912L, "gPjx7mYN", "Pending Approval Supplier 99912", null,
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.WAITING_FOR_INFORMATION, 3, 999121L, "zaphodb",
                new Date());

        supplierApprovalDao.update(approval);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_status WHERE party_id = ? AND party_audit_status_id NOT IN (9991211)",
                approval.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(approval.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(approval.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(approval.getVersion()));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(approval.getApprovalStatus().getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT count(*) party_count FROM party");
        assertThat((Long) partyRowMap.get("party_count"), is(new Long(DATA_SQL_PARTY_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) party_audit_count FROM party_audit");
        assertThat((Long) partyAuditRowMap.get("party_audit_count"), is(new Long(DATA_SQL_PARTY_AUDIT_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_AUDIT_COUNT)));
    }

    @Test
    public void updateInsertsProductApprovalIntoEventTableOnly() throws Exception {
        SupplierApproval approval = new SupplierApproval(88821L, "88821", "Pending Approval Product 88821", null,
                SupplierApproval.PRODUCT_TYPE, ApprovalStatus.WAITING_FOR_INFORMATION, 5, 88851L, "zaphodb", new Date());

        supplierApprovalDao.update(approval);

        Map<String, Object> productAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit_status WHERE product_id = ? AND product_audit_status_id NOT IN (88861)",
                approval.getId());
        assertThat((Long) productAuditStatusRowMap.get("product_id"), is(approval.getId()));
        assertThat((Long) productAuditStatusRowMap.get("product_audit_id"), is(approval.getSnapshotId()));
        assertThat((Integer) productAuditStatusRowMap.get("version_number"), is(approval.getVersion()));
        assertThat(productAuditStatusRowMap.get("event_type").toString(), is(approval.getApprovalStatus().getName()));
        assertThat((Date) productAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> productRowMap = jdbcTemplate.queryForMap("SELECT count(*) product_count FROM product");
        assertThat((Long) productRowMap.get("product_count"), is(new Long(DATA_SQL_PRODUCT_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PRODUCT_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) product_audit_count FROM product_audit");
        assertThat((Long) partyAuditRowMap.get("product_audit_count"), is(new Long(DATA_SQL_PRODUCT_AUDIT_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PRODUCT_AUDIT_COUNT)));
    }

    @Test
    public void updateInsertsCertificationApprovalIntoEventTableOnly() throws Exception {
        SupplierApproval approval = new SupplierApproval(77711L, "77711", "Pending Approval Certification 77711", null,
                SupplierApproval.CERTIFICATION_TYPE, ApprovalStatus.WAITING_FOR_INFORMATION, 5, 777111L, "zaphodb",
                new Date());

        supplierApprovalDao.insert(approval);

        Map<String, Object> productAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM certification_audit_status WHERE certification_id = ? AND certification_audit_status_id NOT IN (7771111)",
                        approval.getId());
        assertThat((Long) productAuditStatusRowMap.get("certification_id"), is(approval.getId()));
        assertThat((Long) productAuditStatusRowMap.get("certification_audit_id"), is(approval.getSnapshotId()));
        assertThat((Integer) productAuditStatusRowMap.get("version_number"), is(approval.getVersion()));
        assertThat(productAuditStatusRowMap.get("event_type").toString(), is(approval.getApprovalStatus().getName()));
        assertThat((Date) productAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> productRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) certification_count FROM certification WHERE certification_id NOT IN (666,777)");
        assertThat((Long) productRowMap.get("certification_count"), is(new Long(
                SUPPLIER_APPROVAL_SQL_PA_OR_WFI_CERTIFICATION_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) certification_audit_count FROM certification_audit WHERE certification_audit_id NOT IN (668,779)");
        assertThat((Long) partyAuditRowMap.get("certification_audit_count"), is(new Long(
                SUPPLIER_APPROVAL_SQL_PA_OR_WFI_CERTIFICATION_AUDIT_COUNT)));
    }

    private void assertThatApprovalMatchesExpectedValuesForSupplier24(SupplierApproval approval) {
        assertThat(approval.getId(), is(24L));
        assertThat(approval.getTitle(), is("Vogon Battlefleet"));
        assertThat(approval.getType(), is(SupplierApproval.PROFILE_TYPE));
        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.PENDING_APPROVAL));
        assertThat(approval.getVersion(), is(1));
        assertThat(approval.getSnapshotId(), is(2401L));
        assertThat(approval.getLastUpdatedBy(), is("UserDataLoad"));
        assertThat(approval.getLastUpdatedAt(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 3, 15, 23, 00).getTime()));
    }

    private void assertThatApprovalMatchesExpectedValuesForSupplier25(SupplierApproval approval) {
        assertThat(approval.getId(), is(25L));
        assertThat(approval.getTitle(), is("Jedi Enterprises"));
        assertThat(approval.getType(), is(SupplierApproval.PROFILE_TYPE));
        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.WAITING_FOR_INFORMATION));
        assertThat(approval.getVersion(), is(1));
        assertThat(approval.getSnapshotId(), is(2501L));
        assertThat(approval.getLastUpdatedBy(), is("UserDataLoad"));
        assertThat(approval.getLastUpdatedAt(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 8, 15, 23, 00).getTime()));
    }

    private void assertThatApprovalMatchesExpectedValuesForSupplier99912(SupplierApproval approval) {
        assertThat(approval.getId(), is(99912L));
        assertThat(approval.getTitle(), is("Pending Approval Supplier 99912"));
        assertThat(approval.getType(), is(SupplierApproval.PROFILE_TYPE));
        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.PENDING_APPROVAL));
        assertThat(approval.getVersion(), is(1));
        assertThat(approval.getSnapshotId(), is(999121L));
        assertThat(approval.getLastUpdatedBy(), is("SupplierUser"));
        assertThat(approval.getLastUpdatedAt(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 27, 16, 38, 12).getTime()));
    }

    private void assertThatApprovalMatchesExpectedValuesForSupplier99913(SupplierApproval approval) {
        assertThat(approval.getId(), is(99913L));
        assertThat(approval.getTitle(), is("Waiting for Info Supplier 99913"));
        assertThat(approval.getType(), is(SupplierApproval.PROFILE_TYPE));
        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.WAITING_FOR_INFORMATION));
        assertThat(approval.getVersion(), is(2));
        assertThat(approval.getSnapshotId(), is(999131L));
        assertThat(approval.getLastUpdatedBy(), is("ModeratorUser"));
        assertThat(approval.getLastUpdatedAt(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 28, 7, 45, 13).getTime()));
    }

    private void assertThatApprovalMatchesExpectedValuesForProduct88821(SupplierApproval approval) {
        assertThat(approval.getId(), is(88821L));
        assertThat(approval.getTitle(), is("Pending Approval Product 88821"));
        assertThat(approval.getType(), is(SupplierApproval.PRODUCT_TYPE));
        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.PENDING_APPROVAL));
        assertThat(approval.getVersion(), is(3));
        assertThat(approval.getSnapshotId(), is(88851L));
        assertThat(approval.getLastUpdatedBy(), is("SupplierUser"));
        assertThat(approval.getLastUpdatedAt(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 12, 16, 46, 0).getTime()));
    }

    private void assertThatApprovalMatchesExpectedValuesForProduct88822(SupplierApproval approval) {
        assertThat(approval.getId(), is(88822L));
        assertThat(approval.getTitle(), is("Waiting for Info Product 88822"));
        assertThat(approval.getType(), is(SupplierApproval.PRODUCT_TYPE));
        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.WAITING_FOR_INFORMATION));
        assertThat(approval.getVersion(), is(4));
        assertThat(approval.getSnapshotId(), is(88852L));
        assertThat(approval.getLastUpdatedBy(), is("ModeratorUser"));
        assertThat(approval.getLastUpdatedAt(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 12, 16, 55, 0).getTime()));
    }

    private void assertThatApprovalMatchesExpectedValuesForCertification77711(SupplierApproval approval) {
        assertThat(approval.getId(), is(77711L));
        assertThat(approval.getTitle(), is("Pend Approval Cert 77711"));
        assertThat(approval.getType(), is(SupplierApproval.CERTIFICATION_TYPE));
        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.PENDING_APPROVAL));
        assertThat(approval.getVersion(), is(5));
        assertThat(approval.getSnapshotId(), is(777111L));
        assertThat(approval.getLastUpdatedBy(), is("SupplierUser"));
        assertThat(approval.getLastUpdatedAt(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 13, 12, 21, 0).getTime()));
    }

    private void assertThatApprovalMatchesExpectedValuesForCertification77712(SupplierApproval approval) {
        assertThat(approval.getId(), is(77712L));
        assertThat(approval.getTitle(), is("Waiting for Info Cert 77712"));
        assertThat(approval.getType(), is(SupplierApproval.CERTIFICATION_TYPE));
        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.WAITING_FOR_INFORMATION));
        assertThat(approval.getVersion(), is(6));
        assertThat(approval.getSnapshotId(), is(777121L));
        assertThat(approval.getLastUpdatedBy(), is("ModeratorUser"));
        assertThat(approval.getLastUpdatedAt(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 13, 12, 23, 0).getTime()));
    }

}
