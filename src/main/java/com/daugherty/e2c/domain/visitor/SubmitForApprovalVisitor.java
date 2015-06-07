package com.daugherty.e2c.domain.visitor;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;

/**
 * Performs the state transition of an Approvable entity after the user submits completed changes for approval.
 */
@Component("submitForApprovalVisitor")
public class SubmitForApprovalVisitor implements ApprovalStateTransitionVisitor {

    @Override
    public int getNewVersion(int currentVersion, ApprovalStatus currentApprovalStatus) {
        if (ApprovalStatus.DRAFT.equals(currentApprovalStatus)) {
            return currentVersion;
        } else {
            throw new IllegalStateException("Cannot submit for approval something that is " + currentApprovalStatus);
        }
    }

    @Override
    public ApprovalStatus getNewApprovalStatus(ApprovalStatus currentApprovalStatus) {
        if (ApprovalStatus.DRAFT.equals(currentApprovalStatus)) {
            return ApprovalStatus.PENDING_APPROVAL;
        } else {
            throw new IllegalStateException("Cannot submit for approval something that is " + currentApprovalStatus);
        }
    }

}
