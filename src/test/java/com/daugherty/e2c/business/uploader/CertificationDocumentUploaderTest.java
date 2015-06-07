package com.daugherty.e2c.business.uploader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ImageCreator;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationReadDao;
import com.daugherty.e2c.persistence.data.CertificationWriteDao;
import com.daugherty.e2c.persistence.document.DocumentDao;
import com.daugherty.e2c.persistence.document.DocumentType;

@RunWith(MockitoJUnitRunner.class)
public class CertificationDocumentUploaderTest {

    @Mock
    private DocumentDao documentDao;
    @Mock
    private CertificationReadDao certificationReadDao;
    @Mock
    private CertificationWriteDao certificationWriteDao;
    @Mock
    private InputStream inputStream;
    @Mock
    private ApprovalStateTransitionVisitor changeDataVisitor;
    @Mock
    private ImageCreator thumbnailImage;

    @InjectMocks
    private final CertificationDocumentUploader certificationDocumentUploader = new CertificationDocumentUploader();

    @Test
    public void uploadDocumentReturnsURL() throws IOException {

        Long certificationId = 666L;
        Long productId = 51L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 17, 0, 0);

        String documentKey = DocumentType.CERTIFICATION_DOCUMENT.getType() + certificationId;
        String contentType = "contentType";

        Certification testCertification = new Certification(certificationId, "Standard", "cert-number", null,
                productId, "issued-by", calendar.getTimeInMillis(), "scope-range!", ApprovalStatus.DRAFT, 13, 779L);

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(certificationReadDao.loadLatest(certificationId, Locale.ENGLISH)).thenReturn(testCertification);
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        when(changeDataVisitor.getNewVersion(testCertification.getVersion(), testCertification.getApprovalStatus()))
                .thenReturn(13);
        when(changeDataVisitor.getNewApprovalStatus(testCertification.getApprovalStatus())).thenReturn(
                ApprovalStatus.DRAFT);

        String uploadedDocumentKey = certificationDocumentUploader.uploadDocument(certificationId, contentType,
                inputStream);

        assertThat(uploadedDocumentKey, is(documentKey));
        assertThat(testCertification.getLink(), is(documentKey));
        assertThat(testCertification.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        assertThat(testCertification.getVersion(), is(13));
        verify(certificationWriteDao).update(testCertification);
    }

}
