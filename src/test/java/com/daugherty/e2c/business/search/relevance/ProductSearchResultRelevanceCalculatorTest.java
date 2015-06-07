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
public class ProductSearchResultRelevanceCalculatorTest {
    @Mock
    private Map<String, String> applicationParameters;
    @Mock
    private ExactSearchTermRelevanceCalculator exactSearchTermRelevanceCalculator;
    @Mock
    private ExactWordRelevanceCalculator exactWordRelevanceCalculator;
    @Mock
    private PartialWordRelevanceCalculator partialWordRelevanceCalculator;

    @InjectMocks
    private ProductSearchResultRelevanceCalculator calculator = new ProductSearchResultRelevanceCalculator();

    @Before
    public void setUp() {
        when(applicationParameters.get(ApplicationParameterReadDao.EXACT_TERM_MATCH_RELEVANCE_DEGREE))
                .thenReturn("1.5");
        when(applicationParameters.get(ApplicationParameterReadDao.EXACT_WORD_MATCH_RELEVANCE_DEGREE)).thenReturn("2");
        when(applicationParameters.get(ApplicationParameterReadDao.PARTIAL_WORD_MATCH_RELEVANCE_DEGREE))
                .thenReturn("1");

        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_EXACT_TERM_MATCH_RELEVANCE_DEGREE))
                .thenReturn("1.5");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_EXACT_WORD_MATCH_RELEVANCE_DEGREE))
                .thenReturn("2");
        when(applicationParameters.get(ApplicationParameterReadDao.CHINESE_PARTIAL_WORD_MATCH_RELEVANCE_DEGREE))
                .thenReturn("1");
    }

    @Test
    public void addAndMutipliesrResultsFromAllCalculators() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Olive Oil",
                "Extra Virgin Oil from Northern Mountains of Italy", "Olive Oil", 0, 1);
        when(
                exactSearchTermRelevanceCalculator.calculate(productSearchResult, "Oil", Locale.CHINESE,
                        applicationParameters)).thenReturn(10);
        when(exactWordRelevanceCalculator.calculate(productSearchResult, "Oil", Locale.CHINESE, applicationParameters))
                .thenReturn(10);
        when(
                partialWordRelevanceCalculator.calculate(productSearchResult, "Oil", Locale.CHINESE,
                        applicationParameters)).thenReturn(10);

        double relevance = calculator.calculate(productSearchResult, "Oil", Locale.CHINESE, applicationParameters);

        assertThat(relevance, is(10 * 1.5 + 10 * 2 + 10 * 1));
    }
}
