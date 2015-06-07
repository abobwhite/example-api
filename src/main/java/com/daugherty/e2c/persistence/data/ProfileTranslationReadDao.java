package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.ProfileTranslation;

/**
 * Defines database read operations for ProfileTranslation objects.
 */
public interface ProfileTranslationReadDao {

    Long getUpdateIdForLatestSupplier(Long supplierId);

    ProfileTranslation loadByLatestSupplierId(Long supplierId);

}
