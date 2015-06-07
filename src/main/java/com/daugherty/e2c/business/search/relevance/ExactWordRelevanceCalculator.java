package com.daugherty.e2c.business.search.relevance;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.search.RelevanceCalculator;
import com.daugherty.e2c.business.search.SearchTokenizer;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.google.common.collect.Lists;

/**
 * Determines Relevance of Product Search Results when entire individual words in search terms are matched.
 */
@Component("exactWordRelevanceCalculator")
public class ExactWordRelevanceCalculator implements RelevanceCalculator {

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

        Collection<String> searchTerms = buildSearchTermListWithPlurals(searchTerm, locale);

        int relevance = 0;

        for (String term : searchTerms) {

            int name = getNameRelevance(productSearchResult, locale, term, nameWeight);
            int description = 0;

            if (name == 0) {
                description = getDescriptionRelevance(productSearchResult, locale, term, descriptionWeight);
            }

            int keyword = getKeywordRelevance(productSearchResult, locale, term, keywordWeight);

            int category = getCategoryRelevance(productSearchResult, locale, term, categoryWeight);

            relevance += name + keyword + description + category;
        }

        return relevance;
    }

    private int getCategoryRelevance(ProductSearchResult productSearchResult, Locale locale, String searchTerm,
            int weight) {
        int category = 0;
        for (String categoryText : productSearchResult.getCategories()) {
            Collection<String> words = SearchTokenizer.tokenizeComparisonItem(categoryText, locale);
            category += isExactWordMatch(words, searchTerm, weight);
        }
        return category;
    }

    private int getDescriptionRelevance(ProductSearchResult productSearchResult, Locale locale, String searchTerm,
            int weight) {
        int description = isPartialWordMatch(
                SearchTokenizer.tokenizeComparisonItem(productSearchResult.getDescription(), locale), searchTerm,
                weight);
        return description;
    }

    private int getKeywordRelevance(ProductSearchResult productSearchResult, Locale locale, String searchTerm,
            int weight) {
        int keyword = 0;
        if (productSearchResult.getKeywords() != null) {
            keyword = isExactWordMatch(
                    SearchTokenizer.tokenizeComparisonItem(productSearchResult.getKeywords(), locale), searchTerm,
                    weight);
        }
        return keyword;
    }

    private int getNameRelevance(ProductSearchResult productSearchResult, Locale locale, String searchTerm, int weight) {
        int name = isExactWordMatch(SearchTokenizer.tokenizeComparisonItem(productSearchResult.getName(), locale),
                searchTerm, weight);
        return name;
    }

    private Collection<String> buildSearchTermListWithPlurals(String searchTerm, Locale locale) {
        Collection<String> searchTerms = SearchTokenizer.tokenizeAndSanitizeSearchTerms(searchTerm, locale);

        List<String> plurals = Lists.newArrayList();
        for (String term : searchTerms) {
            plurals.add(term + "s");
        }
        searchTerms.addAll(plurals);

        return searchTerms;
    }

    private int isExactWordMatch(Collection<String> words, String term, int weight) {
        int relevance = 0;

        for (String word : words) {
            if (word.equals(term)) {
                relevance += weight;
                break;
            }
        }
        return relevance;
    }

    private int isPartialWordMatch(Collection<String> words, String term, int weight) {
        int relevance = 0;

        for (String word : words) {
            if (word.trim().equals(term)) {
                relevance += weight;
                break;
            }
        }
        return relevance;
    }
}
