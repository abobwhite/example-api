package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.persistence.data.ProductReadDao;

/**
 * Base Implementation for Product Accessor
 */
public abstract class ProductAccessor extends BaseAccessor<Product> {
    @Inject
    protected ProductReadDao productReadDao;

    @Override
    public List<Product> find(Filter<Product> filter) {
        return findProducts(filter);
    }

    @Override
    public Product load(Long id, Locale locale) {
        return getProduct(id, locale);
    }

    protected abstract List<Product> findProducts(Filter<Product> filter);

    protected abstract Product getProduct(Long id, Locale locale);

}
