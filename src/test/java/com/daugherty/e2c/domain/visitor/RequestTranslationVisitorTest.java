package com.daugherty.e2c.domain.visitor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.daugherty.e2c.domain.ApprovalStatus;

public class RequestTranslationVisitorTest {

    private static final int CURRENT_VERSION = 5;

    private final RequestTranslationVisitor visitor = new RequestTranslationVisitor();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getNewVersionForUnprofiledStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException
                .expectMessage("Cannot request translation for something that is " + ApprovalStatus.UNPROFILED);

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.UNPROFILED);
    }

    @Test
    public void getNewVersionForDraftStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is " + ApprovalStatus.DRAFT);

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.DRAFT);
    }

    @Test
    public void getNewVersionForPendingApprovalStatusReturnsExistingVersion() {
        assertThat(visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.PENDING_APPROVAL), is(CURRENT_VERSION));
    }

    @Test
    public void getNewVersionForWaitingForInformationStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is "
                + ApprovalStatus.WAITING_FOR_INFORMATION);

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.WAITING_FOR_INFORMATION);
    }

    @Test
    public void getNewVersionForPendingTranslationStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is "
                + ApprovalStatus.PENDING_TRANSLATION);

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.PENDING_TRANSLATION);
    }

    @Test
    public void getNewVersionForApprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is " + ApprovalStatus.APPROVED);

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.APPROVED);
    }

    @Test
    public void getNewVersionForDisapprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is "
                + ApprovalStatus.DISAPPROVED);

        visitor.getNewVersion(CURRENT_VERSION, ApprovalStatus.DISAPPROVED);
    }

    @Test
    public void getNewApprovalStatusForUnprofiledStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException
                .expectMessage("Cannot request translation for something that is " + ApprovalStatus.UNPROFILED);

        visitor.getNewApprovalStatus(ApprovalStatus.UNPROFILED);
    }

    @Test
    public void getNewApprovalStatusForDraftStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is " + ApprovalStatus.DRAFT);

        visitor.getNewApprovalStatus(ApprovalStatus.DRAFT);
    }

    @Test
    public void getNewApprovalStatusForPendingApprovalStatusReturnsPendingTranslationStatus() {
        assertThat(visitor.getNewApprovalStatus(ApprovalStatus.PENDING_APPROVAL),
                is(ApprovalStatus.PENDING_TRANSLATION));
    }

    @Test
    public void getNewApprovalStatusForWaitingForInformationStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is "
                + ApprovalStatus.WAITING_FOR_INFORMATION);

        visitor.getNewApprovalStatus(ApprovalStatus.WAITING_FOR_INFORMATION);
    }

    @Test
    public void getNewApprovalStatusForPendingTranslationStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is "
                + ApprovalStatus.PENDING_TRANSLATION);

        visitor.getNewApprovalStatus(ApprovalStatus.PENDING_TRANSLATION);
    }

    @Test
    public void getNewApprovalStatusForApprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is " + ApprovalStatus.APPROVED);

        visitor.getNewApprovalStatus(ApprovalStatus.APPROVED);
    }

    @Test
    public void getNewApprovalStatusForDisapprovedStatusThrowsException() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot request translation for something that is "
                + ApprovalStatus.DISAPPROVED);

        visitor.getNewApprovalStatus(ApprovalStatus.DISAPPROVED);
    }

}
