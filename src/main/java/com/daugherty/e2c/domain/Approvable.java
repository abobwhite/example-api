package com.daugherty.e2c.domain;

/**
 * Defines operations supporting Domain Objects whose changes are subject to review and approval.
 */
public interface Approvable {
    ApprovalStatus getApprovalStatus();

    Integer getVersion();

    Long getSnapshotId();

    void setSnapshotId(Long snapshotId);

    void visit(ApprovalStateTransitionVisitor stateTransitionVisitor);
}
