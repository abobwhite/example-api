package com.daugherty.e2c.service.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.LegacyIdMapper;
import com.daugherty.e2c.business.accessor.filter.SitemapUrlFilter;
import com.daugherty.e2c.domain.SitemapUrl;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonId;
import com.daugherty.e2c.service.json.JsonSupplierId;
import com.daugherty.e2c.service.xml.sitemap.XmlSitemap;
import com.daugherty.e2c.service.xml.sitemap.XmlSitemapUrl;

/**
 * REST resource for Search Engine Optimization (SEO) resources, including sitemaps and legacy site redirects.
 */
@Controller
public class SeoService {

    @Inject
    private LegacyIdMapper legacyIdMapper;
    @Inject
    private Accessor<SitemapUrl> sitemapUrlAccessor;

    @RequestMapping(value = "/redirects/categories/{legacyId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonId retrieveCategoryId(@PathVariable Long legacyId) {
        return new JsonId(legacyIdMapper.lookupCategoryId(legacyId));
    }

    @RequestMapping(value = "/redirects/products/{legacyId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonId retrieveProductId(@PathVariable Long legacyId) {
        return new JsonId(legacyIdMapper.lookupProductId(legacyId));
    }

    @RequestMapping(value = "/redirects/suppliers/{legacyId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonSupplierId retrieveSupplierId(@PathVariable Long legacyId) {
        return new JsonSupplierId(legacyIdMapper.lookupSupplierId(legacyId));
    }

    @RequestMapping(value = "/sitemaps/global", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public XmlSitemap buildExportToChinaSitemap() {
        return buildSitemap(Locale.ENGLISH);
    }

    private XmlSitemap buildSitemap(Locale locale) {
        SitemapUrlFilter filter = new SitemapUrlFilter(locale);
        List<SitemapUrl> urls = sitemapUrlAccessor.find(filter);

        XmlSitemap sitemap = new XmlSitemap();
        for (SitemapUrl url : urls) {
            sitemap.add(new XmlSitemapUrl(url));
        }
        return sitemap;
    }

    @RequestMapping(value = "/sitemaps/buyer", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public XmlSitemap buildHuijinkouSitemap() {
        return buildSitemap(Locale.CHINESE);
    }
}
