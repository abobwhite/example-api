package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.persistence.data.ProductTranslationReadDao;

/**
 * Accessor for ProductTranslation.
 */
@Service("productTranslationAccessor")
public class ProductTranslationAccessor extends BaseAccessor<ProductTranslation> {

    @Inject
    private ProductTranslationReadDao productTranslationReadDao;

    @Override
    public List<ProductTranslation> find(Filter<ProductTranslation> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductTranslation load(Long productId, Locale locale) {
        return productTranslationReadDao.load(productId);
    }
}
