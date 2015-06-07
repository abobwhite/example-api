package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.ProductCategoryFilter;
import com.daugherty.e2c.domain.CategoryType;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class ProductCategoryAccessorTest {

    private ProductCategory level1A = new ProductCategory(1L, "level1 NameA", 0, "linkA", true, CategoryType.PRODUCT);
    private ProductCategory level1B = new ProductCategory(2L, "level1 NameB", 0, "linkB", false, CategoryType.PRODUCT);
    private ProductCategory level2A1 = new ProductCategory(3L, "level2 NameAa", 0, null, true, null);
    private final ProductCategory level2A2 = new ProductCategory(4L, "level2 NameAb", 3, null, true, null);
    private ProductCategory level2B1 = new ProductCategory(5L, "level2 NameBa", 0, null, true, null);
    private final ProductCategory level3A1AndB1 = new ProductCategory(6L, "level3 NameAa1", 1, null, true, null);

    @Mock
    private ProductCategoryReadDao productCategoryReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final ProductCategoryAccessor accessor = new ProductCategoryAccessor();

    private Multimap<Long, Long> parentsByChildren;

    @Before
    public void setUpAssociations() {
        parentsByChildren = ArrayListMultimap.create();
        parentsByChildren.put(level1A.getId(), level2A1.getId());
        parentsByChildren.put(level1A.getId(), level2A2.getId());
        parentsByChildren.put(level1B.getId(), level2B1.getId());
        parentsByChildren.put(level2A1.getId(), level3A1AndB1.getId());
        parentsByChildren.put(level2B1.getId(), level3A1AndB1.getId());
    }

    @Test
    public void findWithoutCriteriaBuildsHierarchicalListOfAllProductCategoriesBasedOnFlatDaoList() {
        ProductCategoryFilter filter = new ProductCategoryFilter(null, null, null);
        when(
                productCategoryReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                        filter.isSortDescending(), filter.getStartItem(), filter.getCount(), Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(productCategoryReadDao.getAllCategories(queryCriteria)).thenReturn(
                Lists.newArrayList(level3A1AndB1, level2A1, level2B1, level1A, level2A2, level1B));
        when(productCategoryReadDao.getChildCategoriesByParent()).thenReturn(parentsByChildren);

        List<ProductCategory> hierarchicalCategories = accessor.find(filter);

        assertThat(hierarchicalCategories, is(notNullValue()));
        assertThat(hierarchicalCategories.size(), is(2));

        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0), level1A, 4, 2);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0).getChildren().get(0), level2A1, 1,
                1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0).getChildren().get(0).getChildren()
                .get(0), level3A1AndB1, 1, 0);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0).getChildren().get(1), level2A2, 3,
                0);

        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(1), level1B, 1, 1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(1).getChildren().get(0), level2B1, 1,
                1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(1).getChildren().get(0).getChildren()
                .get(0), level3A1AndB1, 1, 0);
    }

    @Test
    public void findWithLevel2CountCriterionBuildsLimitedTwoLevelHierarchicalListOfProductCategoriesBasedOnFlatDaoList() {
        ProductCategoryFilter filter = new ProductCategoryFilter(1, null, null);
        when(
                productCategoryReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                        filter.isSortDescending(), filter.getStartItem(), filter.getCount(), Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(productCategoryReadDao.getAllCategories(queryCriteria)).thenReturn(
                Lists.newArrayList(level3A1AndB1, level2A1, level2B1, level1A, level2A2, level1B));
        when(productCategoryReadDao.getChildCategoriesByParent()).thenReturn(parentsByChildren);

        List<ProductCategory> hierarchicalCategories = accessor.find(filter);

        assertThat(hierarchicalCategories, is(notNullValue()));
        assertThat(hierarchicalCategories.size(), is(2));

        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0), level1A, 4, 1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0).getChildren().get(0), level2A2, 3,
                0);

        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(1), level1B, 1, 1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(1).getChildren().get(0), level2B1, 1,
                0);
    }

    @Test
    public void findWithCategoryIdCriterionBuildsSpecificCategoryWithDescendantsBasedOnFlatDaoList() {
        ProductCategoryFilter filter = new ProductCategoryFilter(null, level2A1.getId(), null);
        when(
                productCategoryReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                        filter.isSortDescending(), filter.getStartItem(), filter.getCount(), Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(productCategoryReadDao.getAllCategories(queryCriteria)).thenReturn(
                Lists.newArrayList(level3A1AndB1, level2A1, level2B1, level1A, level2A2, level1B));
        when(productCategoryReadDao.getChildCategoriesByParent()).thenReturn(parentsByChildren);

        List<ProductCategory> hierarchicalCategories = accessor.find(filter);

        assertThat(hierarchicalCategories, is(notNullValue()));
        assertThat(hierarchicalCategories.size(), is(1));

        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0), level2A1, 1, 1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0).getChildren().get(0),
                level3A1AndB1, 1, 0);
    }

    @Test
    public void findResetsProductCountsForAllNonLeafCategoriesFromDao() {
        level1A = new ProductCategory(1L, "level1 NameA", 1, "linkA", true, CategoryType.PRODUCT);
        level1B = new ProductCategory(2L, "level1 NameB", 2, "linkB", true, CategoryType.PRODUCT);
        level2A1 = new ProductCategory(3L, "level2 NameAa", 3, null, true, null);
        level2B1 = new ProductCategory(5L, "level2 NameBa", 4, null, true, null);
        ProductCategoryFilter filter = new ProductCategoryFilter(null, null, null);
        when(
                productCategoryReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                        filter.isSortDescending(), filter.getStartItem(), filter.getCount(), Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(productCategoryReadDao.getAllCategories(queryCriteria)).thenReturn(
                Lists.newArrayList(level3A1AndB1, level2A1, level2B1, level1A, level2A2, level1B));
        when(productCategoryReadDao.getChildCategoriesByParent()).thenReturn(parentsByChildren);

        List<ProductCategory> hierarchicalCategories = accessor.find(filter);

        assertThat(hierarchicalCategories, is(notNullValue()));
        assertThat(hierarchicalCategories.size(), is(2));

        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0), level1A, 4, 2);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0).getChildren().get(0), level2A1, 1,
                1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0).getChildren().get(0).getChildren()
                .get(0), level3A1AndB1, 1, 0);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(0).getChildren().get(1), level2A2, 3,
                0);

        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(1), level1B, 1, 1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(1).getChildren().get(0), level2B1, 1,
                1);
        assertThatProductCategoryMatchesExpectedValues(hierarchicalCategories.get(1).getChildren().get(0).getChildren()
                .get(0), level3A1AndB1, 1, 0);
    }

    private void assertThatProductCategoryMatchesExpectedValues(ProductCategory category, ProductCategory daoCategory,
            int productCount, int childCount) {
        assertThat(category.getId(), is(daoCategory.getId()));
        assertThat(category.getName(), is(daoCategory.getName()));
        assertThat(category.getProductCount(), is(productCount));
        assertThat(category.getChildren().size(), is(childCount));
    }

}
