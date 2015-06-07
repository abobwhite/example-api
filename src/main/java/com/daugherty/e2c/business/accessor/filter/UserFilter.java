package com.daugherty.e2c.business.accessor.filter;

import org.apache.commons.lang3.StringUtils;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.User;

/**
 *
 */
public class UserFilter extends BaseFilter<User> {

    public static final String USERNAME_PREFIX = "username_prefix";
    public static final String FIRST_NAME_PREFIX = "first_name_prefix";
    public static final String LAST_NAME_PREFIX = "last_name_prefix";
    public static final String EMAIL_PREFIX = "email_prefix";
    public static final String COMPANY_NAME_PREFIX = "company_name_prefix";
    public static final String APPROVAL_STATUS = "approval_status";
    public static final String ROLE = "role";
    public static final String PRESENCE = "presence";
    public static final String ACCESS = "access_status";

    public static final String LOCKED = "locked";
    public static final String BLOCKED = "blocked";

    public UserFilter(String usernamePrefix, String firstNamePrefix, String lastNamePrefix, String emailPrefix,
                              String companyNamePrefix, ApprovalStatus approvalStatus, String role, Boolean presence,
                              String access, String sortBy, Boolean sortDesc, Integer startItem, Integer count) {
        super(sortBy, sortDesc, startItem, count, null);
        addStringCriterion(USERNAME_PREFIX, usernamePrefix);
        addStringCriterion(FIRST_NAME_PREFIX, firstNamePrefix);
        addStringCriterion(LAST_NAME_PREFIX, lastNamePrefix);
        addStringCriterion(EMAIL_PREFIX, emailPrefix);
        addStringCriterion(COMPANY_NAME_PREFIX, companyNamePrefix);
        addStringCriterion(APPROVAL_STATUS, approvalStatus);
        addStringCriterion(ROLE, role);
        addBooleanCriterion(PRESENCE, presence);
        addBooleanCriterion(LOCKED, is(access, "Locked"));
        addBooleanCriterion(BLOCKED, is(access, "Blocked"));
    }

    private Boolean is(String access, String value) {
        if (StringUtils.isNotBlank(access)) {
            return access.equals(value);
        }
        return null;
    }
}
