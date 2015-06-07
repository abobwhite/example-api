package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Supplier;

/**
 * Defines database change operations for the Supplier domain object.
 */
public interface SupplierWriteDao {
    Supplier insert(Supplier supplier);

    Supplier update(Supplier supplier);

    Supplier recordEvent(Supplier supplier);

    Supplier switchToSupplier(Supplier supplier);
}
