package com.daugherty.e2c.business;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.daugherty.e2c.domain.UrlBuilder;

/**
 * Creates URIs to identify stored documents.
 */
public class DocumentUrlFactory {

    public final static String THUMBNAIL = "-TM";
    public final static String WATERMARK = "-WM";
    public final static String HUIJINKOU_THUMBNAIL = "-HTM";
    public final static String HUIJINKOU_WATERMARK = "-HWM";

    private UrlBuilder urlBuilder;
    private String uri;

    DocumentUrlFactory() {
        // Needed by CGLIB (for profiling and tracing)
    }

    public DocumentUrlFactory(UrlBuilder urlBuilder, String uri) {
        this.urlBuilder = urlBuilder;
        this.uri = uri;
    }

    public String createDocumentUrl(String documentKey, Locale locale) {
        return StringUtils.isEmpty(documentKey) ? null : Locale.CHINESE.equals(locale) ? baseUrlForHuijinkou(locale)
                + documentKey : baseUrlFor(locale) + documentKey;
    }

    private String baseUrlFor(Locale locale) {
        return urlBuilder.buildProtocolIndependentUrl(uri, locale);
    }

    private String baseUrlForHuijinkou(Locale locale) {
        return urlBuilder.buildHttpUrl(uri, locale);
    }

    public String createE2CDocumentThumbnailUrl(String documentKey, Locale locale) {
        return createDocumentUrl(documentKey, locale) + THUMBNAIL;
    }

    public String createE2CDocumentWatermarkUrl(String documentKey, Locale locale) {
        return createDocumentUrl(documentKey, locale) + WATERMARK;
    }

    public String createHuijinkouDocumentThumbnailUrl(String documentKey, Locale locale) {
        return createDocumentUrl(documentKey, locale) + HUIJINKOU_THUMBNAIL;
    }

    public String createHuijinkouDocumentWatermarkUrl(String documentKey, Locale locale) {
        return createDocumentUrl(documentKey, locale) + HUIJINKOU_WATERMARK;
    }

    public String removeDocumentUrl(String doucumentUrl, Locale locale) {

        if (StringUtils.isEmpty(doucumentUrl)) {
            return null;
        }

        int index = doucumentUrl.lastIndexOf(baseUrlFor(locale), 0) + baseUrlFor(locale).length();

        return doucumentUrl.substring(index++);
    }
}
