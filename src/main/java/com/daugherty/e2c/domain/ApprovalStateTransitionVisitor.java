package com.daugherty.e2c.domain;

/**
 * Defines operations for changing the state of Approvable objects.
 * <p>
 * TODO There will be multiple implementations of this interface to encapsulate state transitions (e.g.
 * ChangeDataVisitor, SubmitForApprovalVisitor, DeferApprovalVisitor, ApproveVisitor, RequestTranslationVisitor,
 * DisapproveVisitor).
 */
public interface ApprovalStateTransitionVisitor {

    int getNewVersion(int currentVersion, ApprovalStatus currentApprovalStatus);

    ApprovalStatus getNewApprovalStatus(ApprovalStatus currentApprovalStatus);

}
