package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.SitemapUrl;
import com.daugherty.e2c.persistence.data.SitemapUrlReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

/**
 * Accessor for SitemapUrl objects.
 */
@Service("sitemapUrlAccessor")
public class SitemapUrlAccessor extends BaseAccessor<SitemapUrl> {

    @Inject
    private SitemapUrlReadDao sitemapUrlReadDao;

    @Override
    public List<SitemapUrl> find(Filter<SitemapUrl> filter) {
        List<SitemapUrl> urls = Lists.newArrayList();

        if (isHuijinkouSitemap(filter)) {
            addStaticHuijinkouUrls(urls);
        } else {
            addStaticExportToChinaUrls(urls);
        }
        addDynamicUrls(urls, filter);

        if (urls.size() > SitemapUrl.MAX_URL_COUNT) {
            return urls.subList(0, SitemapUrl.MAX_URL_COUNT);
        } else {
            return urls;
        }
    }

    private boolean isHuijinkouSitemap(Filter<SitemapUrl> filter) {
        return Locale.CHINESE.equals(filter.getLocale());
    }

    private void addStaticHuijinkouUrls(List<SitemapUrl> urls) {
        urls.add(SitemapUrl.BUYER_HOME);
        urls.add(SitemapUrl.BUYER_HOW_WE_WORK);
        urls.add(SitemapUrl.BUYER_BENEFITS);
        urls.add(SitemapUrl.BUYER_HOW_TO_IMPORT);
        urls.add(SitemapUrl.BUYER_PARTNERS);
        urls.add(SitemapUrl.BUYER_DO_GOOD);
        urls.add(SitemapUrl.BUYER_ABOUT);
    }

    private void addStaticExportToChinaUrls(List<SitemapUrl> urls) {
        urls.add(SitemapUrl.GLOBAL_HOME);
        urls.add(SitemapUrl.GLOBAL_HOW_WE_WORK);
        urls.add(SitemapUrl.GLOBAL_BENEFITS);
        urls.add(SitemapUrl.GLOBAL_HOW_TO_EXPORT);
        urls.add(SitemapUrl.GLOBAL_PARTNERS);
        urls.add(SitemapUrl.GLOBAL_DO_GOOD);
        urls.add(SitemapUrl.GLOBAL_ABOUT);
        urls.add(SitemapUrl.GLOBAL_OUR_SERVICES);
        urls.add(SitemapUrl.GLOBAL_CHINA_DIRECT);
        urls.add(SitemapUrl.GLOBAL_CHINA_MARKET_OPPORTUNITY);
        urls.add(SitemapUrl.GLOBAL_WHY_EXPORTING_IS_GOOD);
        urls.add(SitemapUrl.GLOBAL_HOW_TO_START_EXPORTING);
        urls.add(SitemapUrl.GLOBAL_WHY_INTERNET_IS_KEY);
        urls.add(SitemapUrl.GLOBAL_DEMANDED_PRODUCTS);
        urls.add(SitemapUrl.GLOBAL_HOW_WE_HELP_YOU_SUCCEED);
    }

    private void addDynamicUrls(List<SitemapUrl> urls, Filter<SitemapUrl> filter) {
        QueryCriteria criteria = sitemapUrlReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
        if (isHuijinkouSitemap(filter)) {
            urls.addAll(sitemapUrlReadDao.getTranslatedPublishedProductUrls(criteria));
            urls.addAll(sitemapUrlReadDao.getTranslatedApprovedSupplierUrls(criteria));
            urls.addAll(sitemapUrlReadDao.getBuyerCategoryUrls(criteria));
        } else {
            urls.addAll(sitemapUrlReadDao.getUntranslatedPublishedProductUrls(criteria));
            urls.addAll(sitemapUrlReadDao.getUntranslatedApprovedSupplierUrls(criteria));
            urls.addAll(sitemapUrlReadDao.getSupplierCategoryUrls(criteria));
        }
    }

    @Override
    public SitemapUrl load(Long id, Locale locale) {
        throw new UnsupportedOperationException();
    }

}
