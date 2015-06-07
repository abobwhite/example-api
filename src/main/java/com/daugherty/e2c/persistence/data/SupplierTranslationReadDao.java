package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.persistence.QueryCriteria;

/**
 *
 */
public interface SupplierTranslationReadDao extends SortAndPaginationAware {

    List<SupplierTranslation> getAll(QueryCriteria sortingAndPaginationCriteria);

    QueryCriteria createQueryCriteria(String titlePrefix, String approvalType, Boolean translated,
                                      String propertyName, Boolean sortDescending, Integer startItem, Integer count);

    List<SupplierTranslation> find(QueryCriteria criteria);

    SupplierTranslation load(Long id);
}
