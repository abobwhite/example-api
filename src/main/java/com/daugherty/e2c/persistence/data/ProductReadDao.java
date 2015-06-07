package com.daugherty.e2c.persistence.data;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Product;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines database read operations for the Product domain object.
 */
public interface ProductReadDao extends SortAndPaginationAware {

    QueryCriteria createLatestQueryCriteria(Long supplierId, String productName, String approvalStatus,
            Boolean hotProduct, Boolean published, String propertyName, Boolean sortDescending, Integer startItem,
            Integer count, Locale locale);

    QueryCriteria createPublishedQueryCriteria(Long supplierId, String country, List<Long> categoryIds, Boolean hot,
            Long businessType, Integer membershipLevel, String propertyName, Boolean sortDescending, Integer startItem,
            Integer count, Locale locale);

    List<Product> findLatest(QueryCriteria criteria);

    List<Product> findPublished(QueryCriteria criteria);

    Product loadLatest(Long productId, Locale locale);

    Product loadPublished(Long productId, Locale locale);

    Product loadApproved(Long productId, Locale locale);

    List<Product> loadPublishedByProductIds(List<Long> productIds, Locale locale);

    Long loadProductIdMatchingLegacyId(Long legacyId);

}
