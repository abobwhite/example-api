package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.ProductCollectionHydrator;
import com.daugherty.e2c.domain.Product;
import com.google.common.base.Function;

/**
 * Accessor for approved, published Product domain objects.
 */
@Service("approvedProductAccessor")
public class ApprovedProductAccessor extends ProductAccessor {

    static final Function<String, Long> PRODUCT_ID_FUNCTION = new Function<String, Long>() {
        @Override
        public Long apply(String productId) {
            return Long.parseLong(productId);
        }
    };

    @Inject
    protected ProductCollectionHydrator productCollectionHydrator;

    @Override
    protected List<Product> findProducts(Filter<Product> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Product getProduct(Long id, Locale locale) {
        Product product = productReadDao.loadApproved(id, locale);
        return productCollectionHydrator.hydrateApproved(product, locale);
    }

}
