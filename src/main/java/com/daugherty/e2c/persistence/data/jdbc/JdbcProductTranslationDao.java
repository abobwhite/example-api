package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.persistence.data.ProductTranslationReadDao;
import com.daugherty.e2c.persistence.data.ProductTranslationWriteDao;
import com.google.common.collect.Maps;

/**
 * Spring JDBC implementation of the ProductTranslation database operations.
 */
@Repository
public class JdbcProductTranslationDao extends JdbcDao implements ProductTranslationReadDao, ProductTranslationWriteDao {

    public static final String COUNTRY_COLUMN_NAME = "country_of_origin";
    public static final String MODEL_NUMBER_COLUMN_NAME = "model_number";
    public static final String PAYMENT_TERM_ENGLISH_COLUMN_NAME = "payment_term";
    public static final String PAYMENT_TERM_CHINESE_COLUMN_NAME = "translated_payment_term";
    public static final String MINIMUM_ORDER_COLUMN_NAME = "minimum_order";
    public static final String FOB_PORT_COLUMN_NAME = "freight_on_board_port";
    public static final String FOB_PRICE_COLUMN_NAME = "freight_on_board_price";
    public static final String LEAD_TIME_COLUMN_NAME = "lead_time";
    public static final String PRODUCT_NAME_COLUMN_NAME = "product_name";
    public static final String PRODUCT_NAME_TRANSLATION_COLUMN_NAME = "translated_product_name";
    public static final String PRODUCT_DESCRIPTION_COLUMN_NAME = "product_description";
    public static final String PRODUCT_DESCRIPTION_TRANSLATION_COLUNM_NAME = "translated_product_description";
    public static final String KEY_SPECIFICATION_COLUMN_NAME = "key_specification";
    public static final String TRANSLATED_KEY_SPECIFICATION_COLUMN_NAME = "translated_key_specification";
    public static final String KEYWORD_COLUMN_NAME = "keyword_list";
    public static final String KEYWORD_TRANSLATED_COLUMN_NAME = "translated_keyword_list";
    public static final String META_TAG_COLUMN_NAME = "meta_tag_list";
    public static final String META_TAG_TRANSLATED_COLUMN_NAME = "translated_meta_tag_list";
    public static final String SNAPSHOT_ID_COLUMN_NAME = "product_audit_id";
    static final String LANGUAGE_ID_COLUMN_NAME = "language_id";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert productAuditTranslationInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        productAuditTranslationInsert = new SimpleJdbcInsert(dataSource).withTableName("product_audit_translation")
                .usingColumns("product_audit_id", LANGUAGE_ID_COLUMN_NAME, TRANSLATED_KEY_SPECIFICATION_COLUMN_NAME,
                        PAYMENT_TERM_CHINESE_COLUMN_NAME, PRODUCT_NAME_TRANSLATION_COLUMN_NAME,
                        PRODUCT_DESCRIPTION_TRANSLATION_COLUNM_NAME, KEYWORD_TRANSLATED_COLUMN_NAME,
                        META_TAG_TRANSLATED_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Long getUpdateIdForLatestProduct(Long productId) {
        LOGGER.debug("Looking up ID of existing translation record for latest product with ID " + productId);
        String sql = getSql("producttranslation/get-update-id-for-latest-product.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("productId", productId);
        long updateId = jdbcTemplate.queryForLong(sql, parameterSource);
        return updateId == 0 ? null : updateId;
    }

    @Override
    public ProductTranslation load(Long productId) {
        LOGGER.debug("Looking up product translation with product ID " + productId);
        String sql = getSql("producttranslation/load-by-product.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("productId", productId);
        RowMapper<ProductTranslation> rowMapper = new RowMapper<ProductTranslation>() {
            @Override
            public ProductTranslation mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ProductTranslation(rs.getLong(PRODUCT_ID_COLUMN_NAME), rs.getLong(SNAPSHOT_ID_COLUMN_NAME),
                        rs.getString(COUNTRY_COLUMN_NAME), rs.getString(MODEL_NUMBER_COLUMN_NAME),
                        rs.getString(PAYMENT_TERM_ENGLISH_COLUMN_NAME), rs.getString(PAYMENT_TERM_CHINESE_COLUMN_NAME),
                        rs.getString(MINIMUM_ORDER_COLUMN_NAME), rs.getString(FOB_PORT_COLUMN_NAME),
                        rs.getString(FOB_PRICE_COLUMN_NAME), rs.getString(LEAD_TIME_COLUMN_NAME),
                        rs.getString(PRODUCT_NAME_COLUMN_NAME), rs.getString(PRODUCT_NAME_TRANSLATION_COLUMN_NAME),
                        rs.getString(PRODUCT_DESCRIPTION_COLUMN_NAME),
                        rs.getString(PRODUCT_DESCRIPTION_TRANSLATION_COLUNM_NAME),
                        rs.getString(KEY_SPECIFICATION_COLUMN_NAME),
                        rs.getString(TRANSLATED_KEY_SPECIFICATION_COLUMN_NAME), rs.getString(KEYWORD_COLUMN_NAME),
                        rs.getString(KEYWORD_TRANSLATED_COLUMN_NAME), rs.getString(META_TAG_COLUMN_NAME),
                        rs.getString(META_TAG_TRANSLATED_COLUMN_NAME));
            }
        };

        return jdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    @Override
    public Map<Long, ProductTranslation> findBySnapshotIds(List<Long> snapshotIds, Locale locale) {
        LOGGER.debug("Looking up product translations in " + locale.getDisplayLanguage() + " for snapshot IDs "
                + snapshotIds);
        if (snapshotIds.isEmpty()) {
            return Maps.newHashMap();
        }

        String sql = getSql("producttranslation/find-by-snapshot-ids.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("snapshotIds", snapshotIds).addValue("language",
                locale.getLanguage());
        SnapshotIdCallbackHandler callbackHandler = new SnapshotIdCallbackHandler();
        jdbcTemplate.query(sql, parameterSource, callbackHandler);
        return callbackHandler.getTranslationsBySnapshotIds();
    }

    @Override
    public ProductTranslation insert(ProductTranslation translation) {
        LOGGER.info("Inserting product audit translation record for " + translation);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PRODUCT_AUDIT_ID_COLUMN_NAME, translation.getSnapshotId())
                .addValue(LANGUAGE_ID_COLUMN_NAME, Language.CHINESE.getId())
                .addValue(TRANSLATED_KEY_SPECIFICATION_COLUMN_NAME, translation.getKeySpecificationTranslation())
                .addValue(PAYMENT_TERM_CHINESE_COLUMN_NAME, translation.getPaymentTermsTranslation())
                .addValue(PRODUCT_NAME_TRANSLATION_COLUMN_NAME, translation.getProductNameTranslation())
                .addValue(PRODUCT_DESCRIPTION_TRANSLATION_COLUNM_NAME, translation.getProductDescriptionTranslation())
                .addValue(KEYWORD_TRANSLATED_COLUMN_NAME, translation.getKeyWordsTranslation())
                .addValue(META_TAG_TRANSLATED_COLUMN_NAME, translation.getMetaTagsTranslation());
        productAuditTranslationInsert.execute(parameterSource);
        return translation;
    }

    @Override
    public ProductTranslation update(Long updateId, ProductTranslation translation) {
        LOGGER.info("Updating product audit translation record with ID " + updateId + " for " + translation);
        String sql = getSql("producttranslation/update.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue("keySpecifications", translation.getKeySpecificationTranslation())
                .addValue("paymentTerms", translation.getPaymentTermsTranslation())
                .addValue("name", translation.getProductNameTranslation())
                .addValue("description", translation.getProductDescriptionTranslation())
                .addValue("keywords", translation.getKeyWordsTranslation())
                .addValue("metaTags", translation.getMetaTagsTranslation()).addValue("updateId", updateId);
        jdbcTemplate.update(sql, parameterSource);
        return translation;
    }

    private class SnapshotIdCallbackHandler implements RowCallbackHandler {

        private final Map<Long, ProductTranslation> translationsBySnapshotIds = Maps.newHashMap();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            ProductTranslation translation = new ProductTranslation(null, rs.getLong(SNAPSHOT_ID_COLUMN_NAME),
                    rs.getString(PAYMENT_TERM_CHINESE_COLUMN_NAME), rs.getString(PRODUCT_NAME_TRANSLATION_COLUMN_NAME),
                    rs.getString(PRODUCT_DESCRIPTION_TRANSLATION_COLUNM_NAME),
                    rs.getString(TRANSLATED_KEY_SPECIFICATION_COLUMN_NAME),
                    rs.getString(KEYWORD_TRANSLATED_COLUMN_NAME), rs.getString(META_TAG_TRANSLATED_COLUMN_NAME));
            translationsBySnapshotIds.put(translation.getSnapshotId(), translation);
        }

        public Map<Long, ProductTranslation> getTranslationsBySnapshotIds() {
            return translationsBySnapshotIds;
        }

    }

}
