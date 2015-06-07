package com.daugherty.e2c.domain;

/**
 * Represents a state within the Buyer or Supplier's approval process (see Future State Architecture Design).
 */
public enum ApprovalStatus {
    UNPROFILED(1L, "Unprofiled"), // Buyer & Supplier
    DRAFT(2L, "Draft"), // Buyer & Supplier
    PENDING_APPROVAL(3L, "Pending Approval"), // Buyer & Supplier
    APPROVED(4L, "Approved"), // Buyer & Supplier
    DISAPPROVED(5L, "Disapproved"), // Buyer & Supplier
    WAITING_FOR_INFORMATION(6L, "Waiting for Information"), // Buyer & Supplier
    PENDING_TRANSLATION(7L, "Pending Translation"), // Supplier only
    PROVISIONAL(11L, "Provisional"), // Membership
    PAID(12L, "Paid"); // Membership

    private Long id;
    private String name;

    private ApprovalStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ApprovalStatus findById(Long id) {
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        throw new IllegalArgumentException(id + " is not a valid ApprovalStatus Id");
    }

    public static ApprovalStatus findByName(String name) {
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException(name + " is not a valid ApprovalStatus Name");
    }

    @Override
    public String toString() {
        return name;
    }
}
