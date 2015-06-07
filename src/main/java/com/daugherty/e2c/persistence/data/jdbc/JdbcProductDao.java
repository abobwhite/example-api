package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.data.ProductWriteDao;
import com.daugherty.e2c.persistence.data.SortAndPaginationAware;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Spring JDBC implementation of the Product database operations.
 */
@Repository
public class JdbcProductDao extends SortAndPaginationJdbcDao implements ProductReadDao, ProductWriteDao {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    static final String SUPPLIER_ID_COLUMN_NAME = "party_id";
    static final String NAME_COLUMN_NAME = "product_name";
    static final String CHINESE_NAME_COLUMN_NAME = "chinese_product_name";
    static final String DESCRIPTION_COLUMN_NAME = "product_description";
    static final String PAYMENT_TERM_COLUMN_NAME = "payment_term";
    static final String MINIMUM_ORDER_COLUMN_NAME = "minimum_order";
    static final String FREIGHT_ON_BOARD_PORT_COLUMN_NAME = "freight_on_board_port";
    static final String FREIGHT_ON_BOARD_PRICE_COLUMN_NAME = "freight_on_board_price";
    static final String COUNTRY_COLUMN_NAME = "country_of_origin";
    static final String LEAD_TIME_COLUMN_NAME = "lead_time";
    static final String MODEL_NUMBER_COLUMN_NAME = "model_number";
    static final String SPECIFICATIONS_COLUMN_NAME = "key_specification";
    static final String META_TAGS_COLUMN_NAME = "meta_tag_list";
    static final String KEYWORDS_COLUMN_NAME = "keyword_list";
    static final String HOT_PRODUCT_COLUMN_NAME = "is_hot_product";
    static final String HOT_PRODUCT_OVERRIDE_COLUMN_NAME = "hot_product_override";
    static final String PUBLISHED_COLUMN_NAME = "is_published";
    static final String PUBLICATION_DATE_COLUMN_NAME = "publication_datetime";
    static final String IS_PUBLISHED_COLUMN_NAME = "is_published";
    static final String IS_HOT_PRODUCT_COLUMN_NAME = "hot_product";

    private SimpleJdbcInsert productInsert;
    private SimpleJdbcInsert productAuditInsert;
    private SimpleJdbcInsert productAuditStatusInsert;
    private SimpleJdbcInsert publishedProductInsert;
    private SimpleJdbcInsert hotProductInsert;

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(Product.NAME_SERIAL_PROPERTY, NAME_COLUMN_NAME);
        columnsByProperty.put(Product.APPROVAL_STATUS_SERIAL_PROPERTY, EVENT_TYPE_COLUMN_NAME);
        columnsByProperty.put(Product.HOT_PRODUCT_SERIAL_PROPERTY, HOT_PRODUCT_COLUMN_NAME);
        columnsByProperty.put(Product.PUBLISHED_SERIAL_PROPERTY, PUBLISHED_COLUMN_NAME);
        columnsByProperty.put("publicationDate", PUBLICATION_DATE_COLUMN_NAME);
        columnsByProperty.put(Product.CHINESE_NAME_SERIAL_PROPERTY, CHINESE_NAME_COLUMN_NAME);

