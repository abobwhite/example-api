package com.daugherty.e2c.integration.service;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.RootWrappingObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/3/14
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("local")
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/email-context.xml", "file:src/main/webapp/WEB-INF/spring/security.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserMembershipServiceTest {
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
    public void retrieveUserMemberships() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/userMemberships").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userMemberships").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userMembership[0]").doesNotExist());
    }

    @Test
    public void loadById() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/userMemberships/2366667").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userMemberships").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userMembership").exists());


    }

}
