package com.daugherty.e2c.domain.visitor;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;

/**
 * Performs the state transition of an Approvable when a translator sends a translation back to moderator.
 */
@Component("moderateVisitor")
public class ModerateVisitor implements ApprovalStateTransitionVisitor {

    @Override
    public int getNewVersion(int currentVersion, ApprovalStatus currentApprovalStatus) {
        if (currentApprovalStatus.equals(ApprovalStatus.PENDING_TRANSLATION)) {
            return currentVersion;
        } else {
            throw new IllegalStateException("Cannot send something that is " + currentApprovalStatus + " to moderator");
        }
    }

    @Override
    public ApprovalStatus getNewApprovalStatus(ApprovalStatus currentApprovalStatus) {
        if (currentApprovalStatus.equals(ApprovalStatus.PENDING_TRANSLATION)) {
            return ApprovalStatus.PENDING_APPROVAL;
        } else {
            throw new IllegalStateException("Cannot send something that is " + currentApprovalStatus + " to moderator");
        }
    }
}
