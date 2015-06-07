package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class EmployeeFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        EmployeeFilter filter = new EmployeeFilter(null, null, "title", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(EmployeeFilter.USERNAME), is(nullValue()));
        assertThat(filter.getStringCriterion(EmployeeFilter.EMAIL), is(nullValue()));
    }

    @Test
    public void filterHasUsernamePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        EmployeeFilter filter = new EmployeeFilter("user", null, "title", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(EmployeeFilter.USERNAME), is("user"));
        assertThat(filter.getStringCriterion(EmployeeFilter.EMAIL), is(nullValue()));
    }

    @Test
    public void filterHasEmailCriterionWhenCreatedWithNonNullValue() throws Exception {
        EmployeeFilter filter = new EmployeeFilter(null, "emailAddress", "title", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(EmployeeFilter.USERNAME), is(nullValue()));
        assertThat(filter.getStringCriterion(EmployeeFilter.EMAIL), is("emailAddress"));
    }

}
