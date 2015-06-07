package com.daugherty.e2c.business.search.relevance;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.search.RelevanceCalculator;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;

/**
 * Determines Relevance of a Product Search Results for Exact Search Terms
 */
@Component("exactSearchTermRelevanceCalculator")
public class ExactSearchTermRelevanceCalculator implements RelevanceCalculator {

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

        int name = calculateNameRelevance(productSearchResult.getName(), searchTerm, locale, nameWeight);
        int description = calculateDescriptionRelevance(productSearchResult.getDescription(), searchTerm, locale,
                descriptionWeight);
        int keyword = calculateKeywordRelevance(productSearchResult.getKeywords(), searchTerm, locale, keywordWeight);
        int category = calculateCategoryRelevance(productSearchResult.getCategories(), searchTerm, locale,
                categoryWeight);

        return name + keyword + description + category;
    }

    private int calculateNameRelevance(String productName, String searchTerm, Locale locale, int weight) {
        if (productName.toLowerCase(locale).equals(searchTerm.toLowerCase(locale))) {
            return weight;
        }

        return 0;
    }

    private int calculateKeywordRelevance(String keyword, String searchTerm, Locale locale, int weight) {
        if (keyword != null) {
            if (keyword.toLowerCase(locale).equals(searchTerm.toLowerCase(locale))) {
                return weight;
            }
        }

        return 0;
    }

    private int calculateDescriptionRelevance(String description, String searchTerm, Locale locale, int weight) {
        if (description.toLowerCase(locale).equals(searchTerm.toLowerCase(locale))) {
            return weight;
        }

        return 0;
    }

    private int calculateCategoryRelevance(List<String> categories, String searchTerm, Locale locale, int weight) {
        int relevance = 0;

        for (String category : categories) {
            if (category.toLowerCase(locale).equals(searchTerm.toLowerCase(locale))) {
                relevance += weight;
                ;
            }
        }

        return relevance;
    }
}
