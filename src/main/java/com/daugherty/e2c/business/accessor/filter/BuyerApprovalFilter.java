package com.daugherty.e2c.business.accessor.filter;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BuyerApproval;

/**
 * Filter criteria for SupplierApproval retrieval operations.
 */
public class BuyerApprovalFilter extends BaseFilter<BuyerApproval> {

    public static final String TITLE_PREFIX = "title_prefix";
    public static final String EMAIL_PREFIX = "email_prefix";
    public static final String STATUS = "status";
    public static final String NEW_BUYER = "new_buyer";

    public BuyerApprovalFilter(String titlePrefix, String emailPrefix, ApprovalStatus approvalStatus, Boolean newBuyer,
            String sortBy, Boolean sortDesc, Integer startItem, Integer count) {
        super(sortBy, sortDesc, startItem, count, null);
        addStringCriterion(TITLE_PREFIX, titlePrefix);
        addStringCriterion(EMAIL_PREFIX, emailPrefix);
        addStringCriterion(STATUS, approvalStatus);
        addBooleanCriterion(NEW_BUYER, newBuyer);
    }
}
