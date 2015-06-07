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
import com.daugherty.e2c.service.json.JsonProductTranslation;
import com.daugherty.e2c.service.json.JsonProfileTranslation;
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
public class SupplierTranslationServiceTest {

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
    public void retrieveSupplierTranslationsWithoutQueryStringParameters() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.TRANSLATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/supplierTranslations").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierTranslations").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierTranslations[0].title").value("Wyrd Fates"));
    }

    @Test
    public void retrieveSupplierTranslationsWithRestrictingQueryStringParameter() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.TRANSLATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/supplierTranslations?title=Wyrd").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierTranslations").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierTranslations[0].title").value("Wyrd Fates"));
    }

    @Test
    public void saveProfileTranslation() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.TRANSLATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonProfileTranslation translation = new JsonProfileTranslation();
        translation.setCompanyDescription("What was, what is, and what is becoming");
        translation.setCompanyDescriptionTranslation("translated description");

        byte[] bytes = objectMapper.writeValueAsBytes(translation);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/profileTranslations/xo0wK0qL").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes).principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void saveProductTranslation() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.TRANSLATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonProductTranslation translation = new JsonProductTranslation();
        translation.setCountry("England");
        translation.setFobPort("port");
        translation.setFobPrice("123.0");
        translation.setModelNumber("model number");
        translation.setLeadTime("lead");
        translation.setProductName("name");
        translation.setProductNameTranslation("translated name");
        translation.setProductDescription("description");
        translation.setProductDescriptionTranslation("translated description");
        translation.setKeySpecification("key specification");
        translation.setKeySpecificationsTranslation("translated key specification");
        translation.setKeyWords("key words");
        translation.setKeyWordsTranslations("translated words");
        translation.setMetaTags("meta tags");
        translation.setMetaTags("translated tags");

        byte[] bytes = objectMapper.writeValueAsBytes(translation);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/productTranslations/9991").contentType(MediaType.APPLICATION_JSON)
                        .content(bytes).principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void approveTranslation() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.TRANSLATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/supplierTranslations/xo0wK0qL/approved").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void sendToModerator() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.TRANSLATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/supplierTranslations/xo0wK0qL/sendToModerator").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getProfileTranslation() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.TRANSLATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/profileTranslations/xo0wK0qL").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.profileTranslation.companyDescription").value(
                                "What was, what is, and what is becoming"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.profileTranslation.companyDescriptionTranslation").value(
                                "translated description"));
    }

    @Test
    public void getProductTranslation() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.TRANSLATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/productTranslations/9991").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.country").value("England"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.modelNumber").value("model number"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productTranslation.paymentTermsEnglish")
                                .value("payment term"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productTranslation.paymentTermsChinese").value(
                                "latest translated payment term"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.minimumOrder").value("10000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.fobPort").value("freight on board"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.fobPrice").value("203.42"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.leadTime").value("lead time"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.productName").value("product name"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productTranslation.productNameTranslation").value(
                                "latest translated product name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.keyWords").value("these are key words"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productTranslation.keyWordsTranslation").value(
                                "latest key words translations"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTranslation.metaTags").value("these are meta tags"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.productTranslation.metaTagsTranslation").value(
                                "latest meta tags translations"));
    }
}
