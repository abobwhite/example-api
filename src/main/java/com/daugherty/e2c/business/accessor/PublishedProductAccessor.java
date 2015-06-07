package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.ProductCollectionHydrator;
import com.daugherty.e2c.business.accessor.filter.ProductFilter;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Accessor for approved, published Product domain objects.
 */
@Service("publishedProductAccessor")
public class PublishedProductAccessor extends ProductAccessor {

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
        List<Product> products = Lists.newArrayList();

        if (filter.hasNoCriteria()) {
            QueryCriteria criteria = productReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
            products = productReadDao.findPublished(criteria);
        } else {
            if (StringUtils.isBlank(filter.getStringCriterion(ProductFilter.PRODUCT_IDS))) {
                products = findProductsByFilter(filter);
            } else {
                products = findProductsByProductIds(filter);
            }

        }

        return productCollectionHydrator.hydrateApproved(products, filter.getLocale());
    }

    private List<Product> findProductsByFilter(Filter<Product> filter) {
        List<Product> products;
        ProductCategory category = ((ProductFilter) filter).getProductCategoryCriterion(ProductFilter.CATEGORY);
        List<Long> categoryIds = buildListOfCategoryIdsIncludingAllDescendants(category);

        QueryCriteria criteria = productReadDao.createPublishedQueryCriteria(
                getPartyId(filter.getStringCriterion(ProductFilter.PUBLIC_SUPPLIER_ID)),
                filter.getStringCriterion(ProductFilter.COUNTRY), categoryIds,
                filter.getBooleanCriterion(ProductFilter.HOT), filter.getLongCriterion(ProductFilter.BUSINESS_TYPE),
                filter.getIntegerCriterion(ProductFilter.MEMBERSHIP_LEVEL), filter.getSortBy(),
                filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
        products = productReadDao.findPublished(criteria);
        return products;
    }

    private List<Product> findProductsByProductIds(Filter<Product> filter) {
        List<Long> productIds = Lists.transform(
                Lists.newArrayList(filter.getStringCriterion(ProductFilter.PRODUCT_IDS).split(",")),
                PRODUCT_ID_FUNCTION);

        Map<Long, Product> productByProductId = getProductByProductIdMap(productIds, filter.getLocale());

        List<Product> orderedProducts = Lists.newArrayList();

        for (Long productId : productIds) {
            orderedProducts.add(productByProductId.get(productId));
        }

        return orderedProducts;
    }

    private Map<Long, Product> getProductByProductIdMap(List<Long> productIds, Locale locale) {
        List<Product> products = productReadDao.loadPublishedByProductIds(productIds, locale);

        Map<Long, Product> productByProductId = Maps.newHashMapWithExpectedSize(products.size());

        for (Product product : products) {
            productByProductId.put(product.getId(), product);
        }

        return productByProductId;
    }

    private List<Long> buildListOfCategoryIdsIncludingAllDescendants(ProductCategory category) {
        if (category == null) {
            return null;
        }
        List<Long> categoryIds = Lists.newArrayList();
        addCategoryIdToList(category, categoryIds);
        return categoryIds;
    }

    private void addCategoryIdToList(ProductCategory category, List<Long> categoryIds) {
        categoryIds.add(category.getId());
        for (ProductCategory child : category.getChildren()) {
            addCategoryIdToList(child, categoryIds);
        }
    }

    @Override
    protected Product getProduct(Long id, Locale locale) {
        Product product = productReadDao.loadPublished(id, locale);
        return productCollectionHydrator.hydrateApproved(product, locale);
    }

}
