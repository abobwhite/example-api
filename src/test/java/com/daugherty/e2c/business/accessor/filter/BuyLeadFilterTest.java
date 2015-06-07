package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.daugherty.e2c.domain.ProductCategory;

public class BuyLeadFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        BuyLeadFilter filter = new BuyLeadFilter(null, null, null, null, null, null, true, 26, 50);

        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(BuyLeadFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getProductCategoryCriterion(BuyLeadFilter.PRODUCT_CATEGORY), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyLeadFilter.PROVINCE), is(nullValue()));
        assertThat(filter.getBooleanCriterion(BuyLeadFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasEmailCriterionWhenCreatedWithNonNullValue() throws Exception {
        BuyLeadFilter filter = new BuyLeadFilter("email", null, null, null, null, null, true, 26, 50);

        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(BuyLeadFilter.EMAIL_PREFIX), is("email"));
        assertThat(filter.getProductCategoryCriterion(BuyLeadFilter.PRODUCT_CATEGORY), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyLeadFilter.PROVINCE), is(nullValue()));
        assertThat(filter.getBooleanCriterion(BuyLeadFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasProductCategoryCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductCategory productCategory = new ProductCategory(10L);
        
		BuyLeadFilter filter = new BuyLeadFilter(null, productCategory, null, null, null, null, true, 26, 50);

        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(BuyLeadFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getProductCategoryCriterion(BuyLeadFilter.PRODUCT_CATEGORY), is(productCategory));
        assertThat(filter.getStringCriterion(BuyLeadFilter.PROVINCE), is(nullValue()));
        assertThat(filter.getBooleanCriterion(BuyLeadFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasProvinceCriterionWhenCreatedWithNonNullValue() throws Exception {
		BuyLeadFilter filter = new BuyLeadFilter(null, null, "province", null, null, null, true, 26, 50);
		
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(BuyLeadFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getProductCategoryCriterion(BuyLeadFilter.PRODUCT_CATEGORY), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyLeadFilter.PROVINCE), is("province"));
        assertThat(filter.getBooleanCriterion(BuyLeadFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasIncludeExpiredCriterionWhenCreatedWithNonNullValue() throws Exception {
		BuyLeadFilter filter = new BuyLeadFilter(null, null, null, null, true, null, true, 26, 50);
		
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(BuyLeadFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getProductCategoryCriterion(BuyLeadFilter.PRODUCT_CATEGORY), is(nullValue()));
        assertThat(filter.getStringCriterion(BuyLeadFilter.PROVINCE), is(nullValue()));
        assertThat(filter.getBooleanCriterion(BuyLeadFilter.INCLUDE_EXPIRED), is(true));
    }

}
