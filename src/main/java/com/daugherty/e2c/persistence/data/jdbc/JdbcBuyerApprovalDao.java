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
import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.e2c.persistence.data.BuyerApprovalReadDao;
import com.daugherty.e2c.persistence.data.BuyerApprovalWriteDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Spring JDBC implementation of the BuyerApproval database operations.
 */
@Repository
public class JdbcBuyerApprovalDao extends SortAndPaginationJdbcDao implements BuyerApprovalReadDao,
        BuyerApprovalWriteDao {

    static final String APPROVAL_ID_COLUMN_NAME = PARTY_ID_COLUMN_NAME;
    static final String PUBLIC_APPROVAL_ID_COLUMN_NAME = PUBLIC_PARTY_ID_COLUMN_NAME;
    static final String SNAPSHOT_ID_COLUMN_NAME = PARTY_AUDIT_ID_COLUMN_NAME;
    static final String TITLE_COLUMN_NAME = "company_name_english";
    static final String FIRST_NAME_COLUMN_NAME = "first_name";
    static final String LAST_NAME_COLUMN_NAME = "last_name";
    static final String EMAIL_COLUMN_NAME = "email_address";
    static final String APPROVAL_TYPE_COLUMN_NAME = "approval_type";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert partyAuditStatusInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        partyAuditStatusInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARTY_AUDIT_STATUS_TABLE_NAME)
                .usingGeneratedKeyColumns(PARTY_AUDIT_STATUS_ID_COLUMN_NAME)
                .usingColumns(PARTY_ID_COLUMN_NAME, PARTY_AUDIT_ID_COLUMN_NAME, VERSION_COLUMN_NAME,
                        EVENT_TYPE_COLUMN_NAME, EVENT_TIME_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, false);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(BuyerApproval.TITLE_SERIAL_PROPERTY, TITLE_COLUMN_NAME);
        columnsByProperty.put(BuyerApproval.EMAIL_SERIAL_PROPERTY, EMAIL_COLUMN_NAME);
        columnsByProperty.put(BuyerApproval.STATUS_SERIAL_PROPERTY, EVENT_TYPE_COLUMN_NAME);
        columnsByProperty.put(BuyerApproval.LAST_UPDATED_BY_SERIAL_PROPERTY, LAST_MODIFIED_BY_COLUMN_NAME);
        columnsByProperty.put(BuyerApproval.LAST_UPDATED_AT_SERIAL_PROPERTY, EVENT_TIME_COLUMN_NAME);
    }

    @Override
    public List<BuyerApproval> getAll(QueryCriteria sortingAndPaginationCriteria) {
        LOGGER.debug("Getting all Buyer approvals from the database");
        String sql = getSql("/buyerapproval/get-all.sql") + sortingAndPaginationCriteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new BuyerApprovalRowMapper());
    }

    @Override
    public QueryCriteria createQueryCriteria(String titlePrefix, String emailPrefix, String approvalStatus,
            Boolean newBuyer, String propertyName, Boolean sortDescending, Integer startItem, Integer count) {

        if (Boolean.TRUE.equals(newBuyer)) {
            return createSqlQueryCriteria(propertyName, sortDescending, startItem, count, null)
                    .appendLikeSubClause(TITLE_COLUMN_NAME, "titlePrefix", titlePrefix)
                    .appendLikeSubClause(EMAIL_COLUMN_NAME, "emailPrefix", emailPrefix)
                    .appendEqualsSubClause(EVENT_TYPE_COLUMN_NAME, "approvalStatus", approvalStatus)
                    .appendEqualsSubClause(VERSION_COLUMN_NAME, "version", 1);
        } else {
            return createSqlQueryCriteria(propertyName, sortDescending, startItem, count, null)
                    .appendLikeSubClause(TITLE_COLUMN_NAME, "titlePrefix", titlePrefix)
                    .appendLikeSubClause(EMAIL_COLUMN_NAME, "emailPrefix", emailPrefix)
                    .appendEqualsSubClause(EVENT_TYPE_COLUMN_NAME, "approvalStatus", approvalStatus);
        }

    }

    @Override
    public List<BuyerApproval> find(QueryCriteria criteria) {
        LOGGER.debug("Finding all Buyer approvals in the database matching " + criteria.toString());
        String sql = getSql("/buyerapproval/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new BuyerApprovalRowMapper());
    }

    @Override
    public BuyerApproval load(Long id) {
        LOGGER.debug("Looking up buyer translation with ID " + id);
        String sql = getSql("buyerapproval/load.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new BuyerApprovalRowMapper());
    }

    @Override
    public BuyerApproval insert(BuyerApproval approval) {
        LOGGER.info("Creating buyer approval audit status for " + approval);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PARTY_ID_COLUMN_NAME, approval.getId())
                .addValue(PARTY_AUDIT_ID_COLUMN_NAME, approval.getSnapshotId())
                .addValue(VERSION_COLUMN_NAME, approval.getVersion())
                .addValue(EVENT_TYPE_COLUMN_NAME, approval.getApprovalStatus())
                .addValue(EVENT_TIME_COLUMN_NAME, new Date());
        partyAuditStatusInsert.execute(parameterSource);
        return approval;
    }

    @Override
    public BuyerApproval update(BuyerApproval approval) {
        return insert(approval);
    }

    private final class BuyerApprovalRowMapper implements RowMapper<BuyerApproval> {

        @Override
        public BuyerApproval mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BuyerApproval(rs.getLong(APPROVAL_ID_COLUMN_NAME), rs.getString(PUBLIC_PARTY_ID_COLUMN_NAME), rs.getString(TITLE_COLUMN_NAME),
                    rs.getString(FIRST_NAME_COLUMN_NAME), rs.getString(LAST_NAME_COLUMN_NAME),
                    rs.getString(EMAIL_COLUMN_NAME), ApprovalStatus.findByName(rs.getString(EVENT_TYPE_COLUMN_NAME)),
                    rs.getInt(VERSION_COLUMN_NAME), rs.getLong(SNAPSHOT_ID_COLUMN_NAME),
                    rs.getString(LAST_MODIFIED_BY_COLUMN_NAME), rs.getTimestamp(EVENT_TIME_COLUMN_NAME));
        }
    }
}
