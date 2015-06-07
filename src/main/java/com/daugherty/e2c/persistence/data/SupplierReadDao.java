package com.daugherty.e2c.persistence.data;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Supplier;

/**
 * Defines database read operations for the Supplier domain object.
 */
public interface SupplierReadDao {

    Supplier loadLatest(Long id, Locale locale);

    Supplier loadApproved(Long id, Locale locale);

    List<Supplier> loadApprovedBySupplierIds(List<Long> ids, Locale locale);

    List<Supplier> loadLatestBySupplierIds(List<Long> ids, Locale locale);

    String loadSupplierIdMatchingLegacyId(Long legacyId);

}
