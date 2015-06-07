package com.daugherty.e2c.business.uploader;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.BuyerWriteDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.daugherty.e2c.persistence.document.DocumentType;

@Service
@Transactional
public class BuyerLicenseUploader extends BaseDocumentUploader<Buyer> {

    @Inject
    private BuyerReadDao buyerReadDao;
    @Inject
    private BuyerWriteDao buyerWriteDao;
    @Inject
    private PartyBusinessTypeWriteDao businessTypeWriteDao;
    @Inject
    private PartyBusinessTypeReadDao businessTypeReadDao;
    @Inject
    private ApprovalStateTransitionVisitor changeDataVisitor;

    @Override
    protected DocumentType getDocumentType() {
        return DocumentType.BUYER_LICENSE;
    }

    @Override
    protected boolean isValid(Buyer entity, ValidationError validationError) {
        return true;
    }

    @Override
    protected Buyer loadEntityById(Long id) {
        Buyer buyer = buyerReadDao.loadLatest(id);
        buyer.getCompany().setBusinessTypes(businessTypeReadDao.findBySnapshotId(buyer.getSnapshotId()));
        return buyer;
    }

    @Override
    protected void persistNewDocumentKey(Buyer buyer, String documentName, String documentKey) {
        buyer.setImportLicenseRefId(documentKey);
        buyer.visit(changeDataVisitor);
        buyerWriteDao.update(buyer);
        businessTypeWriteDao.updateBusinessTypes(buyer.getSnapshotId(), buyer.getCompany().getBusinessTypes());
    }
}
