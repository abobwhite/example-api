package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.SitemapUrl;
import com.daugherty.e2c.persistence.data.SitemapUrlReadDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Spring JDBC implementation of the SitemapUrl database operations.
 */
@Repository
public class JdbcSitemapUrlDao extends SortAndPaginationJdbcDao implements SitemapUrlReadDao {

    static final String PRODUCT_NAME_COLUMN_NAME = "product_name";
    static final String PRODUCT_CATEGORY_ID = "product_category_id";
    static final String PRODUCT_CATEGORY_NAME = "product_category";
    static final String PRODUCT_CATEGORY_TYPE = "type";
    static final String PRODUCT_CATEGORY_MODIFIED_DATE = "last_modified_date";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
    }

    @Override
    public List<SitemapUrl> getUntranslatedPublishedProductUrls(QueryCriteria criteria) {
        LOGGER.debug("Getting untranslated Product Sitemap URLs from the database");
        String sql = getSql("/sitemapurl/get-untranslated-products.sql") + criteria.getCombinedQueryClauses();
        RowMapper<SitemapUrl> rowMapper = new ProductSitemapUrlRowMapper(false);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), rowMapper);
    }

    @Override
    public List<SitemapUrl> getUntranslatedApprovedSupplierUrls(QueryCriteria criteria) {
        LOGGER.debug("Getting untranslated Supplier Sitemap URLs from the database");
        String sql = getSql("/sitemapurl/get-approved-suppliers.sql") + criteria.getCombinedQueryClauses();
        RowMapper<SitemapUrl> rowMapper = new SupplierSitemapUrlRowMapper(false);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), rowMapper);
    }

    @Override
    public List<SitemapUrl> getTranslatedPublishedProductUrls(QueryCriteria criteria) {
        LOGGER.debug("Getting translated Product Sitemap URLs from the database");
        String sql = getSql("/sitemapurl/get-translated-products.sql") + criteria.getCombinedQueryClauses();
        RowMapper<SitemapUrl> rowMapper = new ProductSitemapUrlRowMapper(true);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), rowMapper);
    }

    @Override
    public List<SitemapUrl> getTranslatedApprovedSupplierUrls(QueryCriteria criteria) {
        LOGGER.debug("Getting translated Supplier Sitemap URLs from the database");
        String sql = getSql("/sitemapurl/get-approved-suppliers.sql") + criteria.getCombinedQueryClauses();
        RowMapper<SitemapUrl> rowMapper = new SupplierSitemapUrlRowMapper(true);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), rowMapper);
    }

    @Override
    public List<SitemapUrl> getBuyerCategoryUrls(QueryCriteria criteria) {
        LOGGER.debug("Getting Category urls from the database");
        String sql = getSql("/sitemapurl/get-categories.sql") + criteria.getCombinedQueryClauses();
        RowMapper<SitemapUrl> rowMapper = new CategorySitemapUrlRowMapper(true);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), rowMapper);
    }

    @Override
    public List<SitemapUrl> getSupplierCategoryUrls(QueryCriteria criteria) {
        LOGGER.debug("Getting Category urls from the database");
        String sql = getSql("/sitemapurl/get-categories.sql") + criteria.getCombinedQueryClauses();
        RowMapper<SitemapUrl> rowMapper = new CategorySitemapUrlRowMapper(false);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), rowMapper);
    }

    private final class ProductSitemapUrlRowMapper implements RowMapper<SitemapUrl> {
        private final boolean forBuyerSite;

        public ProductSitemapUrlRowMapper(boolean forBuyerSite) {
            this.forBuyerSite = forBuyerSite;
        }

        @Override
        public SitemapUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
            return SitemapUrl.createProductUrl(rs.getLong(PRODUCT_ID_COLUMN_NAME),
                    rs.getString(PRODUCT_NAME_COLUMN_NAME), rs.getTimestamp(EVENT_TIME_COLUMN_NAME), forBuyerSite);
        }
    }

    private final class SupplierSitemapUrlRowMapper implements RowMapper<SitemapUrl> {
        private final boolean forBuyerSite;

        public SupplierSitemapUrlRowMapper(boolean forBuyerSite) {
            this.forBuyerSite = forBuyerSite;
        }

        @Override
        public SitemapUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
            return SitemapUrl.createSupplierUrl(rs.getString(PUBLIC_PARTY_ID_COLUMN_NAME),
                    rs.getTimestamp(EVENT_TIME_COLUMN_NAME), forBuyerSite);
        }
    }
    
    private final class CategorySitemapUrlRowMapper implements RowMapper<SitemapUrl> {
        private final boolean forBuyerSite;

        public CategorySitemapUrlRowMapper(boolean forBuyerSite) {
            this.forBuyerSite = forBuyerSite;
        }
        @Override
        public SitemapUrl mapRow(ResultSet rs, int rowNum) throws SQLException {
            return SitemapUrl.createCategoryUrl(rs.getLong(PRODUCT_CATEGORY_ID), rs.getString(PRODUCT_CATEGORY_NAME), rs.getString(PRODUCT_CATEGORY_TYPE),
                    rs.getTimestamp(LAST_MODIFIED_DATE_COLUMN_NAME), forBuyerSite);
        }
    }
}
