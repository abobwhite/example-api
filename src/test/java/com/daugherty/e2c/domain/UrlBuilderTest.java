package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class UrlBuilderTest {

    @Test
    public void generateProtocolIndependentUrlForChineseLocaleUsesBuyerDomain() {
        UrlBuilder builder = new UrlBuilder("buyer.com", "global.com", "", "");
        assertThat(builder.buildProtocolIndependentUrl("/resource/", Locale.CHINESE), is("//buyer.com/resource/"));
    }

    @Test
    public void generateProtocolIndependentUrlForNonChineseLocaleUsesBuyerDomain() {
        UrlBuilder builder = new UrlBuilder("buyer.com", "global.com", "", "");
        assertThat(builder.buildProtocolIndependentUrl("/resource/", new Locale("es")), is("//global.com/resource/"));
    }

    @Test
    public void generateHttpUrlForChineseLocaleUsesBuyerDomain() {
        UrlBuilder builder = new UrlBuilder("buyer.com", "global.com", "", "");
        assertThat(builder.buildHttpUrl("/resource/", Locale.CHINESE), is("http://buyer.com/resource/"));
    }

    @Test
    public void generateHttpUrlForNonChineseLocaleUsesGlobalDomain() {
        // TODO Should not be allowed?
        UrlBuilder builder = new UrlBuilder("buyer.com", "global.com", "", "");
        assertThat(builder.buildHttpUrl("/resource/", new Locale("es")), is("http://global.com/resource/"));
    }

    @Test
    public void generateHttpsUrlForChineseLocaleUsesBuyerDomain() {
        // TODO Should not be allowed?
        UrlBuilder builder = new UrlBuilder("buyer.com", "global.com", "", "");
        assertThat(builder.buildHttpsUrl("/resource/", Locale.CHINESE), is("https://buyer.com/resource/"));
    }

    @Test
    public void generateHttpsUrlForNonChineseLocaleUsesGlobalDomain() {
        UrlBuilder builder = new UrlBuilder("buyer.com", "global.com", "", "");
        assertThat(builder.buildHttpsUrl("/resource/", new Locale("es")), is("https://global.com/resource/"));
    }

}
