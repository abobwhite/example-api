package com.daugherty.e2c.integration.service;

import java.util.Locale;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
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

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonEmployee;
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
public class EmployeeServiceTest {
    @Inject
    private WebApplicationContext wac;
    @Inject
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;

    private final ObjectMapper objectMapper = new RootWrappingObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void postEmployee() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonEmployee employee = new JsonEmployee();
        employee.setFirstName("Mike");
        employee.setLastName("Vogt");
        employee.setEmail("mike.vogt@test.com");
        employee.setUsername("mvogtu");
        employee.setPassword("password");
        employee.setRoles(Lists.newArrayList(Role.BUYER_MODERATOR));

        byte[] bytes = objectMapper.writeValueAsBytes(employee);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/employees").contentType(MediaType.APPLICATION_JSON).content(bytes))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.firstName").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.lastName").value("Vogt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.email").value("mike.vogt@test.com"));
        ;

    }

    @Test
    public void putEmployee() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonEmployee employee = new JsonEmployee();
        employee.setFirstName("Zaphod Updated");
        employee.setLastName("Beeblebrox Updated");
        employee.setEmail("zaphod.beeblebrox@test.com");
        employee.setApprovalStatus(ApprovalStatus.APPROVED.getName());
        employee.setVersion(1);
        employee.setUsername("zaphodb");
        employee.setUserId(103L);
        employee.setRoles(Lists.newArrayList(Role.BUYER_MODERATOR));

        byte[] bytes = objectMapper.writeValueAsBytes(employee);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/employees/94Pa430l").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.firstName").value("Zaphod Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.lastName").value("Beeblebrox Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.email").value("zaphod.beeblebrox@test.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.version").value(2));
        ;

    }

    @Test
    public void retrieveEmployeesByUsername() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees?username=sbfast").locale(Locale.ENGLISH))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].id").value("KyVkyZ0r"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].firstName").value("Slartibart"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].lastName").value("Fast"));
    }

    @Test
    public void retrieveEmployeesById() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/KyVkyZ0r").locale(Locale.ENGLISH))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.firstName").value("Slartibart"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.lastName").value("Fast"));
    }
}
