package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

/**
 *
 */
public class JdbcBuyerApprovalDaoTest extends BaseJdbcDaoTest {
    private static final int BUYER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT = 4;
    private static final int SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_AUDIT_COUNT = 2;

    @Inject
    private JdbcBuyerApprovalDao buyerApprovalDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("buyer-approval.sql");
    }

    @Test
    public void getAllReturnsAllProfilesInPendingApprovalOrWaitingForInformationState() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = buyerApprovalDao.createSortingAndPaginationCriteria(null, false,
                1, 25, Locale.ENGLISH);
        List<BuyerApproval> approvals = buyerApprovalDao.getAll(sortingAndPaginationCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(BUYER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT));

        assertBuyerApproval(getBuyerApproval(approvals, 99923), 99923, "Pending Approval Buyer 99923",
                ApprovalStatus.PENDING_APPROVAL, 1, 999223L, "BuyerUser", new GregorianCalendar(2013, Calendar.AUGUST,
                        27, 16, 38, 12).getTime());

        assertBuyerApproval(getBuyerApproval(approvals, 99924), 99924, "Waiting for Info Buyer 99924",
                ApprovalStatus.WAITING_FOR_INFORMATION, 2, 999233L, "ModeratorUser", new GregorianCalendar(2013,
                        Calendar.AUGUST, 28, 7, 45, 13).getTime());

    }

    @Test
    public void getAllSortsByTitle() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = buyerApprovalDao.createSortingAndPaginationCriteria(
                BuyerApproval.TITLE_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<BuyerApproval> approvals = buyerApprovalDao.getAll(sortingAndPaginationCriteria);
        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(BUYER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT));
        Comparator<BuyerApproval> titleComparator = new Comparator<BuyerApproval>() {
            @Override
            public int compare(BuyerApproval o1, BuyerApproval o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        };
        assertOrder(approvals, titleComparator);
    }

    @Test
    public void getAllSortsByStatus() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = buyerApprovalDao.createSortingAndPaginationCriteria(
                BuyerApproval.STATUS_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<BuyerApproval> approvals = buyerApprovalDao.getAll(sortingAndPaginationCriteria);
        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(BUYER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT));
        Comparator<BuyerApproval> statusComparator = new Comparator<BuyerApproval>() {
            @Override
            public int compare(BuyerApproval o1, BuyerApproval o2) {
                return o1.getApprovalStatus().compareTo(o2.getApprovalStatus());
            }
        };
        assertOrder(approvals, statusComparator);
    }

    @Test
    public void getAllSortsByLastUpdatedBy() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = buyerApprovalDao.createSortingAndPaginationCriteria(
                BuyerApproval.LAST_UPDATED_BY_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<BuyerApproval> approvals = buyerApprovalDao.getAll(sortingAndPaginationCriteria);
        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(BUYER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT));
        Comparator<BuyerApproval> updatedByComparaor = new Comparator<BuyerApproval>() {
            @Override
            public int compare(BuyerApproval o1, BuyerApproval o2) {
                return o1.getLastUpdatedBy().compareTo(o2.getLastUpdatedBy());
            }
        };
        assertOrder(approvals, updatedByComparaor);
    }

    @Test
    public void getAllSortsByLastUpdatedDate() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = buyerApprovalDao.createSortingAndPaginationCriteria(
                BuyerApproval.LAST_UPDATED_AT_SERIAL_PROPERTY, false, 1, 25, Locale.ENGLISH);
        List<BuyerApproval> approvals = buyerApprovalDao.getAll(sortingAndPaginationCriteria);
        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(BUYER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT));
        Comparator<BuyerApproval> updatedDateComparaor = new Comparator<BuyerApproval>() {
            @Override
            public int compare(BuyerApproval o1, BuyerApproval o2) {
                return o1.getLastUpdatedAt().compareTo(o2.getLastUpdatedAt());
            }
        };
        assertOrder(approvals, updatedDateComparaor);
    }

    @Test
    public void getAllPaginates() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = buyerApprovalDao.createSortingAndPaginationCriteria(null, false,
                2, 25, Locale.ENGLISH);
        List<BuyerApproval> approvals = buyerApprovalDao.getAll(sortingAndPaginationCriteria);
        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(BUYER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT - 1));
    }

    @Test
    public void findWithTitlePrefixCriterionOnlyReturnsMatchingApprovals() throws Exception {
        QueryCriteria queryCriteria = buyerApprovalDao.createQueryCriteria("Waiting for", null, null, null, null,
                false, 1, 25);
        List<BuyerApproval> approvals = buyerApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(1));
        assertBuyerApproval(getBuyerApproval(approvals, 99924), 99924, "Waiting for Info Buyer 99924",
                ApprovalStatus.WAITING_FOR_INFORMATION, 2, 999233L, "ModeratorUser", new GregorianCalendar(2013,
                        Calendar.AUGUST, 28, 7, 45, 13).getTime());

    }

    @Test
    public void findWithApprovalStatusCriterionOnlyReturnsMatchingApprovals() throws Exception {
        QueryCriteria queryCriteria = buyerApprovalDao.createQueryCriteria(null, null,
                ApprovalStatus.PENDING_APPROVAL.getName(), null, null, false, 1, 25);
        List<BuyerApproval> approvals = buyerApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(2));
    }

    @Test
    public void findWithCriteriaReturnsEmptyListIfThereAreNoMatchingApprovals() throws Exception {
        QueryCriteria queryCriteria = buyerApprovalDao.createQueryCriteria("Waiting ABCDJKJ", null,
                ApprovalStatus.PENDING_APPROVAL.getName(), null, null, false, 1, 25);
        List<BuyerApproval> approvals = buyerApprovalDao.find(queryCriteria);

        assertThat(approvals, is(notNullValue()));
        assertThat(approvals.size(), is(0));
    }

    @Test
    public void loadByBuyerIdThatExistsReturnsBuyerApproval() {
        BuyerApproval approval = buyerApprovalDao.load(99923L);
        assertBuyerApproval(approval, 99923, "Pending Approval Buyer 99923", ApprovalStatus.PENDING_APPROVAL, 1,
                999223L, "BuyerUser", new GregorianCalendar(2013, Calendar.AUGUST, 27, 16, 38, 12).getTime());
    }

    @Test
    public void loadByIdThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        buyerApprovalDao.load(123456789L);
    }

    @Test
    public void updateInsertsProfileApprovalIntoEventTableOnly() throws Exception {
        BuyerApproval approval = new BuyerApproval(99923L, "p0b8BjgV", "Pending Approval Buyer 99923", null, null, null,
                ApprovalStatus.WAITING_FOR_INFORMATION, 3, 999223L, "tmcmillian", new Date());

        buyerApprovalDao.update(approval);

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
                + BUYER_APPROVAL_SQL_PA_OR_WFI_PARTY_COUNT - 2)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) party_audit_count FROM party_audit");
        assertThat((Long) partyAuditRowMap.get("party_audit_count"), is(new Long(DATA_SQL_PARTY_AUDIT_COUNT
                + SUPPLIER_APPROVAL_SQL_PA_OR_WFI_PARTY_AUDIT_COUNT)));
    }

    private void assertBuyerApproval(BuyerApproval approval, long id, String title, ApprovalStatus approvalStatus,
            int version, long snapshotId, String updatedBy, Date updateDate) {
        assertThat(approval.getId(), is(id));
        assertThat(approval.getTitle(), is(title));
        assertThat(approval.getApprovalStatus(), is(approvalStatus));
        assertThat(approval.getVersion(), is(version));
        assertThat(approval.getSnapshotId(), is(snapshotId));
        assertThat(approval.getLastUpdatedBy(), is(updatedBy));
        assertThat(approval.getLastUpdatedAt(), is(updateDate));
    }

    private BuyerApproval getBuyerApproval(List<BuyerApproval> list, int id) {
        for (BuyerApproval approval : list) {
            if (approval.getId() == id) {
                return approval;
            }
        }
        return null;
    }

    private List<BuyerApproval> sort(List<BuyerApproval> approvals, Comparator<BuyerApproval> comparator) {
        List<BuyerApproval> sortedList = new ArrayList<BuyerApproval>(approvals);
        Collections.sort(sortedList, comparator);
        return sortedList;
    }

    private void assertOrder(List<BuyerApproval> approvals, Comparator<BuyerApproval> comparator) {
        List<BuyerApproval> sortedList = sort(approvals, comparator);
        for (BuyerApproval approval : approvals) {
            assertThat(approvals.indexOf(approval), is(sortedList.indexOf(approval)));
        }
    }
}
