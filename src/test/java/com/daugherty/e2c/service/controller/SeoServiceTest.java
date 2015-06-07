package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.LegacyIdMapper;
import com.daugherty.e2c.business.accessor.filter.SitemapUrlFilter;
import com.daugherty.e2c.domain.SitemapUrl;
import com.daugherty.e2c.service.json.JsonId;
import com.daugherty.e2c.service.json.JsonSupplierId;
import com.daugherty.e2c.service.xml.sitemap.XmlSitemap;
import com.daugherty.e2c.service.xml.sitemap.XmlSitemapUrl;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class SeoServiceTest {

    private static final Date NOV_8_2013 = new GregorianCalendar(2013, Calendar.NOVEMBER, 8).getTime();
    private static final Date NOV_9_2013 = new GregorianCalendar(2013, Calendar.NOVEMBER, 9).getTime();

    @Mock
    private LegacyIdMapper legacyIdMapper;
    @Mock
    private Accessor<SitemapUrl> sitemapUrlAccessor;

    @InjectMocks
    private final SeoService seoService = new SeoService();

    @Captor
    private ArgumentCaptor<Filter<SitemapUrl>> sitemapUrlFilterCaptor;

    @Test
    public void retrieveCategoryIdDelegatesToLegacyIdMapper() {
        when(legacyIdMapper.lookupCategoryId(666L)).thenReturn(42L);

        JsonId jsonId = seoService.retrieveCategoryId(666L);

        assertThat(jsonId, is(notNullValue()));
        assertThat(jsonId.getLongValue(), is(42L));

        verify(legacyIdMapper).lookupCategoryId(666L);
        verifyNoMoreInteractions(legacyIdMapper);
    }

    @Test
    public void retrieveProductIdDelegatesToLegacyIdMapper() {
        when(legacyIdMapper.lookupProductId(666L)).thenReturn(42L);

        JsonId jsonId = seoService.retrieveProductId(666L);

        assertThat(jsonId, is(notNullValue()));
        assertThat(jsonId.getLongValue(), is(42L));

        verify(legacyIdMapper).lookupProductId(666L);
        verifyNoMoreInteractions(legacyIdMapper);
    }

    @Test
    public void retrieveSupplierIdDelegatesToLegacyIdMapper() {
        when(legacyIdMapper.lookupSupplierId(666L)).thenReturn("jKNz4P4q");

        JsonSupplierId jsonId = seoService.retrieveSupplierId(666L);

        assertThat(jsonId, is(notNullValue()));
        assertThat(jsonId.getStringValue(), is("jKNz4P4q"));

        verify(legacyIdMapper).lookupSupplierId(666L);
        verifyNoMoreInteractions(legacyIdMapper);
    }

    @Test
    public void buildExportToChinaSitemapDelegatesToAccessor() throws Exception {
        SitemapUrl productSitemapUrl = SitemapUrl.createProductUrl(1L, "name", NOV_8_2013, false);
        SitemapUrl supplierSitemapUrl = SitemapUrl.createSupplierUrl("GpP8xPem", NOV_9_2013, false);
        SitemapUrl categorySitemapUrl = SitemapUrl.createCategoryUrl(2L, "name", "PARENT", NOV_9_2013, false);
        when(sitemapUrlAccessor.find(any(Filter.class))).thenReturn(
                Lists.newArrayList(productSitemapUrl, supplierSitemapUrl, categorySitemapUrl));

        XmlSitemap sitemap = seoService.buildExportToChinaSitemap();

        verify(sitemapUrlAccessor).find(sitemapUrlFilterCaptor.capture());
        Filter<SitemapUrl> filter = sitemapUrlFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(SitemapUrlFilter.class)));
        assertThat(filter.getCount(), is(50000));
        assertThat(filter.getLocale(), is(Locale.ENGLISH));

        assertThat(sitemap, is(notNullValue()));
        assertThat(sitemap.getUrls(), is(notNullValue()));
        assertThat(sitemap.getUrls().size(), is(3));

        assertThat(sitemap.getUrls().get(0).getLoc(), is(productSitemapUrl.getLocation()));
        assertThat(sitemap.getUrls().get(0).getLastMod(), is("2013-11-08"));
        assertThat(sitemap.getUrls().get(0).getChangeFreq(), is(XmlSitemapUrl.DEFAULT_CHANGE_FREQUENCY));

        assertThat(sitemap.getUrls().get(1).getLoc(), is(supplierSitemapUrl.getLocation()));
        assertThat(sitemap.getUrls().get(1).getLastMod(), is("2013-11-09"));
        assertThat(sitemap.getUrls().get(1).getChangeFreq(), is(XmlSitemapUrl.DEFAULT_CHANGE_FREQUENCY));

        assertThat(sitemap.getUrls().get(2).getLoc(), is(categorySitemapUrl.getLocation()));
        assertThat(sitemap.getUrls().get(2).getLastMod(), is("2013-11-09"));
        assertThat(sitemap.getUrls().get(2).getChangeFreq(), is(XmlSitemapUrl.DEFAULT_CHANGE_FREQUENCY));
    }

    @Test
    public void buildHuijinkouSitemapDelegatesToAccessor() throws Exception {
        SitemapUrl productSitemapUrl = SitemapUrl.createProductUrl(1L, "name", NOV_8_2013, true);
        SitemapUrl supplierSitemapUrl = SitemapUrl.createSupplierUrl("GpP8xPem", NOV_9_2013, true);
        SitemapUrl categorySitemapUrl = SitemapUrl.createCategoryUrl(2L, "name", "", NOV_9_2013, false);
        when(sitemapUrlAccessor.find(any(Filter.class))).thenReturn(
                Lists.newArrayList(productSitemapUrl, supplierSitemapUrl, categorySitemapUrl));

        XmlSitemap sitemap = seoService.buildHuijinkouSitemap();

        verify(sitemapUrlAccessor).find(sitemapUrlFilterCaptor.capture());
        Filter<SitemapUrl> filter = sitemapUrlFilterCaptor.getValue();
        assertThat(filter, is(instanceOf(SitemapUrlFilter.class)));
        assertThat(filter.getCount(), is(50000));
        assertThat(filter.getLocale(), is(Locale.CHINESE));

        assertThat(sitemap, is(notNullValue()));
        assertThat(sitemap.getUrls(), is(notNullValue()));
        assertThat(sitemap.getUrls().size(), is(3));

        assertThat(sitemap.getUrls().get(0).getLoc(), is(productSitemapUrl.getLocation()));
        assertThat(sitemap.getUrls().get(0).getLastMod(), is("2013-11-08"));
        assertThat(sitemap.getUrls().get(0).getChangeFreq(), is(XmlSitemapUrl.DEFAULT_CHANGE_FREQUENCY));

        assertThat(sitemap.getUrls().get(1).getLoc(), is(supplierSitemapUrl.getLocation()));
        assertThat(sitemap.getUrls().get(1).getLastMod(), is("2013-11-09"));
        assertThat(sitemap.getUrls().get(1).getChangeFreq(), is(XmlSitemapUrl.DEFAULT_CHANGE_FREQUENCY));

        assertThat(sitemap.getUrls().get(2).getLoc(), is(categorySitemapUrl.getLocation()));
        assertThat(sitemap.getUrls().get(2).getLastMod(), is("2013-11-09"));
        assertThat(sitemap.getUrls().get(2).getChangeFreq(), is(XmlSitemapUrl.DEFAULT_CHANGE_FREQUENCY));
    }

}
