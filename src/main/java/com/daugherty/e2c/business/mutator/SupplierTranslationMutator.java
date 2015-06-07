package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.persistence.data.SupplierTranslationWriteDao;

/**
 * Mutator for supplierTranslation domain objects.
 */
@Service("supplierTranslationMutator")
@Transactional
public class SupplierTranslationMutator extends BaseMutator<SupplierTranslation> {

    @Inject
    private SupplierTranslationWriteDao supplierTranslationWriteDao;

    @Override
    public SupplierTranslation create(SupplierTranslation translation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SupplierTranslation update(SupplierTranslation translation) {
        return supplierTranslationWriteDao.update(translation);
    }

    @Override
    public void delete(Long translationId) {
        throw new UnsupportedOperationException();
    }
}
