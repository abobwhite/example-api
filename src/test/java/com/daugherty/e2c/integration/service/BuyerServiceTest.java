package com.daugherty.e2c.integration.service;

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
import com.daugherty.e2c.service.json.JsonBuyer;
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
public class BuyerServiceTest {
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
    public void getBuyer() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        final String publicBuyerId = "pBVQJNb4";

        mockMvc.perform(
                MockMvcRequestBuilders.get("/buyers/" + publicBuyerId).accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.id").value("pBVQJNb4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.title").value("Mr."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.partyType").value("Buyer"));

    }

    @Test
    public void postBuyer() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonBuyer buyer = new JsonBuyer();
        buyer.setUsername("JGVOGT");
        buyer.setPassword("talldude");
        buyer.setPasswordConfirmation("talldude");
        buyer.setFirstName("Mike");
        buyer.setLastName("Vogt");
        buyer.setCountry("Albania");
        buyer.setEmail("mike.vogt@test.com");

        byte[] bytes = objectMapper.writeValueAsBytes(buyer);

        mockMvc.perform(MockMvcRequestBuilders.post("/buyers").contentType(MediaType.APPLICATION_JSON).content(bytes))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.partyType").value("Buyer"));

    }

    @Test
    public void putBuyerThrowsStatus422WhenValidaionFails() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonBuyer buyer = new JsonBuyer();
        buyer.setId("pBVQJNb4");
        buyer.setVersion(1);
        buyer.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        buyer.setFirstName("Mike");
        buyer.setLastName("Vogt");
        buyer.setProvince("St. Louis");
        buyer.setEmail("mike.vogt@test.com");
        buyer.setTitle("Mrs.");

        byte[] bytes = objectMapper.writeValueAsBytes(buyer);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/buyers/" + 22L).contentType(MediaType.APPLICATION_JSON).content(bytes)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void putBuyerWithStatusOfIncompleteUpdateStatusToPendingApproval() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonBuyer buyer = new JsonBuyer();
        buyer.setId("pBVQJNb4");
        buyer.setVersion(1);
        buyer.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        buyer.setTitle("Mr.");
        buyer.setFirstName("Mike");
        buyer.setLastName("Vogt");
        buyer.setCountry("United States");
        buyer.setProvince("St. Louis");
        buyer.setEmail("mike.vogt@test.com");
        buyer.setChineseCompanyName("The Awesome Chinese Company");
        buyer.setEnglishCompanyName("The Awesome English Company");
        buyer.setDescription("The Awesome Company Description");
        buyer.setBusinessTelephoneNumber("+321987654321");
        buyer.setBusinessTypes(Lists.newArrayList("Business Service"));

        byte[] bytes = objectMapper.writeValueAsBytes(buyer);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/buyers/" + buyer.getId()).contentType(MediaType.APPLICATION_JSON).content(bytes)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.id").value("pBVQJNb4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.title").value("Mr."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.firstName").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.lastName").value("Vogt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.businessTelephoneNumber").value("+321987654321"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.partyType").value("Buyer"));
    }

}
