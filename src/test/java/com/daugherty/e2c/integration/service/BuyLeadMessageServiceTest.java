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
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonBuyLead;
import com.daugherty.e2c.service.json.JsonBuyLeadMessage;
import com.daugherty.e2c.service.json.JsonInteraction;
import com.daugherty.e2c.service.json.JsonParty;
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
public class BuyLeadMessageServiceTest {

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
    public void postMessage() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonParty receiver = new JsonParty();
        receiver.setId("p2VGOVMw");
        receiver.setPartyType(PartyType.BUYER.toString());
        receiver.setApprovalStatus(ApprovalStatus.APPROVED.getName());

        JsonBuyLead jsonBuyLead = new JsonBuyLead();
        jsonBuyLead.setId(2L);
        jsonBuyLead.setRequester(receiver);
        jsonBuyLead.setCategory(2807L);

        JsonBuyLeadMessage message = new JsonBuyLeadMessage();
        message.setSubject("Inquiry about Passenger Transportation Services");
        JsonParty originator = new JsonParty();
        originator.setId("Y40dgNWM");
        originator.setPartyType(PartyType.SUPPLIER.toString());
        originator.setApprovalStatus(ApprovalStatus.APPROVED.getName());
        message.setOtherParty(originator);
        message.setLead(jsonBuyLead);

        JsonInteraction interaction = new JsonInteraction();
        interaction.setBody("I need some transportation?");
        interaction.setSender(originator);

        interaction.setReceiver(receiver);
        message.setInteractions(Lists.newArrayList(interaction));

        byte[] bytes = objectMapper.writeValueAsBytes(message);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/buyLeadMessages").contentType(MediaType.APPLICATION_JSON).content(bytes))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void retrieveSpecificMessage() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(21L, "Y40dgNWM", AuthorityUtils.createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/buyLeadMessages/kP2BrOOP").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.buyLeadMessage.subject").value(
                                "Inquiry about Passenger Transportation Services"));
    }
}
