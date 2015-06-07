package com.daugherty.e2c.integration.service;

import java.math.BigDecimal;

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

import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonDiscount;
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
public class DiscountServiceTest {

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
    public void getDiscount() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/discounts/" + 1).accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount.code").value("DOLLAR1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount.type").value("Dollar"));
    }

    @Test
    public void findDiscountByCodeNameSorted() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/discounts?sort_by=code&sort_desc=true").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].id").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].code").value("PERCENT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].type").value("Percent"));
    }

    @Test
    public void findDiscountByCodeName() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/discounts?discountCode=DOLLAR1").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].code").value("DOLLAR1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].type").value("Dollar"));
    }

    @Test
    public void findDiscountByTypeName() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/discounts?discountType=Dollar").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].code").value("DOLLAR1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].type").value("Dollar"));
    }

    @Test
    public void findDiscountBySubscriptionTypeName() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/discounts?subscriptionType=Renew").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].code").value("DOLLAR1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts[0].type").value("Dollar"));
    }

    @Test
    public void postDiscount() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonDiscount discount = new JsonDiscount(null, "code", "description", "Dollar", BigDecimal.TEN, null, 
                Boolean.TRUE, Boolean.FALSE, new DateTime(2014, 2, 1, 0, 0).toDateMidnight().toDate(), new DateTime(
                        2014, 2, 1, 0, 0).toDateMidnight().toDate(), Lists.newArrayList(1),
                Lists.newArrayList(SubscriptionType.NEW));

        byte[] bytes = objectMapper.writeValueAsBytes(discount);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/discounts").contentType(MediaType.APPLICATION_JSON).content(bytes))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateDiscount() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonDiscount discount = new JsonDiscount(2L, "code", "description", "Dollar", BigDecimal.TEN, null, 
                Boolean.TRUE, Boolean.FALSE, new DateTime(2014, 2, 1, 0, 0).toDateMidnight().toDate(), new DateTime(
                        2014, 2, 1, 0, 0).toDateMidnight().toDate(), Lists.newArrayList(1), Lists.newArrayList(
                        SubscriptionType.NEW, SubscriptionType.RENEW));

        byte[] bytes = objectMapper.writeValueAsBytes(discount);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/discounts/" + 1).contentType(MediaType.APPLICATION_JSON).content(bytes))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
