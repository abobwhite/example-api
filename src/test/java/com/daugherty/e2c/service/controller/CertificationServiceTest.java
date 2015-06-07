package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.mapper.CertificationMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.service.json.JsonCertification;

@RunWith(MockitoJUnitRunner.class)
public class CertificationServiceTest {

    @Mock
    private Accessor<Certification> latestCertificationAccessor;
    @Mock
    private Accessor<Certification> approvedCertificationAccessor;
    @Mock
    private Mutator<Certification> generalCertificationMutator;
    @Mock
    private Mutator<Certification> productCertificationMutator;
    @Mock
    private Mutator<Certification> supplierCertificationMutator;
    @Mock
    private Uploader certificationDocumentUploader;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private MultipartHttpServletRequest multipartHttpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private CertificationMapper certificationMapper;

    @InjectMocks
    private final CertificationService certificationService = new CertificationService();

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        Iterator<String> filenames = mock(Iterator.class);
        when(filenames.next()).thenReturn("filename");
        when(multipartHttpServletRequest.getFileNames()).thenReturn(filenames);
        when(multipartHttpServletRequest.getFile("filename")).thenReturn(multipartFile);
    }

    @Test
    public void retrieveLatestCertificationDelegatesToAccessor() {
        Long certificationId = 666L;
        Long productId = 51L;
        Calendar calendar = new GregorianCalendar(2013, 8, 18, 0, 0);

        Certification testCertification = new Certification(certificationId, "StandardUPD", "cert-numberUPD",
                "www.cert-linkUPD.com", productId, "issued-byUPD", calendar.getTimeInMillis(), "scope-range!UPD",
                ApprovalStatus.DRAFT, 13, 779L);

        when(latestCertificationAccessor.load(certificationId, Locale.ENGLISH)).thenReturn(testCertification);
        when(documentUrlFactory.createDocumentUrl("www.cert-linkUPD.com", Locale.ENGLISH)).thenReturn(
                "www.cert-linkUPD.com");

        JsonCertification jsonCertification = certificationService.retrieveCertification(certificationId, true,
                Locale.ENGLISH, null);

        assertThat(jsonCertification.getId(), is(testCertification.getId()));
    }

    @Test
    public void updateCertificationDelegatesToMutator() {
        Long certificationId = 666L;
        Calendar calendar = new GregorianCalendar(2013, 8, 18, 0, 0);

        JsonCertification sourceCertification = new JsonCertification();
        sourceCertification.setId(certificationId);
        sourceCertification.setStandard("certification-standard");
        sourceCertification.setCertificateNumber("certification-number");
        sourceCertification.setLink("certificationDocumentURL");
        sourceCertification.setIssuedBy("issued-by");
        sourceCertification.setIssuedDate(calendar.getTime());
        sourceCertification.setScopeRange("scope-range");
        sourceCertification.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        sourceCertification.setVersion(13);
        sourceCertification.setSnapshotId(779L);

        when(documentUrlFactory.createDocumentUrl("certificationDocumentURL", Locale.ENGLISH)).thenReturn(
                "certificationDocumentURL");
        when(documentUrlFactory.removeDocumentUrl("certificationDocumentURL", Locale.ENGLISH)).thenReturn(
                "certificationDocumentURL");

        Certification certification = new Certification(certificationId, sourceCertification.getStandard(), sourceCertification.getCertificateNumber(), 
                sourceCertification.getLink(), null, sourceCertification.getIssuedBy(), new Date().getTime(), 
                sourceCertification.getScopeRange(), ApprovalStatus.DRAFT, sourceCertification.getVersion(), 
                sourceCertification.getSnapshotId());
        when(certificationMapper.toExistingDomainObject(certificationId, sourceCertification, Locale.ENGLISH)).thenReturn(certification);

        when(generalCertificationMutator.update(any(Certification.class))).thenReturn(certification);

        JsonCertification returnedCertification = certificationService.updateCertification(certificationId,
                sourceCertification, null, Locale.ENGLISH);

        assertThat(sourceCertification.getId(), is(returnedCertification.getId()));
    }

    @Test
    public void insertSupplierCertificationDelegatesToMutator() {
        Long certificationId = 666L;
        Long supplierId = 51L;
        String publicSupplierId = "XJPKJ10M";
        Calendar calendar = new GregorianCalendar(2013, 8, 18, 0, 0);

        JsonCertification sourceCertification = new JsonCertification();
        sourceCertification.setId(certificationId);
        sourceCertification.setStandard("certification-standard");
        sourceCertification.setCertificateNumber("certification-number");
        sourceCertification.setLink("certificationDocumentURL");
        sourceCertification.setIssuedBy("issued-by");
        sourceCertification.setIssuedDate(calendar.getTime());
        sourceCertification.setScopeRange("scope-range");
        sourceCertification.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        sourceCertification.setVersion(1);
        sourceCertification.setSnapshotId(779L);

        when(documentUrlFactory.createDocumentUrl("certificationDocumentURL", Locale.ENGLISH)).thenReturn(
                "certificationDocumentURL");
        when(documentUrlFactory.removeDocumentUrl("certificationDocumentURL", Locale.ENGLISH)).thenReturn(
                "certificationDocumentURL");
        
        Certification certification = new Certification(certificationId, sourceCertification.getStandard(), sourceCertification.getCertificateNumber(), 
                sourceCertification.getLink(), supplierId, sourceCertification.getIssuedBy(), new Date().getTime(), 
                sourceCertification.getScopeRange(), ApprovalStatus.DRAFT, sourceCertification.getVersion(), 
                sourceCertification.getSnapshotId());
        when(certificationMapper.toNewDomainObject(sourceCertification, publicSupplierId, Locale.ENGLISH)).thenReturn(certification);

        when(supplierCertificationMutator.create(any(Certification.class))).thenReturn(certification);

        JsonCertification returnedCertification = certificationService.createSupplierCertification(publicSupplierId,
                sourceCertification, null, Locale.ENGLISH);

        assertThat(sourceCertification.getId(), is(returnedCertification.getId()));
    }

    @Test
    public void insertProductCertificationDelegatesToMutator() {
        Long certificationId = 666L;
        Long productId = 51L;
        Calendar calendar = new GregorianCalendar(2013, 8, 18, 0, 0);

        JsonCertification sourceCertification = new JsonCertification();
        sourceCertification.setId(certificationId);
        sourceCertification.setStandard("certification-standard");
        sourceCertification.setCertificateNumber("certification-number");
        sourceCertification.setLink("certificationDocumentURL");
        sourceCertification.setIssuedBy("issued-by");
        sourceCertification.setIssuedDate(calendar.getTime());
        sourceCertification.setScopeRange("scope-range");
        sourceCertification.setApprovalStatus(ApprovalStatus.DRAFT.getName());
        sourceCertification.setVersion(1);
        sourceCertification.setSnapshotId(779L);

        when(documentUrlFactory.createDocumentUrl("certificationDocumentURL", Locale.ENGLISH)).thenReturn(
                "certificationDocumentURL");
        when(documentUrlFactory.removeDocumentUrl("certificationDocumentURL", Locale.ENGLISH)).thenReturn(
                "certificationDocumentURL");
        
        Certification certification = new Certification(certificationId, sourceCertification.getStandard(), sourceCertification.getCertificateNumber(), 
                sourceCertification.getLink(), productId, sourceCertification.getIssuedBy(), new Date().getTime(), 
                sourceCertification.getScopeRange(), ApprovalStatus.DRAFT, sourceCertification.getVersion(), 
                sourceCertification.getSnapshotId());
        when(certificationMapper.toNewDomainObject(sourceCertification, productId, Locale.ENGLISH)).thenReturn(certification);

        when(productCertificationMutator.create(any(Certification.class))).thenReturn(certification);

        JsonCertification returnedCertification = certificationService.createProductCertification(productId,
                sourceCertification, null, Locale.ENGLISH);

        assertThat(sourceCertification.getId(), is(returnedCertification.getId()));
    }

}
