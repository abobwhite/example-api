package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.ProductCollectionHydrator;
import com.daugherty.e2c.business.accessor.filter.ProductFilter;
import com.daugherty.e2c.domain.Product;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

/**
 * Accessor for Product domain objects regardless of status.
 */
@Service("latestProductAccessor")
public class LatestProductAccessor extends ProductAccessor {
    @Inject
    protected ProductCollectionHydrator productCollectionHydrator;

    @Override
    protected List<Product> findProducts(Filter<Product> filter) {
        List<Product> products = Lists.newArrayList();

        if (filter.hasNoCriteria()) {
            QueryCriteria criteria = productReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
            products = productReadDao.findLatest(criteria);
        } else {
            QueryCriteria criteria = productReadDao.createLatestQueryCriteria(
                    getPartyId(filter.getStringCriterion(ProductFilter.PUBLIC_SUPPLIER_ID)),
                    filter.getStringCriterion(ProductFilter.PRODUCT_NAME),
                    filter.getStringCriterion(ProductFilter.STATUS), filter.getBooleanCriterion(ProductFilter.HOT),
                    filter.getBooleanCriterion(ProductFilter.PUBLISHED), filter.getSortBy(), filter.isSortDescending(),
                    filter.getStartItem(), filter.getCount(), filter.getLocale());
            products = productReadDao.findLatest(criteria);
        }
        return productCollectionHydrator.hydrateLatest(products, filter.getLocale());
    }

    @Override
    protected Product getProduct(Long id, Locale locale) {
        Product product = productReadDao.loadLatest(id, locale);
        return productCollectionHydrator.hydrateLatest(product, locale);
    }
}
