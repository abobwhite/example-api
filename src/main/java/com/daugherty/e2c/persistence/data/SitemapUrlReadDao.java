package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.SitemapUrl;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines database read operations for SitemapUrl objects..
 */
public interface SitemapUrlReadDao extends SortAndPaginationAware {

    List<SitemapUrl> getUntranslatedPublishedProductUrls(QueryCriteria sortingAndPaginationCriteria);

    List<SitemapUrl> getUntranslatedApprovedSupplierUrls(QueryCriteria sortingAndPaginationCriteria);

    List<SitemapUrl> getTranslatedPublishedProductUrls(QueryCriteria sortingAndPaginationCriteria);

    List<SitemapUrl> getTranslatedApprovedSupplierUrls(QueryCriteria sortingAndPaginationCriteria);

    List<SitemapUrl> getBuyerCategoryUrls(QueryCriteria sortingAndPaginationCriteria);

    List<SitemapUrl> getSupplierCategoryUrls(QueryCriteria sortingAndPaginationCriteria);

}
