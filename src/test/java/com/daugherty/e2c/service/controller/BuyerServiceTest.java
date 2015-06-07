package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.mapper.BuyerMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.persistence.document.TutorialDao;
import com.daugherty.e2c.service.json.JsonBuyer;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyerServiceTest {

    @Mock
    private Accessor<Buyer> approvedBuyerAccessor;
    @Mock
    private Accessor<Buyer> latestBuyerAccessor;
    @Mock
    private Mutator<Buyer> buyerMutator;
    @Mock
    private Uploader buyerLicenseUploader;
    @Mock
    private Uploader buyerLogoUploader;
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
    private TutorialDao tutorialDao;
    @Mock
    private Resource resource;
    @Mock
    private InputStream inputStream;
    @Mock
    private ServletOutputStream outputStream;
    @Mock
    private BuyerMapper buyerMapper;
    
    @InjectMocks
    private final BuyerService service = new BuyerService();

    @Captor
    private ArgumentCaptor<Buyer> buyerCaptor;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        Iterator<String> filenames = mock(Iterator.class);
        when(filenames.next()).thenReturn("filename");
        when(multipartHttpServletRequest.getFileNames()).thenReturn(filenames);
        when(multipartHttpServletRequest.getFile("filename")).thenReturn(multipartFile);
    }

    @Test
    public void retrieveBuyerDelegatesToAccessor() {
        Long buyerId = 42L;
        String publicBuyerId = "jKNz4P4q";
        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.AGENT, BusinessType.BUSINESS_SERVICE);
        Contact contact = new Contact("title", null, "first_name", "last_name", "country", "province", "email_address",
                "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123", Language.CHINESE,
                new Date());
        Company company = new Company("english_name", "chinese_name", "description", businessTypes, "1-10", "website",
                2013, "200-300", "100-150", "logo_ref_id", null);
        Buyer domainObject = new Buyer(buyerId, publicBuyerId, contact, company, ApprovalStatus.PENDING_APPROVAL, 1, 43L,
                "import_license_ref_id");
        when(latestBuyerAccessor.load(publicBuyerId, Locale.CHINESE)).thenReturn(domainObject);

        JsonBuyer transferObject = service.retrieveBuyer(publicBuyerId, true, null, Locale.CHINESE);

        assertThat(transferObject.getId(), is(publicBuyerId));
    }

    @Test
    public void createBuyerDelegatesToBuyerMutator() {
        JsonBuyer requestBuyer = new JsonBuyer();
        requestBuyer.setUsername("username");
        requestBuyer.setFirstName("first");
        requestBuyer.setLastName("last");
        requestBuyer.setCountry("country");
        requestBuyer.setProvince("province");
        requestBuyer.setEmail("email");

        Buyer persistedBuyer = new Buyer(42L, "jKNz4P4q",
                new Contact(requestBuyer.getFirstName(), requestBuyer.getLastName(), requestBuyer.getCountry(),
                        requestBuyer.getProvince(), requestBuyer.getEmail(), "127.0.0.1", null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);
        when(buyerMapper.toNewDomainObject(requestBuyer, Locale.CHINESE)).thenReturn(persistedBuyer);
        when(buyerMutator.create(buyerMapper.toNewDomainObject(requestBuyer, Locale.CHINESE))).thenReturn(persistedBuyer);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        JsonBuyer createdBuyer = service.createBuyer(requestBuyer, request, Locale.CHINESE);

        assertThat(createdBuyer.getId(), is("jKNz4P4q"));
    }

    @Test
    public void updateBuyerDelegatesToBuyerMutator() {
        JsonBuyer requestBuyer = new JsonBuyer();
        requestBuyer.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        requestBuyer.setImportLicenseUrl("import license ref");
        requestBuyer.setLogoUrl("logo url");
        
        Contact contact = new Contact("title", null, "first_name", "last_name", "country", "province", "email_address",
                "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123", Language.CHINESE,
                new Date());
        Company company = new Company("english_name", "chinese_name", "description", null, "1-10", "website",
                2013, "200-300", "100-150", "logo ref", null);
        
        Buyer domainObject = new Buyer(42L, "jKNz4P4q", contact, company, ApprovalStatus.DRAFT, 1, 43L, "import license ref");

        when(documentUrlFactory.removeDocumentUrl(requestBuyer.getImportLicenseUrl(), Locale.CHINESE)).thenReturn(
                "import license ref");
        when(documentUrlFactory.removeDocumentUrl(requestBuyer.getLogoUrl(), Locale.CHINESE)).thenReturn("logo ref");

        when(buyerMutator.update(any(Buyer.class))).thenReturn(new Buyer(null, null, null));
        when(buyerMapper.toExistingDomainObject("jKNz4P4q", requestBuyer, Locale.CHINESE)).thenReturn(domainObject);

        JsonBuyer responseBuyer = service.updateBuyer("jKNz4P4q", requestBuyer, Locale.CHINESE);
        assertThat(responseBuyer, is(notNullValue()));

        verify(buyerMutator).update(buyerCaptor.capture());
        Buyer mutatedBuyer = buyerCaptor.getValue();
        assertThat(mutatedBuyer.getId(), is(42L));
        assertThat(mutatedBuyer.getImportLicenseRefId(), is("import license ref"));
        assertThat(mutatedBuyer.getCompany().getLogoRefId(), is("logo ref"));
    }

    @Test(expected = UploaderException.class)
    public void addLicenseThrowsUploaderExceptionWhenGettingInputStreamFails() throws IOException {
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        service.addLicense("vXP9l0Or", multipartHttpServletRequest, httpServletResponse, Locale.CHINESE);

        fail("You should have thrown an IOException");
    }

    @Test
    public void addLicenseReturnJsonDocument() throws IOException {
        InputStream inputStream = mock(InputStream.class);

        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getContentType()).thenReturn("content type");
        when(buyerLicenseUploader.uploadDocument("vXP9l0Or", "content type", inputStream)).thenReturn("document");
        when(documentUrlFactory.createDocumentUrl("document", Locale.CHINESE)).thenReturn("documentUrl");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        service.addLicense("vXP9l0Or", multipartHttpServletRequest, httpServletResponse, Locale.CHINESE);

        verify(printWriter).write("{\"documentUrl\":{\"url\":\"documentUrl\"}}");
    }

    @Test(expected = UploaderException.class)
    public void addLogoThrowsUploaderExceptionWhenGettingInputStreamFails() throws IOException {
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        service.addLogo("vXP9l0Or", multipartHttpServletRequest, httpServletResponse, Locale.CHINESE);

        fail("You should have thrown an IOException");
    }

    @Test
    public void addLogoReturnsJsonDocument() throws IOException {
        InputStream inputStream = mock(InputStream.class);

        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getContentType()).thenReturn("content type");
        when(buyerLogoUploader.uploadDocument("vXP9l0Or", "content type", inputStream)).thenReturn("document");
        when(documentUrlFactory.createDocumentUrl("document", Locale.CHINESE)).thenReturn("documentUrl");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        service.addLogo("vXP9l0Or", multipartHttpServletRequest, httpServletResponse, Locale.CHINESE);

        verify(printWriter).write("{\"documentUrl\":{\"url\":\"documentUrl\"}}");
    }

    @Test
    public void getBuyerTutorial() throws IOException {
        Long buyerId = 42L;
        String publicBuyerId = "jKNz4P4q";
        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.AGENT, BusinessType.BUSINESS_SERVICE);
        Contact contact = new Contact("title", null, "first_name", "last_name", "country", "province", "email_address",
                "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123", Language.CHINESE,
                new Date());
        Company company = new Company("english_name", "chinese_name", "description", businessTypes, "1-10", "website",
                2013, "200-300", "100-150", "logo_ref_id", null);
        Buyer domainObject = new Buyer(buyerId, publicBuyerId, contact, company, ApprovalStatus.PENDING_APPROVAL, 1, 43L,
                "import_license_ref_id");
        when(latestBuyerAccessor.load(publicBuyerId, Locale.CHINESE)).thenReturn(domainObject);
        when(tutorialDao.getBuyerTutorial(Locale.CHINESE)).thenReturn(resource);

        when(resource.contentLength()).thenReturn(100L);
        when(resource.getFilename()).thenReturn("fileName");
        when(resource.getInputStream()).thenReturn(inputStream);
        when(inputStream.read()).thenReturn(-1);
        when(httpServletResponse.getOutputStream()).thenReturn(outputStream);

        service.retrieveBuyerTutorial(publicBuyerId, httpServletResponse, Locale.CHINESE);

        verify(httpServletResponse).addHeader("Content-type", "application/pdf");
        verify(httpServletResponse).addHeader("Content-Length", "100");
        verify(httpServletResponse).addHeader("Content-Disposition", "inline; filename=\"" + "fileName" + "\"");
    }
}