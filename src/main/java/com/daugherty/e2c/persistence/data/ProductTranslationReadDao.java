package com.daugherty.e2c.persistence.data;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.daugherty.e2c.domain.ProductTranslation;

/**
 * Defines read operations for ProductTranslation objects.
 */
public interface ProductTranslationReadDao {

    Long getUpdateIdForLatestProduct(Long productId);

    ProductTranslation load(Long productId);

    Map<Long, ProductTranslation> findBySnapshotIds(List<Long> snapshotIds, Locale locale);

}
