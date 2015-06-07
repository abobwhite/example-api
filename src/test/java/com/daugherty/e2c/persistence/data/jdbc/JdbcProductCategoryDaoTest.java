package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class JdbcProductCategoryDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcProductCategoryDao productCategoryDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList();
    }

    @Test
    public void getAllReturnsAllProductCategoriesAsFlatDomainObjectsInEnglish() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productCategoryDao.createSortingAndPaginationCriteria(null, false,
                1, 1000, Locale.ENGLISH);
        List<ProductCategory> categories = productCategoryDao.getAllCategories(sortingAndPaginationCriteria);

        assertThat(categories, is(notNullValue()));
        assertThat(categories.size(), is(252));

        assertThatProductCategoryMatchesExpectedValues(categories.get(0), 1L, "Agriculture", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(190), 406L, "Transportation Related Products", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(201), 1305L, "Medicine", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(202), 1306L, "Physical Therapy Equipment", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(203), 1401L, "Barware", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(208), 1405L, "Home Storage & Organization", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(251), 2810L, "Travel Packages", 0);
    }

    @Test
    public void getAllReturnsAllProductCategoriesAsFlatDomainObjectsInSpanishIfAvailable() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productCategoryDao.createSortingAndPaginationCriteria(null, false,
                1, 1000, new Locale("es"));
        List<ProductCategory> categories = productCategoryDao.getAllCategories(sortingAndPaginationCriteria);

        assertThat(categories, is(notNullValue()));
        assertThat(categories.size(), is(252));

        assertThatProductCategoryMatchesExpectedValues(categories.get(0), 1L, "Agricultura", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(251), 2810L, "Travel Packages", 0);
    }

    @Test
    public void getAllReturnsAllProductCategoriesAsFlatDomainObjectsInChineseIfAvailable() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productCategoryDao.createSortingAndPaginationCriteria(null, false,
                1, 1000, Locale.CHINESE);
        List<ProductCategory> categories = productCategoryDao.getAllCategories(sortingAndPaginationCriteria);

        assertThat(categories, is(notNullValue()));
        assertThat(categories.size(), is(252));

        assertThatProductCategoryMatchesExpectedValues(categories.get(0), 1L, "农业", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(251), 2810L, "Chinese for Travel Packages", 0);
    }

    @Test
    public void getAllSortsByNameInEnglish() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productCategoryDao.createSortingAndPaginationCriteria(
                ProductCategory.NAME_SERIAL_PROPERTY, false, 1, 1000, Locale.ENGLISH);
        List<ProductCategory> categories = productCategoryDao.getAllCategories(sortingAndPaginationCriteria);

        assertThat(categories, is(notNullValue()));
        assertThat(categories.size(), is(252));

        assertThatProductCategoryMatchesExpectedValues(categories.get(0), 1L, "Agriculture", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(251), 2508L, "Yarn & Thread", 0);
    }

    @Test
    public void getAllSortsByNameInSpanish() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productCategoryDao.createSortingAndPaginationCriteria(
                ProductCategory.NAME_SERIAL_PROPERTY, false, 1, 1000, new Locale("es"));
        List<ProductCategory> categories = productCategoryDao.getAllCategories(sortingAndPaginationCriteria);

        assertThat(categories, is(notNullValue()));
        assertThat(categories.size(), is(252));

        assertThatProductCategoryMatchesExpectedValues(categories.get(0), 1L, "Agricultura", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(251), 2508L, "Yarn & Thread", 0);
    }

    @Test
    public void getAllPaginates() throws Exception {
        QueryCriteria sortingAndPaginationCriteria = productCategoryDao.createSortingAndPaginationCriteria(null, false,
                4, 2, Locale.ENGLISH);
        List<ProductCategory> categories = productCategoryDao.getAllCategories(sortingAndPaginationCriteria);

        assertThat(categories, is(notNullValue()));
        assertThat(categories.size(), is(2));

        assertThatProductCategoryMatchesExpectedValues(categories.get(0), 4L, "Automotive & Transport", 0);
        assertThatProductCategoryMatchesExpectedValues(categories.get(1), 27L, "Baby & Childrens Products", 0);
    }

    @Test
    public void getChildrenByParentReturnsAllParentChildAssociations() throws Exception {
        Multimap<Long, Long> childrenByParent = productCategoryDao.getChildCategoriesByParent();

        assertThat(childrenByParent, is(notNullValue()));
        assertThat(childrenByParent.keySet().size(), is(33));
        assertThat(childrenByParent.get(1L).size(), is(8));
        assertThat(childrenByParent.get(1L).contains(101L), is(true));
        assertThat(childrenByParent.get(2405L).size(), is(5));
        assertThat(childrenByParent.get(2405L).contains(240505L), is(true));
    }

    @Test
    public void loadBySnapshotIdsPopulatesSnapshotId() throws Exception {
        List<ProductCategory> categories = productCategoryDao.findCategoriesBySnapshotIds(Lists.newArrayList(71L, 81L));

        assertThat(categories, is(notNullValue()));
        assertThat(categories.size(), is(4));
        assertThat(categories.get(0).getId(), is(240601L));
        assertThat(categories.get(0).getProductSnapshotId(), is(71L));
        assertThat(categories.get(0).getName(), is("Cricket Balls"));

        assertThat(categories.get(1).getId(), is(240602L));
        assertThat(categories.get(1).getProductSnapshotId(), is(71L));
        assertThat(categories.get(1).getName(), is("Football & Soccer Balls"));

        assertThat(categories.get(2).getId(), is(240702L));
        assertThat(categories.get(2).getProductSnapshotId(), is(71L));
        assertThat(categories.get(2).getName(), is("Golf Gloves"));

        assertThat(categories.get(3).getId(), is(240602L));
        assertThat(categories.get(3).getProductSnapshotId(), is(81L));
        assertThat(categories.get(3).getName(), is("Football & Soccer Balls"));
    }

    @Test
    public void loadCategoryIdMatchingLegacyIdThatExistsReturnsMatchingId() throws Exception {
        assertThat(productCategoryDao.loadCategoryIdMatchingLegacyId(666L), is(1L));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void loadCategoryIdMatchingLegacyIdThatDoesNotExistThrowsEmptyResultDataAccessException() throws Exception {
        productCategoryDao.loadCategoryIdMatchingLegacyId(999L);
    }

    @Test
    public void updateCategoriesInsertsIntoAssociationTable() {
        productCategoryDao.updateCategories(123456789L, Lists.newArrayList(new ProductCategory(101L)));

        Map<String, Object> productAuditProductCategoryRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_category_product_audit WHERE product_audit_id = ?", 123456789L);
        assertThat((Long) productAuditProductCategoryRowMap.get("product_audit_id"), is(123456789L));
        assertThat((Long) productAuditProductCategoryRowMap.get("product_category_id"), is(101L));
        assertThat((Date) productAuditProductCategoryRowMap.get("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

    private void assertThatProductCategoryMatchesExpectedValues(ProductCategory category, Long id, String name,
            Integer productCount) {
        assertThat(category.getId(), is(id));
        assertThat(category.getName(), is(name));
        assertThat(category.getProductCount(), is(productCount));
        assertThat(category.getChildren().size(), is(0));
    }

}
