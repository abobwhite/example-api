package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.SitemapUrlFilter;
import com.daugherty.e2c.domain.SitemapUrl;
import com.daugherty.e2c.persistence.data.SitemapUrlReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SitemapUrlAccessorTest {

    private static final SitemapUrl PRODUCT_SITEMAP_URL = SitemapUrl.createProductUrl(1L, "name", new Date(), false);
    private static final SitemapUrl SUPPLIER_SITEMAP_URL = SitemapUrl.createSupplierUrl("GpP8xPem", new Date(), true);
    private static final SitemapUrl CATEGORY_SITEMAP_URL = SitemapUrl.createCategoryUrl(2L, "name", "", new Date(),
            true);

    @Mock
    private SitemapUrlReadDao sitemapUrlReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final SitemapUrlAccessor accessor = new SitemapUrlAccessor();

    @Test
    public void findWithEnglishLocaleAddsExportToChinaUrls() throws Exception {
        SitemapUrlFilter filter = new SitemapUrlFilter(Locale.ENGLISH);
        when(
                sitemapUrlReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, SitemapUrl.MAX_URL_COUNT,
                        Locale.ENGLISH)).thenReturn(queryCriteria);
        when(sitemapUrlReadDao.getUntranslatedPublishedProductUrls(queryCriteria)).thenReturn(
                Lists.newArrayList(PRODUCT_SITEMAP_URL));
        when(sitemapUrlReadDao.getUntranslatedApprovedSupplierUrls(queryCriteria)).thenReturn(
                Lists.newArrayList(SUPPLIER_SITEMAP_URL));
        when(sitemapUrlReadDao.getSupplierCategoryUrls(queryCriteria)).thenReturn(
                Lists.newArrayList(CATEGORY_SITEMAP_URL));

        List<SitemapUrl> urls = accessor.find(filter);

        assertThat(urls, is(notNullValue()));
        assertThat(urls.size(), is(18));
        assertThat(urls.get(0), is(SitemapUrl.GLOBAL_HOME));
        assertThat(urls.get(1), is(SitemapUrl.GLOBAL_HOW_WE_WORK));
        assertThat(urls.get(2), is(SitemapUrl.GLOBAL_BENEFITS));
        assertThat(urls.get(3), is(SitemapUrl.GLOBAL_HOW_TO_EXPORT));
        assertThat(urls.get(4), is(SitemapUrl.GLOBAL_PARTNERS));
        assertThat(urls.get(5), is(SitemapUrl.GLOBAL_DO_GOOD));
        assertThat(urls.get(6), is(SitemapUrl.GLOBAL_ABOUT));
        assertThat(urls.get(7), is(SitemapUrl.GLOBAL_OUR_SERVICES));
        assertThat(urls.get(8), is(SitemapUrl.GLOBAL_CHINA_DIRECT));
        assertThat(urls.get(9), is(SitemapUrl.GLOBAL_CHINA_MARKET_OPPORTUNITY));
        assertThat(urls.get(10), is(SitemapUrl.GLOBAL_WHY_EXPORTING_IS_GOOD));
        assertThat(urls.get(11), is(SitemapUrl.GLOBAL_HOW_TO_START_EXPORTING));
        assertThat(urls.get(12), is(SitemapUrl.GLOBAL_WHY_INTERNET_IS_KEY));
        assertThat(urls.get(13), is(SitemapUrl.GLOBAL_DEMANDED_PRODUCTS));
        assertThat(urls.get(14), is(SitemapUrl.GLOBAL_HOW_WE_HELP_YOU_SUCCEED));
        assertThat(urls.get(15), is(PRODUCT_SITEMAP_URL));
        assertThat(urls.get(16), is(SUPPLIER_SITEMAP_URL));
        assertThat(urls.get(17), is(CATEGORY_SITEMAP_URL));
    }

    @Test
    public void findWithChineseLocaleAddsHuijinkouUrls() throws Exception {
        SitemapUrlFilter filter = new SitemapUrlFilter(Locale.CHINESE);
        when(
                sitemapUrlReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, SitemapUrl.MAX_URL_COUNT,
                        Locale.CHINESE)).thenReturn(queryCriteria);
        when(sitemapUrlReadDao.getTranslatedPublishedProductUrls(queryCriteria)).thenReturn(
                Lists.newArrayList(PRODUCT_SITEMAP_URL));
        when(sitemapUrlReadDao.getTranslatedApprovedSupplierUrls(queryCriteria)).thenReturn(
                Lists.newArrayList(SUPPLIER_SITEMAP_URL));
        when(sitemapUrlReadDao.getBuyerCategoryUrls(queryCriteria))
                .thenReturn(Lists.newArrayList(CATEGORY_SITEMAP_URL));

        List<SitemapUrl> urls = accessor.find(filter);

        assertThat(urls, is(notNullValue()));
        assertThat(urls.size(), is(10));
        assertThat(urls.get(0), is(SitemapUrl.BUYER_HOME));
        assertThat(urls.get(1), is(SitemapUrl.BUYER_HOW_WE_WORK));
        assertThat(urls.get(2), is(SitemapUrl.BUYER_BENEFITS));
        assertThat(urls.get(3), is(SitemapUrl.BUYER_HOW_TO_IMPORT));
        assertThat(urls.get(4), is(SitemapUrl.BUYER_PARTNERS));
        assertThat(urls.get(5), is(SitemapUrl.BUYER_DO_GOOD));
        assertThat(urls.get(6), is(SitemapUrl.BUYER_ABOUT));
        assertThat(urls.get(7), is(PRODUCT_SITEMAP_URL));
        assertThat(urls.get(8), is(SUPPLIER_SITEMAP_URL));
        assertThat(urls.get(9), is(CATEGORY_SITEMAP_URL));
    }

}
