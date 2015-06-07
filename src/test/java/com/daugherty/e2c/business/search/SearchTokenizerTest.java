package com.daugherty.e2c.business.search;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class SearchTokenizerTest {

    @Test
    public void tokenizeGlobalSearchTermsWithSpacesBeforeAndAfterAmpresand() {
        String searchTerms = "M & M";

        String[] terms = SearchTokenizer.tokenizeSearchTerms(searchTerms, Locale.ENGLISH);

        assertThat(terms.length, is(2));
    }

    @Test
    public void tokenizeGlobalSearchTermsWithNoSpacesBeforeAndAfterAmpresand() {
        String searchTerms = "M&M";

        String[] terms = SearchTokenizer.tokenizeSearchTerms(searchTerms, Locale.ENGLISH);

        assertThat(terms.length, is(1));
    }

}
