package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.daugherty.e2c.persistence.data.ApplicationParameterWriteDao;
import com.google.common.collect.Maps;

/**
 * Spring JDBC implementation of application parameter database operations.
 */
@Repository
public class JdbcApplicationParameterDao extends JdbcDao implements ApplicationParameterReadDao,
        ApplicationParameterWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected static final String NAME_COLUMN_NAME = "application_parameter_name";
    protected static final String VALUE_COLUMN_NAME = "application_parameter_value";

    @Override
    public String loadValueForName(String name) {
        String sql = getSql("/applicationParameter/load-value-for-name.sql");
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("name", name), String.class);
    }

    @Override
    public Map<String, String> loadAllValues() {
        String sql = getSql("/applicationParameter/load-all-values.sql");

        ApplicationParamterRowMapper rowMapper = new ApplicationParamterRowMapper();
        jdbcTemplate.query(sql, new MapSqlParameterSource(), rowMapper);

        return rowMapper.parameterMap;
    }

    @Override
    public void updateValueForName(String name, String value) {
        LOGGER.info("Updating application parameter with name " + name + " to " + value);
        String sql = getSql("applicationParameter/update.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("parameterValue", value).addValue(
                "parameterName", name);
        jdbcTemplate.update(sql, parameterSource);
    }

    private final class ApplicationParamterRowMapper implements RowMapper<String> {
        private Map<String, String> parameterMap = Maps.newHashMap();

        public String mapRow(ResultSet rs, int rowNum) throws SQLException {

            String parameterName = rs.getString(NAME_COLUMN_NAME);
            String parameterValue = rs.getString(VALUE_COLUMN_NAME);

            parameterMap.put(parameterName, parameterValue);

            return parameterName;
        }
    }
}
