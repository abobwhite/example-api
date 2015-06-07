package com.daugherty.e2c.persistence.data.jdbc;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.persistence.data.HotProductOverrideWriteDao;

/**
 * Spring JDBC implementation of Hot Product Override database operations.
 */
@Repository
public class JdbcHotProductOverrideDao extends JdbcDao implements HotProductOverrideWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static final String HOT_PRODUCT_OVERRIDE_ID_COLUMN_NAME = "hot_product_override_id";
    public static final String PRODUCT_ID_COLUMN_NAME = "product_id";

    private SimpleJdbcInsert hotProductOverrideInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        hotProductOverrideInsert = new SimpleJdbcInsert(dataSource).withTableName("hot_product_override")
                .usingGeneratedKeyColumns(HOT_PRODUCT_OVERRIDE_ID_COLUMN_NAME)
                .usingColumns(PRODUCT_ID_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public void addHotProductOverride(Long productId) {
        LOGGER.info("Creating Hot Product Override for Product " + productId);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PRODUCT_ID_COLUMN_NAME, productId);
        hotProductOverrideInsert.execute(parameterSource);
    }

    @Override
    public void removeHotProductOverride(Long productId) {
        LOGGER.info("Hot Product Override for Product " + productId + " has been removed");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue(PRODUCT_ID_COLUMN_NAME, productId);
        String sql = getSql("/product/removeHotProductOverride.sql");
        jdbcTemplate.update(sql, parameterSource);
    }

}
