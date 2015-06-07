package com.daugherty.e2c.business.uploader;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.CertificationReadDao;
import com.daugherty.e2c.persistence.data.CertificationWriteDao;
import com.daugherty.e2c.persistence.document.DocumentType;

@Service
@Transactional
public class CertificationDocumentUploader extends BaseDocumentUploader<Certification> {

    @Inject
    private CertificationWriteDao certificationWriteDao;
    @Inject
    CertificationReadDao certificationReadDao;
    @Inject
    private ApprovalStateTransitionVisitor changeDataVisitor;

    @Override
    protected Certification loadEntityById(Long id) {
        return certificationReadDao.loadLatest(id, Locale.ENGLISH);
    }

    @Override
    protected boolean isValid(Certification certification, ValidationError validationError) {
        return true;
    }

    @Override
    protected DocumentType getDocumentType() {
        return DocumentType.CERTIFICATION_DOCUMENT;
    }

    @Override
    protected void persistNewDocumentKey(Certification certification, String documentName, String documentKey) {
        certification.setLink(documentKey);
        certification.visit(changeDataVisitor);
        certificationWriteDao.update(certification);
    }

}
