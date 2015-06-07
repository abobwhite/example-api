package com.daugherty.e2c.business.search.relevance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;

@RunWith(MockitoJUnitRunner.class)
public class ExactWordRelevanceCalculatorTest {
    @Mock
    private Map<String, String> applicationParameters;
    @InjectMocks
    private final ExactWordRelevanceCalculator exactWordRelevanceCalculator = new ExactWordRelevanceCalculator();

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
    public void englishProductNameExactWordMatchWhenExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Extra Virgin Olive Oil", "", "", 0,
                1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5 * 2));
    }

    @Test
    public void englishProductNameExactWordMatchesOneWord() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Mount Olive Oilers", "", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5));
    }

    @Test
    public void englishProductExactWordNotMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Edmonton Oilers", "", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void englishProductKeywordExactWordMatchWhenExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "Extra Virgin Olive Oil", 0,
                1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5 * 2));
    }

    @Test
    public void englishProductKeywordExactWordMatchesOneWord() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "Mount Olive Oilers", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(5));
    }

    @Test
    public void englishProductKeywordNotExactWordMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "Edmonton Oilers", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void englishProductDescriptionExactWordMatchWhenExact() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "Extra Virgin Olive Oil", "", 0,
                1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(4 * 2));
    }

    @Test
    public void englishProductDescriptionExactWordMatchesOneWord() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "Mount Olive Oilers", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(4));
    }

    @Test
    public void englishProductDescriptionNotExactWordMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "Edmonton Oilers", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void englishProductCategoryExactWordMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("Oil");

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(7));
    }

    @Test
    public void englishProductCategoryExactWordMatchTwoCategories() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("Oil");
        productSearchResult.getCategories().add("Olive/Oil");

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Olive Oil", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(7 * 3));
    }

    @Test
    public void englishProductCategoryNotExactWordMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("Healthcare");
        productSearchResult.getCategories().add("Olive Oil");

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "Car", Locale.ENGLISH,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    /*
     * For readability, we are representing Chinese characters (basically words) with English characters
     */

    @Test
    public void chineseProductNameMatchingMultipleWordCharacters() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "BCDE", "", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "CE", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(5 * 2));
    }

    @Test
    public void chineseProductNameMatchingOneWordCharacter() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "BCDE", "", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "C", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(5));
    }

    @Test
    public void chineseProductNameNotMatching() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "BCDE", "", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "X", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void chineseProductKeywordMatchingMultipleWordCharacters() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "BCDE", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "CE", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(5 * 2));
    }

    @Test
    public void chineseProductKeywordMatchingOneWordCharacter() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "BCDE", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "C", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(5));
    }

    @Test
    public void chineseProductKeywordNotMatching() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "BCDE", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "X", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void chineseProductDescriptionMatchingMultipleWordCharacters() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "BCDE", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "CE", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(4 * 2));
    }

    @Test
    public void chineseProductDescriptionMatchingOneWordCharacter() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "BCDE", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "C", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(4));
    }

    @Test
    public void chineseProductDescriptionNotMatching() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "BCDE", "", 0, 1);

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "X", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(0));
    }

    @Test
    public void chineseProductCategoryMatchingOneCategory() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("C");

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "BCDE", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(7));
    }

    @Test
    public void chineseProductCategoryMatchingTwoCategoriesOneOfThemTwice() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("C");
        productSearchResult.getCategories().add("BE");

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "BCDE", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(7 * 3));
    }

    @Test
    public void chineseProductCategoryNotMatching() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 0, 1);
        productSearchResult.getCategories().add("X");
        productSearchResult.getCategories().add("YZ");

        int relevance = exactWordRelevanceCalculator.calculate(productSearchResult, "BCDE", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(0));
    }

}
