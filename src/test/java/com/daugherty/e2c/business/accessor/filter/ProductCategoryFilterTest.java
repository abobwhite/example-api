package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.daugherty.e2c.domain.ProductCategory;

public class ProductCategoryFilterTest {

    @Test
    public void constructorPopulatesSortingAndPaginationFieldsWithDefaultValues() {
        ProductCategoryFilter filter = new ProductCategoryFilter(null, null, Locale.CHINESE);
        assertThat(filter.getSortBy(), is(ProductCategory.NAME_SERIAL_PROPERTY));
        assertThat(filter.isSortDescending(), is(Boolean.FALSE));
        assertThat(filter.getStartItem(), is(1));
        assertThat(filter.getCount(), is(5000));
        assertThat(filter.getLocale(), is(Locale.CHINESE));
    }

    @Test
    public void filterHasNoCriteriaWhenPopulatedWithNullValues() {
        ProductCategoryFilter filter = new ProductCategoryFilter(null, null, Locale.CHINESE);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getIntegerCriterion(ProductCategoryFilter.LEVEL2_COUNT), is(nullValue()));
    }

    @Test
    public void filterHasLevel2CountCriterionWhenPopulatedWithNotNullValue() {
        ProductCategoryFilter filter = new ProductCategoryFilter(8, null, Locale.CHINESE);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getIntegerCriterion(ProductCategoryFilter.LEVEL2_COUNT), is(8));
    }

    @Test
    public void filterHasCategoryIdCriterionWhenPopulatedWithNotNullValue() {
        ProductCategoryFilter filter = new ProductCategoryFilter(null, 42L, Locale.CHINESE);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getLongCriterion(ProductCategoryFilter.CATEGORY_ID), is(42L));
    }

}
