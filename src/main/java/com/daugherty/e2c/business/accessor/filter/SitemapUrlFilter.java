package com.daugherty.e2c.business.accessor.filter;

import java.util.Locale;

import com.daugherty.e2c.domain.SitemapUrl;

/**
 * Filter criteria for SitemapUrl retrieval operations.
 * <p>
 * The only optional thing here is the locale. The count is set to 50000 as that is the maximum number of URLs allowed
 * in a sitemap document.
 */
public class SitemapUrlFilter extends BaseFilter<SitemapUrl> {

    public SitemapUrlFilter(Locale locale) {
        super(null, null, 1, SitemapUrl.MAX_URL_COUNT, locale);
    }

}
