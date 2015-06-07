package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CompanyTest {

    @Test
    public void getWebsiteReturnsNullIfNull() {
        Company company = new Company(null, null, null, null, null, null, null, null, null, null, null);
        assertThat(company.getWebsite(), is(nullValue()));
    }

    @Test
    public void getWebsiteReturnsNullIfEmpty() {
        Company company = new Company(null, null, null, null, null, "", null, null, null, null, null);
        assertThat(company.getWebsite(), is(nullValue()));
    }

    @Test
    public void getWebsiteReturnsNullIfBlank() {
        Company company = new Company(null, null, null, null, null, "  ", null, null, null, null, null);
        assertThat(company.getWebsite(), is(nullValue()));
    }

    @Test
    public void getWebsitePrefixesWithHttpIfNotBlankAndDoesNotStartWithHttp() {
        Company company = new Company(null, null, null, null, null, "www.website.com", null, null, null, null, null);
        assertThat(company.getWebsite(), is("http://www.website.com"));
    }

    @Test
    public void getWebsiteDoesNotPrefixWithHttpIfAlreadyStartsWithHttp() {
        Company company = new Company(null, null, null, null, null, "http://www.website.com", null, null, null, null,
                null);
        assertThat(company.getWebsite(), is("http://www.website.com"));
    }

}
