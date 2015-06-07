package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.SupplierTranslation;

/**
 * Defines database write operations for SupplierTranslation objects.
 */
public interface SupplierTranslationWriteDao {

    SupplierTranslation update(SupplierTranslation supplierTranslation);

}
