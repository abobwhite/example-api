package com.daugherty.e2c.business.accessor;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.ProductCategoryFilter;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * Accessor for ProductCategory domain objects.
 */
@Service("productCategoryAccessor")
public class ProductCategoryAccessor extends BaseAccessor<ProductCategory> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private ProductCategoryReadDao productCategoryReadDao;

    @Override
    public List<ProductCategory> find(Filter<ProductCategory> filter) {
        List<ProductCategory> categories = getCategoryHierarchy(filter);

        Integer level2Count = filter.getIntegerCriterion(ProductCategoryFilter.LEVEL2_COUNT);
        if (level2Count != null) {
            filterAndSortSecondLevelCategories(categories, level2Count);
        }

        Long categoryId = filter.getLongCriterion(ProductCategoryFilter.CATEGORY_ID);
        if (categoryId != null) {
            return Lists.newArrayList(findCategory(categories, categoryId));
        } else {
            return categories;
        }

    }

    private List<ProductCategory> getCategoryHierarchy(Filter<ProductCategory> filter) {
        QueryCriteria criteria = productCategoryReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
        List<ProductCategory> categories = new Vector<ProductCategory>(
                productCategoryReadDao.getAllCategories(criteria));
        Multimap<Long, Long> childCategoriesByParent = productCategoryReadDao.getChildCategoriesByParent();

        Collection<ProductCategory> nonRoots = addChildrenToParents(categories, childCategoriesByParent);
        ensureOnlyLeafCategoriesHaveProductCounts(categories);
        filterRoots(categories, nonRoots);
        aggregateProductCounts(categories);

        return categories;
    }

    private Collection<ProductCategory> addChildrenToParents(List<ProductCategory> categories,
            Multimap<Long, Long> childCategoriesByParent) {
        Set<ProductCategory> nonRoots = Sets.newHashSet();
        for (ProductCategory category : categories) {
            for (Long childCategoryId : childCategoriesByParent.get(category.getId())) {
                try {
                    nonRoots.add(addChildToParent(category, childCategoryId, categories));
                } catch (NoSuchElementException e) {
                    LOGGER.error("Could not find category with Id " + childCategoryId + " as a child of " + category);
                }
            }
        }
        return nonRoots;
    }

    private ProductCategory addChildToParent(ProductCategory parent, final Long childCategoryId,
            List<ProductCategory> categories) {
        ProductCategory child = Iterables.find(categories, new Predicate<ProductCategory>() {
            @Override
            public boolean apply(ProductCategory input) {
                return input.getId().equals(childCategoryId);
            }
        });
        parent.addChild(child);
        return child;
    }

    private void ensureOnlyLeafCategoriesHaveProductCounts(List<ProductCategory> categories) {
        for (ProductCategory category : categories) {
            if (!category.isLeaf()) {
                category.resetProductCount();
            }
        }
    }

    private void filterRoots(List<ProductCategory> categories, Collection<ProductCategory> nonRoots) {
        Iterables.removeAll(categories, nonRoots);
    }

    private void aggregateProductCounts(List<ProductCategory> categories) {
        for (ProductCategory category : categories) {
            try {
                category.addToProductCount(sumDescendentProductCount(category.getChildren()));
                aggregateProductCounts(category.getChildren());
            } catch (StackOverflowError e) {
                LOGGER.error(category + " has an infinite loop somewhere");
            }
        }
    }

    private int sumDescendentProductCount(List<ProductCategory> categories) {
        int sum = 0;
        for (ProductCategory category : categories) {
            if (category.isLeaf()) {
                sum += category.getProductCount();
            } else {
                sum += sumDescendentProductCount(category.getChildren());
            }
        }
        return sum;
    }

    private void filterAndSortSecondLevelCategories(List<ProductCategory> roots, Integer level2Count) {
        for (ProductCategory root : roots) {
            for (ProductCategory child : root.getChildren()) {
                child.removeAllChildrenFromIndex(0);
            }
            Collections.sort(root.getChildren(), new DescendingProductCountComparator());
            root.removeAllChildrenFromIndex(level2Count);
        }
    }

    private ProductCategory findCategory(List<ProductCategory> categories, Long categoryId) {
        for (ProductCategory category : categories) {
            if (category.getId().equals(categoryId)) {
                return category;
            } else {
                ProductCategory childCategory = findCategory(category.getChildren(), categoryId);
                if (childCategory != null) {
                    return childCategory;
                }
            }
        }
        return null;
    }

    @Override
    public ProductCategory load(Long id, Locale locale) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sorts categories in descending order of Product count.
     */
    private final class DescendingProductCountComparator implements Comparator<ProductCategory> {

        @Override
        public int compare(ProductCategory category1, ProductCategory category2) {
            return category2.getProductCount() - category1.getProductCount();
        }

    }

}
