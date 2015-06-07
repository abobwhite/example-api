package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.daugherty.e2c.security.PHPassHasher;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

/**
 * Spring JDBC implementation of the User database write implementations.
 */
@Repository("jdbcUserDao")
public class JdbcUserDao extends SortAndPaginationJdbcDao implements UserWriteDao, UserReadDao, UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static final String USER_ID_COLUMN_NAME = "user_id";
    public static final String USERNAME_COLUMN_NAME = "username";
    static final String PASSWORD_COLUMN_NAME = "password";
    static final String FAILURES_COLUMN_NAME = "failures_since_last_success";
    static final String IS_APPROVED_ONCE_COLUMN_NAME = "was_approved_once";
    public static final String ENABLED_COLUMN_NAME = "enabled";
    public static final String ROLE_NAME_COLUMN_NAME = "role_description";
    public static final String LOCKED_COLUMN_NAME = "locked";
    private static final String USER_ROLE_ID_COLUMN_NAME = "user_role_id";
    private static final String FIRST_NAME_COLUMN_NAME = "first_name";
    private static final String LAST_NAME_COLUMN_NAME = "last_name";
    private static final String EMAIL_ADDRESS_COLUMN_NAME = "email_address";
    private static final String COMPANY_NAME_COLUMN_NAME = "company_name_english";
    private static final String PASSWORD_HASH_START = "$S$";

    private final PHPassHasher phpassHasher = new PHPassHasher();

    private SimpleJdbcInsert userLoginInsert;
    private SimpleJdbcInsert userRoleInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        userLoginInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user_login")
                .usingGeneratedKeyColumns(USER_ID_COLUMN_NAME)
                .usingColumns(USERNAME_COLUMN_NAME, PASSWORD_COLUMN_NAME, ENABLED_COLUMN_NAME, FAILURES_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
        userRoleInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user_role")
                .usingGeneratedKeyColumns(USER_ROLE_ID_COLUMN_NAME)
                .usingColumns(USER_ID_COLUMN_NAME, ROLE_NAME_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public void insert(E2CUser user) {
        LOGGER.info("Creating user " + user);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(USERNAME_COLUMN_NAME, user.getUsername()).addValue(PASSWORD_COLUMN_NAME, user.getPassword())
                .addValue(ENABLED_COLUMN_NAME, user.isEnabled())
                .addValue(FAILURES_COLUMN_NAME, user.getFailuresSinceLastSuccess());

        Long createdUserId = (Long) userLoginInsert.executeAndReturnKey(parameterSource);
        for (GrantedAuthority authority : user.getAuthorities()) {
            insertUserRole(createdUserId, authority);
        }
        updatePartyUserId(user.getParty().getId(), createdUserId);
    }

    @Override
    public void insertUserRole(Long userId, GrantedAuthority authority) {
        LOGGER.info("Assigning role " + authority + " to user with Id " + userId);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(USER_ID_COLUMN_NAME, userId)
                .addValue(ROLE_NAME_COLUMN_NAME, authority.getAuthority());
        userRoleInsert.execute(parameterSource);
    }

    @Override
    public void deleteUserRole(Long userId, GrantedAuthority authority) {
        LOGGER.info("Deleting user role" + authority.getAuthority() + " for user id " + userId);
        String sql = getSql("user/deleteUserRole.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(USER_ID_COLUMN_NAME, userId)
                .addValue(ROLE_NAME_COLUMN_NAME, authority.getAuthority());
        jdbcTemplate.update(sql, parameterSource);
    }

    private void updatePartyUserId(Long partyId, Long userId) {
        LOGGER.info("Associating user " + userId + " to party with Id " + partyId);
        String sql = getSql("user/updatePartyUserId.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("userId", userId).addValue(
                "partyId", partyId);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void incrementFailureCount(String username) {
        LOGGER.info("Incrementing failure count for " + username);
        String sql = getSql("user/increment-failure-count.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("username", username);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void resetFailureCount(String username) {
        LOGGER.info("Resetting failure count for " + username);
        String sql = getSql("user/reset-failure-count.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("username", username);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void disableUser(String username, String password) {
        LOGGER.info("Disabling User for " + username);
        String sql = getSql("user/disable-user.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("username", username).addValue(
                "password", password);
        jdbcTemplate.update(sql, parameterSource);

    }

    @Override
    public void blockUser(String username) {
        LOGGER.info("Blocking User for " + username);
        String sql = getSql("user/block.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("username", username).addValue(
                "enabled", false);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void unblockUser(String username) {
        LOGGER.info("Unblocking User for " + username);
        String sql = getSql("user/block.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("username", username).addValue(
                "enabled", true);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        LOGGER.info("Resetting password for " + username);
        String sql = getSql("user/change-password.sql");
        String hashedPassword = phpassHasher.createHash(newPassword);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("username", username).addValue(
                "newPassword", hashedPassword);
        jdbcTemplate.update(sql, parameterSource);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Loading user " + username);
        String userSql = getSql("user/get-all.sql") + " WHERE username = :username";
        SqlParameterSource userParameterSource = new MapSqlParameterSource().addValue("username", username);

        List<User> listableUsers = jdbcTemplate.query(userSql, userParameterSource, new UserResultSetExtractor());

        if (CollectionUtils.isEmpty(listableUsers)) {
            throw new EmptyResultDataAccessException(1);
        } else {
            return listableUsers.get(0).getE2CUser();
        }
    }

    @Override
    public QueryCriteria createQueryCriteria(String usernamePrefix, String firstNamePrefix, String lastNamePrefix,
            String emailPrefix, String companyNamePrefix, String approvalStatus, String role, Boolean presence,
            Boolean locked, Boolean blocked, String sortByPropertyName, Boolean sortDescending, Integer startItem,
            Integer count) {

        SqlQueryCriteria criteria = createSqlQueryCriteria(sortByPropertyName, sortDescending, startItem, count, null)
                .appendLikeSubClause(USERNAME_COLUMN_NAME, "usernamePrefix", usernamePrefix)
                .appendLikeSubClause(FIRST_NAME_COLUMN_NAME, "firstName", firstNamePrefix)
                .appendLikeSubClause(LAST_NAME_COLUMN_NAME, "lastNamePrefix", lastNamePrefix)
                .appendLikeSubClause(EMAIL_ADDRESS_COLUMN_NAME, "emailPrefix", emailPrefix)
                .appendLikeSubClause(COMPANY_NAME_COLUMN_NAME, "companyNamePrefix", companyNamePrefix)
                .appendEqualsSubClause(ROLE_NAME_COLUMN_NAME, "role", role)
                .appendEqualsSubClause(EVENT_TYPE_COLUMN_NAME, "approvalStatus", approvalStatus)
                .appendEqualsSubClause(LOCKED_COLUMN_NAME, "locked", locked);

        if (blocked != null && blocked) {
            criteria.appendEqualsSubClause(ENABLED_COLUMN_NAME, "enabled", false).appendLikeSubClause(
                    PASSWORD_COLUMN_NAME, "password", PASSWORD_HASH_START);
        }

        return criteria;
    }

    @Override
    public User find(Long userId) {
        LOGGER.info("Getting User for userId " + userId);
        String sql = getSql("user/get-all.sql") + " WHERE user_id = :userId";
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("userId", userId);

        List<User> users = jdbcTemplate.query(sql, parameterSource, new UserResultSetExtractor());

        if (CollectionUtils.isEmpty(users)) {
            throw new EmptyResultDataAccessException(1);
        } else {
            return users.get(0);
        }
    }

    @Override
    public List<User> find(QueryCriteria criteria) {
        LOGGER.debug("Finding all Listable Users in the database matching " + criteria.toString());
        String sql = getSql("/user/get-all.sql") + criteria.getCombinedQueryClauses();
        System.out.println("Finding all Listable Users SQL " + sql);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new UserResultSetExtractor());
    }

    @Override
    public List<User> findPendingUsers(QueryCriteria criteria) {
        LOGGER.debug("Finding all Listable Users in Pending status the database matching " + criteria.toString());
        String sql = getSql("/listableUser/get-all-pending.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new UserResultSetExtractor());
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(User.USERNAME_SERIAL_PROPERTY, USERNAME_COLUMN_NAME);
        columnsByProperty.put(User.FIRST_NAME_SERIAL_PROPERTY, FIRST_NAME_COLUMN_NAME);
        columnsByProperty.put(User.LAST_NAME_SERIAL_PROPERTY, LAST_NAME_COLUMN_NAME);
        columnsByProperty.put(User.EMAIL_SERIAL_PROPERTY, EMAIL_ADDRESS_COLUMN_NAME);
        columnsByProperty.put(User.COMPANY_NAME_SERIAL_PROPERTY, COMPANY_NAME_COLUMN_NAME);
        columnsByProperty.put(User.APPROVAL_STATUS_SERIAL_PROPERTY, EVENT_TYPE_COLUMN_NAME);
        columnsByProperty.put(User.ROLE_SERIAL_PROPERTY, ROLE_NAME_COLUMN_NAME);
        columnsByProperty.put(User.LOCKED_SERIAL_PROPERTY, LOCKED_COLUMN_NAME);
        columnsByProperty.put(User.LAST_UPDATED_SERIAL_PROPERTY, EVENT_TIME_COLUMN_NAME);
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    public String getEmailWithPersonalNameForPartyId(Long id) {
        LOGGER.info("Getting Email Address for party id " + id);
        String sql = getSql("user/getEmailAddressByPartyId.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("partyId", id);
        return jdbcTemplate.queryForObject(sql, parameterSource, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Party.buildEmailWithPersonalName(rs.getString(JdbcPartyDao.FIRST_NAME_COLUMN_NAME),
                        rs.getString(JdbcPartyDao.LAST_NAME_COLUMN_NAME), rs.getString(JdbcPartyDao.EMAIL_COLUMN_NAME));
            }

        });
    }

    @Override
    public String getUsernameByPassword(String password) {
        LOGGER.info("Getting Username for password " + password);
        String sql = getSql("user/get-all.sql") + " WHERE password = :password";
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("password", password);

        List<User> users = jdbcTemplate.query(sql, parameterSource, new UserResultSetExtractor());

        if (CollectionUtils.isEmpty(users)) {
            throw new EmptyResultDataAccessException(1);
        } else {
            return users.get(0).getUsername();
        }
    }

    @Override
    public String getUsernameByEmailAddress(String emailAddress) {
        LOGGER.info("Getting Username for emailAddress " + emailAddress);
        String sql = getSql("user/getUsernameByEmailAddress.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("emailAddress", emailAddress);
        return jdbcTemplate.queryForObject(sql, parameterSource, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(USERNAME_COLUMN_NAME);
            }

        });
    }

    @Override
    public User findByPartyId(Long partyId) {
        LOGGER.info("Getting User for partyId " + partyId);
        String sql = getSql("user/get-all.sql") + " WHERE party_id = :partyId";
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("partyId", partyId);

        List<User> users = jdbcTemplate.query(sql, parameterSource, new UserResultSetExtractor());

        if (CollectionUtils.isEmpty(users)) {
            throw new EmptyResultDataAccessException(1);
        } else {
            return users.get(0);
        }
    }

    @Override
    public int deleteUser(Long userId) {
        LOGGER.info("Deleting user" + userId);

        SqlParameterSource partyParameterSource = new MapSqlParameterSource().addValue("userId", userId);

        jdbcTemplate.update(getSql("/user/deleteUserRoleByUserId.sql"), partyParameterSource);

        int numberOfDeletes = jdbcTemplate.update(getSql("/user/deleteByUserId.sql"), partyParameterSource);

        LOGGER.info("Deleted user" + userId);

        return numberOfDeletes;
    }

    private final class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        private UserRowMapper listableUserRowMapper = new UserRowMapper();

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, User> listableUsers = new LinkedHashMap<Long, User>();
            while (rs.next()) {
                Long userId = rs.getLong(USER_ID_COLUMN_NAME);
                User listableUser = listableUsers.get(userId);
                if (listableUser == null) {
                    listableUser = listableUserRowMapper.mapRow(rs, 0);
                    listableUsers.put(userId, listableUser);
                }
                String role = rs.getString(ROLE_NAME_COLUMN_NAME);
                listableUser.getRoles().add(role);
            }
            return Lists.newArrayList(listableUsers.values());
        }
    }

    private class UserRowMapper implements RowMapper<User> {
        private PartyRowMapper partyRowMapper = new PartyRowMapper();
        static final String CONFIRMATION_TOKEN_REGEX = "\\-?\\d{10}";

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong(USER_ID_COLUMN_NAME), rs.getString(USERNAME_COLUMN_NAME),
                    rs.getString(PASSWORD_COLUMN_NAME), new ArrayList<String>(), rs.getInt(FAILURES_COLUMN_NAME),
                    rs.getBoolean(LOCKED_COLUMN_NAME), isBlocked(rs.getBoolean(ENABLED_COLUMN_NAME),
                            rs.getString(PASSWORD_COLUMN_NAME)), rs.getBoolean(ENABLED_COLUMN_NAME),
                    partyRowMapper.mapRow(rs, rowNum), rs.getBoolean(IS_APPROVED_ONCE_COLUMN_NAME),
                    rs.getTimestamp(EVENT_TIME_COLUMN_NAME));
        }

        private boolean isBlocked(boolean enabled, String password) {
            return !enabled && !Pattern.matches(CONFIRMATION_TOKEN_REGEX, password);
        }
    }

}
