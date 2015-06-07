package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.persistence.data.ProductTranslationReadDao;
import com.daugherty.e2c.persistence.data.ProductTranslationWriteDao;

/**
 * Mutator for ProfileTranslation domain objects.
 */
@Service("productTranslationMutator")
@Transactional
public class ProductTranslationMutator extends BaseMutator<ProductTranslation> {

    @Inject
    private ProductTranslationReadDao productTranslationReadDao;
    @Inject
    private ProductTranslationWriteDao productTranslationWriteDao;

    @Override
    public ProductTranslation create(ProductTranslation translation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductTranslation update(ProductTranslation translation) {
        Long updateId = productTranslationReadDao.getUpdateIdForLatestProduct(translation.getId());
        if (updateId == null) {
            return productTranslationWriteDao.insert(translation);
        } else {
            return productTranslationWriteDao.update(updateId, translation);
        }
    }

    @Override
    public void delete(Long entityId) {
        throw new UnsupportedOperationException();
    }
}
