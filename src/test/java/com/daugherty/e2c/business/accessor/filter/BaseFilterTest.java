package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.daugherty.e2c.domain.Entity;

public class BaseFilterTest {

    @Test
    public void constructorWithValuesPopulatesFieldsWithThoseValues() {
        BaseFilter<Entity> filter = new BaseFilter<Entity>("property", true, 26, 50, Locale.CHINESE) {
        };
        assertThat(filter.getSortBy(), is("property"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));
        assertThat(filter.getLocale(), is(Locale.CHINESE));
    }

    @Test
    public void constructorWithoutValuesPopulatesFieldsWithDefaultValues() {
        BaseFilter<Entity> filter = new BaseFilter<Entity>(null, null, null, null, null) {
        };
        assertThat(filter.getSortBy(), is(nullValue()));
        assertThat(filter.isSortDescending(), is(false));
        assertThat(filter.getStartItem(), is(1));
        assertThat(filter.getCount(), is(250));
        assertThat(filter.getLocale(), is(Locale.ENGLISH));
    }

}
