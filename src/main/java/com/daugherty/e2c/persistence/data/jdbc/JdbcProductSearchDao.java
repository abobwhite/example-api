package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.domain.ProductSearchResult;
import com.daugherty.e2c.persistence.data.ProductSearchReadDao;
import com.google.common.base.Joiner;

/**
 * Spring JDBC implementation of the Product Search database operations.
 */
@Repository
public class JdbcProductSearchDao extends JdbcDao implements ProductSearchReadDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    static final String SNAPSHOT_ID_COLUMN_NAME = "snapshot_id";
    static final String NAME_COLUMN_NAME = "product_name";
    static final String DESCRIPTION_COLUMN_NAME = "product_description";
    static final String KEYWORDS_COLUMN_NAME = "keyword_list";
    static final String CATEGORY_COUNT_COLUMN_NAME = "cat_count";
    static final String PRODUCT_CATEGORY_COLUMN_NAME = "product_category";
    static final String MEMBERSHIP_LEVEL_COLUMN_NAME = "level";
    static final String COUNTRY_COLUMN_NAME = "country_of_origin";
    static final String TRANSLATED_NAME_COLUMN_NAME = "translated_product_name";
    static final String TRANSLATED_DESCRIPTION_COLUMN_NAME = "translated_product_description";
    static final String TRANSLATED_KEYWORDS_COLUMN_NAME = "translated_keyword_list";
    static final String TRANSLATED_PRODUCT_CATEGORY_COLUMN_NAME = "translated_product_category";

    private final static String LOWER = "LOWER";

    @Override
    public List<ProductSearchResult> find(String[] searchTerms, String country, List<Long> categoryIds, Locale locale) {
        LOGGER.debug("Finding Product Search in the database matching " + Arrays.toString(searchTerms));
        String sql = Locale.CHINESE.equals(locale) ? searchChinese(searchTerms, country, categoryIds) : search(
                searchTerms, country, categoryIds, locale);

        LOGGER.debug("Product Search SQL " + sql);

        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new RowMapper<ProductSearchResult>() {

            @Override
            public ProductSearchResult mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ProductSearchResult(rs.getLong(PRODUCT_ID_COLUMN_NAME), rs.getLong(SNAPSHOT_ID_COLUMN_NAME),
                        rs.getString(NAME_COLUMN_NAME), rs.getString(DESCRIPTION_COLUMN_NAME), rs
                                .getString(KEYWORDS_COLUMN_NAME), rs.getInt(CATEGORY_COUNT_COLUMN_NAME), rs
                                .getInt(MEMBERSHIP_LEVEL_COLUMN_NAME));
            }

        });
    }

    private String search(String[] searchTerms, String country, List<Long> categoryIds, Locale locale) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return getSqlQuery(searchTerms, country, locale);
        } else {
            return getSqlQueryForCategory(searchTerms, country, categoryIds, locale);
        }
    }

    private String searchChinese(String[] searchTerms, String country, List<Long> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return getChineseSqlQuery(searchTerms, country);
        } else {
            return getChineseSqlQueryForCategory(searchTerms, country, categoryIds);
        }
    }

    private String getSqlQuery(String[] searchTerms, String country, Locale locale) {
        StringBuilder query = new StringBuilder(getSql("/productsearch/loadBySearchTerms.sql"));
        if (StringUtils.isNotEmpty(country)) {
            query.append(" AND ").append(COUNTRY_COLUMN_NAME).append(" = '").append(country).append("'");
        }

        StringBuilder productCategoryFilterClause = new StringBuilder();
        StringBuilder productFilterClause = new StringBuilder();

        // Setup Product Category Filter Clause
        for (int i = 0; i <= searchTerms.length - 1; i++) {
            productCategoryFilterClause.append(LOWER).append("(").append(PRODUCT_CATEGORY_COLUMN_NAME)
                    .append(") LIKE '%").append(searchTerms[i].toLowerCase(locale)).append("%' ");

            if (i < (searchTerms.length - 1)) {
                productCategoryFilterClause.append(" OR ");
            }
        }

        // Setup Product Filter Clause
        for (int i = 0; i <= searchTerms.length - 1; i++) {
            productFilterClause.append(LOWER).append("(").append(NAME_COLUMN_NAME).append(") LIKE '%")
                    .append(searchTerms[i].toLowerCase(locale)).append("%' OR ");
            productFilterClause.append(LOWER).append("(").append(KEYWORDS_COLUMN_NAME).append(") LIKE '%")
                    .append(searchTerms[i].toLowerCase(locale)).append("%' OR ");
            productFilterClause.append(LOWER).append("(").append(DESCRIPTION_COLUMN_NAME).append(") LIKE '%")
                    .append(searchTerms[i].toLowerCase(locale)).append("%'");

            if (i < (searchTerms.length - 1)) {
                productFilterClause.append(" OR ");
            }
        }

        return String.format(query.toString(), new Object[] { productCategoryFilterClause.toString(),
                productFilterClause.toString() });
    }

    private String getSqlQueryForCategory(String[] searchTerms, String country, List<Long> categoryIds, Locale locale) {
        StringBuilder query = new StringBuilder(getSql("/productsearch/loadBySearchTermsForCategory.sql"));
        query.append(" AND product_category_id IN (").append(Joiner.on(",").join(categoryIds)).append(")");
        if (StringUtils.isNotEmpty(country)) {
            query.append(" AND ").append(COUNTRY_COLUMN_NAME).append(" = '").append(country).append("'");
        }

        StringBuilder productFilterClause = new StringBuilder();

        // Setup Product Filter Clause
        for (int i = 0; i <= searchTerms.length - 1; i++) {
            productFilterClause.append(LOWER).append("(").append(NAME_COLUMN_NAME).append(") LIKE '%")
                    .append(searchTerms[i].toLowerCase(locale)).append("%' OR ");
            productFilterClause.append(LOWER).append("(").append(KEYWORDS_COLUMN_NAME).append(") LIKE '%")
                    .append(searchTerms[i].toLowerCase(locale)).append("%' OR ");
            productFilterClause.append(LOWER).append("(").append(DESCRIPTION_COLUMN_NAME).append(") LIKE '%")
                    .append(searchTerms[i].toLowerCase(locale)).append("%'");

            if (i < (searchTerms.length - 1)) {
                productFilterClause.append(" OR ");
            }
        }

        return String.format(query.toString(), new Object[] { productFilterClause.toString() });
    }

    private String getChineseSqlQuery(String[] searchTerms, String country) {
        StringBuilder query = new StringBuilder(getSql("/productsearch/loadByChineseSearchTerms.sql"));
        if (StringUtils.isNotEmpty(country)) {
            query.append(" AND ").append(COUNTRY_COLUMN_NAME).append(" = '").append(country).append("'");
        }

        StringBuilder productCategoryFilterClause = new StringBuilder();
        StringBuilder productFilterClause = new StringBuilder();

        // Setup Product Category Filter Clause
        for (int i = 0; i <= searchTerms.length - 1; i++) {
            productCategoryFilterClause.append(TRANSLATED_PRODUCT_CATEGORY_COLUMN_NAME + " LIKE '%" + searchTerms[i]
                    + "%' ");
            if (i < (searchTerms.length - 1)) {
                productCategoryFilterClause.append(" OR ");
            }
        }

        // Setup Product Filter Clause
        for (int i = 0; i <= searchTerms.length - 1; i++) {
            productFilterClause.append(TRANSLATED_NAME_COLUMN_NAME).append(" LIKE '%").append(searchTerms[i])
                    .append("%' OR ");
            productFilterClause.append(TRANSLATED_KEYWORDS_COLUMN_NAME + " LIKE '%" + searchTerms[i] + "%' OR ");
            productFilterClause.append(TRANSLATED_DESCRIPTION_COLUMN_NAME + " LIKE '%" + searchTerms[i] + "%'");
            if (i < (searchTerms.length - 1)) {
                productFilterClause.append(" OR ");
            }
        }

        return String.format(query.toString(), new Object[] { productCategoryFilterClause.toString(),
                productFilterClause.toString() });
    }

    private String getChineseSqlQueryForCategory(String[] searchTerms, String country, List<Long> categoryIds) {
        StringBuilder query = new StringBuilder(getSql("/productsearch/loadByChineseSearchTermsForCategory.sql"));
        query.append(" AND product_category_id IN (").append(Joiner.on(",").join(categoryIds)).append(")");
        if (StringUtils.isNotEmpty(country)) {
            query.append(" AND ").append(COUNTRY_COLUMN_NAME).append(" = '").append(country).append("'");
        }

        StringBuilder productFilterClause = new StringBuilder();

        // Setup Product Filter Clause
        for (int i = 0; i <= searchTerms.length - 1; i++) {
            productFilterClause.append(TRANSLATED_NAME_COLUMN_NAME).append(" LIKE '%").append(searchTerms[i])
                    .append("%' OR ");
            productFilterClause.append(TRANSLATED_KEYWORDS_COLUMN_NAME + " LIKE '%" + searchTerms[i] + "%' OR ");
            productFilterClause.append(TRANSLATED_DESCRIPTION_COLUMN_NAME + " LIKE '%" + searchTerms[i] + "%'");
            if (i < (searchTerms.length - 1)) {
                productFilterClause.append(" OR ");
            }
        }

        return String.format(query.toString(), new Object[] { productFilterClause.toString() });
    }
}
