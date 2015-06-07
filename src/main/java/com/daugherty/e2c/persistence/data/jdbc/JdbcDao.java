package com.daugherty.e2c.persistence.data.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Base class for all Spring JDBC DAOs, simplifying externalixed SQL lookup.
 */
public abstract class JdbcDao implements ResourceLoaderAware {

    protected static final String EVENT_TYPE_COLUMN_NAME = "event_type";
    protected static final String EVENT_TIME_COLUMN_NAME = "event_datetime";
    protected static final String VERSION_COLUMN_NAME = "version_number";
    protected static final String LAST_MODIFIED_BY_COLUMN_NAME = "last_modified_by";
    protected static final String LAST_MODIFIED_DATE_COLUMN_NAME = "last_modified_date";

    protected static final String PARTY_ID_COLUMN_NAME = "party_id";
    protected static final String PUBLIC_PARTY_ID_COLUMN_NAME = "public_party_id";
    protected static final String ENGLISH_NAME_COLUMN_NAME = "company_name_english";
    protected static final String PARTY_AUDIT_ID_COLUMN_NAME = "party_audit_id";
    protected static final String PARTY_AUDIT_STATUS_TABLE_NAME = "party_audit_status";
    protected static final String PARTY_AUDIT_STATUS_ID_COLUMN_NAME = "party_audit_status_id";
    protected static final String PARTY_WAS_APPROVED_ONCE_COLUMN_NAME = "was_approved_once";

    protected static final String PRODUCT_ID_COLUMN_NAME = "product_id";
    protected static final String PRODUCT_AUDIT_ID_COLUMN_NAME = "product_audit_id";
    protected static final String PRODUCT_AUDIT_STATUS_TABLE_NAME = "product_audit_status";
    protected static final String PRODUCT_AUDIT_STATUS_ID_COLUMN_NAME = "product_audit_status_id";

    protected static final String CERTIFICATION_ID_COLUMN_NAME = "certification_id";
    protected static final String CERTIFICATION_AUDIT_ID_COLUMN_NAME = "certification_audit_id";
    protected static final String CERTIFICATION_AUDIT_STATUS_TABLE_NAME = "certification_audit_status";
    protected static final String CERTIFICATION_AUDIT_STATUS_ID_COLUMN_NAME = "certification_audit_status_id";

    protected static final String MESSAGE_ID_COLUMN_NAME = "message_id";
    protected static final String PUBLIC_MESSAGE_ID_COLUMN_NAME = "public_message_id";

    private ResourceLoader resourceLoader;
    protected NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Inject
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        createSimpleJdbcInserts(dataSource);
    }

    /**
     * Override to define any SimpleJdbcInsert objects needed by the DAO.
     */
    protected void createSimpleJdbcInserts(DataSource dataSource) {
    }

    /**
     * Retrieves SQL from a file.
     * 
     * @param resourceFilePath
     *            should be relative to sql file path. If file path is "src/main/resources/sql/buyer/load.sql" then
     *            resource file path is "buyer/load.sql"
     */
    protected String getSql(String resourceFilePath) {
        String outStr = "";

        try {
            Resource resource = resourceLoader.getResource("classpath:sql/" + resourceFilePath);

            InputStream is = resource.getInputStream();
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String str;
                while ((str = br.readLine()) != null) {
                    outStr += " " + str;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outStr;
    }

    protected Date today() {
        return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    }

    protected Integer getNullSafeInteger(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }

    protected Long getNullSafeLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    protected String getPossiblyTranslatedString(ResultSet rs, String englishColumnName, String translatedColumnName)
            throws SQLException {
        String name = rs.getString(translatedColumnName);
        if (StringUtils.isBlank(name)) {
            name = rs.getString(englishColumnName);
        }
        return name;
    }

}
