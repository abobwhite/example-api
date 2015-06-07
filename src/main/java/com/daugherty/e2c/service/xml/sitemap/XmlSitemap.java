package com.daugherty.e2c.service.xml.sitemap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

/**
 * Represents a Sitemap document (per http://www.sitemaps.org/schemas/sitemap/0.9).
 */
@XmlRootElement(name = "urlset", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlSitemap {

    @XmlElement(name = "url")
    private final List<XmlSitemapUrl> urls = Lists.newArrayList();

    public List<XmlSitemapUrl> getUrls() {
        return urls;
    }

    public void add(XmlSitemapUrl url) {
        urls.add(url);
    }

}
