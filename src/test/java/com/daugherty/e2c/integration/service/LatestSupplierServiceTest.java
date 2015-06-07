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
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonLatestSupplier;
import com.daugherty.e2c.service.json.JsonSupplier;
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
public class LatestSupplierServiceTest {
    
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
    public void getSupplier() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        final String publicSupplierId = "Y40dgNWM";

        mockMvc.perform(
                MockMvcRequestBuilders.get("/latestSuppliers/" + publicSupplierId).accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestSupplier.id").value("Y40dgNWM"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestSupplier.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestSupplier.partyType").value("Supplier"));
    }
    
    @Test
    public void postSupplier() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonLatestSupplier supplier = new JsonLatestSupplier();
        supplier.setUsername("JGVOGT");
        supplier.setPassword("talldude");
        supplier.setPasswordConfirmation("talldude");
        supplier.setFirstName("Mike");
        supplier.setLastName("Vogt");
        supplier.setCountry("USA");
        supplier.setEmail("mike.vogt@test.com");

        byte[] bytes = objectMapper.writeValueAsBytes(supplier);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/latestSuppliers").contentType(MediaType.APPLICATION_JSON).content(bytes))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void putSupplierThrowsStatus422WhenValidaionFails() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonLatestSupplier supplier = new JsonLatestSupplier();
        supplier.setId("Y40dgNWM");
        supplier.setVersion(1);
        supplier.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        supplier.setFirstName("Mike");
        supplier.setLastName("Vogt");
        supplier.setCountry("USA");
        supplier.setEmail("mike.vogt@test.com");

        byte[] bytes = objectMapper.writeValueAsBytes(supplier);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/latestSuppliers/" + supplier.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(bytes).principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void putSupplierWithStatusOfIncompleteUpdateStatusToPendingApproval() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonLatestSupplier supplier = new JsonLatestSupplier();
        supplier.setId("Y40dgNWM");
        supplier.setVersion(1);
        supplier.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        supplier.setCountry("United States");
        supplier.setFirstName("Mike");
        supplier.setLastName("Vogt");
        supplier.setEmail("mike.vogt@test.com");
        supplier.setGender(Gender.FEMALE.toString());
        supplier.setEnglishCompanyName("The Awesome English Company");
        supplier.setDescription("The Awesome Company Description");
        supplier.setBusinessTypes(Lists.newArrayList("Business Service"));
        supplier.setBusinessTelephoneNumber("+1234567");

        byte[] bytes = objectMapper.writeValueAsBytes(supplier);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/latestSuppliers/" + supplier.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(bytes).principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestSupplier.id").value(supplier.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestSupplier.firstName").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestSupplier.lastName").value("Vogt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestSupplier.businessTelephoneNumber").value("+1234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestSupplier.partyType").value("Supplier"));
    }

}
