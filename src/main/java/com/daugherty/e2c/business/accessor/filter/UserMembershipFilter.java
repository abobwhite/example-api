package com.daugherty.e2c.business.accessor.filter;

import com.daugherty.e2c.domain.UserMembership;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/8/14
 * Time: 2:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserMembershipFilter extends BaseFilter<UserMembership> {
    public static final String USERNAME_PREFIX = "username_prefix";
    public static final String EMAIL_PREFIX = "email_prefix";
    public static final String COMPANY_NAME_PREFIX = "company_name_prefix";
    public static final String MEMBERSHIP_FILTER = "membership_level";

    public UserMembershipFilter(String usernamePrefix, String emailPrefix, String companyNamePrefix, Integer membershipLevel,
                                String sortBy, Boolean sortDesc, Integer startItem, Integer count) {

        super(sortBy, sortDesc, startItem, count, null);
        addStringCriterion(USERNAME_PREFIX, usernamePrefix);
        addStringCriterion(EMAIL_PREFIX, emailPrefix);
        addStringCriterion(COMPANY_NAME_PREFIX, companyNamePrefix);
        addIntegerCriterion(MEMBERSHIP_FILTER, membershipLevel);
    }

}
