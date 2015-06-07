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
public class ExactSearchTermRelevanceCalculatorTest {
    @Mock
    private Map<String, String> applicationParameters;

    private ExactSearchTermRelevanceCalculator exactSearchTermRelevanceCalculator = new ExactSearchTermRelevanceCalculator();

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
    public void productNameExactMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Oil", "", "", 0, 1);

        int relevance = exactSearchTermRelevanceCalculator.calculate(productSearchResult, "Oil", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(5));

    }

    @Test
    public void productKeywordExactMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "Oil", 0, 1);

        int relevance = exactSearchTermRelevanceCalculator.calculate(productSearchResult, "Oil", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(5));

    }

    @Test
    public void productCategoryCountMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "", "", 1, 1);
        productSearchResult.getCategories().add("Oil");

        int relevance = exactSearchTermRelevanceCalculator.calculate(productSearchResult, "Oil", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(7));

    }

    @Test
    public void productDescriptionExactMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "", "Oil", "", 0, 1);

        int relevance = exactSearchTermRelevanceCalculator.calculate(productSearchResult, "Oil", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(4));

    }

    @Test
    public void productNameAndKeywordExactMatch() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Oil", "", "Oil", 0, 1);

        int relevance = exactSearchTermRelevanceCalculator.calculate(productSearchResult, "Oil", Locale.CHINESE,
                applicationParameters);

        assertThat(relevance, is(5 + 5));

    }
}
