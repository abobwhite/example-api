package com.daugherty.e2c.persistence.data.jdbc;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.persistence.data.ProductPublicationWriteDao;

/**
 * Spring JDBC implementation of the Product publication database change operations.
 */
@Repository
public class JdbcProductPublicationDao extends JdbcDao implements ProductPublicationWriteDao {

    private static final String PUBLISHED_COLUMN_NAME = "is_published";
    private static final String HOT_COLUMN_NAME = "hot_product";

    private SimpleJdbcInsert publishedProductInsert;
    private SimpleJdbcInsert hotProductInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        publishedProductInsert = new SimpleJdbcInsert(dataSource).withTableName("published_product_status")
                .usingColumns(PRODUCT_ID_COLUMN_NAME, PUBLISHED_COLUMN_NAME, EVENT_TIME_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        hotProductInsert = new SimpleJdbcInsert(dataSource).withTableName("hot_product_status").usingColumns(
                PRODUCT_ID_COLUMN_NAME, HOT_COLUMN_NAME, EVENT_TIME_COLUMN_NAME,
                AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public void insertPublished(Long id, boolean published) {
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PRODUCT_ID_COLUMN_NAME, id)
                .addValue(PUBLISHED_COLUMN_NAME, published).addValue(EVENT_TIME_COLUMN_NAME, new Date());
        publishedProductInsert.execute(parameterSource);
    }

    @Override
    public void insertHot(Long id, boolean hot) {
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PRODUCT_ID_COLUMN_NAME, id)
                .addValue(HOT_COLUMN_NAME, hot).addValue(EVENT_TIME_COLUMN_NAME, new Date());
        hotProductInsert.execute(parameterSource);
    }

}
