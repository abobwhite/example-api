package com.daugherty.e2c.persistence.data;

import java.util.Locale;

import com.daugherty.persistence.QueryCriteria;

/**
 * Defines operations for DAOs that can sort and paginate data sets.
 */
public interface SortAndPaginationAware {

    public String RANDOM_SORT_PROPERTY = "random";

    QueryCriteria createSortingAndPaginationCriteria(String sortPropertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale);

}
