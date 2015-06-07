package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.mapper.LatestSupplierMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.document.TutorialDao;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonLatestSupplier;
import com.daugherty.e2c.service.json.JsonSupplier;

@RunWith(MockitoJUnitRunner.class)
public class LatestSupplierServiceTest {
    @Mock
    private Accessor<Supplier> latestSupplierAccessor;
    @Mock
    private Mutator<Supplier> supplierMutator;
    @Mock
    private Uploader supplierLicenseUploader;
    @Mock
    private Uploader supplierLogoUploader;
    @Mock
    private MultipartHttpServletRequest multipartHttpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private HttpServletRequest request;
    @Mock
    private TutorialDao tutorialDao;
    @Mock
    private Resource resource;
    @Mock
    private InputStream inputStream;
    @Mock
    private ServletOutputStream outputStream;
    @Mock
    private LatestSupplierMapper latestSupplierMapper;
    @Mock
    private RememberMeAuthenticationToken principal;
    
    @InjectMocks
    private final LatestSupplierService service = new LatestSupplierService();
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRemoteAddr("127.0.0.1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));

        Iterator<String> filenames = mock(Iterator.class);
        when(filenames.next()).thenReturn("filename");
        when(multipartHttpServletRequest.getFileNames()).thenReturn(filenames);
        when(multipartHttpServletRequest.getFile("filename")).thenReturn(multipartFile);
    }

    @Test
    public void retrieveLatestSupplierDelegatesToLatestAccessorAndReturnsLinkAsLatestFalse() {
        Long supplierId = 42L;
        String publicSupplierId = "jKNz4P4q";
        Membership membership = new Membership(1L, new MembershipLevel(1L, 3, BigDecimal.valueOf(10), 6, 10, 20, 20,
                false, 10, false, false, false, 10, false, true, false, true, false, false, false));
        Supplier domainObject = buildSupplierDomainObject(supplierId, publicSupplierId, membership);

        E2CUser e2cUser = new E2CUser("fordp", "hoopy", true, 0,
                AuthorityUtils.createAuthorityList(Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY), domainObject, true);
        Authentication authentication = new RememberMeAuthenticationToken("e2cApiKey", e2cUser,
                e2cUser.getAuthorities());
        authentication.setAuthenticated(true);

        when(((Authentication) principal).getPrincipal()).thenReturn(e2cUser);

        JsonLatestSupplier mappedSupplier = new JsonLatestSupplier(domainObject, documentUrlFactory, Locale.ENGLISH, principal);

        when(latestSupplierAccessor.load(publicSupplierId, Locale.ENGLISH)).thenReturn(domainObject);
        when(documentUrlFactory.createDocumentUrl("export_license_ref_id", Locale.ENGLISH)).thenReturn(
                "export_license_ref_id_url");
        when(documentUrlFactory.createDocumentUrl("logo_ref_id", Locale.ENGLISH)).thenReturn("logo_ref_id_url");
        when(latestSupplierMapper.fromDomainObject(domainObject, Locale.ENGLISH, principal)).thenReturn(mappedSupplier);

        JsonLatestSupplier transferObject = service.retrieveSupplier(publicSupplierId, Locale.ENGLISH, principal);

        assertThat(transferObject.getId(), is(publicSupplierId));
        assertTrue(transferObject.getLinks().get("products").contains("suppliers/jKNz4P4q/products?latest=false"));
    }
    
    @Test
    public void createSupplierDelegatesToSupplierMutator() {
        JsonLatestSupplier requestSupplier = new JsonLatestSupplier();
        requestSupplier.setUsername("username");
        requestSupplier.setFirstName("first");
        requestSupplier.setLastName("last");
        requestSupplier.setCountry("country");
        requestSupplier.setEmail("email");

        when(request.getRemoteAddr()).thenReturn("123.456.789.012");

        Supplier persistedSupplier = new Supplier(42L, "jKNz4P4q", new Contact(requestSupplier.getFirstName(),
                requestSupplier.getLastName(), requestSupplier.getCountry(), null, requestSupplier.getEmail(),
                "123.456.789.012", Language.SPANISH, new Date()), null, ApprovalStatus.UNPROFILED, null, null, null,
                null, null);
        when(latestSupplierMapper.toNewDomainObject(requestSupplier, Locale.ENGLISH)).thenReturn(persistedSupplier);
        when(supplierMutator.create(latestSupplierMapper.toNewDomainObject(requestSupplier, Locale.ENGLISH))).thenReturn(
                persistedSupplier);

        JsonLatestSupplier newJsonSupplier = new JsonLatestSupplier(persistedSupplier, documentUrlFactory, Locale.ENGLISH);
        when(latestSupplierMapper.fromDomainObject(persistedSupplier, Locale.ENGLISH)).thenReturn(newJsonSupplier);

        JsonSupplier responseSupplier = service.createSupplier(requestSupplier, request, Locale.ENGLISH);

        assertThat(responseSupplier.getId(), is("jKNz4P4q"));
    }

