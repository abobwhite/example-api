package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.ProductImage;

/**
 * Defines database read operations for the Product Image domain object.
 */
public interface ProductImageReadDao {
    List<ProductImage> loadBySnapshotIds(List<Long> productSnapshotIds);
}
