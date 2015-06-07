package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.security.UserDeleter;

@RunWith(MockitoJUnitRunner.class)
public class CertificationApprovalServiceTest {

    private static final SupplierApproval CERTIFICATION_APPROVAL = new SupplierApproval(42L, "42",
            "certification title", new Supplier(42L), SupplierApproval.CERTIFICATION_TYPE,
            ApprovalStatus.PENDING_APPROVAL, 3, 242L, "lastUpdater", new Date());

    @Mock
    private Accessor<SupplierApproval> accessor;
    @Mock
    private Mutator<SupplierApproval> mutator;
    @Mock
    private ApprovalStateTransitionVisitor submitForApprovalVisitor;
    @Mock
    private ApprovalStateTransitionVisitor deferApprovalVisitor;
    @Mock
    private ApprovalStateTransitionVisitor disapproveVisitor;
    @Mock
    private ApprovalStateTransitionVisitor approveVisitor;
    @Mock
    private ApprovalStateTransitionVisitor requestTranslationVisitor;
    @Mock
    private UserDeleter userDeleter;

    @InjectMocks
    private final CertificationApprovalService service = new CertificationApprovalService();

    @Captor
    private ArgumentCaptor<Filter<SupplierApproval>> filterCaptor;
    @Captor
    private ArgumentCaptor<SupplierApproval> approvalCaptor;

    @Test
    public void submitForApprovalLoadsProxyApprovalAppliesSubmitForApprovalVisitorAndCreatesActualApproval()
            throws Exception {
        when(accessor.load(42L, Locale.ENGLISH)).thenReturn(CERTIFICATION_APPROVAL);

        when(submitForApprovalVisitor.getNewApprovalStatus(CERTIFICATION_APPROVAL.getApprovalStatus())).thenReturn(
                ApprovalStatus.PENDING_APPROVAL);
        when(
                submitForApprovalVisitor.getNewVersion(CERTIFICATION_APPROVAL.getVersion(),
                        CERTIFICATION_APPROVAL.getApprovalStatus())).thenReturn(23);

        service.submitForApproval(42L);

        assertThat(CERTIFICATION_APPROVAL.getApprovalStatus(), is(ApprovalStatus.PENDING_APPROVAL));
        assertThat(CERTIFICATION_APPROVAL.getVersion(), is(23));
        verify(mutator).create(CERTIFICATION_APPROVAL);

        verifyZeroInteractions(deferApprovalVisitor, disapproveVisitor, requestTranslationVisitor);
    }

    @Test
    public void deferApprovalLoadsProxyApprovalAppliesDeferApprovalVisitorAndUpdatesApproval() throws Exception {
        when(accessor.load(42L, Locale.ENGLISH)).thenReturn(CERTIFICATION_APPROVAL);

        when(deferApprovalVisitor.getNewApprovalStatus(CERTIFICATION_APPROVAL.getApprovalStatus())).thenReturn(
                ApprovalStatus.WAITING_FOR_INFORMATION);
        when(
                deferApprovalVisitor.getNewVersion(CERTIFICATION_APPROVAL.getVersion(),
                        CERTIFICATION_APPROVAL.getApprovalStatus())).thenReturn(23);

        service.deferApproval(42L);

        assertThat(CERTIFICATION_APPROVAL.getApprovalStatus(), is(ApprovalStatus.WAITING_FOR_INFORMATION));
        assertThat(CERTIFICATION_APPROVAL.getVersion(), is(23));
        verify(mutator).update(CERTIFICATION_APPROVAL);

        verifyZeroInteractions(submitForApprovalVisitor, disapproveVisitor, requestTranslationVisitor);
    }

    @Test
    public void disapproveLoadsProxyApprovalAppliesDisapproveVisitorAndUpdatesApproval() throws Exception {
        when(accessor.load(42L, Locale.ENGLISH)).thenReturn(CERTIFICATION_APPROVAL);

        when(disapproveVisitor.getNewApprovalStatus(CERTIFICATION_APPROVAL.getApprovalStatus())).thenReturn(
                ApprovalStatus.DISAPPROVED);
        when(
                disapproveVisitor.getNewVersion(CERTIFICATION_APPROVAL.getVersion(),
                        CERTIFICATION_APPROVAL.getApprovalStatus())).thenReturn(23);

        service.disapprove(42L);

        assertThat(CERTIFICATION_APPROVAL.getApprovalStatus(), is(ApprovalStatus.DISAPPROVED));
        assertThat(CERTIFICATION_APPROVAL.getVersion(), is(23));
        verify(mutator).update(CERTIFICATION_APPROVAL);

        verifyZeroInteractions(submitForApprovalVisitor, deferApprovalVisitor, requestTranslationVisitor);
    }

    @Test
    public void approveLoadsProxyApprovalAppliesApprovalVisitorAndUpdatesApproval() throws Exception {
        when(accessor.load(42L, Locale.ENGLISH)).thenReturn(CERTIFICATION_APPROVAL);

        when(approveVisitor.getNewApprovalStatus(CERTIFICATION_APPROVAL.getApprovalStatus())).thenReturn(
                ApprovalStatus.APPROVED);
        when(
                approveVisitor.getNewVersion(CERTIFICATION_APPROVAL.getVersion(),
                        CERTIFICATION_APPROVAL.getApprovalStatus())).thenReturn(23);

        service.approve(42L);

        assertThat(CERTIFICATION_APPROVAL.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(CERTIFICATION_APPROVAL.getVersion(), is(23));
        verify(mutator).update(CERTIFICATION_APPROVAL);

        verifyZeroInteractions(submitForApprovalVisitor, deferApprovalVisitor, disapproveVisitor);
    }
}
