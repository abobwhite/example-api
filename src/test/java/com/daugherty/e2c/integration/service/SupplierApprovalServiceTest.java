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

import com.daugherty.e2c.security.Role;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("local")
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/email-context.xml", "file:src/main/webapp/WEB-INF/spring/security.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class SupplierApprovalServiceTest {

    @Inject
    private WebApplicationContext wac;
    @Inject
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void retrieveSupplierApprovalsWithoutQueryStringParameters() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/supplierApprovals").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierApprovals").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierApprovals[0].title").value("Vogon Battlefleet"));
    }

    @Test
    public void retrieveSupplierApprovalsWithRestrictingQueryStringParameter() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/supplierApprovals?title_prefix=Vogon").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierApprovals").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierApprovals[0].title").value("Vogon Battlefleet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierApprovals[0].supplier.id").value("MjVgGVEn"));
    }

    @Test
    public void retrieveSupplierApprovalsWithPaginationQueryStringParameterBeyondBoundsOfDataset() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/supplierApprovals?start_item=26").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierApprovals").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierApprovals[0]").doesNotExist());
    }

    @Test
    public void submitForApproval() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/supplierApprovals/Jp0bwPjG/submitted").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deferApproval() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/supplierApprovals/MjVgGVEn/deferred").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void disapprove() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/supplierApprovals/MjVgGVEn/disapproved").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void requestTranslation() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/supplierApprovals/MjVgGVEn/translations").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
