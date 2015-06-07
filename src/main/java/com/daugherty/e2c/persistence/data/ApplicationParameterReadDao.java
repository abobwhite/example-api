package com.daugherty.e2c.persistence.data;

import java.util.Map;

/**
 * Defines database read operations for application parameters.
 */
public interface ApplicationParameterReadDao {

    static final String RECIPIENT_APPROVAL_THRESHOLD_NAME = "Recipient Approval Threshold";

    // Global Site
    static final String NAME_ATTRIBUTE_RELEVANCE_WEIGHT = "Name Attribute Relevance Weight";
    static final String KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT = "Keyword Attribute Relevance Weight";
    static final String CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT = "Category Attribute Relevance Weight";
    static final String DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT = "Description Attribute Relevance Weight";

    static final String EXACT_TERM_MATCH_RELEVANCE_DEGREE = "Exact Search Term Relevance Degree";
    static final String EXACT_WORD_MATCH_RELEVANCE_DEGREE = "Exact Word Search Term Relevance Degree";
    static final String PARTIAL_WORD_MATCH_RELEVANCE_DEGREE = "Partial Word Search Term Relevance Degree";

    static final String RELEVANCE_BUCKETS = "Relevance Buckets";
    static final String RELEVANCE_FLOOR = "Relevance Floor";

    // Chinese Site
    static final String CHINESE_NAME_ATTRIBUTE_RELEVANCE_WEIGHT = "Chinese Name Attribute Relevance Weight";
    static final String CHINESE_KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT = "Chinese Keyword Attribute Relevance Weight";
    static final String CHINESE_CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT = "Chinese Category Attribute Relevance Weight";
    static final String CHINESE_DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT = "Chinese Description Attribute Relevance Weight";

    static final String CHINESE_EXACT_TERM_MATCH_RELEVANCE_DEGREE = "Chinese Exact Search Term Relevance Degree";
    static final String CHINESE_EXACT_WORD_MATCH_RELEVANCE_DEGREE = "Chinese Exact Word Search Term Relevance Degree";
    static final String CHINESE_PARTIAL_WORD_MATCH_RELEVANCE_DEGREE = "Chinese Partial Word Search Term Relevance Degree";

    static final String CHINESE_RELEVANCE_BUCKETS = "Chinese Relevance Buckets";
    static final String CHINESE_RELEVANCE_FLOOR = "Chinese Relevance Floor";

    String loadValueForName(String name);

    Map<String, String> loadAllValues();

}
