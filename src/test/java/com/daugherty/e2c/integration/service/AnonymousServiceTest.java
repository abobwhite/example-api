package com.daugherty.e2c.integration.service;

import javax.inject.Inject;
import javax.sql.DataSource;

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

import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonAnonymous;
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
public class AnonymousServiceTest {
    @Inject
    private WebApplicationContext wac;
    @Inject
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    @Inject
    private DataSource dataSource;

    private final ObjectMapper objectMapper = new RootWrappingObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void postAnonymous() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonAnonymous anonymous = new JsonAnonymous();
        anonymous.setFirstName("Mike");
        anonymous.setLastName("Vogt");
        anonymous.setEnglishCompanyName("englishCompnayName");
        anonymous.setEmail("mike.vogt@test.com");
        anonymous.setCountry("United States");
        anonymous.setProvince("St. Louis");
        anonymous.setBusinessTelephoneNumber("+1234567");
        anonymous.setBusinessTypes(Lists.newArrayList("Business Service"));

        byte[] bytes = objectMapper.writeValueAsBytes(anonymous);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/anonymous").contentType(MediaType.APPLICATION_JSON).content(bytes))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.anonymous.firstName").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.anonymous.lastName").value("Vogt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.anonymous.englishCompanyName").value("englishCompnayName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.anonymous.email").value("mike.vogt@test.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.anonymous.country").value("United States"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.anonymous.businessTelephoneNumber").value("+1234567"));
        ;

    }
}
