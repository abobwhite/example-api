package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.persistence.data.SupplierTranslationReadDao;
import com.daugherty.e2c.persistence.data.SupplierTranslationWriteDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Spring JDBC implementation of the SupplierTranslation database operations.
 */
@Repository
public class JdbcSupplierTranslationDao extends SortAndPaginationJdbcDao implements SupplierTranslationReadDao,
        SupplierTranslationWriteDao {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    static final String APPROVAL_ID_COLUMN_NAME = "approval_id";
    static final String PUBLIC_APPROVAL_ID_COLUMN_NAME = "public_approval_id";
    static final String SNAPSHOT_ID_COLUMN_NAME = "snapshot_id";
    static final String TITLE_COLUMN_NAME = "title";
    static final String TRANSLATED_COLUMN_NAME = "translated";
    static final String TYPE_COLUMN_NAME = "approval_type";

    private SimpleJdbcInsert partyAuditStatusInsert;
    private SimpleJdbcInsert productAuditStatusInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        partyAuditStatusInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARTY_AUDIT_STATUS_TABLE_NAME)
                .usingGeneratedKeyColumns(PARTY_AUDIT_STATUS_ID_COLUMN_NAME)
                .usingColumns(PARTY_ID_COLUMN_NAME, PARTY_AUDIT_ID_COLUMN_NAME, VERSION_COLUMN_NAME,
                        EVENT_TYPE_COLUMN_NAME, EVENT_TIME_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
        productAuditStatusInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PRODUCT_AUDIT_STATUS_TABLE_NAME)
                .usingGeneratedKeyColumns(PRODUCT_AUDIT_STATUS_ID_COLUMN_NAME)
                .usingColumns(PRODUCT_ID_COLUMN_NAME, PRODUCT_AUDIT_ID_COLUMN_NAME, VERSION_COLUMN_NAME,
                        EVENT_TYPE_COLUMN_NAME, EVENT_TIME_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(SupplierTranslation.TITLE_SERIAL_PROPERTY, TITLE_COLUMN_NAME);
        columnsByProperty.put(SupplierTranslation.STATUS_SERIAL_PROPERTY, EVENT_TYPE_COLUMN_NAME);
        columnsByProperty.put(SupplierTranslation.TRANSLATED_SERIAL_PROPERTY, TRANSLATED_COLUMN_NAME);
        columnsByProperty.put(SupplierTranslation.TYPE_SERIAL_PROPERTY, TYPE_COLUMN_NAME);
        columnsByProperty.put(SupplierTranslation.LAST_UPDATED_BY_SERIAL_PROPERTY, LAST_MODIFIED_BY_COLUMN_NAME);
        columnsByProperty.put(SupplierTranslation.LAST_UPDATED_AT_SERIAL_PROPERTY, EVENT_TIME_COLUMN_NAME);
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    public List<SupplierTranslation> getAll(QueryCriteria criteria) {
        LOGGER.debug("Getting all Supplier translations from the database");
        String sql = getSql("/suppliertranslation/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new SupplierTranslationRowMapper());
    }

    @Override
    public QueryCriteria createQueryCriteria(String titlePrefix, String type, Boolean translated, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count) {
        return createSqlQueryCriteria(propertyName, sortDescending, startItem, count, null)
                .appendLikeSubClause(TITLE_COLUMN_NAME, "titlePrefix", titlePrefix)
                .appendEqualsSubClause(TYPE_COLUMN_NAME, "type", type)
                .appendEqualsSubClause(TRANSLATED_COLUMN_NAME, "translated", translated);
    }

    @Override
    public List<SupplierTranslation> find(QueryCriteria criteria) {
        LOGGER.debug("Finding all Supplier translations in the database matching " + criteria);
        String sql = getSql("/suppliertranslation/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new SupplierTranslationRowMapper());
    }

    @Override
    public SupplierTranslation load(Long id) {
        LOGGER.debug("Looking up supplier translation with ID " + id);
        String sql = getSql("suppliertranslation/load.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new SupplierTranslationRowMapper());
    }

    @Override
    public SupplierTranslation update(SupplierTranslation supplierTranslation) {
        LOGGER.info("Creating " + supplierTranslation.getType() + " audit status for " + supplierTranslation);
        // Not very OCP, I know. But is it really worth 3 subclasses for this? Maybe if we ever add a fourth...
        if (SupplierTranslation.PROFILE_TYPE.equals(supplierTranslation.getType())) {
            recordEvent(supplierTranslation, partyAuditStatusInsert, PARTY_ID_COLUMN_NAME, PARTY_AUDIT_ID_COLUMN_NAME);
        } else if (SupplierTranslation.PRODUCT_TYPE.equals(supplierTranslation.getType())) {
            recordEvent(supplierTranslation, productAuditStatusInsert, PRODUCT_ID_COLUMN_NAME,
                    PRODUCT_AUDIT_ID_COLUMN_NAME);
        }
        return supplierTranslation;
    }

    private void recordEvent(SupplierTranslation translation, SimpleJdbcInsert jdbcInsert, String idColumnName,
            String snapshotIdColumnName) {
        LOGGER.info("Creating " + translation.getType() + " audit status for " + translation);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(idColumnName, translation.getId())
                .addValue(snapshotIdColumnName, translation.getSnapshotId())
                .addValue(VERSION_COLUMN_NAME, translation.getVersion())
                .addValue(EVENT_TYPE_COLUMN_NAME, translation.getApprovalStatus())
                .addValue(EVENT_TIME_COLUMN_NAME, new Date());
        jdbcInsert.execute(parameterSource);
    }

    private final class SupplierTranslationRowMapper implements RowMapper<SupplierTranslation> {

        @Override
        public SupplierTranslation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SupplierTranslation(rs.getLong(APPROVAL_ID_COLUMN_NAME), rs.getString(PUBLIC_APPROVAL_ID_COLUMN_NAME), rs.getString(TITLE_COLUMN_NAME),
                    ApprovalStatus.findByName(rs.getString(EVENT_TYPE_COLUMN_NAME)), rs.getInt(VERSION_COLUMN_NAME),
                    rs.getLong(SNAPSHOT_ID_COLUMN_NAME), rs.getString(TYPE_COLUMN_NAME).trim(),
                    rs.getBoolean(TRANSLATED_COLUMN_NAME), rs.getString(LAST_MODIFIED_BY_COLUMN_NAME),
                    rs.getTimestamp(EVENT_TIME_COLUMN_NAME));
        }
    }
}
