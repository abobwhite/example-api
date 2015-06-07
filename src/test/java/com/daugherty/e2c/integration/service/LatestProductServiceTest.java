package com.daugherty.e2c.integration.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
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
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonLatestProduct;
import com.daugherty.e2c.service.json.JsonProductImage;
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
public class LatestProductServiceTest {

    @Inject
    private WebApplicationContext wac;
    @Inject
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    @Inject
    private DataSource dataSource;

    private final ObjectMapper objectMapper = new RootWrappingObjectMapper();

    private MockMvc mockMvc;

    private JdbcTemplate jdbcTemplate;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void retrieveSupplierProductsWithoutQueryStringParameters() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/suppliers/Y40dgNWM/latestProducts").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestProducts").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestProducts[0].name").value("Black Beans (ABOB)"));
    }

    @Test
    public void retrieveProductWhenIsLatestIsTrue() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/latestProducts/51").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestProduct").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.latestProduct.name").value("Pinto Beans"));
    }

    @Test
    public void retrieveProductWhenNotFoundThrowsEmptyDataResultException() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/latestProducts/1200").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void retrieveMembershipProductSummary() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/memberships/2166667/productSummary").accept(MediaType.APPLICATION_JSON)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.id").value(2166667))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.published").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.pendingApproval").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.pendingTranslation").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.waitingForInformation").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.unpublished").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.hotProducts").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.disapproved").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSummary.productsRemaining").value(24));
    }

    @Test
    public void createValidProductReturns200Status() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonSupplier jsonSupplier = new JsonSupplier();
        jsonSupplier.setId("Y40dgNWM");

        JsonLatestProduct product = new JsonLatestProduct();
        product.setSupplier(jsonSupplier);
        product.setName("The Hitch-Hikers Guide to the Galaxy");
        product.setDescription("Don't Panic!");
        product.setMinimumOrder("1");
        product.setCountry("Ursa Minor Beta");
        product.setCategories(Lists.newArrayList(2801L));

        byte[] bytes = objectMapper.writeValueAsBytes(product);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/latestProducts").contentType(MediaType.APPLICATION_JSON).content(bytes)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createInvalidProductReturns422Status() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonLatestProduct product = new JsonLatestProduct();

        byte[] bytes = objectMapper.writeValueAsBytes(product);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/latestProducts").contentType(MediaType.APPLICATION_JSON).content(bytes)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void updateValidProductReturns200Status() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonSupplier jsonSupplier = new JsonSupplier();
        jsonSupplier.setId("Y40dgNWM");

        JsonLatestProduct product = new JsonLatestProduct();
        product.setId(41L);
        product.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        product.setVersion(1);
        product.setSupplier(jsonSupplier);
        product.setName("The Hitch-Hikers Guide to the Galaxy");
        product.setDescription("Don't Panic!");
        product.setMinimumOrder("1");
        product.setCountry("Ursa Minor Beta");
        product.setCategories(Lists.newArrayList(2801L));
        JsonProductImage image = new JsonProductImage();
        image.setImageUrl("//export-to-china/e2c-api/documents/productImageUrl");
        image.setPrimary(true);
        product.setImages(Lists.newArrayList(image));

        byte[] bytes = objectMapper.writeValueAsBytes(product);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/latestProducts/41").contentType(MediaType.APPLICATION_JSON).content(bytes)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateInvalidProductReturns422Status() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        JsonSupplier jsonSupplier = new JsonSupplier();
        jsonSupplier.setId("Y40dgNWM");

        JsonLatestProduct product = new JsonLatestProduct();
        product.setId(41L);
        product.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        product.setVersion(1);
        product.setSupplier(jsonSupplier);
        product.setName(null);
        product.setDescription("Don't Panic!");
        product.setMinimumOrder("1");
        product.setCountry("Ursa Minor Beta");
        product.setCategories(Lists.newArrayList(2801L));

        byte[] bytes = objectMapper.writeValueAsBytes(product);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/latestProducts/41").contentType(MediaType.APPLICATION_JSON).content(bytes)
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    public void publishProduct() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/latestProducts/41/published").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void unpublishProduct() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/latestProducts/41/unpublished").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void heatProduct() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/latestProducts/41/hot").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void coolProduct() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/latestProducts/41/cold").principal(authentication))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addHotProductOvverride() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/latestProducts/41/addHotProductOverride")
                        .accept(MediaType.APPLICATION_JSON).locale(Locale.ENGLISH))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

        Map<String, Object> hotProductOverrideMap = jdbcTemplate.queryForMap(
                "SELECT * FROM hot_product_override WHERE product_id = ?", 41L);
        assertThat((Long) hotProductOverrideMap.get("product_id"), is(41L));
    }

    @Test
    public void removeHotProductOvverride() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.ADMIN));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/latestProducts/41/addHotProductOverride")
                        .accept(MediaType.APPLICATION_JSON).locale(Locale.ENGLISH))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

        Map<String, Object> hotProductOverrideMap = jdbcTemplate.queryForMap(
                "SELECT * FROM hot_product_override WHERE product_id = ?", 41L);
        assertThat((Long) hotProductOverrideMap.get("product_id"), is(41L));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/latestProducts/41/removeHotProductOverride")
                        .accept(MediaType.APPLICATION_JSON).locale(Locale.ENGLISH))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

        jdbcTemplate.queryForMap("SELECT * FROM hot_product_override WHERE product_id = ?", 41L);

    }

}
