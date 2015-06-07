package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.daugherty.e2c.domain.SubscriptionType;

public class DiscountFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        DiscountFilter filter = new DiscountFilter(null, null, null, null, null, null, true, 26, 50, Locale.ENGLISH);

        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_CODE), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_TYPE), is(nullValue()));
        assertThat(filter.getIntegerCriterion(DiscountFilter.MEMBERSHIP_LEVEL), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE), is(nullValue()));
        assertThat(filter.getBooleanCriterion(DiscountFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasCodeCriterionWhenCreatedWithNonNullValue() throws Exception {
        DiscountFilter filter = new DiscountFilter("code", null, null, null, null, null, true, 26, 50, Locale.ENGLISH);

        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_CODE), is("code"));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_TYPE), is(nullValue()));
        assertThat(filter.getIntegerCriterion(DiscountFilter.MEMBERSHIP_LEVEL), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE), is(nullValue()));
        assertThat(filter.getBooleanCriterion(DiscountFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasTypeCriterionWhenCreatedWithNonNullValue() throws Exception {
        DiscountFilter filter = new DiscountFilter(null, "Dollar", null, null, null, null, true, 26, 50, Locale.ENGLISH);

        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_CODE), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_TYPE), is("D"));
        assertThat(filter.getIntegerCriterion(DiscountFilter.MEMBERSHIP_LEVEL), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE), is(nullValue()));
        assertThat(filter.getBooleanCriterion(DiscountFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasMembershipLevelCriterionWhenCreatedWithNonNullValue() throws Exception {
        DiscountFilter filter = new DiscountFilter(null, null, 3, null, null, null, true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_CODE), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_TYPE), is(nullValue()));
        assertThat(filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), is(3));
        assertThat(filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE), is(nullValue()));
        assertThat(filter.getBooleanCriterion(DiscountFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasSubscriptionTypeCriterionWhenCreatedWithNonNullValue() throws Exception {
        DiscountFilter filter = new DiscountFilter(null, null, null, SubscriptionType.UPGRADE, null, null, true, 26,
                50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_CODE), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_TYPE), is(nullValue()));
        assertThat(filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE), is(SubscriptionType.UPGRADE.getName()));
        assertThat(filter.getBooleanCriterion(DiscountFilter.INCLUDE_EXPIRED), is(nullValue()));
    }

    @Test
    public void filterHasIncludeExpiredCriterionWhenCreatedWithNonNullValue() throws Exception {
        DiscountFilter filter = new DiscountFilter(null, null, null, SubscriptionType.UPGRADE, Boolean.TRUE, null,
                true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_CODE), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.DISCOUNT_TYPE), is(nullValue()));
        assertThat(filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), is(nullValue()));
        assertThat(filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE), is(SubscriptionType.UPGRADE.getName()));
        assertThat(filter.getBooleanCriterion(DiscountFilter.INCLUDE_EXPIRED), is(Boolean.TRUE));
    }

}
