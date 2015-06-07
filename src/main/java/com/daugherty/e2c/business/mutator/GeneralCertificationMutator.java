package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationWriteDao;

@Service("generalCertificationMutator")
public class GeneralCertificationMutator extends CertificationMutator {

	@Inject
	private CertificationWriteDao certificationWriteDao; 
	
	@Override
	protected Certification doCreate(Certification certification) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Certification doUpdate(Certification certification) {
		return certificationWriteDao.update(certification);
	}
	
	@Override
	protected void doDelete(Long certificationId) {
		certificationWriteDao.delete(certificationId);
	}

}
