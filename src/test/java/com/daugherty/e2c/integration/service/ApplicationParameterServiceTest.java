package com.daugherty.e2c.integration.service;

import java.util.Locale;

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
public class ApplicationParameterServiceTest {

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
    public void retrieveApplicationParameters() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/parameters/search/1").accept(MediaType.APPLICATION_JSON)
                        .locale(Locale.ENGLISH))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.nameAttributeRelevanceWeight").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.keywordAttributeRelevanceWeight").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.categoryAttributeRelevanceWeight").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.descriptionAttributeRelevanceWeight").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.exactSearchTermRelevanceDegree").value(1.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.exactWordSearchTermRelevanceDegree").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.partialWordSearchTermRelevanceDegree").value(1.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.relevanceBuckets").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.relevanceFloor").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.chineseNameAttributeRelevanceWeight").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.chineseKeywordAttributeRelevanceWeight").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.chineseCategoryAttributeRelevanceWeight").value(7))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$search.chineseDescriptionAttributeRelevanceWeight").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.chineseExactSearchTermRelevanceDegree").value(1.5))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$search.chineseExactWordSearchTermRelevanceDegree").value(2.0))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$search.chinesePartialWordSearchTermRelevanceDegree")
                                .value(1.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.chineseRelevanceBuckets").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$search.chineseRelevanceFloor").value(2));
    }
}
