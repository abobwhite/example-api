package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Certification;

public interface CertificationWriteDao {

    Certification insertSupplierCertification(Certification certification);

    Certification insertProductCertification(Certification certification);

    Certification update(Certification certification);

    void delete(Long certificationId);

    int deleteByPartyId(Long partyId);

}
