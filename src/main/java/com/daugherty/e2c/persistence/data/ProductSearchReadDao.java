package com.daugherty.e2c.persistence.data;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.ProductSearchResult;

/**
 * Defines database read operations for the Product Search Result domain object.
 */
public interface ProductSearchReadDao {
    List<ProductSearchResult> find(String[] searchTerms, String country, List<Long> categoryIds, Locale locale);
}
