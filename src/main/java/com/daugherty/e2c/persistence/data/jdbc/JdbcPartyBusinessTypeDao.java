package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

/**
 * Spring JDBC implementation of Party to Business Type DAO.
 */
@Repository
public class JdbcPartyBusinessTypeDao extends JdbcDao implements PartyBusinessTypeReadDao, PartyBusinessTypeWriteDao {

    private static final String BUSINESS_TYPE_ID_COLUMN_NAME = "business_type_id";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert partyAuditBusinessTypeInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        partyAuditBusinessTypeInsert = new SimpleJdbcInsert(dataSource).withTableName("party_audit_business_type")
                .usingColumns(PARTY_AUDIT_ID_COLUMN_NAME, BUSINESS_TYPE_ID_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public List<BusinessType> findBySnapshotId(Long snapshotId) {
        LOGGER.debug("Retrieving business types for Party with snapshot Id " + snapshotId);
        String sql = getSql("businesstype/findByPartySnapshotId.sql");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("snapshotId", snapshotId);
        RowMapper<BusinessType> mapper = new RowMapper<BusinessType>() {
            @Override
            public BusinessType mapRow(ResultSet rs, int rowNum) throws SQLException {
                return BusinessType.findById(rs.getLong("business_type_id"));
            }
        };

        return jdbcTemplate.query(sql, parameterSource, mapper);
    }

    @Override
    public ArrayListMultimap<Long, BusinessType> findBySnapshotIds(List<Long> snapshotIds) {
        LOGGER.debug("Getting all Business Types from the database for " + snapshotIds);
        String sql = getSql("/businesstype/findByPartySnapshotIds.sql");
        SupplierSnapshotBusinessTypeCallbackHandler handler = new SupplierSnapshotBusinessTypeCallbackHandler();

        for (List<Long> partitionedIds : Lists.partition(snapshotIds, 1000)) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource("snapshotIds", partitionedIds);
            jdbcTemplate.query(sql, paramSource, handler);
        }
        return handler.getBusinessTypesBySnapshot();
    }

    @Override
    public List<BusinessType> updateBusinessTypes(Long snapshotId, List<BusinessType> businessTypes) {
        LOGGER.debug("Creating Party Business Types with snapshot Id " + snapshotId);

        for (BusinessType businessType : businessTypes) {
            SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PARTY_AUDIT_ID_COLUMN_NAME,
                    snapshotId).addValue(BUSINESS_TYPE_ID_COLUMN_NAME, businessType.getId());
            partyAuditBusinessTypeInsert.execute(parameterSource);
        }

        return businessTypes;
    }

    /**
     * Maps child categories to parent categories based on the association table.
     */
    private final class SupplierSnapshotBusinessTypeCallbackHandler implements RowCallbackHandler {

        private final ArrayListMultimap<Long, BusinessType> businessTypeBySnapshot = ArrayListMultimap.create();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            businessTypeBySnapshot.put(rs.getLong(PARTY_AUDIT_ID_COLUMN_NAME),
                    BusinessType.findById(rs.getLong(BUSINESS_TYPE_ID_COLUMN_NAME)));
        }

        public ArrayListMultimap<Long, BusinessType> getBusinessTypesBySnapshot() {
            return businessTypeBySnapshot;
        }
    }
}
