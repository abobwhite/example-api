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
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonBuyer;
import com.daugherty.e2c.service.json.JsonInquiry;
import com.daugherty.e2c.service.json.JsonProductIds;
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
public class InquiryServiceTest {

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
    public void stageAndUnstageProduct() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(22L, "pBVQJNb4", AuthorityUtils.createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        // Buyer initially has no products in Inquiry Basket
        mockMvc.perform(
                MockMvcRequestBuilders.get("/inquiry").accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.id").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds[0]").doesNotExist());

        // Buyer adds product to basket
        JsonProductIds productIds = new JsonProductIds();
        productIds.add(41L);
        byte[] bytes = objectMapper.writeValueAsBytes(productIds);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/inquiry/products?senderId=pBVQJNb4").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Buyer now has one product in basket
        mockMvc.perform(
                MockMvcRequestBuilders.get("/inquiry").accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.id").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds[0]").value(41));

        // Buyer removes product from basket
        mockMvc.perform(MockMvcRequestBuilders.delete("/inquiry/products/41?senderId=pBVQJNb4"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

        // Buyer again has no products in basket
        mockMvc.perform(
                MockMvcRequestBuilders.get("/inquiry").accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.id").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds[0]").doesNotExist());
    }

    @Test
    public void submitInquiryBasket() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(22L, "pBVQJNb4", AuthorityUtils.createAuthorityList(Role.BUYER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        // Buyer adds product to basket
        JsonProductIds productIds = new JsonProductIds();
        productIds.add(41L);
        byte[] addProductBytes = objectMapper.writeValueAsBytes(productIds);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/inquiry/products?senderId=pBVQJNb4").contentType(MediaType.APPLICATION_JSON)
                        .content(addProductBytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Buyer now has one product in basket
        mockMvc.perform(
                MockMvcRequestBuilders.get("/inquiry").accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.id").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds[0]").value(41));

        // Buyer submits basket, after which it is cleared
        JsonInquiry inquiry = new JsonInquiry();
        JsonBuyer originator = new JsonBuyer();
        originator.setId("pBVQJNb4");
        originator.setPartyType(PartyType.BUYER.toString());
        originator.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        inquiry.setOriginator(originator);
        inquiry.setProductIds(Lists.newArrayList(41L));
        inquiry.setSubject("subject");
        inquiry.setBody("body");
        inquiry.setMessageTags(Lists.newArrayList(MessageTag.SPECIFICATIONS.getName()));
        byte[] inquiryBytes = objectMapper.writeValueAsBytes(inquiry);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/inquiry/submissions").contentType(MediaType.APPLICATION_JSON)
                        .content(inquiryBytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.id").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds[0]").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.subject").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.body").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.messageTags").doesNotExist());
    }

    @Test
    public void submitAnonymousInquiryBasket() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        // Anonymous adds product to basket
        JsonProductIds productIds = new JsonProductIds();
        productIds.add(41L);
        byte[] addProductBytes = objectMapper.writeValueAsBytes(productIds);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/inquiry/products?senderId=42").contentType(MediaType.APPLICATION_JSON)
                        .content(addProductBytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Anonymous submits basket, after which it is cleared
        JsonInquiry inquiry = new JsonInquiry();
        JsonBuyer originator = new JsonBuyer();
        originator.setId("jKNz4P4q");
        originator.setPartyType(PartyType.ANONYMOUS.toString());
        originator.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        inquiry.setOriginator(originator);
        inquiry.setProductIds(Lists.newArrayList(41L));
        inquiry.setSubject("subject");
        inquiry.setBody("body");
        inquiry.setMessageTags(Lists.newArrayList(MessageTag.SPECIFICATIONS.getName()));
        byte[] inquiryBytes = objectMapper.writeValueAsBytes(inquiry);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/inquiry/submissions").contentType(MediaType.APPLICATION_JSON)
                        .content(inquiryBytes)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.id").value(42))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.productIds[0]").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.subject").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.body").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.messageTags").doesNotExist());
    }

    @Test
    public void retrieveInquiriesNeedingApproval() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(null, null, AuthorityUtils.createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/inquiries?disapproved=false").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries[0].id").value(221))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries[0].productIds").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries[0].productIds[0]").value(41))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries[0].subject").value("Arthur Spam List"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.inquiries[0].body").value(
                                "I need to survive out there in the galaxy!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries[0].messageTags").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.inquiries[0].messageTags[0]").value(
                                MessageTag.COMPANY_OVERVIEW.getName()));
    }

    @Test
    public void retrieveInquiry() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(null, null, AuthorityUtils.createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/inquiries/221").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.id").value(221))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiry.subject").value("Arthur Spam List"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.inquiry.originator.englishCompanyName").value(
                                "East India Tea Company"));
    }

    @Test
    public void approveInquiry() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(null, null, AuthorityUtils.createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/inquiries/221/approvals")).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Pending inquiry no longer exists
        mockMvc.perform(
                MockMvcRequestBuilders.get("/inquiries?disapproved=false").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries[0]").doesNotExist());
    }

    @Test
    public void disapproveInquiry() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(null, null, AuthorityUtils.createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/inquiries/221/disapprovals"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

        // Pending inquiry no longer exists
        mockMvc.perform(
                MockMvcRequestBuilders.get("/inquiries?disapproved=false").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inquiries[0]").doesNotExist());
    }

}
