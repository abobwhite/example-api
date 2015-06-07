package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.ProductTranslation;

/**
 * Defines database change operations for ProductTranslation objects.
 */
public interface ProductTranslationWriteDao {

    ProductTranslation insert(ProductTranslation translation);

    ProductTranslation update(Long updateId, ProductTranslation translation);

}
