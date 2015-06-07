package com.daugherty.e2c.business.search;

import java.util.Locale;
import java.util.Map;

import com.daugherty.e2c.domain.ProductSearchResult;

public interface RelevanceCalculator {

    int calculate(ProductSearchResult productSearchResult, String searchTerm, Locale locale,
            Map<String, String> applicationParameters);
}
