package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.daugherty.e2c.domain.UrlBuilder;

public class DocumentUrlFactoryTest {

    private final DocumentUrlFactory factory = new DocumentUrlFactory(
            new UrlBuilder("buyer.e2c", "global.e2c", "", ""), "/baseUri/");

    @Test
    public void createUrlForChineseLocaleGeneratesProtocolIndependentUrlForBuyerDomain() {
        assertThat(factory.createDocumentUrl("Document Key", Locale.CHINESE),
                is("http://buyer.e2c/baseUri/Document Key"));
    }

    @Test
    public void createThumbnailUrlForChineseLocaleGeneratesProtocolIndependentUrlForBuyerDomain() {
        assertThat(factory.createE2CDocumentThumbnailUrl("Document Key", Locale.CHINESE),
                is("http://buyer.e2c/baseUri/Document Key" + DocumentUrlFactory.THUMBNAIL));
    }

    @Test
    public void createWatermarkUrlForChineseLocaleGeneratesProtocolIndependentUrlForBuyerDomain() {
        assertThat(factory.createE2CDocumentWatermarkUrl("Document Key", Locale.CHINESE),
                is("http://buyer.e2c/baseUri/Document Key" + DocumentUrlFactory.WATERMARK));
    }

    @Test
    public void createHuijinkouThumbnailUrlForChineseLocaleGeneratesProtocolIndependentUrlForBuyerDomain() {
        assertThat(factory.createHuijinkouDocumentThumbnailUrl("Document Key", Locale.CHINESE),
                is("http://buyer.e2c/baseUri/Document Key" + DocumentUrlFactory.HUIJINKOU_THUMBNAIL));
    }

    @Test
    public void createHuijinkouWatermarkUrlForChineseLocaleGeneratesProtocolIndependentUrlForBuyerDomain() {
        assertThat(factory.createHuijinkouDocumentWatermarkUrl("Document Key", Locale.CHINESE),
                is("http://buyer.e2c/baseUri/Document Key" + DocumentUrlFactory.HUIJINKOU_WATERMARK));
    }

    @Test
    public void removeUrlForChineseLocaleRemovesProtocolIndependentUrlForBuyerDomain() {
        assertThat(factory.removeDocumentUrl("//buyer.e2c/baseUri/Document Key", Locale.CHINESE), is("Document Key"));
    }

    @Test
    public void createUrlForNonChineseLocaleGeneratesProtocolIndependentUrlForGlobalDomain() {
        assertThat(factory.createDocumentUrl("Document Key", Locale.ENGLISH), is("//global.e2c/baseUri/Document Key"));
    }

    @Test
    public void createThumbnailUrlForNonChineseLocaleGeneratesProtocolIndependentUrlForGlobalDomain() {
        assertThat(factory.createE2CDocumentThumbnailUrl("Document Key", Locale.ENGLISH),
                is("//global.e2c/baseUri/Document Key" + DocumentUrlFactory.THUMBNAIL));
    }

    @Test
    public void createWatermarkUrlForNonChineseLocaleGeneratesProtocolIndependentUrlForGlobalDomain() {
        assertThat(factory.createE2CDocumentWatermarkUrl("Document Key", Locale.ENGLISH),
                is("//global.e2c/baseUri/Document Key" + DocumentUrlFactory.WATERMARK));
    }

    @Test
    public void createHuijinkouThumbnailUrlForNonChineseLocaleGeneratesProtocolIndependentUrlForGlobalDomain() {
        assertThat(factory.createHuijinkouDocumentThumbnailUrl("Document Key", Locale.ENGLISH),
                is("//global.e2c/baseUri/Document Key" + DocumentUrlFactory.HUIJINKOU_THUMBNAIL));
    }

    @Test
    public void createHuijinkouWatermarkUrlForNonChineseLocaleGeneratesProtocolIndependentUrlForGlobalDomain() {
        assertThat(factory.createHuijinkouDocumentWatermarkUrl("Document Key", Locale.ENGLISH),
                is("//global.e2c/baseUri/Document Key" + DocumentUrlFactory.HUIJINKOU_WATERMARK));
    }

    @Test
    public void removeUrlForNonChineseLocaleRemovesProtocolIndependentUrlForGlobalDomain() {
        assertThat(factory.removeDocumentUrl("//global.e2c/baseUri/Document Key", Locale.ENGLISH), is("Document Key"));
    }

}
