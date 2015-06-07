package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SupplierTranslationFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        SupplierTranslationFilter filter = new SupplierTranslationFilter(null, null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TITLE_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TYPE), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TRANSLATED), is(nullValue()));
    }

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsBlanksOrEmptyStringsOnly() throws Exception {
        SupplierTranslationFilter filter = new SupplierTranslationFilter("", "  ", null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TITLE_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TYPE), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TRANSLATED), is(nullValue()));
    }

    @Test
    public void filterHasTitlePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        SupplierTranslationFilter filter = new SupplierTranslationFilter("prefix", null, null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TITLE_PREFIX), is("prefix"));
    }

    @Test
    public void filterHasTypeCriterionWhenCreatedWithNonNullValue() throws Exception {
        SupplierTranslationFilter filter = new SupplierTranslationFilter(null, "type", null, "title", true, 26, 50);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TYPE), is("type"));
    }
}
