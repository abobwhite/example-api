package com.daugherty.e2c.domain.visitor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.daugherty.e2c.domain.ApprovalStatus;

/**
 *
 */
public class ModerateVisitorTest {

    private static final int CURRENT_VERSION = 5;

    private final ModerateVisitor visitor = new ModerateVisitor();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getNewVersionForUnprofiledStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.UNPROFILED + " to moderator");

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.UNPROFILED);
    }

    @Test
    public void getNewVersionForDraftStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.DRAFT + " to moderator");

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.DRAFT);
    }

    @Test
    public void getNewVersionForPendingTranslationStatusReturnsExistingVersion() {
        assertThat(visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.PENDING_TRANSLATION), is(CURRENT_VERSION));
    }

    @Test
    public void getNewVersionForWaitingForInformationStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException
                .expectMessage("Cannot send something that is " + ApprovalStatus.WAITING_FOR_INFORMATION + " to moderator");

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.WAITING_FOR_INFORMATION);
    }

    @Test
    public void getNewVersionForPendingApprovalStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.PENDING_APPROVAL + " to moderator");

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.PENDING_APPROVAL);
    }

    @Test
    public void getNewVersionForApprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.APPROVED + " to moderator");

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.APPROVED);
    }

    @Test
    public void getNewVersionForDisapprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.DISAPPROVED + " to moderator");

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.DISAPPROVED);
    }

    @Test
    public void getNewApprovalStatusForUnprofiledStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.UNPROFILED + " to moderator");

        visitor.getNewApprovalStatus(ApprovalStatus.UNPROFILED);
    }

    @Test
    public void getNewApprovalStatusForDraftStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.DRAFT + " to moderator");

        visitor.getNewApprovalStatus(ApprovalStatus.DRAFT);
    }

    @Test
    public void getNewApprovalStatusForPendingTranslationStatusReturnsPendingApprovalStatus() {
        assertThat(visitor.getNewApprovalStatus(ApprovalStatus.PENDING_TRANSLATION), is(ApprovalStatus.PENDING_APPROVAL));
    }

    @Test
    public void getNewApprovalStatusForWaitingForInformationStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException
                .expectMessage("Cannot send something that is " + ApprovalStatus.WAITING_FOR_INFORMATION + " to moderator");

        visitor.getNewApprovalStatus(ApprovalStatus.WAITING_FOR_INFORMATION);
    }

    @Test
    public void getNewApprovalStatusForPendingTranslationStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.PENDING_APPROVAL + " to moderator");

        visitor.getNewApprovalStatus(ApprovalStatus.PENDING_APPROVAL);
    }

    @Test
    public void getNewApprovalStatusForApprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.APPROVED + " to moderator");

        visitor.getNewApprovalStatus(ApprovalStatus.APPROVED);
    }

    @Test
    public void getNewApprovalStatusForDisapprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot send something that is " + ApprovalStatus.DISAPPROVED + " to moderator");

        visitor.getNewApprovalStatus(ApprovalStatus.DISAPPROVED);
    }
}
