package com.daugherty.e2c.business.accessor;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

/**
 * Accessor for Supplier regardless of status
 */
@Service("latestSupplierAccessor")
public class LatestSupplierAccessor extends SupplierAccessor {
    @Inject
    private SupplierReadDao supplierReadDao;

    @Override
    protected Supplier loadSupplier(Long supplierId, Locale locale) {
        return supplierReadDao.loadLatest(supplierId, locale);
    }

}
