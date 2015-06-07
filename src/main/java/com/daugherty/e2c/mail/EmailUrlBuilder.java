package com.daugherty.e2c.mail;

import java.util.Locale;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.UrlBuilder;

/**
 * Builds URLs for use in Emails.
 */
public class EmailUrlBuilder {

    private UrlBuilder urlBuilder;
    private String mailUri;

    EmailUrlBuilder() {
        // Needed by CGLIB (for profiling and tracing)
    }

    public EmailUrlBuilder(UrlBuilder urlBuilder, String mailUri) {
        this.urlBuilder = urlBuilder;
        this.mailUri = mailUri;
    }

    public String buildMailUrl(Locale locale) {
        return mailUri.startsWith("/") ? buildMailUrlWithProtocolBasedOn(locale) : mailUri;
    }

    private String buildMailUrlWithProtocolBasedOn(Locale locale) {
        return urlBuilder.buildHttpUrl(mailUri, locale);
    }

    public String buildMailUrl(Language language) {
        return buildMailUrl(new Locale(language.getAbbreviation()));
    }

    public String buildBuyerSiteUrl() {
        return urlBuilder.buildBuyerSiteUrl();
    }

    public String buildGlobalSiteUrl() {
        return urlBuilder.buildGlobalSiteUrl();
    }

}
