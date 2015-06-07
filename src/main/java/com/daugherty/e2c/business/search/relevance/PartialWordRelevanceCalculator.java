package com.daugherty.e2c.business.search.relevance;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.search.RelevanceCalculator;
import com.daugherty.e2c.business.search.SearchTokenizer;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;

/**
 * Determines relevance of Product Search Results based on comparison items starting with a search term.
 */
@Component("partialWordRelevanceCalculator")
public class PartialWordRelevanceCalculator implements RelevanceCalculator {

    @Override
    public int calculate(ProductSearchResult productSearchResult, String searchTerm, Locale locale,
            Map<String, String> applicationParameters) {
        int nameWeight = Locale.CHINESE.equals(locale) ? Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_NAME_ATTRIBUTE_RELEVANCE_WEIGHT)) : Integer
                .valueOf(applicationParameters.get(ApplicationParameterReadDao.NAME_ATTRIBUTE_RELEVANCE_WEIGHT));
        int descriptionWeight = Locale.CHINESE.equals(locale) ? Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT)) : Integer
                .valueOf(applicationParameters.get(ApplicationParameterReadDao.DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT));
        int keywordWeight = Locale.CHINESE.equals(locale) ? Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT)) : Integer
                .valueOf(applicationParameters.get(ApplicationParameterReadDao.KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT));
        int categoryWeight = Locale.CHINESE.equals(locale) ? Integer.valueOf(applicationParameters
                .get(ApplicationParameterReadDao.CHINESE_CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT)) : Integer
                .valueOf(applicationParameters.get(ApplicationParameterReadDao.CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT));

        int relevance = 0;

        if (!Locale.CHINESE.equals(locale)) {
            Collection<String> searchTerms = SearchTokenizer.tokenizeAndSanitizeSearchTerms(searchTerm, locale);

            relevance += isPartialWordMatch(
                    SearchTokenizer.tokenizeComparisonItem(productSearchResult.getName(), locale), searchTerms,
                    nameWeight, locale);

            if (productSearchResult.getKeywords() != null) {
                relevance += isPartialWordMatch(
                        SearchTokenizer.tokenizeComparisonItem(productSearchResult.getKeywords(), locale), searchTerms,
                        keywordWeight, locale);
            }

            relevance += isPartialWordMatch(
                    SearchTokenizer.tokenizeComparisonItem(productSearchResult.getDescription(), locale), searchTerms,
                    descriptionWeight, locale);

            for (String category : productSearchResult.getCategories()) {
                Collection<String> words = SearchTokenizer.tokenizeComparisonItem(category, locale);
                relevance += isPartialWordMatch(words, searchTerms, categoryWeight, locale);
            }
        }

        return relevance;
    }

    private int isPartialWordMatch(Collection<String> words, Collection<String> searchTerms, int weight, Locale locale) {
        int relevance = 0;

        for (String term : searchTerms) {
            for (String word : words) {
                if (word.trim().startsWith(term)) {
                    relevance += weight;
                    break;
                }
            }
        }

        return relevance;
    }

}
