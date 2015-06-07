package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.BuyerApprovalFilter;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.e2c.persistence.data.BuyerApprovalReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyerApprovalAccessorTest {
    private static final BuyerApproval APPROVAL_42 = new BuyerApproval(42L, "jKNz4P4q", "first", "last", "title", "email",
            ApprovalStatus.PENDING_APPROVAL, 3, 242L, "lastUpdater", new Date());
    private static final List<BuyerApproval> DAO_APPROVALS = Lists.newArrayList(APPROVAL_42);

    @Mock
    private BuyerApprovalReadDao buyerApprovalReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final BuyerApprovalAccessor accessor = new BuyerApprovalAccessor();

    @Test
    public void findWithoutFilterCriteriaCallsDaoGetAllMethod() throws Exception {
        BuyerApprovalFilter emptyFilter = new BuyerApprovalFilter(null, null, null, null, null, null, null, null);
        when(buyerApprovalReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(buyerApprovalReadDao.getAll(queryCriteria)).thenReturn(DAO_APPROVALS);

        List<BuyerApproval> approvals = accessor.find(emptyFilter);

        verify(buyerApprovalReadDao).getAll(queryCriteria);
        assertThat(approvals, is(DAO_APPROVALS));
    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        when(
                buyerApprovalReadDao.createQueryCriteria("prefix", "email", ApprovalStatus.PENDING_APPROVAL.getName(),
                        null, "title", true, 26, 50)).thenReturn(queryCriteria);
        when(buyerApprovalReadDao.find(queryCriteria)).thenReturn(DAO_APPROVALS);

        BuyerApprovalFilter filter = new BuyerApprovalFilter("prefix", "email", ApprovalStatus.PENDING_APPROVAL, null,
                "title", true, 26, 50);
        List<BuyerApproval> approvals = accessor.find(filter);

        assertThat(approvals, is(DAO_APPROVALS));
    }

    @Test
    public void loadDelegatesToDao() {
        when(buyerApprovalReadDao.load(42L)).thenReturn(APPROVAL_42);

        BuyerApproval persistedApproval = accessor.load(42L, Locale.ENGLISH);

        assertThat(persistedApproval, is(APPROVAL_42));
    }
}
