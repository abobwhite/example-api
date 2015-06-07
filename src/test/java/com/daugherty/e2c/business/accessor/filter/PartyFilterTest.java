package com.daugherty.e2c.business.accessor.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class PartyFilterTest {

    @Test
    public void filterHasNoCriteriaWhenCreatedWithNullsOnly() throws Exception {
        PartyFilter filter = new PartyFilter(null, null, "title", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(true));
        assertThat(filter.getStringCriterion(PartyFilter.USERNAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(PartyFilter.PARTY_TYPE), is(nullValue()));
    }

    @Test
    public void filterHasUsernamePrefixCriterionWhenCreatedWithNonNullValue() throws Exception {
        PartyFilter filter = new PartyFilter("user", null, "title", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(PartyFilter.USERNAME_PREFIX), is("user"));
        assertThat(filter.getStringCriterion(PartyFilter.PARTY_TYPE), is(nullValue()));
    }

    @Test
    public void filterHasPartyTypeCriterionWhenCreatedWithNonNullValue() throws Exception {
        PartyFilter filter = new PartyFilter(null, "partyType", "title", true, 26, 50, Locale.ENGLISH);
        assertThat(filter.hasNoCriteria(), is(false));
        assertThat(filter.getStringCriterion(PartyFilter.USERNAME_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(PartyFilter.PARTY_TYPE), is("partyType"));
    }

}
