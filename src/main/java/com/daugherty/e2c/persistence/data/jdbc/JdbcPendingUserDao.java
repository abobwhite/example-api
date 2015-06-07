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

import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.security.PHPassHasher;

@Repository
public class JdbcPendingUserDao extends JdbcDao implements PendingUserReadDao, PendingUserWriteDao {

    private static final String PENDING_USER_ID_COLUMN_NAME = "pending_user_id";
    private static final String USERNAME_COLUMN_NAME = "username";
    private static final String PASSWORD_COLUMN_NAME = "password";
    private static final String CONFIRMATION_TOKEN_COLUMN_NAME = "confirmation_token";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final PHPassHasher phpassHasher = new PHPassHasher();

    private SimpleJdbcInsert jdbcInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("pending_user")
                .usingGeneratedKeyColumns(PENDING_USER_ID_COLUMN_NAME)
                .usingColumns(USERNAME_COLUMN_NAME, PASSWORD_COLUMN_NAME, PARTY_ID_COLUMN_NAME,
                        CONFIRMATION_TOKEN_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public PendingUser loadByConfirmationToken(String confirmationToken) {
        LOGGER.debug("Looking up pending user for confirmation token " + confirmationToken);
        String sql = getSql("pendinguser/loadByConfirmationToken.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("confirmationToken",
                confirmationToken);
        RowMapper<PendingUser> rowMapper = new PendingUserRowMapper();

        return jdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    @Override
    public PendingUser loadByUsername(String username) {
        LOGGER.debug("Looking up pending user for username " + username);
        String sql = getSql("pendinguser/loadByUsername.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue(USERNAME_COLUMN_NAME, username);
        RowMapper<PendingUser> rowMapper = new PendingUserRowMapper();

        return jdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    @Override
    public PendingUser loadPendingUserByEmailAddress(String emailAddress) {
        LOGGER.info("Getting Pending User for emailAddress " + emailAddress);
        String sql = getSql("pendinguser/loadByEmailAddress.sql");
        LOGGER.debug(sql);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("emailAddress", emailAddress);
        RowMapper<PendingUser> rowMapper = new PendingUserRowMapper();

        return jdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    @Override
    public PendingUser loadPendingUserByPassword(String password) {
        LOGGER.info("Getting Pending User for password " + password);
        String sql = getSql("pendinguser/loadByPassword.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("password", password);
        RowMapper<PendingUser> rowMapper = new PendingUserRowMapper();

        return jdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    }

    @Override
    public PendingUser insert(PendingUser pendingUser) {
        LOGGER.info("Creating pending user " + pendingUser);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(USERNAME_COLUMN_NAME, pendingUser.getUsername())
                .addValue(PASSWORD_COLUMN_NAME, phpassHasher.createHash(pendingUser.getPassword()))
                .addValue(PARTY_ID_COLUMN_NAME, pendingUser.getPartyId())
                .addValue(CONFIRMATION_TOKEN_COLUMN_NAME, pendingUser.getConfirmationToken());
        Number key = jdbcInsert.executeAndReturnKey(parameterSource);

        pendingUser.setId(key.longValue());
        return pendingUser;
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting pending user with Id " + id);
        String sql = getSql("pendinguser/delete.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("pendingUserId", id);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void deleteByPartyId(Long partyId) {
        LOGGER.info("Deleting pending user with party id " + partyId);
        String sql = getSql("pendinguser/deleteByPartyId.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("partyId", partyId);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void disablePendingUser(String username, String password) {
        LOGGER.info("Disabling User for " + username);
        String sql = getSql("pendinguser/disable-user.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("username", username).addValue(
                "password", password);
        jdbcTemplate.update(sql, parameterSource);

    }

    @Override
    public void changePassword(String username, String password) {
        LOGGER.info("Disabling Pending User for " + username);
        String sql = getSql("pendinguser/disable-user.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("username", username).addValue(
                "password", phpassHasher.createHash(password));
        jdbcTemplate.update(sql, parameterSource);

    }

    /**
     * Maps ResultSet rows to PendingBuyer objects.
     */
    private final class PendingUserRowMapper implements RowMapper<PendingUser> {
        @Override
        public PendingUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PendingUser(rs.getLong(PENDING_USER_ID_COLUMN_NAME), rs.getString(USERNAME_COLUMN_NAME),
                    rs.getString(PASSWORD_COLUMN_NAME), rs.getLong(PARTY_ID_COLUMN_NAME),
                    rs.getString(CONFIRMATION_TOKEN_COLUMN_NAME));
        }
    }

}
