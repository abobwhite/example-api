package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.ProductImage;

/**
 * Defines database change operations for ProductImage domain objects.
 */
public interface ProductImageWriteDao {

    void updateImages(Long productSnapshotId, List<ProductImage> images);

}
