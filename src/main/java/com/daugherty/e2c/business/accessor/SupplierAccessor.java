package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;

/**
 * Base Accessor for Suppliers domain objects.
 */
public abstract class SupplierAccessor extends BaseAccessor<Supplier> {
    @Inject
    private PartyBusinessTypeReadDao partyBusinessTypeReadDao;

    @Override
    public List<Supplier> find(Filter<Supplier> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Supplier load(Long id, Locale locale) {
        Supplier supplier = loadSupplier(id, locale);
        addBusinessTypesTo(supplier);
        return supplier;
    }

    private void addBusinessTypesTo(Supplier supplier) {
        supplier.getCompany().setBusinessTypes(partyBusinessTypeReadDao.findBySnapshotId(supplier.getSnapshotId()));
    }

    protected abstract Supplier loadSupplier(Long supplierId, Locale locale);
}
