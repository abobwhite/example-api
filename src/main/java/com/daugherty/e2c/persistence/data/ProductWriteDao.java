package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Product;

/**
 * Defines database change operations for the Product domain object.
 */
public interface ProductWriteDao {

    Product insert(Product product);

    Product update(Product product);

    int deleteProductsByPartyId(Long partyId);
}
