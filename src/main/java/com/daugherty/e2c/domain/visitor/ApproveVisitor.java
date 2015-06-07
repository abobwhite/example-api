package com.daugherty.e2c.domain.visitor;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;

/**
 *
 */
@Component("approveVisitor")
public class ApproveVisitor implements ApprovalStateTransitionVisitor {

    @Override
    public int getNewVersion(int currentVersion, ApprovalStatus currentApprovalStatus) {
        if (currentApprovalStatus.equals(ApprovalStatus.PENDING_TRANSLATION)
                || currentApprovalStatus.equals(ApprovalStatus.PENDING_APPROVAL)) {
            return currentVersion;
        } else {
            throw new IllegalStateException("Cannot approve translation that is " + currentApprovalStatus);
        }
    }

    @Override
    public ApprovalStatus getNewApprovalStatus(ApprovalStatus currentApprovalStatus) {
        if (currentApprovalStatus.equals(ApprovalStatus.PENDING_TRANSLATION)
                || currentApprovalStatus.equals(ApprovalStatus.PENDING_APPROVAL)) {
            return ApprovalStatus.APPROVED;
        } else {
            throw new IllegalStateException("Cannot approve translation that is " + currentApprovalStatus);
        }
    }
}
