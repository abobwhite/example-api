package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.ProductMessage;

/**
 * Defines database write operations for Message objects.
 */
public interface ProductMessageWriteDao extends MessageWriteDao {

	ProductMessage insert(ProductMessage message);
}
