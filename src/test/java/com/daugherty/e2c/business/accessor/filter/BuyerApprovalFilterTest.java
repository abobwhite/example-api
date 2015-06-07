package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.daugherty.e2c.domain.ApprovalStatus;

public class BuyerApprovalFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        BuyerApprovalFilter filter = new BuyerApprovalFilter(null, null, null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.TITLE_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.STATUS), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.NEW_BUYER), is(nullValue()));
    }

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsBlanksOrEmptyStringsOnly() throws Exception {
        BuyerApprovalFilter filter = new BuyerApprovalFilter("", "", null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.TITLE_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.STATUS), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.NEW_BUYER), is(nullValue()));
    }

    @Test
    public void filterHasTitlePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        BuyerApprovalFilter filter = new BuyerApprovalFilter("prefix", null, null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.TITLE_PREFIX), is("prefix"));
    }

    @Test
    public void filterHasEmailPrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        BuyerApprovalFilter filter = new BuyerApprovalFilter(null, "email", null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.EMAIL_PREFIX), is("email"));
    }

    @Test
    public void filterHasStatusCriterionWhenCreatedWithNonNullValue() throws Exception {
        BuyerApprovalFilter filter = new BuyerApprovalFilter(null, null, ApprovalStatus.PENDING_APPROVAL, null,
                "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(BuyerApprovalFilter.STATUS), is(ApprovalStatus.PENDING_APPROVAL.getName()));
    }

    @Test
    public void filterHasBuyerCriterionWhenCreatedWithNonNullValue() throws Exception {
        BuyerApprovalFilter filter = new BuyerApprovalFilter(null, null, ApprovalStatus.PENDING_APPROVAL, true,
                "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(BuyerApprovalFilter.NEW_BUYER), is(Boolean.TRUE));
    }

}
