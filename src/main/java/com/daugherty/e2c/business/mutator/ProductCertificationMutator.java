package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationWriteDao;

@Service("productCertificationMutator")
@Transactional
public class ProductCertificationMutator extends CertificationMutator {

	@Inject
    CertificationWriteDao certificationWriteDao;
    
	@Override
	protected final Certification doCreate(Certification certification) {
		return certificationWriteDao.insertProductCertification(certification);
	}
	
	@Override
	protected final Certification doUpdate(Certification certification) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected final void doDelete(Long certificationId) {
		throw new UnsupportedOperationException();
	}
}
