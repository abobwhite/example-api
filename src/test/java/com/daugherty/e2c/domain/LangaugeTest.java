package com.daugherty.e2c.domain;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LangaugeTest {

    @Test
    public void findByIdFindsAllSupportedLanguages() {
        assertThat(Language.findById(Language.ENGLISH.getId()), is(Language.ENGLISH));
        assertThat(Language.findById(Language.CHINESE.getId()), is(Language.CHINESE));
        assertThat(Language.findById(Language.SPANISH.getId()), is(Language.SPANISH));
    }

    @Test
    public void findByIdReturnsNullIfIdNull() {
        assertThat(Language.findById(null), is(nullValue()));
    }

    @Test
    public void findByIdReturnsNullIfIdZero() {
        assertThat(Language.findById(0L), is(nullValue()));
    }

    @Test(expected = EnumConstantNotPresentException.class)
    public void findByIdThrowsExceptionWhenIdNotNullOrZeroOrValidId() {
        Language.findById(9999999L);
    }

    @Test
    public void findByAbbreviationFindsAllSupportedLanguages() {
        assertThat(Language.findByAbbreviation(Language.ENGLISH.getAbbreviation()), is(Language.ENGLISH));
        assertThat(Language.findByAbbreviation(Language.CHINESE.getAbbreviation()), is(Language.CHINESE));
        assertThat(Language.findByAbbreviation(Language.SPANISH.getAbbreviation()), is(Language.SPANISH));
    }

    @Test
    public void findByAbbreviationReturnsNullIfAbbreviationNull() {
        assertThat(Language.findByAbbreviation(null), is(nullValue()));
    }

    @Test
    public void findByAbbreviationReturnsNullIfAbbreviationJustSpaces() {
        assertThat(Language.findByAbbreviation("   "), is(nullValue()));
    }

    @Test(expected = EnumConstantNotPresentException.class)
    public void findByAbbreviationThrowsExceptionWhenAbbreviationNotRecognized() {
        Language.findByAbbreviation("fr");
    }
}
