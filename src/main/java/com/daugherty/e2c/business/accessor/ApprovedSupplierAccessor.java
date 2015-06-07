package com.daugherty.e2c.business.accessor;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

/**
 * Accessor for Supplier with status of Approved
 */
@Service("approvedSupplierAccessor")
public class ApprovedSupplierAccessor extends SupplierAccessor {
    @Inject
    private SupplierReadDao supplierReadDao;

    @Override
    protected Supplier loadSupplier(Long supplierId, Locale locale) {
        return supplierReadDao.loadApproved(supplierId, locale);
    }

}
