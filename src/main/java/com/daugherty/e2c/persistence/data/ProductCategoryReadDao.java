package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Multimap;

/**
 * Defines read operations for ProductCategory objects.
 */
public interface ProductCategoryReadDao extends SortAndPaginationAware {

    List<ProductCategory> getAllCategories(QueryCriteria queryCriteria);

    List<ProductCategory> findCategoriesBySnapshotIds(List<Long> productSnapshotIds);

    Multimap<Long, Long> getChildCategoriesByParent();

    Long loadCategoryIdMatchingLegacyId(Long legacyId);

}
