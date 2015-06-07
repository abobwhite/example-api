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
import com.daugherty.e2c.service.json.JsonInteraction;
import com.daugherty.e2c.service.json.JsonPublicId;
import com.daugherty.e2c.service.json.JsonSupplier;
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
public class MessageServiceTest {

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
    public void retrieveInboxMessages() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(21L, "Y40dgNWM",
                AuthorityUtils.createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/messages?receiverId=Y40dgNWM").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.messages[0].subject").value(
                                "Inquiry about Black Beans (ABOB)"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.messages[0].mostRecentInteractionTime").value(1385160300000L));
    }

    @Test
    public void retrieveSentMessages() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(21L, "Y40dgNWM",
                AuthorityUtils.createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/messages?senderId=Y40dgNWM").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.messages[0].subject").value(
                                "Inquiry about Black Beans (ABOB)"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.messages[0].mostRecentInteractionTime").value(1385160180000L));
    }

    @Test
    public void retrieveInteractionsForMessage() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/messages/KNzl759P/interactions").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.interactions").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.interactions[2].body").value(
                                "I think Trillian ate them all with her last Pan-Galactic Gargle Blaster"));
    }

    @Test
    public void retrieveBuyerMessageSummary() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(22L, "pBVQJNb4",
                AuthorityUtils.createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/buyers/pBVQJNb4/messageSummary").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageSummary.unread").value(0));
    }

    @Test
    public void retrieveSupplierMessageSummary() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(21L, "Y40dgNWM",
                AuthorityUtils.createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/suppliers/Y40dgNWM/messageSummary").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageSummary.unread").value(2));
    }

    @Test
    public void flagAndUnflagMessage() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(21L, "Y40dgNWM",
                AuthorityUtils.createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        // Supplier initially has no flagged messages
        mockMvc.perform(
                MockMvcRequestBuilders.get("/messages?receiverId=Y40dgNWM&flagged=true").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages[0]").doesNotExist());

        // Supplier flags message
        JsonPublicId id = new JsonPublicId("Y40dgNWM");
        byte[] bytes = objectMapper.writeValueAsBytes(id);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/messages/KNzl759P/flags").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Supplier now has one flagged message
        mockMvc.perform(
                MockMvcRequestBuilders.get("/messages?receiverId=Y40dgNWM&flagged=true").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.messages[0].subject").value(
                                "Inquiry about Black Beans (ABOB)"));

        // Supplier unflags message
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/KNzl759P/flags/Y40dgNWM"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

        // Supplier again has no flagged messages
        mockMvc.perform(
                MockMvcRequestBuilders.get("/messages?receiverId=Y40dgNWM&flagged=true").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messages[0]").doesNotExist());
    }

    @Test
    public void replyToMessage() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonInteraction interaction = new JsonInteraction();
        interaction.setBody("Just have a Pan-Galactic Gargle Blaster instead!");
        JsonSupplier jsonSender = new JsonSupplier();
        jsonSender.setId("Y40dgNWM");
        jsonSender.setPartyType(PartyType.SUPPLIER.toString());
        jsonSender.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        interaction.setSender(jsonSender);
        JsonSupplier jsonReceiver = new JsonSupplier();
        jsonReceiver.setId("pBVQJNb4");
        jsonReceiver.setPartyType(PartyType.BUYER.toString());
        jsonReceiver.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        interaction.setReceiver(jsonReceiver);

        byte[] bytes = objectMapper.writeValueAsBytes(interaction);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/messages/KNzl759P/interactions").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void replyToMessageWithNoSender() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonInteraction interaction = new JsonInteraction();
        interaction.setBody("Just have a Pan-Galactic Gargle Blaster instead!");
        JsonSupplier jsonReceiver = new JsonSupplier();
        jsonReceiver.setId("pBVQJNb4");
        jsonReceiver.setPartyType(PartyType.BUYER.toString());
        jsonReceiver.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        interaction.setReceiver(jsonReceiver);

        byte[] bytes = objectMapper.writeValueAsBytes(interaction);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/messages/KNzl759P/interactions")
                        .contentType(MediaType.APPLICATION_JSON).content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void markInteractionAsRead() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/interactions/2221413/reads").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
