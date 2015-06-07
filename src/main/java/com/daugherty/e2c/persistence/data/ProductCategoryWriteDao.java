package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.ProductCategory;

/**
 * Defines database change operations for the ProductCategory domain object.
 */
public interface ProductCategoryWriteDao {

    void updateCategories(Long productSnapshotId, List<ProductCategory> categories);

}
