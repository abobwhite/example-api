package com.daugherty.e2c.business.accessor.filter;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.SupplierApproval;

/**
 * Filter criteria for SupplierApproval retrieval operations.
 */
public class SupplierApprovalFilter extends BaseFilter<SupplierApproval> {

    public static final String TITLE_PREFIX = "title_prefix";
    public static final String EMAIL_PREFIX = "email_prefix";
    public static final String TYPE = "type";
    public static final String STATUS = "status";
    public static final String NEW_SUPPLIER = "new_supplier";
    public static final String PAID_SUPPLIER = "paid_supplier";

    public SupplierApprovalFilter(String titlePrefix, String emailPrefix, String approvalType,
            ApprovalStatus approvalStatus, Boolean newSupplier, Boolean paidSupplier, String sortBy, Boolean sortDesc,
            Integer startItem, Integer count) {
        super(sortBy, sortDesc, startItem, count, null);
        addStringCriterion(TITLE_PREFIX, titlePrefix);
        addStringCriterion(EMAIL_PREFIX, emailPrefix);
        addStringCriterion(TYPE, approvalType);
        addStringCriterion(STATUS, approvalStatus);
        addBooleanCriterion(NEW_SUPPLIER, newSupplier);
        addBooleanCriterion(PAID_SUPPLIER, paidSupplier);
    }
}
