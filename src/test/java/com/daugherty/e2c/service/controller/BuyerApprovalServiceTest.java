package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.BuyerApprovalFilter;
import com.daugherty.e2c.business.mapper.BuyerApprovalMapper;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.e2c.security.UserDeleter;
import com.daugherty.e2c.service.json.JsonBuyerApproval;
import com.daugherty.e2c.service.json.JsonBuyerApprovals;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyerApprovalServiceTest {
    private static final BuyerApproval APPROVAL = new BuyerApproval(1L, "vXP9l0Or", "profile title", "first", "last",
            "profile email", ApprovalStatus.PENDING_APPROVAL, 3, 242L, "lastUpdater", new Date());

    @Mock
    private Accessor<BuyerApproval> buyerApprovalAccessor;
    @Mock
    private Mutator<BuyerApproval> buyerApprovalMutator;
    @Mock
    private ApprovalStateTransitionVisitor submitForApprovalVisitor;
    @Mock
    private ApprovalStateTransitionVisitor deferApprovalVisitor;
    @Mock
    private ApprovalStateTransitionVisitor disapproveVisitor;
    @Mock
    private ApprovalStateTransitionVisitor approveVisitor;
    @Mock
    private UserDeleter userDeleter;
    @Mock
    private BuyerApprovalMapper buyerApprovalMapper;

    @InjectMocks
    private final BuyerApprovalService buyerApprovalService = new BuyerApprovalService();

    @Captor
    private ArgumentCaptor<Filter<BuyerApproval>> filterCaptor;

    @Test
    public void submitForApprovalLoadsProxyApprovalAppliesSubmitForApprovalVisitorAndCreatesActualApproval()
            throws Exception {
        BuyerApproval approval = new BuyerApproval(100L, "DkP2BmPw", "Surts Fireware", "first", "last", "Email",
                ApprovalStatus.DRAFT, 1, 242L, "fordp", new Date());
        when(buyerApprovalAccessor.load("DkP2BmPw", Locale.ENGLISH)).thenReturn(approval);

        when(submitForApprovalVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(
                ApprovalStatus.PENDING_APPROVAL);
        when(submitForApprovalVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus()))
                .thenReturn(23);

        buyerApprovalService.submitForApproval("DkP2BmPw");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.PENDING_APPROVAL));
        assertThat(approval.getVersion(), is(23));
        verify(buyerApprovalMutator).create(approval);

        verifyZeroInteractions(deferApprovalVisitor, disapproveVisitor);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveBuyerApprovalsWithQueryStringArgumentsDelegatesToAccessor() {
        when(buyerApprovalAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(APPROVAL));
        
        JsonBuyerApproval json = new JsonBuyerApproval();
        json.setId(APPROVAL.getPublicId());
        json.setApprovalStatus(APPROVAL.getApprovalStatus().getName());
        json.setTitle(APPROVAL.getTitle());
        json.setEmail(APPROVAL.getEmail());
        json.setVersion(APPROVAL.getVersion());
        json.setLastUpdatedAt(APPROVAL.getLastUpdatedAt());
        json.setLastUpdatedBy(APPROVAL.getLastUpdatedBy());
        
        when(buyerApprovalMapper.fromExistinDomainObject(APPROVAL)).thenReturn(json);

        JsonBuyerApprovals jsonApprovals = buyerApprovalService.retrieveBuyerApprovals("title", "email",
                ApprovalStatus.PENDING_APPROVAL.getName(), null, "title", Boolean.TRUE, 26, 50);

        verify(buyerApprovalAccessor).find(filterCaptor.capture());
        Filter<BuyerApproval> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(BuyerApprovalFilter.class)));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.TITLE_PREFIX), is("title"));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.EMAIL_PREFIX), is("email"));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.STATUS), is(ApprovalStatus.PENDING_APPROVAL.getName()));
        assertThat(filter.getSortBy(), is("title"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonApprovals, is(notNullValue()));
        assertThat(jsonApprovals.size(), is(1));
        assertThatJsonApprovalMatchesExpectedValues(jsonApprovals.get(0), APPROVAL);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveBuyerApprovalsWithEmptyArgumentsDelegatesToAccessor() {
        when(buyerApprovalAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(APPROVAL));
        
        JsonBuyerApproval json = new JsonBuyerApproval();
        json.setId(APPROVAL.getPublicId());
        json.setApprovalStatus(APPROVAL.getApprovalStatus().getName());
        json.setTitle(APPROVAL.getTitle());
        json.setEmail(APPROVAL.getEmail());
        json.setVersion(APPROVAL.getVersion());
        json.setLastUpdatedAt(APPROVAL.getLastUpdatedAt());
        json.setLastUpdatedBy(APPROVAL.getLastUpdatedBy());
        
        when(buyerApprovalMapper.fromExistinDomainObject(APPROVAL)).thenReturn(json);

        JsonBuyerApprovals jsonApprovals = buyerApprovalService.retrieveBuyerApprovals("", "", "", null, "",
                Boolean.FALSE, 1, 250);

        verify(buyerApprovalAccessor).find(filterCaptor.capture());
        Filter<BuyerApproval> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(BuyerApprovalFilter.class)));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.TITLE_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.STATUS), is(nullValue()));
        assertThat(filter.getSortBy(), is(""));
        assertThat(filter.isSortDescending(), is(false));
        assertThat(filter.getStartItem(), is(1));
        assertThat(filter.getCount(), is(250));

        assertThat(jsonApprovals, is(notNullValue()));
        assertThat(jsonApprovals.size(), is(1));
        assertThatJsonApprovalMatchesExpectedValues(jsonApprovals.get(0), APPROVAL);
    }

    private void assertThatJsonApprovalMatchesExpectedValues(JsonBuyerApproval jsonApproval, BuyerApproval approval) {
        assertThat(jsonApproval.getId(), is(approval.getPublicId()));
        assertThat(jsonApproval.getTitle(), is(approval.getTitle()));
        assertThat(jsonApproval.getEmail(), is(approval.getEmail()));
        assertThat(jsonApproval.getApprovalStatus(), is(approval.getApprovalStatus().getName()));
        assertThat(jsonApproval.getLastUpdatedBy(), is(approval.getLastUpdatedBy()));
        assertThat(jsonApproval.getLastUpdatedAt(), is(approval.getLastUpdatedAt()));
    }

    @Test
    public void deferApprovalLoadsProxyApprovalAppliesDeferApprovalVisitorAndUpdatesApproval() throws Exception {
        BuyerApproval approval = new BuyerApproval(100L, "DkP2BmPw", "Surts Fireware", "first", "last", "Email",
                ApprovalStatus.PENDING_APPROVAL, 1, 242L, "fordp", new Date());
        when(buyerApprovalAccessor.load("DkP2BmPw", Locale.ENGLISH)).thenReturn(approval);

        when(deferApprovalVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(
                ApprovalStatus.WAITING_FOR_INFORMATION);
        when(deferApprovalVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus())).thenReturn(23);

        buyerApprovalService.deferApproval("DkP2BmPw");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.WAITING_FOR_INFORMATION));
        assertThat(approval.getVersion(), is(23));
        verify(buyerApprovalMutator).update(approval);

        verifyZeroInteractions(submitForApprovalVisitor, disapproveVisitor);
    }

    @Test
    public void disapproveLoadsProxyApprovalAppliesDisapproveVisitorAndUpdatesApproval() throws Exception {
        BuyerApproval approval = new BuyerApproval(100L, "DkP2BmPw", "Surts Fireware", "first", "last", "Email",
                ApprovalStatus.PENDING_APPROVAL, 1, 242L, "fordp", new Date());
        when(buyerApprovalAccessor.load("DkP2BmPw", Locale.ENGLISH)).thenReturn(approval);

        when(disapproveVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(
                ApprovalStatus.DISAPPROVED);
        when(disapproveVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus())).thenReturn(23);

        buyerApprovalService.disapprove("DkP2BmPw");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.DISAPPROVED));
        assertThat(approval.getVersion(), is(23));
        verify(buyerApprovalMutator).update(approval);
        verify(userDeleter).deleteDisapprovedBuyer("DkP2BmPw");

        verifyZeroInteractions(submitForApprovalVisitor, deferApprovalVisitor);
    }

    @Test
    public void approveLoadsProxyApprovalAppliesApprovalVisitorAndUpdatesApproval() throws Exception {
        BuyerApproval approval = new BuyerApproval(100L, "DkP2BmPw", "Surts Fireware", "first", "last", "Email",
                ApprovalStatus.PENDING_APPROVAL, 1, 242L, "fordp", new Date());
        when(buyerApprovalAccessor.load("DkP2BmPw", Locale.ENGLISH)).thenReturn(approval);

        when(approveVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(ApprovalStatus.APPROVED);
        when(approveVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus())).thenReturn(23);

        buyerApprovalService.approve("DkP2BmPw");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(approval.getVersion(), is(23));
        verify(buyerApprovalMutator).update(approval);

        verifyZeroInteractions(submitForApprovalVisitor, deferApprovalVisitor, disapproveVisitor);
    }

}
