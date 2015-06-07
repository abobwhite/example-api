package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationReadDao;
import com.daugherty.e2c.persistence.data.CertificationWriteDao;

@Repository
public class JdbcCertificationDao extends JdbcDao implements CertificationWriteDao, CertificationReadDao {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert certificationInsert;
    private SimpleJdbcInsert partyCertificationInsert;
    private SimpleJdbcInsert productCertificationInsert;
    private SimpleJdbcInsert certificationAuditInsert;
    private SimpleJdbcInsert certificationAuditStatusInsert;

    // certification
    protected static final String CERTIFICATION_TABLE_NAME = "certification";
    protected static final String CERTIFICATION_ID_COLUMN_NAME = "certification_id";

    // certification_audit
    protected static final String CERTIFICATION_AUDIT_TABLE_NAME = "certification_audit";
    protected static final String CERTIFICATION_AUDIT_ID_COLUMN_NAME = "certification_audit_id";
    protected static final String CERTIFICATION_STANDARD_COLUMN_NAME = "certification_standard";
    protected static final String CERTIFICATION_NUMBER_COLUMN_NAME = "certification_number";
    protected static final String CERTIFICATION_LINK_COLUMN_NAME = "certification_link";
    protected static final String ISSUED_DATE_COLUMN = "issued_date";
    protected static final String SCOPE_RANGE_COLUMN = "scope_range";
    protected static final String ISSUED_BY_COLUMN = "issued_by";

    // certification_audit_status
    protected static final String CERTIFICATION_AUDIT_STATUS_TABLE_NAME = "certification_audit_status";
    protected static final String CERTIFICATION_AUDIT_STATUS_ID_COLUMN_NAME = "certification_audit_status_id";

    // party_certification (supplier)
    protected static final String PARTY_CERTIFICATION_TABLE_NAME = "party_certification";
    protected static final String PARTY_CERTIFICATION_ID_COLUMN_NAME = "party_certification_id";
    protected static final String PARTY_ID_COLUMN_NAME = "party_id";

    // product_certification
    protected static final String PRODUCT_CERTIFICATION_TABLE_NAME = "product_certification";
    protected static final String PRODUCT_CERTIFICATION_ID_COLUMN_NAME = "product_certification_id";
    protected static final String PRODUCT_ID_COLUMN_NAME = "product_id";

