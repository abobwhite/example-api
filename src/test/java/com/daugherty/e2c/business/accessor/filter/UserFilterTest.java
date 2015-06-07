package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.daugherty.e2c.domain.ApprovalStatus;

public class UserFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        UserFilter filter = new UserFilter(null, null, null, null, null, null, null, null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(UserFilter.USERNAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.FIRST_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.LAST_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.COMPANY_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.APPROVAL_STATUS), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.ROLE), is(nullValue()));
    }

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsBlanksOrEmptyStringsOnly() throws Exception {
        UserFilter filter = new UserFilter("", "", null, "", null, null, "", null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(UserFilter.USERNAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.FIRST_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.LAST_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.COMPANY_NAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.APPROVAL_STATUS), is(nullValue()));
        assertThat(filter.getStringCriterion(UserFilter.ROLE), is(nullValue()));
    }

    @Test
    public void filterHasUsernamePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter("prefix", null, null, null, null, null, null, null, null, "title", true, 26,
                50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(UserFilter.USERNAME_PREFIX), is("prefix"));
    }

    @Test
    public void filterHasFirstNamePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter(null, "prefix", null, null, null, null, null, null, null, "title", true, 26,
                50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(UserFilter.FIRST_NAME_PREFIX), is("prefix"));
    }

    @Test
    public void filterHasLastNamePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter(null, null, "prefix", null, null, null, null, null, null, "title", true, 26,
                50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(UserFilter.LAST_NAME_PREFIX), is("prefix"));
    }

    @Test
    public void filterHasEmailPrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter(null, null, null, "prefix", null, null, null, null, null, "title", true, 26,
                50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(UserFilter.EMAIL_PREFIX), is("prefix"));
    }

    @Test
    public void filterHasCompanyNamePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter(null, null, null, null, "prefix", null, null, null, null, "title", true, 26,
                50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(UserFilter.COMPANY_NAME_PREFIX), is("prefix"));
    }

    @Test
    public void filterHasRoleCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter(null, null, null, null, null, null, "ROLE", null, null, "title", true, 26,
                50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(UserFilter.ROLE), is("ROLE"));
    }

    @Test
    public void filterHasApprovalStatusCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter(null, null, null, null, null, ApprovalStatus.APPROVED, null, null, null,
                "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(UserFilter.APPROVAL_STATUS), is("Approved"));
    }

    @Test
    public void filterHasLockedCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter(null, null, null, null, null, ApprovalStatus.APPROVED, null, null, "Locked",
                "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(UserFilter.LOCKED), is(true));
    }

    @Test
    public void filterHasBlockedCriterionWhenCreatedWithNonNullValue() throws Exception {
        UserFilter filter = new UserFilter(null, null, null, null, null, ApprovalStatus.APPROVED, null, null,
                "Blocked", "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(UserFilter.BLOCKED), is(true));
    }
}
