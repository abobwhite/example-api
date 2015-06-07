package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.ProductCategory;

public class ProductFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, null, null, null, null, "name",
                true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(ProductFilter.COUNTRY), is(nullValue()));
        assertThat(filter.getLongCriterion(ProductFilter.CATEGORY), is(nullValue()));
        assertThat(filter.getBooleanCriterion(ProductFilter.HOT), is(nullValue()));
        assertThat(filter.getLongCriterion(ProductFilter.BUSINESS_TYPE), is(nullValue()));
        assertThat(filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), is(nullValue()));
        assertThat(filter.getStringCriterion(ProductFilter.PUBLIC_SUPPLIER_ID), is(nullValue()));
        assertThat(filter.getStringCriterion(ProductFilter.PRODUCT_NAME), is(nullValue()));
        assertThat(filter.getStringCriterion(ProductFilter.STATUS), is(nullValue()));
        assertThat(filter.getBooleanCriterion(ProductFilter.PUBLISHED), is(nullValue()));
        assertThat(filter.getBooleanCriterion(ProductFilter.PRODUCT_IDS), is(nullValue()));
    }

    @Test
    public void filterHasCountryCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter("country", null, null, null, null, null, null, null, null, null,
                "name", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(ProductFilter.COUNTRY), is("country"));
    }

    @Test
    public void filterHasCategoryCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, new ProductCategory(42L), null, null, null, null, null, null,
                null, null, "name", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getProductCategoryCriterion(ProductFilter.CATEGORY), is(new ProductCategory(42L)));
    }

    @Test
    public void filterHasHotCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, true, null, null, null, null, null, null, null, "name",
                true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(ProductFilter.HOT), is(true));
    }

    @Test
    public void filterHasBusinessTypeCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, null, 42L, null, null, null, null, null, null, "name",
                true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getLongCriterion(ProductFilter.BUSINESS_TYPE), is(42L));
    }

    @Test
    public void filterHasMembershipLevelCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, null, null, 3, null, null, null, null, null, "name", true,
                26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), is(3));
    }

    @Test
    public void filterHasSupplierIdCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, "opNq1Nl5", null, null, null, "name",
                true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(ProductFilter.PUBLIC_SUPPLIER_ID), is("opNq1Nl5"));
    }

    @Test
    public void filterHasProductNameCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, null, "productName", null, null,
                "name", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(ProductFilter.PRODUCT_NAME), is("productName"));
    }

    @Test
    public void filterHasStatusCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, null, null,
                ApprovalStatus.APPROVED, null, "name", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(ProductFilter.STATUS), is("Approved"));
    }

    @Test
    public void filterHasPublishedsCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, null, null, null, true, "name",
                true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getBooleanCriterion(ProductFilter.PUBLISHED), is(true));
    }

    @Test
    public void filterHasProductIdsCriterionWhenCreatedWithNonNullValue() throws Exception {
        ProductFilter filter = new ProductFilter(null, null, null, null, null, "ProductIds", null, null, null, true,
                "name", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(ProductFilter.PRODUCT_IDS), is("ProductIds"));
    }
}
