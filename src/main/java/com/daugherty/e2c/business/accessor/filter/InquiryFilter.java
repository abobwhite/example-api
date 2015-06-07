package com.daugherty.e2c.business.accessor.filter;

import com.daugherty.e2c.domain.Inquiry;

/**
 * Filter criteria for Inquiry retrieval operations.
 */
public class InquiryFilter extends BaseFilter<Inquiry> {

    public static final String SENDER_ID = "senderId";
    public static final String UNSUBMITTED = "unsubmitted";
    public static final String DISAPPROVED = "disapproved";
    public static final String ORGINATOR_COMPANY = "originatorCompanyName";

    public InquiryFilter(Long senderId, Boolean unsubmitted, Boolean disapproved, String orginatorCompany,
            String sortBy, Boolean sortDescending, Integer startItem, Integer count) {
        super(sortBy, sortDescending, startItem, count, null);
        addLongCriterion(SENDER_ID, senderId);
        addBooleanCriterion(UNSUBMITTED, unsubmitted);
        addBooleanCriterion(DISAPPROVED, disapproved);
        addStringCriterion(ORGINATOR_COMPANY, orginatorCompany);
    }

}
