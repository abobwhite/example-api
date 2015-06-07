package com.daugherty.e2c.integration.service;

import java.util.Calendar;
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

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonCertification;
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
public class CertificationServiceTest {

    @Inject
    private WebApplicationContext wac;
    @Inject
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    @Inject
    private DocumentUrlFactory documentUrlFactory;

    private final ObjectMapper objectMapper = new RootWrappingObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void insertSupplierCertification() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        final Long supplierId = 21L;

        JsonCertification jsonCertification = new JsonCertification();
        jsonCertification.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        jsonCertification.setCertificateNumber("1234X");
        jsonCertification.setIssuedBy("issuer");
        jsonCertification.setLink("//export-to-china/e2c-api/documents/www.whocares.???");
        jsonCertification.setStandard("standard");
        jsonCertification.setScopeRange("scopey");
        jsonCertification.setIssuedDate(new Date());
        jsonCertification.setVersion(1);

        byte[] bytes = objectMapper.writeValueAsBytes(jsonCertification);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/suppliers/" + supplierId + "/certifications")
                        .contentType(MediaType.APPLICATION_JSON).content(bytes).principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.id").exists())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.approvalStatus").value(
                                ApprovalStatus.DRAFT.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.certificateNumber").value("1234X"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.issuedBy").value("issuer"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.link").value(
                                "//export-to-china/e2c-api/documents/www.whocares.???"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.standard").value("standard"))
                .andExpect(MockMvcResultMatchers.jsonPath("$certification.scopeRange").value("scopey"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.version").value(1));
    }

    @Test
    public void insertProductCertification() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        final Long productId = 777L;

        JsonCertification jsonCertification = new JsonCertification();
        jsonCertification.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        jsonCertification.setCertificateNumber("2345Y");
        jsonCertification.setIssuedBy("some-issuer");
        jsonCertification.setLink("//export-to-china/e2c-api/documents/www.some-site.???");
        jsonCertification.setStandard("some-standard");
        jsonCertification.setScopeRange("some-scope-range");
        jsonCertification.setIssuedDate(new Date());
        jsonCertification.setVersion(1);

        byte[] bytes = objectMapper.writeValueAsBytes(jsonCertification);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/products/" + productId + "/certifications")
                        .contentType(MediaType.APPLICATION_JSON).content(bytes).principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.id").exists())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.approvalStatus").value(
                                ApprovalStatus.DRAFT.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.certificateNumber").value("2345Y"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.issuedBy").value("some-issuer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.standard").value("some-standard"))
                .andExpect(MockMvcResultMatchers.jsonPath("$certification.scopeRange").value("some-scope-range"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.version").value(1));
    }

    @Test
    public void getSupplierCertificationNoQueryParamDefaultsToApproved() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        Long certificationId = 777L;
        Long supplierId = 21L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 17, 0, 0);

        Certification testCertification = new Certification(certificationId, "Standard", "cert-number", null,
                supplierId, "issued-by", calendar.getTimeInMillis(), "scope-range!", ApprovalStatus.APPROVED, 13, 779L);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/certifications/" + certificationId.toString())
                        .accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.approvalStatus").value(
                                testCertification.getApprovalStatus().getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.certificateNumber").value(
                                testCertification.getCertificateNumber()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.issuedBy").value(
                                testCertification.getIssuedBy()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.link").value(testCertification.getLink()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.standard").value(
                                testCertification.getStandard()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$certification.scopeRange").value(
                                testCertification.getScopeRange()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.version").value(testCertification.getVersion()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.issuedDate").value(
                                testCertification.getIssuedDate()));
    }

    @Test
    public void getLatestProductCertification() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        Long certificationId = 666L;
        Long productId = 51L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 17, 0, 0);

        Certification testCertification = new Certification(certificationId, "Standard", "cert-number",
                "www.cert-link.com", productId, "issued-by", calendar.getTimeInMillis(), "scope-range!",
                ApprovalStatus.DRAFT, 13, 779L);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/certifications/" + certificationId + "?latest=true")
                        .accept(MediaType.APPLICATION_JSON).principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.approvalStatus").value(
                                testCertification.getApprovalStatus().getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.certificateNumber").value(
                                testCertification.getCertificateNumber()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.issuedBy").value(
                                testCertification.getIssuedBy()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.link").value(
                                "//export-to-china/e2c-api/documents/" + testCertification.getLink()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.standard").value(
                                testCertification.getStandard()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$certification.scopeRange").value(
                                testCertification.getScopeRange()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.version").value(testCertification.getVersion()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.issuedDate").value(
                                testCertification.getIssuedDate()));
    }

    @Test
    public void getApprovedCertification() throws Exception {
        Long certificationId = 777L;
        Long supplierId = 21L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 17, 0, 0);

        Certification testCertification = new Certification(certificationId, "Standard", "cert-number", null,
                supplierId, "issued-by", calendar.getTimeInMillis(), "scope-range!", ApprovalStatus.APPROVED, 13, 779L);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/certifications/" + certificationId + "?latest=false").accept(
                        MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.approvalStatus").value(
                                testCertification.getApprovalStatus().getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.certificateNumber").value(
                                testCertification.getCertificateNumber()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.issuedBy").value(
                                testCertification.getIssuedBy()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.link").value(testCertification.getLink()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.standard").value(
                                testCertification.getStandard()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$certification.scopeRange").value(
                                testCertification.getScopeRange()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.version").value(testCertification.getVersion()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.issuedDate").value(
                                testCertification.getIssuedDate()));
    }

    @Test
    public void updateCertification() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        Long certificationId = 666L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 18, 0, 0);

        JsonCertification jsonCertification = new JsonCertification();
        jsonCertification.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        jsonCertification.setCertificateNumber("cert-numberUPD");
        jsonCertification.setIssuedBy("issued-byUPD");
        jsonCertification.setLink("//export-to-china/e2c-api/documents/www.cert-linkUPD.com");
        jsonCertification.setStandard("StandardUPD");
        jsonCertification.setScopeRange("scope-range!UPD");
        jsonCertification.setIssuedDate(calendar.getTime());
        jsonCertification.setVersion(13);

        Certification certification = new Certification(certificationId, jsonCertification.getStandard(),
                jsonCertification.getCertificateNumber(), jsonCertification.getLink(), null,
                jsonCertification.getIssuedBy(), calendar.getTimeInMillis(), jsonCertification.getScopeRange(),
                ApprovalStatus.DRAFT, jsonCertification.getVersion(), jsonCertification.getSnapshotId());

        byte[] bytes = objectMapper.writeValueAsBytes(jsonCertification);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/certifications/" + certificationId.toString())
                        .contentType(MediaType.APPLICATION_JSON).content(bytes).principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.approvalStatus").value(
                                certification.getApprovalStatus().getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.certificateNumber").value(
                                certification.getCertificateNumber()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.issuedBy").value(certification.getIssuedBy()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.standard").value(certification.getStandard()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$certification.scopeRange")
                                .value(certification.getScopeRange()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.certification.version").value(certification.getVersion()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.certification.issuedDate").value(
                                certification.getIssuedDate()));
    }

    @Test
    public void deleteCertification() throws Exception {
        Authentication authentication = SecurityFixtures.buildAuthentication(AuthorityUtils
                .createAuthorityList(Role.SUPPLIER_MODERATOR));
        SecurityFixtures.setUpContext(rememberMeAuthenticationProvider, authentication);

        Long certificationId = 666L;
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/certifications/" + certificationId.toString())
                        .principal(authentication)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