        translatedColumnsByProperty.put(Product.NAME_SERIAL_PROPERTY, CHINESE_NAME_COLUMN_NAME);
    }

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        productInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingGeneratedKeyColumns(PRODUCT_ID_COLUMN_NAME)
                .usingColumns(PARTY_ID_COLUMN_NAME, AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        productAuditInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("product_audit")
                .usingGeneratedKeyColumns(PRODUCT_AUDIT_ID_COLUMN_NAME)
                .usingColumns(NAME_COLUMN_NAME, DESCRIPTION_COLUMN_NAME, PAYMENT_TERM_COLUMN_NAME,
                        MINIMUM_ORDER_COLUMN_NAME, FREIGHT_ON_BOARD_PORT_COLUMN_NAME,
                        FREIGHT_ON_BOARD_PRICE_COLUMN_NAME, COUNTRY_COLUMN_NAME, LEAD_TIME_COLUMN_NAME,
                        MODEL_NUMBER_COLUMN_NAME, SPECIFICATIONS_COLUMN_NAME, META_TAGS_COLUMN_NAME,
                        KEYWORDS_COLUMN_NAME, AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        productAuditStatusInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("product_audit_status")
                .usingGeneratedKeyColumns(PRODUCT_AUDIT_STATUS_ID_COLUMN_NAME)
                .usingColumns(PRODUCT_ID_COLUMN_NAME, PRODUCT_AUDIT_ID_COLUMN_NAME, VERSION_COLUMN_NAME,
                        EVENT_TYPE_COLUMN_NAME, EVENT_TIME_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        publishedProductInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("published_product_status")
                .usingGeneratedKeyColumns("published_product_id")
                .usingColumns(PRODUCT_ID_COLUMN_NAME, IS_PUBLISHED_COLUMN_NAME, EVENT_TIME_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        hotProductInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("hot_product_status")
                .usingGeneratedKeyColumns("hot_product_id")
                .usingColumns(PRODUCT_ID_COLUMN_NAME, IS_HOT_PRODUCT_COLUMN_NAME, EVENT_TIME_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public List<Product> loadPublishedByProductIds(List<Long> productIds, Locale locale) {
        List<Product> products = Lists.newArrayList();

        HashSet<Long> uniqueIds = Sets.newHashSet(productIds);
        LOGGER.debug("Getting Supplier Products from the database by product ids " + uniqueIds);
        for (List<Long> partitionedIds : Lists.partition(Lists.newArrayList(uniqueIds), 1000)) {
            String sql = getSql("/product/get-all-published.sql") + " WHERE ppv.product_id IN (:productIds)";
            SqlParameterSource parameterSource = new MapSqlParameterSource("productIds", partitionedIds).addValue(
                    "language", locale.getLanguage());
            products.addAll(jdbcTemplate.query(sql, parameterSource, new ProductRowMapper()));
        }

        return products;
    }

    @Override
    public QueryCriteria createLatestQueryCriteria(Long supplierId, String productName, String approvalStatus,
            Boolean hotProduct, Boolean published, String sortPropertyName, Boolean sortDescending, Integer startItem,
            Integer count, Locale locale) {
        return createSqlQueryCriteria(sortPropertyName, sortDescending, startItem, count, locale)
                .appendEqualsSubClause(PARTY_ID_COLUMN_NAME, "partyId", supplierId)
                .appendLikeSubClause(NAME_COLUMN_NAME, "productName", productName)
                .appendEqualsSubClause(EVENT_TYPE_COLUMN_NAME, "approvalStatus", approvalStatus)
                .appendEqualsSubClause(HOT_PRODUCT_COLUMN_NAME, "hotProduct", hotProduct)
                .appendEqualsSubClause(PUBLISHED_COLUMN_NAME, "published", published);
    }

    @Override
    public QueryCriteria createPublishedQueryCriteria(Long supplierId, String country, List<Long> categoryIds,
            Boolean hot, Long businessType, Integer membershipLevel, String sortPropertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale) {
        // Must include "ppv." prefix for product_audit_id otherwise MySQL complains about its ambiguity
        SqlQueryCriteria criteria = null;
        if (hot != null && SortAndPaginationAware.RANDOM_SORT_PROPERTY.equals(sortPropertyName)) {
            criteria = createSqlQueryCriteria(null, null, null, null, locale).appendInnerJoinWithPaginatedRandomSort(
                    "last_hot_product_change_view", PRODUCT_ID_COLUMN_NAME, "ppv", "hot_product", "hot", " = ", hot,
                    startItem, count);
        } else {
            criteria = createSqlQueryCriteria(sortPropertyName, sortDescending, startItem, count, locale);
        }

        criteria.appendEqualsSubClause(HOT_PRODUCT_COLUMN_NAME, "hot", hot)
                .appendEqualsSubClause(PARTY_ID_COLUMN_NAME, "partyId", supplierId)
                .appendLikeSubClause(COUNTRY_COLUMN_NAME, "country", country)
                .appendSubQuerySubClause("ppv.", PRODUCT_AUDIT_ID_COLUMN_NAME, "product_category_product_audit",
                        "product_category_id", "categoryIds", categoryIds)
                .appendSubQuerySubClause(null, PARTY_AUDIT_ID_COLUMN_NAME, "party_audit_business_type",
                        "business_type_id", "businessTypeId", businessType)
                .appendEqualsSubClause("level", "level", membershipLevel);

        if (Boolean.TRUE.equals(hot)) {
            criteria.append(" hot_product_listing > 0 AND hot_product_override = FALSE");
        }
        return criteria;
    }

    @Override
    public List<Product> findLatest(QueryCriteria criteria) {
        LOGGER.debug("Finding all Supplier Products in the database matching " + criteria);
        String sql = getSql("/product/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new ProductRowMapper());
    }

    @Override
    public List<Product> findPublished(QueryCriteria criteria) {
        LOGGER.debug("Finding all Pubished Supplier Products in the database matching " + criteria);
        String sql = getSql("/product/get-all-published.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new ProductRowMapper());
    }

    @Override
    public Product loadLatest(Long productId, Locale locale) {
        LOGGER.debug("Looking up product with ID " + productId);
        String sql = getSql("/product/get-all.sql") + " WHERE product_id = :productId";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("productId", productId).addValue(
                SqlQueryCriteria.LANGUAGE_PARAMETER_NAME, locale.getLanguage());

        return jdbcTemplate.queryForObject(sql, parameterSource, new ProductRowMapper());
    }

    @Override
    public Product loadPublished(Long productId, Locale locale) {
        LOGGER.debug("Looking up approved product with ID " + productId);
        String sql = getSql("/product/get-all-published.sql") + " WHERE product_id = :productId";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("productId", productId).addValue(
                SqlQueryCriteria.LANGUAGE_PARAMETER_NAME, locale.getLanguage());

        return jdbcTemplate.queryForObject(sql, parameterSource, new ProductRowMapper());
    }

    @Override
    public Product loadApproved(Long productId, Locale locale) {
        LOGGER.debug("Looking up approved product with ID " + productId);
        String sql = getSql("/product/get-approved.sql");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("productId", productId).addValue(
                SqlQueryCriteria.LANGUAGE_PARAMETER_NAME, locale.getLanguage());

        return jdbcTemplate.queryForObject(sql, parameterSource, new ProductRowMapper());
    }

    @Override
    public Long loadProductIdMatchingLegacyId(Long legacyId) {
        LOGGER.debug("Looking up product ID matching legacy id " + legacyId);
        String sql = getSql("product/load-id-matching-legacy-id.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("legacyId", legacyId);
        return jdbcTemplate.queryForLong(sql, parameterSource);
    }

    @Override
    public Product insert(Product product) {
        createProduct(product);
        createPublishedProduct(product);
        createHotProduct(product);
        createProductAudit(product);
        createProductAuditStatus(product);
        return product;
    }

    @Override
    public Product update(Product product) {
        createProductAudit(product);
        createProductAuditStatus(product);
        return product;
    }

    @Override
    public int deleteProductsByPartyId(Long partyId) {
        LOGGER.info("Deleting products for party" + partyId);

        SqlParameterSource partyParameterSource = new MapSqlParameterSource().addValue("partyId", partyId);

        List<Long> productAuditIds = jdbcTemplate.queryForList(getSql("/product/loadProductAuditIdsByPartyId.sql"),
                partyParameterSource, Long.class);

        SqlParameterSource productAuditParameterSource = new MapSqlParameterSource().addValue("productAuditIds",
                productAuditIds);

        int numberOfDeletes = 0;

        if (!productAuditIds.isEmpty()) {
            jdbcTemplate.update(getSql("/product/deleteHotProductStatusByPartyId.sql"), partyParameterSource);
            jdbcTemplate.update(getSql("/product/deleteHotProductOverrideByPartyId.sql"), partyParameterSource);
            jdbcTemplate.update(getSql("/product/deletePublishedProductStatusByPartyId.sql"), partyParameterSource);
            jdbcTemplate.update(getSql("/product/deleteProductAuditStatusByPartyId.sql"), partyParameterSource);
            jdbcTemplate.update(getSql("/product/deleteProductAuditImageLinkByProductAuditId.sql"),
                    productAuditParameterSource);
            jdbcTemplate.update(getSql("/product/deleteProductCategoryProductAuditByProductAuditId.sql"),
                    productAuditParameterSource);
            jdbcTemplate.update(getSql("/product/deleteProductAuditTranslationByProductAuditId.sql"),
                    productAuditParameterSource);
            jdbcTemplate.update(getSql("/product/deleteProductAuditByProductAuditId.sql"), productAuditParameterSource);

            numberOfDeletes = jdbcTemplate.update(getSql("/product/deleteByPartyId.sql"), partyParameterSource);
        }

        LOGGER.info("Deleted " + numberOfDeletes + " products for party" + partyId);

        return numberOfDeletes;
    }

    private void createProduct(Product product) {
        LOGGER.info("Creating product for " + product);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PARTY_ID_COLUMN_NAME, product
                .getInformation().getSupplier().getId());
        Number partyKey = productInsert.executeAndReturnKey(parameterSource);
        product.setId(partyKey.longValue());
    }

    private void createPublishedProduct(Product product) {
        LOGGER.info("Creating published product for " + product);

        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PRODUCT_ID_COLUMN_NAME, product.getId()).addValue(IS_PUBLISHED_COLUMN_NAME, false)
                .addValue(EVENT_TIME_COLUMN_NAME, new Date());
        Number productAuditKey = publishedProductInsert.executeAndReturnKey(parameterSource);
        product.setSnapshotId(productAuditKey.longValue());
    }

    private void createHotProduct(Product product) {
        LOGGER.info("Creating hot product for " + product);

        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PRODUCT_ID_COLUMN_NAME, product.getId()).addValue(IS_HOT_PRODUCT_COLUMN_NAME, false)
                .addValue(EVENT_TIME_COLUMN_NAME, new Date());
        Number productAuditKey = hotProductInsert.executeAndReturnKey(parameterSource);
        product.setSnapshotId(productAuditKey.longValue());
    }

    private void createProductAudit(Product product) {
        LOGGER.info("Creating product audit for " + product);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(NAME_COLUMN_NAME, product.getInformation().getName())
                .addValue(DESCRIPTION_COLUMN_NAME, product.getInformation().getDescription())
                .addValue(PAYMENT_TERM_COLUMN_NAME, product.getInformation().getPaymentTerms())
                .addValue(MINIMUM_ORDER_COLUMN_NAME, product.getInformation().getMinimumOrder())
                .addValue(FREIGHT_ON_BOARD_PORT_COLUMN_NAME, product.getInformation().getFreightOnBoardPort())
                .addValue(FREIGHT_ON_BOARD_PRICE_COLUMN_NAME, product.getInformation().getFreightOnBoardPrice())
                .addValue(COUNTRY_COLUMN_NAME, product.getInformation().getCountry())
                .addValue(LEAD_TIME_COLUMN_NAME, product.getInformation().getLeadTime())
                .addValue(MODEL_NUMBER_COLUMN_NAME, product.getInformation().getModelNumber())
                .addValue(SPECIFICATIONS_COLUMN_NAME, product.getInformation().getSpecifications())
                .addValue(META_TAGS_COLUMN_NAME, product.getMetadata().getMetaTags())
                .addValue(KEYWORDS_COLUMN_NAME, product.getMetadata().getKeywords());
        Number productAuditKey = productAuditInsert.executeAndReturnKey(parameterSource);
        product.setSnapshotId(productAuditKey.longValue());
    }

    private void createProductAuditStatus(Product product) {
        LOGGER.info("Creating product audit status for " + product);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PRODUCT_ID_COLUMN_NAME, product.getId())
                .addValue(PRODUCT_AUDIT_ID_COLUMN_NAME, product.getSnapshotId())
                .addValue(VERSION_COLUMN_NAME, product.getVersion())
                .addValue(EVENT_TYPE_COLUMN_NAME, product.getApprovalStatus())
                .addValue(EVENT_TIME_COLUMN_NAME, new Date());
        productAuditStatusInsert.execute(parameterSource);
    }

    private final class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductInformation information = new ProductInformation(rs.getLong(SUPPLIER_ID_COLUMN_NAME),
                    rs.getString(NAME_COLUMN_NAME), rs.getString(DESCRIPTION_COLUMN_NAME),
                    rs.getString(PAYMENT_TERM_COLUMN_NAME), rs.getString(MINIMUM_ORDER_COLUMN_NAME),
                    rs.getString(FREIGHT_ON_BOARD_PORT_COLUMN_NAME), rs.getString(FREIGHT_ON_BOARD_PRICE_COLUMN_NAME),
                    rs.getString(COUNTRY_COLUMN_NAME), rs.getString(LEAD_TIME_COLUMN_NAME),
                    rs.getString(MODEL_NUMBER_COLUMN_NAME), rs.getString(SPECIFICATIONS_COLUMN_NAME));
            ProductMetadata metadata = new ProductMetadata(rs.getString(CHINESE_NAME_COLUMN_NAME),
                    rs.getString(NAME_COLUMN_NAME), rs.getString(META_TAGS_COLUMN_NAME),
                    rs.getString(KEYWORDS_COLUMN_NAME), rs.getBoolean(PUBLISHED_COLUMN_NAME),
                    rs.getBoolean(HOT_PRODUCT_COLUMN_NAME), rs.getBoolean(HOT_PRODUCT_OVERRIDE_COLUMN_NAME),
                    rs.getDate(PUBLICATION_DATE_COLUMN_NAME));
            return new Product(rs.getLong(PRODUCT_ID_COLUMN_NAME), information, metadata, getNullSafeLong(rs,
                    CERTIFICATION_ID_COLUMN_NAME), ApprovalStatus.findByName(rs.getString(EVENT_TYPE_COLUMN_NAME)),
                    rs.getInt(VERSION_COLUMN_NAME), rs.getLong(PRODUCT_AUDIT_ID_COLUMN_NAME));
        }
    }

}