    @Test
    public void updateSupplierDelegatesToSupplierMutator() {
        Long supplierId = 42L;
        String publicSupplierId = "jKNz4P4q";
        Membership membership = new Membership(1L, new MembershipLevel(1L, 3, BigDecimal.valueOf(10), 6, 10, 20, 20,
                false, 10, false, false, false, 10, false, true, false, true, false, false, false));

        Supplier domainObject = buildSupplierDomainObject(supplierId, publicSupplierId, membership);

        JsonLatestSupplier requestSupplier = new JsonLatestSupplier(domainObject, documentUrlFactory, Locale.ENGLISH);
        requestSupplier.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        requestSupplier.setExportLicenseUrl("export license url");
        requestSupplier.setLogoUrl("logo url");

        when(documentUrlFactory.removeDocumentUrl(requestSupplier.getExportLicenseUrl(), Locale.ENGLISH)).thenReturn(
                "export license ref");
        when(documentUrlFactory.removeDocumentUrl(requestSupplier.getLogoUrl(), Locale.ENGLISH)).thenReturn("logo ref");

        when(latestSupplierMapper.toExistingDomainObject(publicSupplierId, requestSupplier, Locale.ENGLISH)).thenReturn(domainObject);

        // Create updated supplier
        List<BusinessType> businessTypes = new ArrayList<BusinessType>();
        businessTypes.add(BusinessType.MANUFACTURER);
        businessTypes.add(BusinessType.PROCUREMENT_OFFICE);
        Contact contact = new Contact("title", Gender.MALE, "first_name", "last_name", "country", "province",
                "email_address", "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123",
                Language.ENGLISH, new Date());
        Company company = new Company("english_name", "chinese_name", "description", businessTypes, "1-10", "website",
                2013, "200-300", "100-150", "logo ref", null);
        Supplier newDomainObject = new Supplier(supplierId, publicSupplierId, contact, company,
                ApprovalStatus.PENDING_APPROVAL, 1, 43L, "export license ref", null, null);

        when(supplierMutator.update(domainObject)).thenReturn(newDomainObject);
        when(latestSupplierMapper.fromDomainObject(newDomainObject, Locale.ENGLISH)).thenReturn(requestSupplier);

        JsonSupplier responseSupplier = service.updateSupplier("jKNz4P4q", requestSupplier, Locale.ENGLISH);
        assertThat(responseSupplier, is(notNullValue()));

        assertThat(newDomainObject.getId(), is(42L));
        assertThat(newDomainObject.getExportLicenseRefId(), is("export license ref"));
        assertThat(newDomainObject.getCompany().getLogoRefId(), is("logo ref"));
    }

    @Test(expected = UploaderException.class)
    public void addLicenseThrowsUploaderExceptionWhenGettingInputStreamFails() throws IOException {
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        service.addLicense("vXP9l0Or", multipartHttpServletRequest, httpServletResponse, Locale.ENGLISH);

        fail("You should have thrown an IOException");
    }

    @Test
    public void addLicenseReturnJsonDocument() throws IOException {
        InputStream inputStream = mock(InputStream.class);

        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getContentType()).thenReturn("content type");
        when(supplierLicenseUploader.uploadDocument("vXP9l0Or", "content type", inputStream)).thenReturn("document");
        when(documentUrlFactory.createDocumentUrl("document", Locale.ENGLISH)).thenReturn("documentUrl");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        service.addLicense("vXP9l0Or", multipartHttpServletRequest, httpServletResponse, Locale.ENGLISH);

        verify(printWriter).write("{\"documentUrl\":{\"url\":\"documentUrl\"}}");
    }

    @Test(expected = UploaderException.class)
    public void addLogoThrowsUploaderExceptionWhenGettingInputStreamFails() throws IOException {
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        service.addLogo("vXP9l0Or", multipartHttpServletRequest, httpServletResponse, Locale.ENGLISH);

        fail("You should have thrown an IOException");
    }

    @Test
    public void addLogoReturnJsonDocument() throws IOException {
        InputStream inputStream = mock(InputStream.class);

        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getContentType()).thenReturn("content type");
        when(supplierLogoUploader.uploadDocument("vXP9l0Or", "content type", inputStream)).thenReturn("document");
        when(documentUrlFactory.createDocumentUrl("document", Locale.ENGLISH)).thenReturn("documentUrl");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        service.addLogo("vXP9l0Or", multipartHttpServletRequest, httpServletResponse, Locale.ENGLISH);

        verify(printWriter).write("{\"documentUrl\":{\"url\":\"documentUrl\"}}");
    }
    
    private Supplier buildSupplierDomainObject(Long supplierId, String publicSupplierId, Membership membership) {
        List<BusinessType> businessTypes = new ArrayList<BusinessType>();
        businessTypes.add(BusinessType.MANUFACTURER);
        businessTypes.add(BusinessType.PROCUREMENT_OFFICE);
        Contact contact = new Contact("title", Gender.MALE, "first_name", "last_name", "country", "province",
                "email_address", "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123",
                Language.ENGLISH, new Date());
        Company company = new Company("english_name", "chinese_name", "description", businessTypes, "1-10", "website",
                2013, "200-300", "100-150", "logo_ref_id", null);
        Supplier supplier = new Supplier(supplierId, publicSupplierId, contact, company,
                ApprovalStatus.PENDING_APPROVAL, 1, 43L, "export_license_ref_id", null, null);

        supplier.getMemberships().add(membership);
        return supplier;
    }
}
