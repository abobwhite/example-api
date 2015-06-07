package com.daugherty.e2c.domain.visitor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.daugherty.e2c.domain.ApprovalStatus;

public class ChangeDataVisitorTest {

    private static final int CURRENT_VERSION = 5;

    private final ChangeDataVisitor visitor = new ChangeDataVisitor();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getNewVersionForUnprofiledStatusReturnsExistingVersion() {
        assertThat(visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.UNPROFILED), is(CURRENT_VERSION));
    }

    @Test
    public void getNewVersionForDraftStatusReturnsExistingVersion() {
        assertThat(visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.DRAFT), is(CURRENT_VERSION));
    }

    @Test
    public void getNewVersionForPendingApprovalStatusReturnsExistingVersion() {
        assertThat(visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.PENDING_APPROVAL), is(CURRENT_VERSION));
    }

    @Test
    public void getNewVersionForWaitingForInformationStatusReturnsExistingVersion() {
        assertThat(visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.WAITING_FOR_INFORMATION), is(CURRENT_VERSION));
    }

    @Test
    public void getNewVersionForPendingTranslationStatusReturnsExistingVersion() {
        assertThat(visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.PENDING_TRANSLATION), is(CURRENT_VERSION));
    }

    @Test
    public void getNewVersionForApprovedStatusReturnsIncrementedVersion() {
        assertThat(visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.APPROVED), is(1 + CURRENT_VERSION));
    }

    @Test
    public void getNewVersionForDisapprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot change data of something that is " + ApprovalStatus.DISAPPROVED);

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.DISAPPROVED);
    }

    @Test
    public void getNewApprovalStatusForUnprofiledStatusReturnsDraftStatus() {
        assertThat(visitor.getNewApprovalStatus(ApprovalStatus.UNPROFILED), is(ApprovalStatus.DRAFT));
    }

    @Test
    public void getNewApprovalStatusForDraftStatusReturnsDraftStatus() {
        assertThat(visitor.getNewApprovalStatus(ApprovalStatus.DRAFT), is(ApprovalStatus.DRAFT));
    }

    @Test
    public void getNewApprovalStatusForPendingApprovalStatusReturnsPendingApprovalStatus() {
        assertThat(visitor.getNewApprovalStatus(ApprovalStatus.PENDING_APPROVAL), is(ApprovalStatus.PENDING_APPROVAL));
    }

    @Test
    public void getNewApprovalStatusForWaitingForInformationStatusReturnsDraftStatus() {
        assertThat(visitor.getNewApprovalStatus(ApprovalStatus.WAITING_FOR_INFORMATION), is(ApprovalStatus.DRAFT));
    }

    @Test
    public void getNewApprovalStatusForPendingTranslationStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException
                .expectMessage("Cannot change data of something that is " + ApprovalStatus.PENDING_TRANSLATION);

        visitor.getNewApprovalStatus(ApprovalStatus.PENDING_TRANSLATION);
    }

    @Test
    public void getNewApprovalStatusForApprovedStatusReturnsDraftStatus() {
        assertThat(visitor.getNewApprovalStatus(ApprovalStatus.APPROVED), is(ApprovalStatus.DRAFT));
    }

    @Test
    public void getNewApprovalStatusForDisapprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot change data of something that is " + ApprovalStatus.DISAPPROVED);

        visitor.getNewApprovalStatus(ApprovalStatus.DISAPPROVED);
    }

}
