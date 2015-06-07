package com.daugherty.e2c.domain.visitor;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;

/**
 * Performs the state transition of an Approvable entity after the Moderator requests clarification.
 */
@Component("deferApprovalVisitor")
public class DeferApprovalVisitor implements ApprovalStateTransitionVisitor {

    @Override
    public int getNewVersion(int currentVersion, ApprovalStatus currentApprovalStatus) {
        if (ApprovalStatus.PENDING_APPROVAL.equals(currentApprovalStatus)) {
            return currentVersion;
        } else {
            throw new IllegalStateException("Cannot defer approval of something that is " + currentApprovalStatus);
        }
    }

    @Override
    public ApprovalStatus getNewApprovalStatus(ApprovalStatus currentApprovalStatus) {
        if (ApprovalStatus.PENDING_APPROVAL.equals(currentApprovalStatus)) {
            return ApprovalStatus.WAITING_FOR_INFORMATION;
        } else {
            throw new IllegalStateException("Cannot defer approval of something that is " + currentApprovalStatus);
        }
    }

}
