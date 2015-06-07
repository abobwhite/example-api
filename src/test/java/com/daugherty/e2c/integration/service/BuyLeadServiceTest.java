package com.daugherty.e2c.integration.service;

import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import org.joda.time.DateTime;
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

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonBuyLead;
import com.daugherty.e2c.service.json.RootWrappingObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("local")
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/email-context.xml", "file:src/main/webapp/WEB-INF/spring/security.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class BuyLeadServiceTest {
    @Inject
    private WebApplicationContext wac;
    @Inject
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    @Inject
    private DocumentUrlFactory documentUrlFactory;

    private final ObjectMapper objectMapper = new RootWrappingObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void postBuyLead() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonBuyLead buyLead = new JsonBuyLead(new BuyLead(new Party(22L, "pBVQJNb4"), new ProductCategory(2810L), new Date(),
                new Date()), documentUrlFactory, Locale.ENGLISH);

        byte[] bytes = objectMapper.writeValueAsBytes(buyLead);

        mockMvc.perform(MockMvcRequestBuilders.post("/buyLeads").contentType(MediaType.APPLICATION_JSON).content(bytes))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLead.requester.id").value("pBVQJNb4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLead.category").value(2810));

    }

    @Test
    public void findBuyLeadsByEmail() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/buyLeads?emailAddress=hue.jinko@gmail.com")
                        .accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].requester.id").value("p2VGOVMw"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].category").value(2807));
    }

    @Test
    public void findBuyLeadsByProvince() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/buyLeads?province=testProvince").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].requester.id").value("Qm0XBVaZ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].category").value(2810));
    }

    @Test
    public void findBuyLeadsByEffectiveSince() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        Date effectiveSince = new DateTime().minusMonths(1).minusDays(2).toDate();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/buyLeads?effectiveSince=" + effectiveSince.getTime())
                        .accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].requester.id").value("p2VGOVMw"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].category").value(2807));
    }

    @Test
    public void findAllBuyLeads() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/buyLeads").accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].requester.id").value("Qm0XBVaZ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[0].category").value(2810))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[1].requester.id").value("p2VGOVMw"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyLeads[1].category").value(2807));
    }
}
