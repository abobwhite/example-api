package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.persistence.data.ProfileTranslationReadDao;
import com.daugherty.e2c.persistence.data.ProfileTranslationWriteDao;

/**
 * Spring JDBC implementation of the ProfileTransation database operations.
 */
@Repository
public class JdbcProfileTranslationDao extends JdbcDao implements ProfileTranslationReadDao, ProfileTranslationWriteDao {

    static final String LANGUAGE_ID_COLUMN_NAME = "language_id";
    static final String COMPANY_DESCRIPTION_COLUMN_NAME = "company_description";
    static final String COMPANY_DESCRIPTION_TRANSLATION_COLUMN_NAME = "company_description_translation";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert partyAuditTranslationInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        partyAuditTranslationInsert = new SimpleJdbcInsert(dataSource).withTableName("party_audit_translation")
                .usingColumns(LANGUAGE_ID_COLUMN_NAME, PARTY_AUDIT_ID_COLUMN_NAME, COMPANY_DESCRIPTION_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Long getUpdateIdForLatestSupplier(Long supplierId) {
        LOGGER.debug("Looking up ID of existing translation record for latest supplier with ID " + supplierId);
        String sql = getSql("profiletranslation/get-update-id-for-latest-supplier.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("supplierId", supplierId);
        long updateId = jdbcTemplate.queryForLong(sql, parameterSource);
        return updateId == 0 ? null : updateId;
    }

    @Override
    public ProfileTranslation loadByLatestSupplierId(Long supplierId) {
        LOGGER.debug("Looking up profile translation with supplier ID " + supplierId);
        String sql = getSql("profiletranslation/load-by-supplier.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("supplierId", supplierId);
        RowMapper<ProfileTranslation> rowMapper = new RowMapper<ProfileTranslation>() {
            @Override
            public ProfileTranslation mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ProfileTranslation(rs.getLong(PARTY_ID_COLUMN_NAME), rs.getString(PUBLIC_PARTY_ID_COLUMN_NAME), rs.getLong(PARTY_AUDIT_ID_COLUMN_NAME),
                        rs.getString(COMPANY_DESCRIPTION_COLUMN_NAME),
                        rs.getString(COMPANY_DESCRIPTION_TRANSLATION_COLUMN_NAME));
            }
        };

        return jdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    @Override
    public ProfileTranslation insert(ProfileTranslation translation) {
        LOGGER.info("Inserting party audit translation record for " + translation);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(LANGUAGE_ID_COLUMN_NAME, Language.CHINESE.getId())
                .addValue(PARTY_AUDIT_ID_COLUMN_NAME, translation.getSnapshotId())
                .addValue(COMPANY_DESCRIPTION_COLUMN_NAME, translation.getCompanyDescriptionTranslation());
        partyAuditTranslationInsert.execute(parameterSource);
        return translation;
    }

    @Override
    public ProfileTranslation update(Long updateId, ProfileTranslation translation) {
        LOGGER.info("Updating party audit translation record with ID " + updateId + " for " + translation);
        String sql = getSql("profiletranslation/update.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("description",
                translation.getCompanyDescriptionTranslation()).addValue("updateId", updateId);
        jdbcTemplate.update(sql, parameterSource);
        return translation;
    }

}
