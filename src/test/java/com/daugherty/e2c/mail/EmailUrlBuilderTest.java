package com.daugherty.e2c.mail;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.UrlBuilder;

public class EmailUrlBuilderTest {

    @Test
    public void buildMailUrlForChineseLocaleWithPlainUri() {
        EmailUrlBuilder builder = new EmailUrlBuilder(new UrlBuilder("buyer.e2c", "global.e2c", "/buyer", "/global"),
                "/mail");
        assertThat(builder.buildMailUrl(Locale.CHINESE), is("http://buyer.e2c/mail"));
    }

    @Test
    public void buildMailUrlForNonChineseLocaleWithPlainUri() {
        EmailUrlBuilder builder = new EmailUrlBuilder(new UrlBuilder("buyer.e2c", "global.e2c", "/buyer", "/global"),
                "/mail");
        assertThat(builder.buildMailUrl(new Locale("es")), is("http://global.e2c/mail"));
    }

    @Test
    public void buildMailUrlWithFullUrlRegardlessOfLocale() {
        EmailUrlBuilder builder = new EmailUrlBuilder(new UrlBuilder("buyer.e2c", "global.e2c", "/buyer", "/global"),
                "http://other/emails");
        assertThat(builder.buildMailUrl(new Locale("es")), is("http://other/emails"));
    }

    @Test
    public void buildMailUrlForChineseLanguageWithPlainUri() {
        EmailUrlBuilder builder = new EmailUrlBuilder(new UrlBuilder("buyer.e2c", "global.e2c", "/buyer", "/global"),
                "/mail");
        assertThat(builder.buildMailUrl(Language.CHINESE), is("http://buyer.e2c/mail"));
    }

    @Test
    public void buildMailUrlForNonChineseLanguageWithPlainUri() {
        EmailUrlBuilder builder = new EmailUrlBuilder(new UrlBuilder("buyer.e2c", "global.e2c", "/buyer", "/global"),
                "/mail");
        assertThat(builder.buildMailUrl(Language.SPANISH), is("http://global.e2c/mail"));
    }

    @Test
    public void buildMailUrlWithFullUrlRegardlessOfLanguage() {
        EmailUrlBuilder builder = new EmailUrlBuilder(new UrlBuilder("buyer.e2c", "global.e2c", "/buyer", "/global"),
                "http://other/emails");
        assertThat(builder.buildMailUrl(Language.SPANISH), is("http://other/emails"));
    }

    @Test
    public void buildBuyerSiteUrl() {
        EmailUrlBuilder builder = new EmailUrlBuilder(new UrlBuilder("buyer.e2c", "global.e2c", "/buyer", "/global"),
                "/mail");
        assertThat(builder.buildBuyerSiteUrl(), is("http://buyer.e2c/buyer"));
    }

    @Test
    public void buildGlobalSiteUrl() {
        EmailUrlBuilder builder = new EmailUrlBuilder(new UrlBuilder("buyer.e2c", "global.e2c", "/buyer", "/global"),
                "/mail");
        assertThat(builder.buildGlobalSiteUrl(), is("http://global.e2c/global"));
    }

}
