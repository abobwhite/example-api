package com.daugherty.e2c.business.search.relevance;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.search.RelevanceCalculator;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;

/**
 * Determines Overall Relevance of a Product Search Result
 */
@Component("productSearchResultRelevanceCalculator")
public class ProductSearchResultRelevanceCalculator implements RelevanceCalculator {
    @Inject
    private ExactSearchTermRelevanceCalculator exactSearchTermRelevanceCalculator;
    @Inject
    private ExactWordRelevanceCalculator exactWordRelevanceCalculator;
    @Inject
    private PartialWordRelevanceCalculator partialWordRelevanceCalculator;

    @Override
    public int calculate(ProductSearchResult productSearchResult, String searchTerms, Locale locale,
            Map<String, String> applicationParameters) {

        double exactTermDegree = Locale.CHINESE.equals(locale) ? Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_EXACT_TERM_MATCH_RELEVANCE_DEGREE)) : Double
                .valueOf(applicationParameters.get(ApplicationParameterReadDao.EXACT_TERM_MATCH_RELEVANCE_DEGREE));
        double exactWordDegree = Locale.CHINESE.equals(locale) ? Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_EXACT_WORD_MATCH_RELEVANCE_DEGREE)) : Double
                .valueOf(applicationParameters.get(ApplicationParameterReadDao.EXACT_WORD_MATCH_RELEVANCE_DEGREE));
        double partialWordDegree = Locale.CHINESE.equals(locale) ? Double.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_PARTIAL_WORD_MATCH_RELEVANCE_DEGREE)) : Double
                .valueOf(applicationParameters.get(ApplicationParameterReadDao.PARTIAL_WORD_MATCH_RELEVANCE_DEGREE));

        double exactSearchTermRelevance = exactSearchTermRelevanceCalculator.calculate(productSearchResult,
                searchTerms, locale, applicationParameters) * exactTermDegree;

        double exactWordTermRelevance = exactWordRelevanceCalculator.calculate(productSearchResult, searchTerms,
                locale, applicationParameters) * exactWordDegree;

        double partialWordTermRelevance = partialWordRelevanceCalculator.calculate(productSearchResult, searchTerms,
                locale, applicationParameters) * partialWordDegree;

        return (int) (exactSearchTermRelevance + exactWordTermRelevance + partialWordTermRelevance);
    }
}
