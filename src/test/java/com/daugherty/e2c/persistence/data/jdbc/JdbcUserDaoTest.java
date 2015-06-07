package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.security.PHPassHasher;
import com.daugherty.e2c.security.Role;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcUserDaoTest extends BaseJdbcDaoTest {

    private static final int SUPPLIER_COUNT = 5;
    private static final int BUYER_COUNT = 5;
    private static final int SUPPLIER_MODERATOR_COUNT = 2;
    private static final int BUYER_MODERATOR_COUNT = 2;
    private static final int TRANSLATOR_COUNT = 2;
    private static final int ADMIN_COUNT = 1;
    private static final int BLOCKED_COUNT = 1;

    @Inject
    private JdbcUserDao dao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("buyer.sql", "pendingUser.sql", "user.sql");
    }

    @Test
    public void createCreatesConfirmedUserAndAssociatesWithParty() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(Role.BUYER));
        E2CUser user = new E2CUser("username", "password", true, 0, Lists.newArrayList(new SimpleGrantedAuthority(
                Role.BUYER)), new Party(99911L), false);

        dao.insert(user);

        Map<String, Object> userRowMap = jdbcTemplate.queryForMap("SELECT * FROM user_login WHERE username = ?",
                user.getUsername());
        Long createdUserId = (Long) userRowMap.get("user_id");
        assertThat(userRowMap.get("password").toString(), is(user.getPassword()));
        assertThat((Boolean) userRowMap.get("enabled"), is(user.isEnabled()));
        assertThat((Integer) userRowMap.get("failures_since_last_success"), is(0));

        Map<String, Object> roleRowMap = jdbcTemplate.queryForMap("SELECT * FROM user_role WHERE user_id = ?",
                createdUserId);
        assertThat(roleRowMap.get("role_description").toString(), is(authorities.get(0).getAuthority()));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT * FROM party WHERE party_id = ?", user
                .getParty().getId());
        assertThat((Long) partyRowMap.get("user_id"), is(createdUserId));
    }

    @Test
    public void incrementFailureCountUpdatesOnlyRowMatchingUsername() throws Exception {
        assertFailureCountMatchesExpectedValue("fordp", 0);

        dao.incrementFailureCount("fordp");

        assertFailureCountMatchesExpectedValue("fordp", 1);
        assertFailureCountMatchesExpectedValue("apdent", 0);
        assertFailureCountMatchesExpectedValue("zaphodb", 2);
        assertFailureCountMatchesExpectedValue("tmcmillian", 1);
        assertFailureCountMatchesExpectedValue("babelf", 0);
        assertFailureCountMatchesExpectedValue("sbfast", 0);
    }

    @Test
    public void resetFailureCountUpdatesOnlyRowMatchingUsername() throws Exception {
        assertFailureCountMatchesExpectedValue("zaphodb", 2);

        dao.resetFailureCount("zaphodb");

        assertFailureCountMatchesExpectedValue("fordp", 0);
        assertFailureCountMatchesExpectedValue("apdent", 0);
        assertFailureCountMatchesExpectedValue("zaphodb", 0);
        assertFailureCountMatchesExpectedValue("tmcmillian", 1);
        assertFailureCountMatchesExpectedValue("babelf", 0);
        assertFailureCountMatchesExpectedValue("sbfast", 0);
    }

    @Test
    public void disbaleUser() throws Exception {
        dao.disableUser("fordp", "disabledPassword");

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM user_login WHERE username = ?", "fordp");
        assertThat((String) partyAuditStatusRowMap.get("username"), is("fordp"));
        assertThat((String) partyAuditStatusRowMap.get("password"), is("disabledPassword"));
        assertThat((Boolean) partyAuditStatusRowMap.get("enabled"), is(false));
    }

    @Test
    public void loadUserByUsernameThatIsAnExistingBuyerOrSupplierReturnsE2CUserWithPartyId() {
        UserDetails user = dao.loadUserByUsername("fordp");

        assertThat(user.getUsername(), is("fordp"));
        assertThat(user.getPassword(), is("$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg"));
        assertThat(user.getAuthorities().size(), is(1));
        assertThat(user.getAuthorities().iterator().next().getAuthority(), is(Role.SUPPLIER));
        assertThat(user.isEnabled(), is(true));

        assertThat(user, instanceOf(E2CUser.class));
        E2CUser e2cUser = (E2CUser) user;
        assertThat(e2cUser.getParty().getId(), is(21L));
        assertThat(e2cUser.isApprovedOnce(), is(true));
        assertThat(e2cUser.getFailuresSinceLastSuccess(), is(0));
    }

    @Test
    public void loadUserByUsernameThatExistsButIsNotABuyerOrSupplierReturnsE2CUserWithoutPartyId() {
        UserDetails user = dao.loadUserByUsername("zaphodb");

        assertThat(user.getUsername(), is("zaphodb"));
        assertThat(user.getPassword(), is("$S$Dp5VCmJwu9IIM/Isd.faKmfjakrtk/1lAKQOUZMakUa/JR7dJsFR"));
        assertThat(user.getAuthorities().size(), is(1));
        assertThat(user.getAuthorities().iterator().next().getAuthority(), is(Role.SUPPLIER_MODERATOR));
        assertThat(user.isEnabled(), is(true));

        assertThat(user, instanceOf(E2CUser.class));
        E2CUser e2cUser = (E2CUser) user;
        assertThat(e2cUser.getParty().getId(), is(77L));
        assertThat(e2cUser.getFailuresSinceLastSuccess(), is(2));
    }

    @Test
    public void loadUserByUsernameThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        dao.loadUserByUsername("non-existent");
    }

    @Test
    public void getEmailWithPersonalNameForPartyId() throws Exception {
        String emailAddress = dao.getEmailWithPersonalNameForPartyId(21L);

        assertThat(emailAddress, is("Ford Prefect <prefect@megadodo.com>"));

    }

    @Test
    public void getUsernameByPassword() throws Exception {
        String username = dao.getUsernameByPassword("$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg");

        assertThat(username, is("fordp"));
    }

    @Test
    public void getUsernameByEmailAddress() throws Exception {
        String username = dao.getUsernameByEmailAddress("prefect@megadodo.com");

        assertThat(username, is("fordp"));
    }

    @Test
    public void changePasswordForUsername() throws Exception {
        String newPassword = "newPAssword";
        dao.changePassword("fordp", "hoopyfrood", newPassword);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM user_login WHERE username = ?", "fordp");
        String persistedPassword = (String) partyAuditStatusRowMap.get("password");

        assertThat((String) partyAuditStatusRowMap.get("username"), is("fordp"));
        assertThat(new PHPassHasher().isMatch(newPassword, persistedPassword), is(true));
        assertThat((Boolean) partyAuditStatusRowMap.get("enabled"), is(true));
    }

    @Test
    public void blockUser() throws Exception {
        dao.blockUser("fordp");

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM user_login WHERE username = ?", "fordp");

        assertThat((String) partyAuditStatusRowMap.get("username"), is("fordp"));
        assertThat((Boolean) partyAuditStatusRowMap.get("enabled"), is(false));
    }

    @Test
    public void unblockUser() throws Exception {
        dao.unblockUser("pendingApprovalBuyer");

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM user_login WHERE username = ?", "pendingApprovalBuyer");

        assertThat((String) partyAuditStatusRowMap.get("username"), is("pendingApprovalBuyer"));
        assertThat((Boolean) partyAuditStatusRowMap.get("enabled"), is(true));
    }

    @Test
    public void findById() {
        User user = dao.find(107L);

        assertListableUser(getBuyerOrSupplierByPartyId(Lists.newArrayList(user), 107), "hostess", "Jeltz",
                "Prostetnic", "Vogon Battlefleet", "jeltz@vogonbattlefleet.mil", ApprovalStatus.PENDING_APPROVAL,
                Lists.newArrayList(Role.SUPPLIER), false, false, false, 2401L, 1);
    }

    @Test
    public void findWithSupplierRoleReturnsAllSuppliers() {
        QueryCriteria sortingAndPaginationCriteria = dao.createQueryCriteria(null, null, null, null, null, null,
                Role.SUPPLIER, false, false, false, null, false, 1, 25);

        List<User> users = dao.find(sortingAndPaginationCriteria);

        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(SUPPLIER_COUNT));

        assertListableUser(getBuyerOrSupplierByPartyId(users, 107), "hostess", "Jeltz", "Prostetnic",
                "Vogon Battlefleet", "jeltz@vogonbattlefleet.mil", ApprovalStatus.PENDING_APPROVAL,
                Lists.newArrayList(Role.SUPPLIER), false, false, false, 2401L, 1);
        assertListableUser(getBuyerOrSupplierByPartyId(users, 101), "fordp", "Ford", "Prefect",
                "Megadodo Publications", "prefect@megadodo.com", ApprovalStatus.APPROVED,
                Lists.newArrayList(Role.SUPPLIER), false, false, false, 2101L, 1);
        assertListableUser(getBuyerOrSupplierByPartyId(users, 130), "cherryBo", "Cherry", "Spice", "Cherry Spice",
                "cherry@spice.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.SUPPLIER), false, false, false,
                3001L, 1);
        assertListableUser(getBuyerOrSupplierByPartyId(users, 1717), "changedPassword", "Change", "Password",
                "Changed Password", "cp@changepassword.com", ApprovalStatus.APPROVED,
                Lists.newArrayList(Role.SUPPLIER), false, false, false, 242424L, 1);
    }

    @Test
    public void findWithBuyerRoleReturnsAllBuyers() {
        QueryCriteria sortingAndPaginationCriteria = dao.createQueryCriteria(null, null, null, null, null, null,
                Role.BUYER, null, null, null, null, false, 1, 25);

        List<User> users = dao.find(sortingAndPaginationCriteria);

        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(BUYER_COUNT));
        assertListableUser(getBuyerOrSupplierByPartyId(users, 108), "pendingApprovalBuyer", "hue", "www",
                "Hue Buyers Inc", "hue.jinko@gmail.com", ApprovalStatus.PENDING_APPROVAL,
                Lists.newArrayList(Role.BUYER), true, true, false, 2801L, 1);
        assertListableUser(getBuyerOrSupplierByPartyId(users, 102), "apdent", "Arthur", "Dent",
                "East India Tea Company", "dent@eastindiatea.com", ApprovalStatus.APPROVED,
                Lists.newArrayList(Role.BUYER), false, false, false, 2201L, 1);
        assertListableUser(getBuyerOrSupplierByPartyId(users, 1717), "changedPassword", "Change", "Password",
                "Changed Password", "cp@changepassword.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.BUYER),
                false, false, false, 242424L, 1);
    }

    @Test
    public void findPendingUsersReturnsAllPendingUsers() {
        QueryCriteria sortingAndPaginationCriteria = dao.createQueryCriteria(null, null, null, null, null,
                ApprovalStatus.UNPROFILED.getName(), null, false, false, false, null, false, 1, 25);

        List<User> users = dao.findPendingUsers(sortingAndPaginationCriteria);
        User pendingSupplier = getBuyerOrSupplierByPartyId(users, 123);

        assertListableUser(pendingSupplier, "hotblackd", null, null, null, "export2chinatester+5@gmail.com",
                ApprovalStatus.UNPROFILED, null, false, false, false, 81111L, 1);
    }

    @Test
    public void findWithSupplierModeratorRoleReturnsAllSupplierModerators() {
        QueryCriteria sortingAndPaginationCriteria = dao.createQueryCriteria(null, null, null, null, null, null,
                Role.SUPPLIER_MODERATOR, false, false, false, null, false, 1, 25);

        List<User> users = dao.find(sortingAndPaginationCriteria);

        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(SUPPLIER_MODERATOR_COUNT));
        assertListableUser(getModeratorOrAdminByUserId(users, 103), "zaphodb", "Zaphod", "Beeblebrox", null,
                "export2chinatester+1@gmail.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.SUPPLIER_MODERATOR),
                false, false, false, 77111L, 1);
        assertListableUser(getModeratorOrAdminByUserId(users, 106), "sbfast", "Slartibart", "Fast", null,
                "export2chinatester+4@gmail.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.SUPPLIER_MODERATOR),
                false, false, false, 80111L, 1);
    }

    @Test
    public void findWithBuyerModeratorRoleReturnsAllBuyerModerators() {
        QueryCriteria sortingAndPaginationCriteria = dao.createQueryCriteria(null, null, null, null, null, null,
                Role.BUYER_MODERATOR, false, false, false, null, false, 1, 25);

        List<User> users = dao.find(sortingAndPaginationCriteria);

        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(BUYER_MODERATOR_COUNT));
        assertListableUser(getModeratorOrAdminByUserId(users, 104), "tmcmillian", "Tricia (AKA Trillion)", "McMillian",
                null, "export2chinatester+2@gmail.com", ApprovalStatus.APPROVED,
                Lists.newArrayList(Role.BUYER_MODERATOR), false, false, false, 78111L, 1);
        assertListableUser(getModeratorOrAdminByUserId(users, 106), "sbfast", "Slartibart", "Fast", null,
                "export2chinatester+4@gmail.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.BUYER_MODERATOR),
                false, false, false, 80111L, 1);
    }

    @Test
    public void findWithTranslatorRoleReturnsAllTranslators() {
        QueryCriteria sortingAndPaginationCriteria = dao.createQueryCriteria(null, null, null, null, null, null,
                Role.TRANSLATOR, false, false, false, null, false, 1, 25);

        List<User> users = dao.find(sortingAndPaginationCriteria);

        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(TRANSLATOR_COUNT));
        assertListableUser(getModeratorOrAdminByUserId(users, 105), "babelf", "Babel", "Fish", null,
                "export2chinatester+3@gmail.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.TRANSLATOR), false,
                false, false, 79111L, 1);
        assertListableUser(getModeratorOrAdminByUserId(users, 106), "sbfast", "Slartibart", "Fast", null,
                "export2chinatester+4@gmail.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.TRANSLATOR), false,
                false, false, 80111L, 1);
    }

    @Test
    public void findWithAdminRoleReturnsAllAdmins() {
        QueryCriteria sortingAndPaginationCriteria = dao.createQueryCriteria(null, null, null, null, null, null,
                Role.ADMIN, false, false, false, null, false, 1, 25);

        List<User> users = dao.find(sortingAndPaginationCriteria);

        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(ADMIN_COUNT));
        assertListableUser(getModeratorOrAdminByUserId(users, 106), "sbfast", "Slartibart", "Fast", null,
                "export2chinatester+4@gmail.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.ADMIN), false,
                false, false, 80111L, 1);
    }

    @Test
    public void findWithBlockedSpecifiedReturnsBlocked() {
        QueryCriteria sortingAndPaginationCriteria = dao.createQueryCriteria(null, null, null, null, null, null,
                Role.BUYER, false, false, Boolean.TRUE, null, false, 1, 25);

        List<User> users = dao.find(sortingAndPaginationCriteria);

        assertThat(users, is(notNullValue()));
        assertThat(users.size(), is(BLOCKED_COUNT));
        assertListableUser(getBuyerOrSupplierByPartyId(users, 1818), "Blocked", "Blocked", "User", "Blocked",
                "user@blocked.com", ApprovalStatus.APPROVED, Lists.newArrayList(Role.BUYER), false, Boolean.TRUE,
                false, 252525L, 1);
    }

    @Test
    public void deleteUserRole() {
        List<GrantedAuthority> authorities = Lists.newArrayList(AuthorityUtils.createAuthorityList((Role.SUPPLIER)));

        dao.deleteUserRole(101L, authorities.get(0));

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM user_login ul LEFT OUTER JOIN user_role ur ON ul.user_id = ur.user_id WHERE ul.username = ?",
                        "fordp");
        assertThat((String) partyAuditStatusRowMap.get("username"), is("fordp"));
        assertThat((String) partyAuditStatusRowMap.get("password"),
                is("$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg"));
        assertThat((Boolean) partyAuditStatusRowMap.get("enabled"), is(true));
        assertThat((String) partyAuditStatusRowMap.get("role_descrpition"), nullValue());
    }

    @Test
    public void deleteUser() {
        assertThat(1, is(dao.deleteUser(101L)));
    }

    private void assertListableUser(User listableUser, String username, String firstName, String lastName,
            String companyName, String email, ApprovalStatus approvalStatus, List<String> roles, boolean locked,
            boolean blocked, boolean presence, Long snapshotId, Integer version) {

        assertThat(listableUser.getParty().getApprovalStatus(), is(approvalStatus));
        assertThat(listableUser.getParty().getVersion(), is(version));
        assertThat(listableUser.isBlocked(), is(blocked));
        assertThat(listableUser.isLocked(), is(locked));
        if (username == null) {
            assertThat(listableUser.getUsername(), is(nullValue()));
        } else {
            assertThat(listableUser.getUsername(), is(username));
        }
        if (firstName == null) {
            assertThat(listableUser.getParty().getContact().getFirstName(), is(nullValue()));
        } else {
            assertThat(listableUser.getParty().getContact().getFirstName(), is(firstName));
        }
        if (lastName == null) {
            assertThat(listableUser.getParty().getContact().getLastName(), is(nullValue()));
        } else {
            assertThat(listableUser.getParty().getContact().getLastName(), is(lastName));
        }
        if (companyName == null) {
            assertThat(listableUser.getParty().getCompany().getEnglishName(), is(nullValue()));
        } else {
            assertThat(listableUser.getParty().getCompany().getEnglishName(), is(companyName));
        }
        if (email == null) {
            assertThat(listableUser.getParty().getContact().getEmailAddress(), is(nullValue()));
        } else {
            assertThat(listableUser.getParty().getContact().getEmailAddress(), is(email));
        }

        if (roles != null) {
            assertThat(listableUser.getRoles().size(), is(roles.size()));
            assertThat(listableUser.getRoles(), is(roles));
        }

        if (snapshotId == null) {
            assertThat(listableUser.getParty().getSnapshotId(), is(nullValue()));
        } else {
            assertThat(listableUser.getParty().getSnapshotId(), is(snapshotId));
        }
    }

    private User getBuyerOrSupplierByPartyId(List<User> list, int partyId) {
        return getListableUser(list, partyId);
    }

    private User getModeratorOrAdminByUserId(List<User> list, int userId) {
        return getListableUser(list, userId);
    }

    private User getListableUser(List<User> list, int id) {
        for (User listableUser : list) {
            if (listableUser.getId() == id) {
                return listableUser;
            }
        }
        return null;
    }

    private void assertFailureCountMatchesExpectedValue(String username, int failuresSinceLastSuccess) {
        Map<String, Object> userRowMap = jdbcTemplate.queryForMap("SELECT * FROM user_login WHERE username = ?",
                username);
        assertThat((Integer) userRowMap.get("failures_since_last_success"), is(failuresSinceLastSuccess));
    }

}
