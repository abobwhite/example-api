package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import com.daugherty.e2c.business.mapper.SupplierMapper;
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
import com.daugherty.e2c.service.json.JsonSupplier;

@RunWith(MockitoJUnitRunner.class)
public class SupplierServiceTest {

    @Mock
    private Accessor<Supplier> approvedSupplierAccessor;
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
    private SupplierMapper supplierMapper;
    @Mock
    private RememberMeAuthenticationToken principal;

    @InjectMocks
    private final SupplierService service = new SupplierService();

    @Captor
    private ArgumentCaptor<Supplier> supplierCaptor;

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
    public void retrieveApprovedSupplierDelegatesToApprovedAccessor() {
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

        JsonSupplier mappedSupplier = new JsonSupplier(domainObject, documentUrlFactory, Locale.ENGLISH, principal);

        when(approvedSupplierAccessor.load(publicSupplierId, Locale.ENGLISH)).thenReturn(domainObject);
        when(documentUrlFactory.createDocumentUrl("export_license_ref_id", Locale.ENGLISH)).thenReturn(
                "export_license_ref_id_url");
        when(documentUrlFactory.createDocumentUrl("logo_ref_id", Locale.ENGLISH)).thenReturn("logo_ref_id_url");
        when(supplierMapper.fromDomainObject(domainObject, Locale.ENGLISH, principal)).thenReturn(mappedSupplier);

        JsonSupplier transferObject = service.retrieveSupplier(publicSupplierId, Locale.ENGLISH, principal);

        assertThat(transferObject.getId(), is(publicSupplierId));
    }

    @Test
    public void getSupplierTutorialWhenMembershipLevelGreaterThan2() throws IOException {
        Long supplierId = 42L;
        String publicSupplierId = "jKNz4P4q";
        Membership membership = new Membership(1L, new MembershipLevel(1L, 3, BigDecimal.valueOf(10), 6, 10, 20, 20,
                false, 10, false, false, true, 10, false, true, false, true, false, false, false));
        Supplier domainObject = buildSupplierDomainObject(supplierId, publicSupplierId, membership);

        when(approvedSupplierAccessor.load(publicSupplierId, Locale.ENGLISH)).thenReturn(domainObject);
        when(tutorialDao.getSupplierTutorial(Locale.ENGLISH)).thenReturn(resource);

        when(resource.contentLength()).thenReturn(100L);
        when(resource.getFilename()).thenReturn("fileName");
        when(resource.getInputStream()).thenReturn(inputStream);
        when(inputStream.read()).thenReturn(-1);
        when(httpServletResponse.getOutputStream()).thenReturn(outputStream);

        service.retrieveSupplierTutorial(publicSupplierId, httpServletResponse, "en");

        verify(httpServletResponse).addHeader("Content-type", "application/pdf");
        verify(httpServletResponse).addHeader("Content-Length", "100");
        verify(httpServletResponse).addHeader("Content-Disposition", "inline; filename=\"" + "fileName" + "\"");
    }

    @Test
    public void getSupplierTutorialWhenMembershipLevelLessThan3() throws IOException {
        Long supplierId = 42L;
        String publicSupplierId = "jKNz4P4q";
        Membership membership = new Membership(1L, new MembershipLevel(1L, 2, BigDecimal.valueOf(10), 6, 10, 20, 20,
                false, 10, false, false, false, 10, false, true, false, true, false, false, false));
        Supplier domainObject = buildSupplierDomainObject(supplierId, publicSupplierId, membership);

        when(approvedSupplierAccessor.load(publicSupplierId, Locale.ENGLISH)).thenReturn(domainObject);
        when(tutorialDao.getSupplierTutorial(Locale.ENGLISH)).thenReturn(resource);

        service.retrieveSupplierTutorial(publicSupplierId, httpServletResponse, "en");

        verify(httpServletResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
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
