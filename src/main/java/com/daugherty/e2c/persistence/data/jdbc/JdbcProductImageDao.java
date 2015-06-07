package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.persistence.data.ProductImageReadDao;
import com.daugherty.e2c.persistence.data.ProductImageWriteDao;
import com.google.common.collect.Lists;

/**
 * Spring JDBC implementation of the Product Image database operations.
 */
@Repository
public class JdbcProductImageDao extends JdbcDao implements ProductImageReadDao, ProductImageWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    static final String PRODUCT_IMAGE_ID = "product_audit_image_link_id";
    static final String PRODUCT_IMAGE_LINK = "product_image_link";
    static final String PRIMARY_IMAGE_COLUMN_NAME = "is_primary";

    private SimpleJdbcInsert productAuditImageLink;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        productAuditImageLink = new SimpleJdbcInsert(dataSource).withTableName("product_audit_image_link")
                .usingColumns(PRODUCT_AUDIT_ID_COLUMN_NAME, PRODUCT_IMAGE_LINK, PRIMARY_IMAGE_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public List<ProductImage> loadBySnapshotIds(List<Long> productSnapshotIds) {
        List<ProductImage> productImages = Lists.newArrayList();

        LOGGER.debug("Looking up product images with snapshot ids " + productSnapshotIds);
        for (List<Long> partitionedIds : Lists.partition(productSnapshotIds, 1000)) {

            String sql = getSql("productimage/loadBySnapshotIds.sql");
            SqlParameterSource parameterSource = new MapSqlParameterSource("snapshotIds", partitionedIds);
            RowMapper<ProductImage> rowMapper = new RowMapper<ProductImage>() {
                @Override
                public ProductImage mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new ProductImage(rs.getLong(PRODUCT_IMAGE_ID), rs.getLong(PRODUCT_AUDIT_ID_COLUMN_NAME),
                            rs.getString(PRODUCT_IMAGE_LINK), rs.getBoolean(PRIMARY_IMAGE_COLUMN_NAME));
                }
            };

            productImages.addAll(jdbcTemplate.query(sql, parameterSource, rowMapper));
        }

        return productImages;
    }

    @Override
    public void updateImages(Long productSnapshotId, List<ProductImage> images) {

        LOGGER.debug("Creating Product Images with snapshot Id " + productSnapshotId);
        for (ProductImage image : images) {
            SqlParameterSource parameterSource = new AuditSqlParameterSource()
                    .addValue(PRODUCT_AUDIT_ID_COLUMN_NAME, productSnapshotId)
                    .addValue(PRODUCT_IMAGE_LINK, image.getProductImageLink())
                    .addValue(PRIMARY_IMAGE_COLUMN_NAME, image.isPrimary());
            productAuditImageLink.execute(parameterSource);
        }
    }
}