    // shared between party and product certification
    protected static final String IS_ACTIVE_COLUMN_NAME = "is_active";

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        certificationInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(CERTIFICATION_TABLE_NAME)
                .usingGeneratedKeyColumns(CERTIFICATION_ID_COLUMN_NAME)
                .usingColumns(AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        partyCertificationInsert = new SimpleJdbcInsert(dataSource).withTableName(PARTY_CERTIFICATION_TABLE_NAME)
                .usingColumns(CERTIFICATION_ID_COLUMN_NAME, PARTY_ID_COLUMN_NAME, IS_ACTIVE_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        productCertificationInsert = new SimpleJdbcInsert(dataSource).withTableName(PRODUCT_CERTIFICATION_TABLE_NAME)
                .usingColumns(CERTIFICATION_ID_COLUMN_NAME, PRODUCT_ID_COLUMN_NAME, IS_ACTIVE_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        certificationAuditInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(CERTIFICATION_AUDIT_TABLE_NAME)
                .usingGeneratedKeyColumns(CERTIFICATION_AUDIT_ID_COLUMN_NAME)
                .usingColumns(CERTIFICATION_STANDARD_COLUMN_NAME, CERTIFICATION_NUMBER_COLUMN_NAME,
                        CERTIFICATION_LINK_COLUMN_NAME, ISSUED_DATE_COLUMN, ISSUED_BY_COLUMN, SCOPE_RANGE_COLUMN,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
        certificationAuditStatusInsert = new SimpleJdbcInsert(dataSource).withTableName(
                CERTIFICATION_AUDIT_STATUS_TABLE_NAME).usingColumns(CERTIFICATION_ID_COLUMN_NAME,
                CERTIFICATION_AUDIT_ID_COLUMN_NAME, EVENT_TYPE_COLUMN_NAME, EVENT_TIME_COLUMN_NAME,
                VERSION_COLUMN_NAME, AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Certification insertSupplierCertification(Certification certification) {
        insertCertification(certification);
        insertPartyCertificationContent(certification);
        insertCertificationAudit(certification);
        insertCertificationAuditStatus(certification);
        return certification;
    }

    @Override
    public Certification insertProductCertification(Certification certification) {
        insertCertification(certification);
        insertProductCertificationContent(certification);
        insertCertificationAudit(certification);
        insertCertificationAuditStatus(certification);
        return certification;
    }

    @Override
    public Certification update(Certification certification) {
        insertCertificationAudit(certification);
        insertCertificationAuditStatus(certification);
        return certification;
    }

    @Override
    public Certification loadLatest(Long certificationId, Locale locale) {
        LOGGER.info("Retrieving Certification for Certification Id = " + certificationId);

        String sql = getSql("certification/loadLatest.sql");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("certification_id", certificationId);
        return jdbcTemplate.queryForObject(sql, parameterSource, new CertificationRowMapper());
    }

    @Override
    public Certification loadApproved(Long id, Locale locale) {
        LOGGER.debug("Looking up approved certification with ID " + id);

        String sql = getSql("certification/loadApproved.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("certification_id", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new CertificationRowMapper());
    }

    @Override
    public void delete(Long certificationId) {
        LOGGER.info("Deleting Certification for Certification Id = " + certificationId);

        String sql = getSql("certification/deleteSupplierCertification.sql");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("certification_id", certificationId);
        jdbcTemplate.update(sql, parameterSource);

        sql = getSql("certification/deleteProductCertification.sql");
        parameterSource = new MapSqlParameterSource("certification_id", certificationId);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public int deleteByPartyId(Long partyId) {

        LOGGER.info("Deleting certifications for party" + partyId);

        List<Long> certificationIds = getCertificationsIds(partyId);

        int numberOfDeletes = 0;

        if (!certificationIds.isEmpty()) {

            SqlParameterSource certificationParameterSource = new MapSqlParameterSource().addValue("certificationIds",
                    certificationIds);

            List<Long> certificationAuditIds = jdbcTemplate.queryForList(
                    getSql("/certification/loadCertificationAuditIdsByCertificationIds.sql"),
                    certificationParameterSource, Long.class);

            SqlParameterSource certificationAuditParameterSource = new MapSqlParameterSource().addValue(
                    "certificationAuditIds", certificationAuditIds);

            if (!certificationAuditIds.isEmpty()) {
                jdbcTemplate.update(getSql("/certification/deleteCertificationAuditStatusByCertificationId.sql"),
                        certificationParameterSource);
                jdbcTemplate.update(getSql("/certification/deleteCertificationAuditByCertificationAuditId.sql"),
                        certificationAuditParameterSource);
                jdbcTemplate.update(getSql("/certification/deleteProductCertificationByCertificationId.sql"),
                        certificationParameterSource);
                jdbcTemplate.update(getSql("/certification/deletePartyCertificationByCertificationId.sql"),
                        certificationParameterSource);

                numberOfDeletes = jdbcTemplate
                        .update(getSql("/certification/deleteCertificationByCertificationId.sql"),
                                certificationParameterSource);
            }
        }

        return numberOfDeletes;
    }

    private List<Long> getCertificationsIds(Long partyId) {
        SqlParameterSource partyParameterSource = new MapSqlParameterSource().addValue("partyId", partyId);

        List<Long> partyCertificationsIds = jdbcTemplate.queryForList(
                getSql("/certification/loadPartyCertificationIdsPartyId.sql"), partyParameterSource, Long.class);

        List<Long> productCertificationsIds = jdbcTemplate.queryForList(
                getSql("/certification/loadProductCertificationIdsPartyId.sql"), partyParameterSource, Long.class);

        List<Long> certificationIds = new ArrayList<Long>();
        certificationIds.addAll(partyCertificationsIds);
        certificationIds.addAll(productCertificationsIds);

        certificationIds.remove(null);

        return certificationIds;
    }

    private void insertCertification(Certification certification) {
        SqlParameterSource parameterSource = new AuditSqlParameterSource();
        Number certificationKey = certificationInsert.executeAndReturnKey(parameterSource);
        certification.setId(certificationKey.longValue());
    }

    private void insertPartyCertificationContent(Certification certification) {
        LOGGER.info("Creating Party Certification for: " + certification);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(CERTIFICATION_ID_COLUMN_NAME, certification.getId())
                .addValue(PARTY_ID_COLUMN_NAME, certification.getSupplierOrProductId())
                .addValue(IS_ACTIVE_COLUMN_NAME, Boolean.TRUE);
        partyCertificationInsert.execute(parameterSource);
    }

    private void insertProductCertificationContent(Certification certification) {
        LOGGER.info("Creating Product Certification for: " + certification);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(CERTIFICATION_ID_COLUMN_NAME, certification.getId())
                .addValue(PRODUCT_ID_COLUMN_NAME, certification.getSupplierOrProductId())
                .addValue(IS_ACTIVE_COLUMN_NAME, Boolean.TRUE);
        productCertificationInsert.execute(parameterSource);
    }

    private void insertCertificationAudit(Certification certification) {
        LOGGER.info("Creating Certification Audit for: " + certification);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(CERTIFICATION_STANDARD_COLUMN_NAME, certification.getStandard())
                .addValue(CERTIFICATION_NUMBER_COLUMN_NAME, certification.getCertificateNumber())
                .addValue(CERTIFICATION_LINK_COLUMN_NAME, certification.getLink())
                .addValue(ISSUED_DATE_COLUMN, new java.util.Date(certification.getIssuedDate()))
                .addValue(SCOPE_RANGE_COLUMN, certification.getScopeRange())
                .addValue(ISSUED_BY_COLUMN, certification.getIssuedBy());
        Number certificationKey = certificationAuditInsert.executeAndReturnKey(parameterSource);
        certification.setSnapshotId(certificationKey.longValue());
    }

    private void insertCertificationAuditStatus(Certification certification) {
        LOGGER.info("Creating Certification Audit Status for: " + certification);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(CERTIFICATION_ID_COLUMN_NAME, certification.getId())
                .addValue(CERTIFICATION_AUDIT_ID_COLUMN_NAME, certification.getSnapshotId())
                .addValue(EVENT_TYPE_COLUMN_NAME, certification.getApprovalStatus())
                .addValue(EVENT_TIME_COLUMN_NAME, new Date()).addValue(VERSION_COLUMN_NAME, certification.getVersion());
        certificationAuditStatusInsert.execute(parameterSource);
    }

    private final class CertificationRowMapper implements RowMapper<Certification> {

        @Override
        public Certification mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Certification(rs.getLong(CERTIFICATION_ID_COLUMN_NAME),
                    rs.getString(CERTIFICATION_STANDARD_COLUMN_NAME), rs.getString(CERTIFICATION_NUMBER_COLUMN_NAME),
                    rs.getString(CERTIFICATION_LINK_COLUMN_NAME), null, rs.getString(ISSUED_BY_COLUMN), rs.getDate(
                            ISSUED_DATE_COLUMN).getTime(), rs.getString(SCOPE_RANGE_COLUMN),
                    ApprovalStatus.findByName(rs.getString(EVENT_TYPE_COLUMN_NAME)), rs.getInt(VERSION_COLUMN_NAME),
                    rs.getLong(CERTIFICATION_AUDIT_ID_COLUMN_NAME));
        }

    }
}
