package com.daugherty.e2c.persistence.data.jdbc;

import java.util.Date;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.daugherty.e2c.domain.SecurityUtils;

/**
 * Base ParameterSource that includes audit column information.
 */
public class AuditSqlParameterSource extends MapSqlParameterSource {

    static final String LAST_MODIFIED_BY_COLUMN_NAME = JdbcDao.LAST_MODIFIED_BY_COLUMN_NAME;
    static final String LAST_MODIFIED_DATE_COLUMN_NAME = JdbcDao.LAST_MODIFIED_DATE_COLUMN_NAME;

    public AuditSqlParameterSource() {
        addValue(LAST_MODIFIED_DATE_COLUMN_NAME, new Date());
        addValue(LAST_MODIFIED_BY_COLUMN_NAME, SecurityUtils.getAuthenticatedUsername());
    }

}
