package com.daugherty.e2c.business.search;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Tokenizes search terms and comparison items based on Locale.
 */
public class SearchTokenizer {

    public static final String SPLIT_REGEX = "(\\s\\&\\s)|[\\s\\/\\,\\?\\!\\.\\'\\_\\-\\(\\)\\:\\;\\\"\\++]+";

    public static final List<String> BLACK_LIST = Lists.newArrayList("the", "is", "or", "and", "are", "an", ".",
            "that", "some", "this", "those", "will", " ", "", "el", "la", "es", "un", "al", "products", "productos", "产品");

    private SearchTokenizer() {
    }

    public static String[] tokenizeSearchTerms(String searchTerms, Locale locale) {
        if (Locale.CHINESE.equals(locale)) {
            return arrayFrom(uniqueCharactersFrom(searchTerms));
        } else {
            return searchTerms.split(SPLIT_REGEX);
        }
    }

    private static Set<String> uniqueCharactersFrom(String searchTerms) {
        Set<String> uniqueTokens = Sets.newLinkedHashSet();
        char[] searchTermChars = searchTerms.toCharArray();
        for (int i = 0; i < searchTermChars.length; i++) {
            uniqueTokens.add(new String(searchTermChars, i, 1));
        }
        return uniqueTokens;
    }

    private static String[] arrayFrom(Set<String> uniqueTokens) {
        String[] tokens = new String[uniqueTokens.size()];
        return uniqueTokens.toArray(tokens);
    }

    public static Collection<String> tokenizeAndSanitizeSearchTerms(String searchTerms, Locale locale) {
        String[] searchTokens = tokenizeSearchTerms(searchTerms.toLowerCase(locale), locale);
        List<String> searchTermList = Lists.newArrayList(searchTokens);
        searchTermList.removeAll(BLACK_LIST);

        if (Locale.CHINESE.equals(locale)) {
            return searchTermList;
        } else {
            List<String> sanitized = Lists.newArrayList();

            for (String term : searchTermList) {
                if (term.length() > 1) {
                    sanitized.add(term);
                }
            }

            return sanitized;
        }
    }

    public static Collection<String> tokenizeComparisonItem(String comparisonItem, Locale locale) {
        if (Locale.CHINESE.equals(locale)) {
            return uniqueCharactersFrom(comparisonItem.toLowerCase(locale));
        } else {
            return Lists.newArrayList(comparisonItem.toLowerCase(locale).split(SPLIT_REGEX));
        }
    }

}
