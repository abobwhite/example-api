package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.daugherty.e2c.domain.ApprovalStatus;

public class SupplierApprovalFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        SupplierApprovalFilter filter = new SupplierApprovalFilter(null, null, null, null, null, null, "title", true,
                26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TITLE_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TYPE), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.STATUS), is(nullValue()));
        assertThat(filter.getBooleanCriterion(SupplierApprovalFilter.NEW_SUPPLIER), is(nullValue()));
    }

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsBlanksOrEmptyStringsOnly() throws Exception {
        SupplierApprovalFilter filter = new SupplierApprovalFilter("", "", "  ", null, null, null, "title", true, 26,
                50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TITLE_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TYPE), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.STATUS), is(nullValue()));
        assertThat(filter.getBooleanCriterion(SupplierApprovalFilter.NEW_SUPPLIER), is(nullValue()));
    }

    @Test
    public void filterHasTitlePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        SupplierApprovalFilter filter = new SupplierApprovalFilter("prefix", null, null, null, null, null, "title",
                true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TITLE_PREFIX), is("prefix"));
    }

    @Test
    public void filterHasEmailPrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        SupplierApprovalFilter filter = new SupplierApprovalFilter(null, "email", null, null, null, null, "title",
                true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.EMAIL_PREFIX), is("email"));
    }

    @Test
    public void filterHasTypeCriterionWhenCreatedWithNonNullValue() throws Exception {
        SupplierApprovalFilter filter = new SupplierApprovalFilter(null, null, "type", null, null, null, "title", true,
                26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TYPE), is("type"));
    }

    @Test
    public void filterHasStatusCriterionWhenCreatedWithNonNullValue() throws Exception {
        SupplierApprovalFilter filter = new SupplierApprovalFilter(null, null, null, ApprovalStatus.PENDING_APPROVAL,
                null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.STATUS),
                is(ApprovalStatus.PENDING_APPROVAL.getName()));
    }

    @Test
    public void filterHasNewSupplierCriterionWhenCreatedWithNonNullValue() throws Exception {
        SupplierApprovalFilter filter = new SupplierApprovalFilter(null, null, null, ApprovalStatus.PENDING_APPROVAL,
                true, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(SupplierApprovalFilter.NEW_SUPPLIER), is(Boolean.TRUE));
    }

    @Test
    public void filterHasPaisSupplierCriterionWhenCreatedWithNonNullValue() throws Exception {
        SupplierApprovalFilter filter = new SupplierApprovalFilter(null, null, null, ApprovalStatus.PENDING_APPROVAL,
                null, true, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(SupplierApprovalFilter.PAID_SUPPLIER), is(Boolean.TRUE));
    }
}
