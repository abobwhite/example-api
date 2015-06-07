package com.daugherty.persistence;

import java.util.Map;

/**
 * Defines operations for generating (relatively) ad-hoc query criteria.
 */
public interface QueryCriteria {

    public static final String LANGUAGE_PARAMETER_NAME = "language";

    Map<String, Object> getParameterMap();

    String getLanguage();

    String getCombinedQueryClauses();

}
