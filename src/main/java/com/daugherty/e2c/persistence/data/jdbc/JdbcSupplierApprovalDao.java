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
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.persistence.data.SupplierApprovalReadDao;
import com.daugherty.e2c.persistence.data.SupplierApprovalWriteDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Spring JDBC implementation of the SupplierApproval database operations.
 */
@Repository
public class JdbcSupplierApprovalDao extends SortAndPaginationJdbcDao implements SupplierApprovalReadDao,
        SupplierApprovalWriteDao {

    static final String APPROVAL_ID_COLUMN_NAME = "approval_id";
    static final String PUBLIC_APPROVAL_ID_COLUMN_NAME = "public_approval_id";
    static final String SNAPSHOT_ID_COLUMN_NAME = "snapshot_id";
    static final String TITLE_COLUMN_NAME = "title";
    static final String EMAIL_COLUMN_NAME = "email_address";
    static final String SUPPLIER_ID_COLUMN_NAME = "party_id";
    static final String APPROVAL_TYPE_COLUMN_NAME = "approval_type";
    static final String ACTIVE_MEMBERSHIP_COLUMN_NAME = "active_membership";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert partyAuditStatusInsert;
    private SimpleJdbcInsert productAuditStatusInsert;
    private SimpleJdbcInsert certificationAuditStatusInsert;

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
        certificationAuditStatusInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(CERTIFICATION_AUDIT_STATUS_TABLE_NAME)
                .usingGeneratedKeyColumns(CERTIFICATION_AUDIT_STATUS_ID_COLUMN_NAME)
                .usingColumns(CERTIFICATION_ID_COLUMN_NAME, CERTIFICATION_AUDIT_ID_COLUMN_NAME, VERSION_COLUMN_NAME,
                        EVENT_TYPE_COLUMN_NAME, EVENT_TIME_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(SupplierApproval.TITLE_SERIAL_PROPERTY, TITLE_COLUMN_NAME);
        columnsByProperty.put(SupplierApproval.EMAIL_SERIAL_PROPERTY, EMAIL_COLUMN_NAME);
        columnsByProperty.put(SupplierApproval.TYPE_SERIAL_PROPERTY, APPROVAL_TYPE_COLUMN_NAME);
        columnsByProperty.put(SupplierApproval.STATUS_SERIAL_PROPERTY, EVENT_TYPE_COLUMN_NAME);
        columnsByProperty.put(SupplierApproval.LAST_UPDATED_BY_SERIAL_PROPERTY, LAST_MODIFIED_BY_COLUMN_NAME);
        columnsByProperty.put(SupplierApproval.LAST_UPDATED_AT_SERIAL_PROPERTY, EVENT_TIME_COLUMN_NAME);
    }

    @Override
    public QueryCriteria createQueryCriteria(String titlePrefix, String emailPrefix, String approvalType,
            String approvalStatus, Boolean newSupplier, Boolean paidSupplier, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count) {

        QueryCriteria queryCriteria = null;

        if (Boolean.TRUE.equals(newSupplier)) {
            queryCriteria = createSqlQueryCriteria(propertyName, sortDescending, startItem, count, null)
                    .appendLikeSubClause(TITLE_COLUMN_NAME, "titlePrefix", titlePrefix)
                    .appendLikeSubClause(EMAIL_COLUMN_NAME, "emailPrefix", emailPrefix)
                    .appendEqualsSubClause(APPROVAL_TYPE_COLUMN_NAME, "approvalType", approvalType)
                    .appendEqualsSubClause(ACTIVE_MEMBERSHIP_COLUMN_NAME, "paidSupplier",
                            Boolean.TRUE.equals(paidSupplier) ? Boolean.TRUE : null)
                    .appendEqualsSubClause(EVENT_TYPE_COLUMN_NAME, "approvalStatus", approvalStatus)
                    .appendEqualsSubClause(VERSION_COLUMN_NAME, "version", 1);
        } else {
            queryCriteria = createSqlQueryCriteria(propertyName, sortDescending, startItem, count, null)
                    .appendLikeSubClause(TITLE_COLUMN_NAME, "titlePrefix", titlePrefix)
                    .appendLikeSubClause(EMAIL_COLUMN_NAME, "emailPrefix", emailPrefix)
                    .appendEqualsSubClause(APPROVAL_TYPE_COLUMN_NAME, "approvalType", approvalType)
                    .appendEqualsSubClause(ACTIVE_MEMBERSHIP_COLUMN_NAME, "paidSupplier",
                            Boolean.TRUE.equals(paidSupplier) ? Boolean.TRUE : null)
                    .appendEqualsSubClause(EVENT_TYPE_COLUMN_NAME, "approvalStatus", approvalStatus);
        }

        return queryCriteria;

    }

    @Override
    public List<SupplierApproval> find(QueryCriteria criteria) {
        LOGGER.debug("Finding all Supplier approvals in the database matching " + criteria.toString());
        String sql = getSql("/supplierapproval/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new SupplierApprovalRowMapper());
    }

    @Override
    public SupplierApproval load(Long id) {
        LOGGER.debug("Looking up supplier translation with ID " + id);
        String sql = getSql("supplierapproval/load.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new SupplierApprovalRowMapper());
    }

    @Override
    public SupplierApproval insert(SupplierApproval approval) {
        // Not very OCP, I know. But is it really worth 3 subclasses for this? Maybe if we ever add a fourth...
        if (SupplierApproval.PROFILE_TYPE.equals(approval.getType())) {
            recordEvent(approval, partyAuditStatusInsert, PARTY_ID_COLUMN_NAME, PARTY_AUDIT_ID_COLUMN_NAME);
        } else if (SupplierApproval.PRODUCT_TYPE.equals(approval.getType())) {
            recordEvent(approval, productAuditStatusInsert, PRODUCT_ID_COLUMN_NAME, PRODUCT_AUDIT_ID_COLUMN_NAME);
        } else if (SupplierApproval.CERTIFICATION_TYPE.equals(approval.getType())) {
            recordEvent(approval, certificationAuditStatusInsert, CERTIFICATION_ID_COLUMN_NAME,
                    CERTIFICATION_AUDIT_ID_COLUMN_NAME);
        }
        return approval;
    }

    private void recordEvent(SupplierApproval approval, SimpleJdbcInsert jdbcInsert, String idColumnName,
            String snapshotIdColumnName) {
        LOGGER.info("Creating " + approval.getType() + " audit status for " + approval);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(idColumnName, approval.getId())
                .addValue(snapshotIdColumnName, approval.getSnapshotId())
                .addValue(VERSION_COLUMN_NAME, approval.getVersion())
                .addValue(EVENT_TYPE_COLUMN_NAME, approval.getApprovalStatus())
                .addValue(EVENT_TIME_COLUMN_NAME, new Date());
        jdbcInsert.execute(parameterSource);
    }

    @Override
    public SupplierApproval update(SupplierApproval approval) {
        return insert(approval);
    }

    private final class SupplierApprovalRowMapper implements RowMapper<SupplierApproval> {

        @Override
        public SupplierApproval mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SupplierApproval(rs.getLong(APPROVAL_ID_COLUMN_NAME),
                    rs.getString(PUBLIC_APPROVAL_ID_COLUMN_NAME), rs.getString(TITLE_COLUMN_NAME), new Supplier(
                            rs.getLong(SUPPLIER_ID_COLUMN_NAME)), rs.getString(APPROVAL_TYPE_COLUMN_NAME).trim(),
                    ApprovalStatus.findByName(rs.getString(EVENT_TYPE_COLUMN_NAME)), rs.getInt(VERSION_COLUMN_NAME),
                    rs.getLong(SNAPSHOT_ID_COLUMN_NAME), rs.getString(LAST_MODIFIED_BY_COLUMN_NAME),
                    rs.getTimestamp(EVENT_TIME_COLUMN_NAME));
        }
    }

}
