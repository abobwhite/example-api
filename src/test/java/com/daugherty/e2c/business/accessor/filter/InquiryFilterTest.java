package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class InquiryFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        InquiryFilter filter = new InquiryFilter(null, null, null, null, "whatever", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getLongCriterion(InquiryFilter.SENDER_ID), is(nullValue()));
        assertThat(filter.getBooleanCriterion(InquiryFilter.UNSUBMITTED), is(nullValue()));
    }

    @Test
    public void filterHasSenderIdCriterionWhenCreatedWithNonNullValue() throws Exception {
        InquiryFilter filter = new InquiryFilter(42L, null, null, null, "whatever", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getLongCriterion(InquiryFilter.SENDER_ID), is(42L));
    }

    @Test
    public void filterHasUnsubmittedCriterionWhenCreatedWithNonNullValue() throws Exception {
        InquiryFilter filter = new InquiryFilter(null, true, null, null, "whatever", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(InquiryFilter.UNSUBMITTED), is(true));
    }

    @Test
    public void filterHasDisapprovedCriterionWhenCreatedWithNonNullValue() throws Exception {
        InquiryFilter filter = new InquiryFilter(null, null, true, null, "whatever", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(InquiryFilter.DISAPPROVED), is(true));
    }

    @Test
    public void filterHasOriginatorCriterionWhenCreatedWithNonNullValue() throws Exception {
        InquiryFilter filter = new InquiryFilter(null, null, null, "Orgiginator", "whatever", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(InquiryFilter.ORGINATOR_COMPANY), is("Orgiginator"));
    }

}
