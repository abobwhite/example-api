package com.daugherty.e2c.business.uploader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.SupplierWriteDao;
import com.daugherty.e2c.persistence.document.DocumentDao;
import com.daugherty.e2c.persistence.document.DocumentType;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SupplierLicenseUploaderTest {
    @Mock
    private DocumentDao documentDao;
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private SupplierWriteDao supplierWriteDao;
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
    private final SupplierLicenseUploader supplierLicenseUploader = new SupplierLicenseUploader();

    @Test
    public void uploadDocumentReturnsURL() throws IOException {
        Long id = 1L;
        String documentKey = DocumentType.SUPPLIER_LICENSE.getType() + id;
        String contentType = "contentType";

        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.AGENT, BusinessType.GOVERNMENT);
        Company company = new Company("englishCompanyName", "chineseCompanyName", "description", businessTypes, "1-10",
                "website", 2013, "200-300", "100-150", "logoRefId", null);
        Supplier supplier = new Supplier(null, null, company);
        supplier.setExportLicenseRefId("oldExportLicenseRefId");
        supplier.setSnapshotId(99L);
        when(businessTypeReadDao.findBySnapshotId(99L)).thenReturn(businessTypes);
        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(supplierReadDao.loadLatest(id, Locale.ENGLISH)).thenReturn(supplier);
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        when(changeDataVisitor.getNewVersion(supplier.getVersion(), supplier.getApprovalStatus())).thenReturn(13);
        when(changeDataVisitor.getNewApprovalStatus(supplier.getApprovalStatus())).thenReturn(ApprovalStatus.DRAFT);

        String uploadedDocumentKey = supplierLicenseUploader.uploadDocument(id, contentType, inputStream);

        assertThat(uploadedDocumentKey, is(documentKey));
        assertThat(supplier.getExportLicenseRefId(), is(documentKey));
        assertThat(supplier.getApprovalStatus(), is(ApprovalStatus.DRAFT));
        assertThat(supplier.getVersion(), is(13));
        verify(supplierWriteDao).update(supplier);
    }
}
