package com.daugherty.e2c.business.search.relevance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;

@RunWith(MockitoJUnitRunner.class)
public class PartialWordRelevanceCalculatorTest {
    @Mock
    private Map<String, String> applicationParameters;

    private final PartialWordRelevanceCalculator partialWordRelevanceCalculator = new PartialWordRelevanceCalculator();

    @Before
    public void setUp() {
        when(applicationParameters.get(ApplicationParameterReadDao.NAME_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("7");
        when(applicationParameters.get(ApplicationParameterReadDao.DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT)).thenReturn(
                "4");

        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_NAME_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("5");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("7");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT))
                .thenReturn("4");
    }

    @Test
    public void englishProductNamePartialMatchWhenExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Extra Virgin Olive Oil", "", "", 0,
                1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5 * 2));
    }

    @Test
    public void englishProductNamePartialMatchWhenNotExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Mount Olive Oilers", "", "", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5 * 2));
    }

    @Test
    public void englishProductNamePartialNotMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Edmonton Oilers", "", "", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5));
    }

    @Test
    public void englishProductKeywordPartialMatchWhenExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "Extra Virgin Olive Oil", 0,
                1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5 * 2));
    }

    @Test
    public void englishProductKeywordPartialMatchWhenNotExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "Mount Olive Oilers", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5 * 2));
    }

    @Test
    public void englishProductKeywordPartialNotMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "Edmonton Oilers", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5));
    }

    @Test
    public void englishProductDescriptionPartialMatchWhenExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "Extra Virgin Olive Oil", "", 0,
                1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(4 * 2));
    }

    @Test
    public void englishProductDescriptionPartialMatchWhenNotExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "Mount Olive Oilers", "", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(4 * 2));
    }

    @Test
    public void englishProductDescriptionPartialNotMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "Edmonton Oilers", "", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(4));
    }

    @Test
    public void englishProductCategoryPartialWordMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("Cars");

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Car", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(7));
    }

    @Test
    public void englishProductCategoryExactWordMatchTwoCategories() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("Oil");
        productSearchResult.getCategories().add("Olive.Oiliers");

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(7 * 3));
    }

    @Test
    public void englishProductCategoryNotExactWordMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("Healthcare");

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "Car", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void chineseProductNamePartialMatchAlwaysReturnsZero() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "BCDE", "", "", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "BC", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void chineseProductKeywordPartialMatchAlwaysReturnsZero() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "BCDE", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "BC", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void chineseProductDescriptionPartialMatchAlwaysReturnsZero() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "BCDE", "", 0, 1);

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "BC", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void chineseProductCategoryPartialWordMatchAlwaysReturnsZero() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("BCDE");

        int relevance = partialWordRelevanceCalculator.calculate(productSearchResult, "BC", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(0));
    }

}
