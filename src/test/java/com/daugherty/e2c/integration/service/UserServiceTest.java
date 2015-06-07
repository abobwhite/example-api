package com.daugherty.e2c.integration.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.UrlBuilder;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.security.PHPassHasher;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonUser;
import com.daugherty.e2c.service.json.JsonUserConfirmation;
import com.daugherty.e2c.service.json.JsonUserReset;
import com.daugherty.e2c.service.json.RootWrappingObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("local")
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/email-context.xml", "file:src/main/webapp/WEB-INF/spring/security.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserServiceTest {
    @Inject
    private WebApplicationContext wac;
    @Inject
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    @Inject
    private DataSource dataSource;

    private final ObjectMapper objectMapper = new RootWrappingObjectMapper();

    private MockMvc mockMvc;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void confirmUserWithCorrectParameters() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUserConfirmation userConfirmation = new JsonUserConfirmation();
        userConfirmation.setConfirmationToken("-1234567890");
        userConfirmation.setUsername("hotblackd");
        userConfirmation.setPassword("disasterarea");
        userConfirmation.setRole(Role.SUPPLIER);

        byte[] bytes = objectMapper.writeValueAsBytes(userConfirmation);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/confirmation").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Map<String, Object> userRowMap = jdbcTemplate.queryForMap("SELECT * FROM user_login WHERE username = ?",
                "hotblackd");
        assertThat((String) userRowMap.get("username"), is("hotblackd"));
        assertThat((String) userRowMap.get("password"), is("$S$DP9AyuIundnCwzPO1qSJBNXPt6r.rNoyVPiwj8ZMQXKf7VQTelCu"));
        assertThat((Boolean) userRowMap.get("enabled"), is(true));
    }

    @Test
    public void confirmUserWithBadConfirmationToken() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUserConfirmation userConfirmation = new JsonUserConfirmation();
        userConfirmation.setConfirmationToken("bad");
        userConfirmation.setUsername("hotblackd");
        userConfirmation.setPassword("disasterarea");
        userConfirmation.setRole(Role.SUPPLIER);

        byte[] bytes = objectMapper.writeValueAsBytes(userConfirmation);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/confirmation").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_login", "username = 'hotblackd'"), is(0));
    }

    @Test
    public void confirmUserWithBadUsername() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUserConfirmation userConfirmation = new JsonUserConfirmation();
        userConfirmation.setConfirmationToken("-1234567890");
        userConfirmation.setUsername("bad");
        userConfirmation.setPassword("disasterarea");
        userConfirmation.setRole(Role.SUPPLIER);

        byte[] bytes = objectMapper.writeValueAsBytes(userConfirmation);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/confirmation").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes).principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_login", "username = 'hotblackd'"), is(0));
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_login", "username = 'bad'"), is(0));
    }

    @Test
    public void confirmUserWithBadPassword() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUserConfirmation userConfirmation = new JsonUserConfirmation();
        userConfirmation.setConfirmationToken("-1234567890");
        userConfirmation.setUsername("hotblackd");
        userConfirmation.setPassword("bad");
        userConfirmation.setRole(Role.SUPPLIER);

        byte[] bytes = objectMapper.writeValueAsBytes(userConfirmation);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/confirmation").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_login", "username = 'hotblackd'"), is(0));
    }

    @Test
    public void forgotPasswordUsernameNotFoundValidationError() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUserReset user = new JsonUserReset();
        user.setUsername("MVOGT");

        byte[] bytes = objectMapper.writeValueAsBytes(user);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/forgotPassword").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(bytes).locale(new Locale("es")))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void forgotPasswordSendsEmail() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUserReset user = new JsonUserReset();
        user.setUsername("fordp");
        user.setEmailAddress("prefect@megadodo.com");

        byte[] bytes = objectMapper.writeValueAsBytes(user);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/forgotPassword").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(bytes).locale(new Locale("es")))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void resetPasswordSendsEmail() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);
        JsonUserReset user = new JsonUserReset();
        user.setOldPassword("$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg");
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirmation("newPassword");

        byte[] bytes = objectMapper.writeValueAsBytes(user);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/reset").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(bytes).locale(new Locale("es")))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void resetPassword() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUserReset user = new JsonUserReset();
        user.setOldPassword("$S$DwyerOrphbdKB7O4AxIBpSHc6NIfHvaxGLI9tc4YpcEKznarvoxg");
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirmation("newPassword");

        byte[] bytes = objectMapper.writeValueAsBytes(user);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/reset").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Map<String, Object> userRowMap = jdbcTemplate.queryForMap("SELECT * FROM user_login WHERE username = ?",
                "fordp");
        assertThat((String) userRowMap.get("username"), is("fordp"));
        String persistedPassword = (String) userRowMap.get("password");
        assertThat(new PHPassHasher().isMatch("newPassword", persistedPassword), is(true));
        assertThat((Boolean) userRowMap.get("enabled"), is(true));
    }

    @Test
    public void changePasswordSendsEmail() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUserReset user = new JsonUserReset();
        user.setUsername("fordp");
        user.setOldPassword("hoopyfrood");
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirmation("newPassword");

        byte[] bytes = objectMapper.writeValueAsBytes(user);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/changePassword").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(bytes).locale(new Locale("es"))
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Map<String, Object> userRowMap = jdbcTemplate.queryForMap("SELECT * FROM user_login WHERE username = ?",
                "fordp");
        assertThat((String) userRowMap.get("username"), is("fordp"));
        String persistedPassword = (String) userRowMap.get("password");
        assertThat(new PHPassHasher().isMatch("newPassword", persistedPassword), is(true));
        assertThat((Boolean) userRowMap.get("enabled"), is(true));
    }

    @Test
    public void unlockUser() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUser user = new JsonUser();
        user.setUsername("pendingApprovalBuyer");

        byte[] bytes = objectMapper.writeValueAsBytes(user);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/unlock").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Map<String, Object> userRowMap = jdbcTemplate.queryForMap("SELECT * FROM user_login WHERE username = ?",
                "pendingApprovalBuyer");
        assertThat((String) userRowMap.get("username"), is("pendingApprovalBuyer"));
        assertThat((Integer) userRowMap.get("failures_since_last_success"), is(0));
    }

    @Test
    public void blockUser() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonUser user = new JsonUser();
        user.setUsername("fordp");

        byte[] bytes = objectMapper.writeValueAsBytes(user);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/block").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Map<String, Object> userRowMap = jdbcTemplate.queryForMap("SELECT * FROM user_login WHERE username = ?",
                "fordp");
        assertThat((String) userRowMap.get("username"), is("fordp"));
        assertThat((Boolean) userRowMap.get("enabled"), is(false));
    }

    @Test
    public void retrieveListableUsersWithoutQueryStringParameters() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500));
    }

    @Test
    public void retrieveListableUsersWithRestrictingQueryStringParameter() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users?username_prefix=ford").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.users").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].username").value("fordp2"));
    }

    @Test
    public void retrieveListableUsersWithRestrictingQueryStringParameterForPendingUser() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users?approval_status=Unprofiled").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.users").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].username").value("hotblackd"));
    }

    @Test
    public void retrieveListableUsersWithPaginationQueryStringParameterBeyondBoundsOfDataset() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users?start_item=26&role=ROLE_BUYER").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.users").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0]").doesNotExist());
    }

    @Test
    public void retrieveUsersByUserId() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/106").accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(106))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value("sbfast"));
    }

    @Test
    public void switchUser() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        DocumentUrlFactory documentUrlFactory = new DocumentUrlFactory(
                new UrlBuilder("buyer.e2c", "global.e2c", "", ""), "/test/");

        Contact contact = new Contact("first", "last", "country", null, "email@address.net", null, null, new Date());
        Company company = new Company("company name", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(21L, "Y40dgNWM", contact, company, ApprovalStatus.APPROVED, PartyType.SUPPLIER, 1, null);
        User user = new User(101L, "fordp", "password", Lists.newArrayList(Role.SUPPLIER), 0, true, false, true, party,
                true, new Date());

        JsonUser jsonUser = new JsonUser(user, documentUrlFactory, Locale.ENGLISH);

        byte[] bytes = objectMapper.writeValueAsBytes(jsonUser);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/101/switch").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(bytes).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value("fordp"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.party.partyType").value("Buyer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.role[0]").value("ROLE_BUYER"));
    }

}
