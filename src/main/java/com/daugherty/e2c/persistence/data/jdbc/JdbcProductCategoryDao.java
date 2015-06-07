package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.CategoryType;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.e2c.persistence.data.ProductCategoryWriteDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Spring JDBC implementation of the ProductCategory database operations.
 */
@Repository
public class JdbcProductCategoryDao extends SortAndPaginationJdbcDao implements ProductCategoryReadDao,
        ProductCategoryWriteDao {

    protected static final String PRODUCT_CATEGORY_ID_COLUMN_NAME = "product_category_id";
    protected static final String NAME_COLUMN_NAME = "product_category";
    protected static final String LINK_COLUMN_NAME = "product_category_link";
    protected static final String TYPE_COLUMN_NAME = "display_type";
    protected static final String VISIBLE_HOME_SCREEN_COLUMN_NAME = "is_visible_on_home_screen";
    protected static final String TRANSLATED_NAME_COLUMN_NAME = "translated_product_category";
    protected static final String PRODUCT_COUNT_COLUMN_NAME = "product_count";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert productAuditCategoryInsert;

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true).appendSort("display_order", false);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(ProductCategory.NAME_SERIAL_PROPERTY, NAME_COLUMN_NAME);
        translatedColumnsByProperty.put(ProductCategory.NAME_SERIAL_PROPERTY, TRANSLATED_NAME_COLUMN_NAME);
    }

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        productAuditCategoryInsert = new SimpleJdbcInsert(dataSource).withTableName("product_category_product_audit")
                .usingColumns(PRODUCT_AUDIT_ID_COLUMN_NAME, PRODUCT_CATEGORY_ID_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    @Cacheable(value = "getAllCategories", key = "#criteria.language")
    public List<ProductCategory> getAllCategories(QueryCriteria criteria) {
        LOGGER.debug("Getting Product Categories from the database for language: "
                + criteria.getParameterMap().get(SqlQueryCriteria.LANGUAGE_PARAMETER_NAME));
        String sql = getSql("/productcategory/get-all.sql") + criteria.getCombinedQueryClauses();
        RowMapper<ProductCategory> rowMapper = new RowMapper<ProductCategory>() {
            @Override
            public ProductCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ProductCategory(rs.getLong(PRODUCT_CATEGORY_ID_COLUMN_NAME), getPossiblyTranslatedString(rs,
                        NAME_COLUMN_NAME, TRANSLATED_NAME_COLUMN_NAME), rs.getInt(PRODUCT_COUNT_COLUMN_NAME),
                        rs.getString(LINK_COLUMN_NAME), rs.getBoolean(VISIBLE_HOME_SCREEN_COLUMN_NAME),
                        CategoryType.findByType(rs.getString(TYPE_COLUMN_NAME)));
            }
        };

        return jdbcTemplate.query(sql, criteria.getParameterMap(), rowMapper);
    }

    @Override
    @Cacheable(value = "getChildCategoriesByParent")
    public Multimap<Long, Long> getChildCategoriesByParent() {
        LOGGER.debug("Getting all Product Categories from the database");
        String sql = getSql("/productcategory/get-children-by-parent.sql");
        HierarchyRowCallbackHandler handler = new HierarchyRowCallbackHandler();
        jdbcTemplate.query(sql, new MapSqlParameterSource(), handler);
        return handler.getChildrenByParent();
    }

    @Override
    public List<ProductCategory> findCategoriesBySnapshotIds(List<Long> productSnapshotIds) {
        List<ProductCategory> productCategories = Lists.newArrayList();

        LOGGER.debug("Looking up product categories with snapshot ids " + productSnapshotIds);
        for (List<Long> partitionedIds : Lists.partition(productSnapshotIds, 1000)) {
            String sql = getSql("productcategory/loadBySnapshotIds.sql");
            SqlParameterSource parameterSource = new MapSqlParameterSource("snapshotIds", partitionedIds);
            RowMapper<ProductCategory> rowMapper = new RowMapper<ProductCategory>() {
                @Override
                public ProductCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new ProductCategory(rs.getLong(PRODUCT_CATEGORY_ID_COLUMN_NAME),
                            rs.getString(NAME_COLUMN_NAME), rs.getLong(PRODUCT_AUDIT_ID_COLUMN_NAME),
                            rs.getString(LINK_COLUMN_NAME), null, null);
                }
            };

            productCategories.addAll(jdbcTemplate.query(sql, parameterSource, rowMapper));
        }

        return productCategories;
    }

    @Override
    public Long loadCategoryIdMatchingLegacyId(Long legacyId) {
        LOGGER.debug("Looking up product category ID matching legacy id " + legacyId);
        String sql = getSql("productcategory/load-id-matching-legacy-id.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("legacyId", legacyId);
        return jdbcTemplate.queryForLong(sql, parameterSource);
    }

    @Override
    public void updateCategories(Long productSnapshotId, List<ProductCategory> categories) {
        LOGGER.debug("Creating Product Categories with snapshot Id " + productSnapshotId);
        for (ProductCategory category : categories) {
            SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PRODUCT_AUDIT_ID_COLUMN_NAME,
                    productSnapshotId).addValue(PRODUCT_CATEGORY_ID_COLUMN_NAME, category.getId());
            productAuditCategoryInsert.execute(parameterSource);
        }
    }

    /**
     * Maps child categories to parent categories based on the association table.
     */
    private final class HierarchyRowCallbackHandler implements RowCallbackHandler {

        private static final String PARENT_PRODUCT_CATEGORY_ID_COLUMN_NAME = "parent_product_category_id";
        private static final String CHILD_PRODUCT_CATEGORY_ID_COLUMN_NAME = "child_product_category_id";

        private final Multimap<Long, Long> childrenByParent = ArrayListMultimap.create();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            childrenByParent.put(rs.getLong(PARENT_PRODUCT_CATEGORY_ID_COLUMN_NAME),
                    rs.getLong(CHILD_PRODUCT_CATEGORY_ID_COLUMN_NAME));
        }

        public Multimap<Long, Long> getChildrenByParent() {
            return childrenByParent;
        }
    }

}
