package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.SitemapUrl;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcSitemapUrlDaoTest extends BaseJdbcDaoTest {

    private static final String GLOBAL_BASE_URL = "https://www.export-to-china.com/";
    private static final String BUYER_BASE_URL = "http://www.huijinkou.com/";

    @Inject
    private JdbcSitemapUrlDao dao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("product.sql");
    }

    @Test
    public void getUntranslatedPublishedProductUrlsReturnsUrlsWithEnglishNames() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = dao.createSortingAndPaginationCriteria(null, false, 1, 123,
                Locale.ENGLISH);
        List<SitemapUrl> urls = dao.getUntranslatedPublishedProductUrls(sortingAndPaginationCriteria);

        assertThat(urls, is(notNullValue()));
        assertThat(urls.size(), is(2));
        assertThat(urls.get(0).getLocation(), is(GLOBAL_BASE_URL + "product/41/Black+Beans+%28ABOB%29"));
        assertThat(urls.get(0).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 8, 15, 23, 0).getTime()));
        assertThat(urls.get(1).getLocation(), is(GLOBAL_BASE_URL + "product/24/Product+24"));
        assertThat(urls.get(1).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 25, 18, 46, 0).getTime()));
    }

    @Test
    public void getUntranslatedApprovedSupplierUrlsReturnsUrls() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = dao.createSortingAndPaginationCriteria(null, false, 1, 123,
                Locale.ENGLISH);
        List<SitemapUrl> urls = dao.getUntranslatedApprovedSupplierUrls(sortingAndPaginationCriteria);

        assertThat(urls, is(notNullValue()));
        assertThat(urls.size(), is(4));
        assertThat(urls.get(0).getLocation(), is(GLOBAL_BASE_URL + "supplier/Y40dgNWM"));
        assertThat(urls.get(0).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 7, 19, 49, 0).getTime()));
        assertThat(urls.get(1).getLocation(), is(GLOBAL_BASE_URL + "supplier/690O6NL4"));
        assertThat(urls.get(1).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 7, 19, 49, 0).getTime()));
        assertThat(urls.get(2).getLocation(), is(GLOBAL_BASE_URL + "supplier/3VlkjQDP"));
        assertThat(urls.get(2).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 16, 13, 13, 13).getTime()));
        assertThat(urls.get(3).getLocation(), is(GLOBAL_BASE_URL + "supplier/RPekAL6N"));
        assertThat(urls.get(3).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 16, 13, 13, 13).getTime()));
    }

    @Test
    public void getTranslatedPublishedProductUrlsReturnsUrlsWithPinyinNames() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = dao.createSortingAndPaginationCriteria(null, false, 1, 123,
                Locale.CHINESE);
        List<SitemapUrl> urls = dao.getTranslatedPublishedProductUrls(sortingAndPaginationCriteria);

        assertThat(urls, is(notNullValue()));
        assertThat(urls.size(), is(2));

        assertThat(urls.get(0).getLocation(), is(BUYER_BASE_URL + "product/41/Translation+of+Black+Beans+%28ABOB%29"));
        assertThat(urls.get(0).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 8, 15, 23, 0).getTime()));
        assertThat(urls.get(1).getLocation(), is(BUYER_BASE_URL + "product/24/null"));
        assertThat(urls.get(1).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.SEPTEMBER, 25, 18, 46, 0).getTime()));
    }

    @Test
    public void getTranslatedApprovedSupplierUrlsReturnsUrls() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = dao.createSortingAndPaginationCriteria(null, false, 1, 123,
                Locale.CHINESE);
        List<SitemapUrl> urls = dao.getTranslatedApprovedSupplierUrls(sortingAndPaginationCriteria);

        assertThat(urls, is(notNullValue()));
        assertThat(urls.size(), is(4));
        assertThat(urls.get(0).getLocation(), is(BUYER_BASE_URL + "supplier/Y40dgNWM"));
        assertThat(urls.get(0).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 7, 19, 49, 0).getTime()));
        assertThat(urls.get(1).getLocation(), is(BUYER_BASE_URL + "supplier/690O6NL4"));
        assertThat(urls.get(1).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 7, 19, 49, 0).getTime()));
        assertThat(urls.get(2).getLocation(), is(BUYER_BASE_URL + "supplier/3VlkjQDP"));
        assertThat(urls.get(2).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 16, 13, 13, 13).getTime()));
        assertThat(urls.get(3).getLocation(), is(BUYER_BASE_URL + "supplier/RPekAL6N"));
        assertThat(urls.get(3).getLastModification(),
                is(new GregorianCalendar(2013, Calendar.AUGUST, 16, 13, 13, 13).getTime()));
    }

    @Test
    public void getBuyerCategoryUrlsReturnsUrls() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = dao.createSortingAndPaginationCriteria(null, false, 1, 1000,
                Locale.CHINESE);
        List<SitemapUrl> urls = dao.getBuyerCategoryUrls(sortingAndPaginationCriteria);
        System.out.println("urls = " + urls.size());

        assertThat(urls, is(notNullValue()));
        assertThat(urls.size(), is(252));
        assertThat(urls.get(0).getLocation(), is(BUYER_BASE_URL + "product/category/1"));
        assertThat(urls.get(0).getLastModification().after(new GregorianCalendar().getTime()), equalTo(false));
    }

    @Test
    public void getSupplierCategoryUrlsReturnsUrls() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = dao.createSortingAndPaginationCriteria(null, false, 1, 1000,
                Locale.ENGLISH);
        List<SitemapUrl> urls = dao.getSupplierCategoryUrls(sortingAndPaginationCriteria);
        System.out.println("urls = " + urls.size());

        assertThat(urls, is(notNullValue()));
        assertThat(urls.size(), is(252));
        assertThat(urls.get(0).getLocation(), is(GLOBAL_BASE_URL + "product/category/1"));
        assertThat(urls.get(0).getLastModification().after(new GregorianCalendar().getTime()), equalTo(false));
    }

}
