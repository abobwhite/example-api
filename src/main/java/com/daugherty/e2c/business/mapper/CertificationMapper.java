package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonCertification;

@Component
public class CertificationMapper {
    @Inject
    private Hashids hashids;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    
    // Use for supplier certification until product uses string ids as well
    public Certification toNewDomainObject(JsonCertification json, String publicSupplierId, Locale locale) {
        Long supplierId = hashids.decode(publicSupplierId)[0];
        return toNewDomainObject(json, supplierId, locale);
    }
    
    public Certification toNewDomainObject(JsonCertification json, Long supplierOrProductId, Locale locale) {
        return new Certification(json.getId(), json.getStandard(), json.getCertificateNumber(), documentUrlFactory.removeDocumentUrl(
                json.getLink(), locale), supplierOrProductId, json.getIssuedBy(), json.getIssuedDate().getTime(), json.getScopeRange(),
                ApprovalStatus.findByName(json.getApprovalStatus()), 1, json.getSnapshotId());
    }

    public Certification toExistingDomainObject(Long certificationId, JsonCertification json, Locale locale) {
        return new Certification(certificationId, json.getStandard(), json.getCertificateNumber(),
                documentUrlFactory.removeDocumentUrl(json.getLink(), locale), null, json.getIssuedBy(),
                json.getIssuedDate().getTime(), json.getScopeRange(), ApprovalStatus.findByName(json.getApprovalStatus()),
                json.getVersion(), json.getSnapshotId());
    }
}
