package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.persistence.data.EmployeeReadDao;
import com.daugherty.e2c.persistence.data.EmployeeWriteDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@Repository("employeeDao")
public class JdbcEmployeeDao extends JdbcPartyDao implements EmployeeReadDao, EmployeeWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert partyAuditInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        partyAuditInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("party_audit")
                .usingGeneratedKeyColumns(PARTY_AUDIT_ID_COLUMN_NAME)
                .usingColumns(PARTY_TYPE_COLUMN_NAME, FIRST_NAME_COLUMN_NAME, LAST_NAME_COLUMN_NAME, EMAIL_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Employee insert(Employee employee) {
        employee.setPartyType(PartyType.INTERNAL);

        createParty(employee);
        createPartyAuditForInsert(employee);
        createPartyAuditStatus(employee);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        employee.setPartyType(PartyType.INTERNAL);

        createPartyAuditForUpdate(employee);
        createPartyAuditStatus(employee);
        return employee;
    }

    @Override
    public QueryCriteria createQueryCriteria(String username, String emailAddress, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count, Locale locale) {
        return createSqlQueryCriteria(propertyName, sortDescending, startItem, count, locale).appendLikeSubClause(
                USERNAME_COLUMN_NAME, "username", username).appendLikeSubClause(EMAIL_COLUMN_NAME, "emailAddress",
                emailAddress);
    }

    @Override
    public List<Employee> find(QueryCriteria queryCriteria) {
        LOGGER.debug("Getting all Employees from the database");
        String sql = getSql("/employee/get-all.sql") + queryCriteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, queryCriteria.getParameterMap(), new EmployeeResultSetExtractor());
    }

    @Override
    public Employee load(Long employeeId) {
        LOGGER.debug("Looking up Employee by Id " + employeeId);
        String sql = getSql("/employee/get-all.sql") + " WHERE party_id = :employeeId";
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("employeeId", employeeId);
        return jdbcTemplate.query(sql, parameterSource, new EmployeeResultSetExtractor()).get(0);
    }

    private void createPartyAuditForInsert(Employee employee) {
        LOGGER.info("Creating party audit for creation of " + employee);
        SqlParameterSource partyAuditUpdateParameterSource = new PartyAuditSqlParameterSource(employee);

        executePartyAuditInsert(employee, partyAuditUpdateParameterSource);
    }

    private void createPartyAuditForUpdate(Employee employee) {
        LOGGER.info("Creating party audit for update of " + employee);
        SqlParameterSource partyAuditUpdateParameterSource = new PartyAuditSqlParameterSource(employee);
        executePartyAuditInsert(employee, partyAuditUpdateParameterSource);
    }

    private void executePartyAuditInsert(Party party, SqlParameterSource partyAuditUpdateParameterSource) {
        Number partyAuditKey = partyAuditInsert.executeAndReturnKey(partyAuditUpdateParameterSource);
        party.setSnapshotId(partyAuditKey.longValue());
    }

    private final class EmployeeResultSetExtractor implements ResultSetExtractor<List<Employee>> {
        private EmployeeRowMapper employeeRowMapper = new EmployeeRowMapper();

        @Override
        public List<Employee> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Employee> employees = new LinkedHashMap<Long, Employee>();
            while (rs.next()) {
                Long employeeId = rs.getLong(PARTY_ID_COLUMN_NAME);
                Employee employee = employees.get(employeeId);
                if (employee == null) {
                    employee = employeeRowMapper.mapRow(rs, 0);
                    employees.put(employeeId, employee);
                }
                String role = rs.getString(JdbcUserDao.ROLE_NAME_COLUMN_NAME);
                employee.getRoles().add(role);
            }
            return Lists.newArrayList(employees.values());
        }
    }
}
