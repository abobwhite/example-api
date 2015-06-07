package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.security.PHPassHasher;
import com.google.common.collect.Lists;

public class JdbcPendingUserDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcPendingUserDao pendingUserDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("pendingUser.sql");
    }

    @Test
    public void loadByConfirmationTokenThatExistsRetrievesPendingUser() {
        PendingUser pendingUser = pendingUserDao.loadByConfirmationToken("Test Confirmation Token");

        assertThat(pendingUser.getId(), is(999242L));
        assertThat(pendingUser.getUsername(), is("Test Username"));
        assertThat(pendingUser.getPartyId(), is(9992421L));
        assertThat(pendingUser.getConfirmationToken(), is("Test Confirmation Token"));
    }

    @Test
    public void loadByConfirmationTokenThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        pendingUserDao.loadByConfirmationToken("non-existent token");
    }

    @Test
    public void loadByUsernameThatExistsRetrievesPendingUser() {
        PendingUser pendingUser = pendingUserDao.loadByUsername("Test Username");

        assertThat(pendingUser.getId(), is(999242L));
        assertThat(pendingUser.getUsername(), is("Test Username"));
        assertThat(pendingUser.getPartyId(), is(9992421L));
        assertThat(pendingUser.getConfirmationToken(), is("Test Confirmation Token"));
    }

    @Test
    public void loadByUsernameThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        pendingUserDao.loadByUsername("non-existent username");
    }

    @Test
    public void createPendingUserInsertsDatabaseRecord() {
        PendingUser pendingUser = new PendingUser("Pending Username", "password", "passwordConfirmation");
        pendingUser.setPartyId(42L);

        PendingUser createdPendingUser = pendingUserDao.insert(pendingUser);
        assertThat(createdPendingUser.getId(), notNullValue());

        Map<String, Object> rowMap = jdbcTemplate.queryForMap("SELECT * FROM pending_user WHERE pending_user_id = ?",
                createdPendingUser.getId());
        assertThat(rowMap.get("username").toString(), is(pendingUser.getUsername()));
        Object persistedPassword = rowMap.get("password");
        assertThat(new PHPassHasher().isMatch(pendingUser.getPassword(), persistedPassword.toString()), is(true));
        assertThat((Long) rowMap.get("party_id"), is(pendingUser.getPartyId()));
        assertThat(rowMap.get("confirmation_token").toString(), is(pendingUser.getConfirmationToken()));
    }

    @Test
    public void deleteUserThatExists() {
        pendingUserDao.delete(-111L);

        int pendingUserCount = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM pending_user WHERE pending_user_id = ?",
                -111L);
        assertThat(pendingUserCount, is(0));
    }

    @Test
    public void loadPendingUserByEmailAddress() {
        PendingUser pendingUser = pendingUserDao.loadPendingUserByEmailAddress("pending@user.com");

        assertThat(pendingUser.getId(), is(999242L));
        assertThat(pendingUser.getUsername(), is("Test Username"));
        assertThat(pendingUser.getPartyId(), is(9992421L));
        assertThat(pendingUser.getConfirmationToken(), is("Test Confirmation Token"));
    }

    @Test
    public void loadByEmailAddressThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        pendingUserDao.loadPendingUserByEmailAddress("non-existent emailAddress");
    }

    @Test
    public void disbalePendingUser() throws Exception {
        pendingUserDao.disablePendingUser("Test Username", "disabled");

        Map<String, Object> rowMap = jdbcTemplate.queryForMap("SELECT * FROM pending_user WHERE pending_user_id = ?",
                999242L);
        assertThat(rowMap.get("username").toString(), is("Test Username"));
        assertThat(rowMap.get("password").toString(), is("disabled"));
        assertThat((Long) rowMap.get("party_id"), is(9992421L));
        assertThat(rowMap.get("confirmation_token").toString(), is("Test Confirmation Token"));
    }

    @Test
    public void changePasswordForUsername() throws Exception {
        String newPassword = "newPAssword";
        pendingUserDao.changePassword("Test Username", newPassword);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM pending_user WHERE username = ?", "Test Username");
        String persistedPassword = (String) partyAuditStatusRowMap.get("password");

        assertThat((String) partyAuditStatusRowMap.get("username"), is("Test Username"));
        assertThat(new PHPassHasher().isMatch(newPassword, persistedPassword), is(true));
    }
}
