package com.daugherty.e2c.business.uploader;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.SupplierWriteDao;
import com.daugherty.e2c.persistence.document.DocumentType;

@Service
@Transactional
public class SupplierLicenseUploader extends BaseDocumentUploader<Supplier> {

    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private SupplierWriteDao supplierWriteDao;
    @Inject
    private PartyBusinessTypeWriteDao businessTypeWriteDao;
    @Inject
    private PartyBusinessTypeReadDao businessTypeReadDao;
    @Inject
    private ApprovalStateTransitionVisitor changeDataVisitor;

    @Override
    protected DocumentType getDocumentType() {
        return DocumentType.SUPPLIER_LICENSE;
    }

    @Override
    protected boolean isValid(Supplier entity, ValidationError validationError) {
        return true;
    }

    @Override
    protected Supplier loadEntityById(Long id) {
        Supplier supplier = supplierReadDao.loadLatest(id, Locale.ENGLISH);
        supplier.getCompany().setBusinessTypes(businessTypeReadDao.findBySnapshotId(supplier.getSnapshotId()));
        return supplier;
    }

    @Override
    protected void persistNewDocumentKey(Supplier supplier, String documentName, String documentKey) {
        supplier.setExportLicenseRefId(documentKey);
        supplier.visit(changeDataVisitor);
        supplierWriteDao.update(supplier);
        businessTypeWriteDao.updateBusinessTypes(supplier.getSnapshotId(), supplier.getCompany().getBusinessTypes());
    }
}
