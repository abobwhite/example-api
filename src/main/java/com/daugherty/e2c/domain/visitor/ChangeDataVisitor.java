package com.daugherty.e2c.domain.visitor;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;

/**
 * Performs the state transition of an Approvable entity after the user changes data.
 */
@Component("changeDataVisitor")
public class ChangeDataVisitor implements ApprovalStateTransitionVisitor {

    @Override
    public int getNewVersion(int currentVersion, ApprovalStatus currentApprovalStatus) {
        if (ApprovalStatus.DISAPPROVED.equals(currentApprovalStatus)) {
            throw new IllegalStateException("Cannot change data of something that is " + currentApprovalStatus);
        } else if (ApprovalStatus.APPROVED.equals(currentApprovalStatus)) {
            return 1 + currentVersion;
        } else {
            return currentVersion;
        }
    }

    @Override
    public ApprovalStatus getNewApprovalStatus(ApprovalStatus currentApprovalStatus) {
        if (ApprovalStatus.DISAPPROVED.equals(currentApprovalStatus)
                || ApprovalStatus.PENDING_TRANSLATION.equals(currentApprovalStatus)) {
            throw new IllegalStateException("Cannot change data of something that is " + currentApprovalStatus);
        } else if (ApprovalStatus.PENDING_APPROVAL.equals(currentApprovalStatus)) {
            return ApprovalStatus.PENDING_APPROVAL;
        } else {
            return ApprovalStatus.DRAFT;
        }
    }

}
