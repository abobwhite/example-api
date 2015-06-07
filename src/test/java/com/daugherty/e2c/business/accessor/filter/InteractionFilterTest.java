package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class InteractionFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        InteractionFilter filter = new InteractionFilter(null, "sentTime", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getLongCriterion(InteractionFilter.PUBLIC_MESSAGE_ID), is(nullValue()));
    }

    @Test
    public void filterHasMessageIdCriterionWhenCreatedWithNonNullValue() throws Exception {
        InteractionFilter filter = new InteractionFilter("jKNz4P4q", "sentTime", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(InteractionFilter.PUBLIC_MESSAGE_ID), is("jKNz4P4q"));
    }

}
