package com.daugherty.e2c.domain;

import java.util.Locale;

/**
 * Builds URLs for resources exposed by the API.
 */
public class UrlBuilder {

    private static final String PROTOCOL_INDEPENDENT_PREFIX = "//";
    static final String HTTP_PREFIX = "http:" + PROTOCOL_INDEPENDENT_PREFIX;
    static final String HTTPS_PREFIX = "https:" + PROTOCOL_INDEPENDENT_PREFIX;

    private String buyerDomain;
    private String globalDomain;
    private String buyerSiteUri;
    private String globalSiteUri;

    UrlBuilder() {
        // Needed by CGLIB (for profiling and tracing)
    }

    public UrlBuilder(String buyerDomain, String globalDomain, String buyerSiteUri, String globalSiteUri) {
        this.buyerDomain = buyerDomain;
        this.globalDomain = globalDomain;
        this.buyerSiteUri = buyerSiteUri;
        this.globalSiteUri = globalSiteUri;
    }

    public String buildProtocolIndependentUrl(String uri, Locale locale) {
        return buildUrl(PROTOCOL_INDEPENDENT_PREFIX, uri, locale);
    }

    private String buildUrl(String prefix, String uri, Locale locale) {
        return prefix + domainFor(locale) + uri;
    }

    private String domainFor(Locale locale) {
        return Locale.CHINESE.equals(locale) ? buyerDomain : globalDomain;
    }

    public String buildHttpUrl(String uri, Locale locale) {
        return buildUrl(HTTP_PREFIX, uri, locale);
    }

    public String buildHttpsUrl(String uri, Locale locale) {
        return buildUrl(HTTPS_PREFIX, uri, locale);
    }

    public String buildBuyerSiteUrl() {
        return buildHttpUrl(buyerSiteUri, Locale.CHINESE);
    }

    public String buildGlobalSiteUrl() {
        return buildHttpUrl(globalSiteUri, Locale.ENGLISH);
    }

}
