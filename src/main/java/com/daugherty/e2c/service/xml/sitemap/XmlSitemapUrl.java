package com.daugherty.e2c.service.xml.sitemap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.daugherty.e2c.domain.SitemapUrl;

/**
 * Marshals/unmarshals a SitemapUrl object to/from XML.
 * 
 */
@XmlType(propOrder = { "loc", "lastMod", "changeFreq" })
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlSitemapUrl {

    public static final String DEFAULT_CHANGE_FREQUENCY = "daily";
    private static final DateFormat ISO8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @XmlElement(name = "loc")
    private String loc;
    @XmlElement(name = "lastmod")
    private String lastMod;
    @XmlElement(name = "changefreq")
    private String changeFreq;

    public XmlSitemapUrl() {
    }

    public XmlSitemapUrl(SitemapUrl url) {
        loc = url.getLocation();
        lastMod = ISO8601_DATE_FORMAT.format(url.getLastModification());
        changeFreq = DEFAULT_CHANGE_FREQUENCY;
    }

    public String getLoc() {
        return loc;
    }

    public String getLastMod() {
        return lastMod;
    }

    public String getChangeFreq() {
        return changeFreq;
    }

}
