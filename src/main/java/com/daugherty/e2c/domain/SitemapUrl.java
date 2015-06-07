package com.daugherty.e2c.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents the URL of a specific page within the website, for inclusion in the Sitemap.
 * <p>
 * TODO This class should use UrlBuilder rather than hardcode URLs, although Sitemaps are only relevant to Production.
 */
public class SitemapUrl extends Entity {

    private static final long serialVersionUID = 1L;

    private static final Date APP_STARTUP_DATE = new Date();
    private static final String GLOBAL_SITE_URL = "https://www.export-to-china.com/";
    private static final String BUYER_SITE_URL = "http://www.huijinkou.com/";

    public static final SitemapUrl GLOBAL_HOME = new SitemapUrl(GLOBAL_SITE_URL);
    public static final SitemapUrl GLOBAL_HOW_WE_WORK = new SitemapUrl(GLOBAL_SITE_URL + "howWeWork");
    public static final SitemapUrl GLOBAL_BENEFITS = new SitemapUrl(GLOBAL_SITE_URL + "benefits");
    public static final SitemapUrl GLOBAL_HOW_TO_EXPORT = new SitemapUrl(GLOBAL_SITE_URL + "howToExport");
    public static final SitemapUrl GLOBAL_PARTNERS = new SitemapUrl(GLOBAL_SITE_URL + "partners");
    public static final SitemapUrl GLOBAL_DO_GOOD = new SitemapUrl(GLOBAL_SITE_URL + "doGood");
    public static final SitemapUrl GLOBAL_ABOUT = new SitemapUrl(GLOBAL_SITE_URL + "about");
    public static final SitemapUrl GLOBAL_OUR_SERVICES = new SitemapUrl(GLOBAL_SITE_URL + "ourServices");
    public static final SitemapUrl GLOBAL_CHINA_DIRECT = new SitemapUrl(GLOBAL_SITE_URL + "chinaDirect");
    public static final SitemapUrl GLOBAL_CHINA_MARKET_OPPORTUNITY = new SitemapUrl(GLOBAL_SITE_URL
            + "chinaMarketOpportunity");
    public static final SitemapUrl GLOBAL_WHY_EXPORTING_IS_GOOD = new SitemapUrl(GLOBAL_SITE_URL + "whyExportingIsGood");
    public static final SitemapUrl GLOBAL_HOW_TO_START_EXPORTING = new SitemapUrl(GLOBAL_SITE_URL
            + "howToStartExportingToChina");
    public static final SitemapUrl GLOBAL_WHY_INTERNET_IS_KEY = new SitemapUrl(GLOBAL_SITE_URL
            + "whyInternetPresenceIsKey");
    public static final SitemapUrl GLOBAL_DEMANDED_PRODUCTS = new SitemapUrl(GLOBAL_SITE_URL + "demandedProducts");
    public static final SitemapUrl GLOBAL_HOW_WE_HELP_YOU_SUCCEED = new SitemapUrl(GLOBAL_SITE_URL
            + "howWeHelpYouSucceed");

    public static final SitemapUrl BUYER_HOME = new SitemapUrl(BUYER_SITE_URL);
    public static final SitemapUrl BUYER_HOW_WE_WORK = new SitemapUrl(BUYER_SITE_URL + "howWeWork");
    public static final SitemapUrl BUYER_BENEFITS = new SitemapUrl(BUYER_SITE_URL + "benefits");
    public static final SitemapUrl BUYER_HOW_TO_IMPORT = new SitemapUrl(BUYER_SITE_URL + "howToImport");
    public static final SitemapUrl BUYER_PARTNERS = new SitemapUrl(BUYER_SITE_URL + "partners");
    public static final SitemapUrl BUYER_DO_GOOD = new SitemapUrl(BUYER_SITE_URL + "doGood");
    public static final SitemapUrl BUYER_ABOUT = new SitemapUrl(BUYER_SITE_URL + "about");

    public static final int MAX_URL_COUNT = 50000;

    public static SitemapUrl createSupplierUrl(String publicId, Date lastModification, boolean forBuyerSite) {
        if (forBuyerSite) {
            return new SitemapUrl(BUYER_SITE_URL + "supplier/" + publicId, lastModification);
        } else {
            return new SitemapUrl(GLOBAL_SITE_URL + "supplier/" + publicId, lastModification);
        }
    }

    public static SitemapUrl createProductUrl(long id, String name, Date lastModification, boolean forBuyerSite) {
        if (forBuyerSite) {
            return new SitemapUrl(buildProductUrl(BUYER_SITE_URL, id, name, forBuyerSite), lastModification);
        } else {
            return new SitemapUrl(buildProductUrl(GLOBAL_SITE_URL, id, name, forBuyerSite), lastModification);
        }
    }

    public static SitemapUrl createCategoryUrl(long id, String name, String type, Date lastModification,
            boolean forBuyerSite) {
        String category = "PARENT".equalsIgnoreCase(type) ? "category/" : "subcategory/";
        if (forBuyerSite) {
            return new SitemapUrl(BUYER_SITE_URL + "product/" + category + id, lastModification);
        } else {
            return new SitemapUrl(GLOBAL_SITE_URL + "product/" + category + id, lastModification);
        }
    }

    public static String buildProductUrl(String siteUrl, Long id, String name, boolean forBuyerSite) {
        if (forBuyerSite) {
            String pinyinName = TranslationUtils.toPinyinIfChinese(name, Locale.CHINESE);
            return siteUrl + "product/" + id + "/" + urlEncode(pinyinName);
        } else {
            return siteUrl + "product/" + id + "/" + urlEncode(name);
        }
    }

    public static String urlEncode(String encodedPortion) {
        if (StringUtils.isNotBlank(encodedPortion)) {
            try {
                return URLEncoder.encode(encodedPortion, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            return null;
        }
    }

    private final String location;
    private final Date lastModification;

    private SitemapUrl(String location) {
        this(location, APP_STARTUP_DATE);
    }

    private SitemapUrl(String location, Date lastModification) {
        this.location = location;
        this.lastModification = lastModification;
    }

    public String getLocation() {
        return location;
    }

    public Date getLastModification() {
        return lastModification;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        SitemapUrl rhs = (SitemapUrl) obj;
        builder.append(location, rhs.location).append(lastModification, rhs.lastModification);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 77;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(location).append(lastModification);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("location", location).append("lastModification", lastModification);
    }

}
