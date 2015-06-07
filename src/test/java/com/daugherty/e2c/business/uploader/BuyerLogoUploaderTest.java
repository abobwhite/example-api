package com.daugherty.e2c.business.uploader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ImageCreator;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.BuyerWriteDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.daugherty.e2c.persistence.document.DocumentDao;
import com.daugherty.e2c.persistence.document.DocumentType;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyerLogoUploaderTest {
    @Mock
    private DocumentDao documentDao;
    @Mock
    private BuyerReadDao buyerReadDao;
    @Mock
    private BuyerWriteDao buyerWriteDao;
    @Mock
    private PartyBusinessTypeWriteDao businessTypeWriteDao;
    @Mock
    private PartyBusinessTypeReadDao businessTypeReadDao;
    @Mock
    private InputStream inputStream;
    @Mock
    private ApprovalStateTransitionVisitor changeDataVisitor;
    @Mock
    private ImageCreator thumbnailImage;

    @InjectMocks
    private final BuyerLogoUploader buyerLogoUploader = new BuyerLogoUploader();

    @Test
    public void uploadDocumentReturnsURL() throws IOException {
        Long id = 1L;
        String documentKey = DocumentType.BUYER_LOGO.getType() + id;
        String contentType = "contentType";

        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.AGENT, BusinessType.GOVERNMENT);
        Company company = new Company("englishCompanyName", "chineseCompanyName", "description", businessTypes, "1-10",
                "website", 2013, "200-300", "100-150", "logoRefId", null);
        Buyer buyer = new Buyer(null, null, company);
        buyer.setSnapshotId(99L);
        when(businessTypeReadDao.findBySnapshotId(99L)).thenReturn(businessTypes);
        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(buyerReadDao.loadLatest(id)).thenReturn(buyer);
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        when(changeDataVisitor.getNewVersion(buyer.getVersion(), buyer.getApprovalStatus())).thenReturn(13);
        when(changeDataVisitor.getNewApprovalStatus(buyer.getApprovalStatus())).thenReturn(ApprovalStatus.DRAFT);

        String uploadedDocumentKey = buyerLogoUploader.uploadDocument(id, contentType, inputStream);

        assertThat(uploadedDocumentKey, is(documentKey));
        assertThat(company.getLogoRefId(), is(documentKey));
        assertThat(buyer.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        assertThat(buyer.getVersion(), is(13));
        verify(buyerWriteDao).update(buyer);
    }
}
