package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MessageFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        MessageFilter filter = new MessageFilter(null, null, null, "lastInteractionSentTime", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getLongCriterion(MessageFilter.RECEIVER_ID), is(nullValue()));
        assertThat(filter.getLongCriterion(MessageFilter.FLAGGED), is(nullValue()));
        assertThat(filter.getLongCriterion(MessageFilter.SENDER_ID), is(nullValue()));
    }

    @Test
    public void filterHasReceiverIdCriterionWhenCreatedWithNonNullValue() throws Exception {
        MessageFilter filter = new MessageFilter("jKNz4P4q", null, null, "lastInteractionSentTime", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(MessageFilter.RECEIVER_ID), is("jKNz4P4q"));
    }

    @Test
    public void filterHasFlaggedCriterionWhenCreatedWithNonNullValue() throws Exception {
        MessageFilter filter = new MessageFilter("jKNz4P4q", true, null, "lastInteractionSentTime", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(MessageFilter.FLAGGED), is(true));
    }

    @Test
    public void filterHasSenderIdCriterionWhenCreatedWithNonNullValue() throws Exception {
        MessageFilter filter = new MessageFilter(null, null, "jKNz4P4q", "lastInteractionSentTime", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(MessageFilter.SENDER_ID), is("jKNz4P4q"));
    }

}
