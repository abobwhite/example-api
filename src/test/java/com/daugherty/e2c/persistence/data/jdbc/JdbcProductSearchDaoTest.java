package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.ProductSearchResult;
import com.google.common.collect.Lists;

public class JdbcProductSearchDaoTest extends BaseJdbcDaoTest {
    @Inject
    private JdbcProductSearchDao productSearchDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("product-search.sql");
    }

    @Test
    public void findPublishedBySearchTerms() {
        String[] searchTerms = new String[] { "Wine" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null, new ArrayList<Long>(),
                Locale.ENGLISH);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(3));
        assertProduct100English(results.get(0), 0);
        assertProduct101English(results.get(1), 0);
        assertProduct102English(results.get(2), 0);

    }

    @Test
    public void findPublishedBySearchTermsWithCountry() {
        String[] searchTerms = new String[] { "Wine" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, "United States", new ArrayList<Long>(),
                Locale.ENGLISH);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(2));
        assertProduct100English(results.get(0), 0);
        assertProduct101English(results.get(1), 0);
    }

    @Test
    public void findPublishedBySearchTermsFoundInCategory() {
        String[] searchTerms = new String[] { "Food" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null, new ArrayList<Long>(),
                Locale.ENGLISH);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(4));
        assertProduct100English(results.get(0), 1);
        assertProduct101English(results.get(1), 1);
        assertProduct102English(results.get(2), 1);
        assertProduct103English(results.get(3), 1);
    }

    @Test
    public void findPublishedBySearchTermsFoundInParentCategory() {
        String[] searchTerms = new String[] { "Food" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null,
                Lists.newArrayList(11L, 1101L, 1102L, 1103L, 1104L, 1105L, 1106L, 1107L, 1108L), Locale.ENGLISH);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(1));
        assertProduct103English(results.get(0), 0);
    }

    @Test
    public void findPublishedBySearchTermsNotFoundWhenNotInParentCategory() {
        String[] searchTerms = new String[] { "Car" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null,
                Lists.newArrayList(1001L, 1002L, 1003L, 1004L, 1005L, 1006L), Locale.ENGLISH);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(0));
    }

    @Test
    public void findPublishedBySearchTermsFoundInCategoryAndProduct() {
        String[] searchTerms = new String[] { "Furniture" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null, new ArrayList<Long>(),
                Locale.ENGLISH);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(1));
        assertProduct103English(results.get(0), 0);
    }

    @Test
    public void findTranslatedPublishedBySearchTerms() {
        String[] searchTerms = new String[] { "Wine" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null, new ArrayList<Long>(),
                Locale.CHINESE);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(3));
        assertProduct100Chinese(results.get(0), 0);
        assertProduct101Chinese(results.get(1), 0);
        assertProduct102Chinese(results.get(2), 0);

    }

    @Test
    public void findTranslatedPublishedBySearchTermsWithCountry() {
        String[] searchTerms = new String[] { "Wine" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, "United States", new ArrayList<Long>(),
                Locale.CHINESE);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(2));
        assertProduct100Chinese(results.get(0), 0);
        assertProduct101Chinese(results.get(1), 0);
    }

    @Test
    public void findTranslatedPublishedBySearchTermsFoundInCategory() {
        String[] searchTerms = new String[] { "Food" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null, new ArrayList<Long>(),
                Locale.CHINESE);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(4));
        assertProduct100Chinese(results.get(0), 1);
        assertProduct101Chinese(results.get(1), 1);
        assertProduct102Chinese(results.get(2), 1);
        assertProduct103Chinese(results.get(3), 1);
    }

    @Test
    public void findTranlatedPublishedBySearchTermsFoundInParentCategory() {
        String[] searchTerms = new String[] { "Food" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null,
                Lists.newArrayList(11L, 1101L, 1102L, 1103L, 1104L, 1105L, 1106L, 1107L, 1108L), Locale.CHINESE);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(1));
        assertProduct103Chinese(results.get(0), 0);
    }

    @Test
    public void findTranslatedPublishedBySearchTermsFoundInCategoryAndProduct() {
        String[] searchTerms = new String[] { "Furniture" };
        List<ProductSearchResult> results = productSearchDao.find(searchTerms, null, new ArrayList<Long>(),
                Locale.CHINESE);

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(1));
        assertProduct103Chinese(results.get(0), 0);
    }

    private void assertProduct100English(ProductSearchResult productSearchResult, int count) {
        assertThat(productSearchResult.getId(), is(100L));
        assertThat(productSearchResult.getName(), is("Red Wine"));
        assertThat(productSearchResult.getDescription(), is("Finest in USA"));
        assertThat(productSearchResult.getKeywords(), is("wine, grapes"));
        assertThat(productSearchResult.getCategoryCount(), is(count));
    }

    private void assertProduct100Chinese(ProductSearchResult productSearchResult, int count) {
        assertThat(productSearchResult.getId(), is(100L));
        assertThat(productSearchResult.getName(), is("Translated Red Wine"));
        assertThat(productSearchResult.getDescription(), is("Translated Finest in USA"));
        assertThat(productSearchResult.getKeywords(), is("Translated wine, grapes"));
        assertThat(productSearchResult.getCategoryCount(), is(count));
    }

    private void assertProduct101English(ProductSearchResult productSearchResult, int count) {
        assertThat(productSearchResult.getId(), is(101L));
        assertThat(productSearchResult.getName(), is("Sparkling Pear Cider"));
        assertThat(productSearchResult.getDescription(), is("Best Sparkling Wine in USA"));
        assertThat(productSearchResult.getKeywords(), is("cider, grapes"));
        assertThat(productSearchResult.getCategoryCount(), is(count));
    }

    private void assertProduct101Chinese(ProductSearchResult productSearchResult, int count) {
        assertThat(productSearchResult.getId(), is(101L));
        assertThat(productSearchResult.getName(), is("Translated Sparkling Pear Cider"));
        assertThat(productSearchResult.getDescription(), is("Translated Best Sparkling Wine in USA"));
        assertThat(productSearchResult.getKeywords(), is("Translated cider, grapes"));
        assertThat(productSearchResult.getCategoryCount(), is(count));
    }

    private void assertProduct102English(ProductSearchResult productSearchResult, int count) {
        assertThat(productSearchResult.getId(), is(102L));
        assertThat(productSearchResult.getName(), is("Sparkling Apple Cider"));
        assertThat(productSearchResult.getDescription(), is("Best Sparkling Cider in Mexico"));
        assertThat(productSearchResult.getKeywords(), is("Wine, grapes"));
        assertThat(productSearchResult.getCategoryCount(), is(count));
    }

    private void assertProduct102Chinese(ProductSearchResult productSearchResult, int count) {
        assertThat(productSearchResult.getId(), is(102L));
        assertThat(productSearchResult.getName(), is("Translated Sparkling Apple Cider"));
        assertThat(productSearchResult.getDescription(), is("Translated Best Sparkling Cider in Mexico"));
        assertThat(productSearchResult.getKeywords(), is("Translated Wine, grapes"));
        assertThat(productSearchResult.getCategoryCount(), is(count));
    }

    private void assertProduct103English(ProductSearchResult productSearchResult, int count) {
        assertThat(productSearchResult.getId(), is(103L));
        assertThat(productSearchResult.getName(), is("Hot Dog"));
        assertThat(productSearchResult.getDescription(), is("All Beef franks"));
        assertThat(productSearchResult.getKeywords(), is("Beef Food Furniture"));
        assertThat(productSearchResult.getCategoryCount(), is(count));
    }

    private void assertProduct103Chinese(ProductSearchResult productSearchResult, int count) {
        assertThat(productSearchResult.getId(), is(103L));
        assertThat(productSearchResult.getName(), is("Translated Hot Dog"));
        assertThat(productSearchResult.getDescription(), is("Translated All Beef franks"));
        assertThat(productSearchResult.getKeywords(), is("Translated Beef Food Furniture"));
        assertThat(productSearchResult.getCategoryCount(), is(count));
    }

}
