package com.daugherty.e2c.integration.service;

import java.math.BigDecimal;
import java.util.Date;

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
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonMembership;
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
public class MembershipServiceTest {

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
    public void getMembership() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/memberships/" + 2166667).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.supplierId").value("Y40dgNWM"));

    }

    @Test
    public void updateMembership() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        MembershipLevel level = new MembershipLevel(4L, 4, new BigDecimal(100), 6, 150, 1500, 1200, true, 150, false,
                false, true, 2, true, false, true, false, true, true, true);

        Membership membership = new Membership(2166667L, 21L, level, ApprovalStatus.PAID, 1, new Date(1407301200000L),
                new Date(1399352400000L), new Date(1415253600000L), BigDecimal.ZERO, BigDecimal.ZERO, 21666667L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);

        JsonMembership jsonMembership = new JsonMembership(membership, "Y40dgNWM");

        byte[] bytes = objectMapper.writeValueAsBytes(jsonMembership);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/memberships/" + 2166667L).contentType(MediaType.APPLICATION_JSON)
                        .content(bytes).principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.supplierId").value("Y40dgNWM"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.productCount").value(150))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.hotProductCount").value(150))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.basePrice").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.purchasePrice").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.paymentAmount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.profilePublic").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.supplierMessagingEnabled").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.exportTutorialAccessible").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.membership.verifiableByThirdParty").value(true));

    }
}
