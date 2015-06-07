package com.daugherty.e2c.business.accessor;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationReadDao;

@Service("approvedCertificationAccessor")
public class ApprovedCertificationAccessor extends CertificationAccessor {
	
	@Inject CertificationReadDao certificationReadDao; 

	@Override
	public Certification doLoad(Long certificationId, Locale locale) {
		return certificationReadDao.loadApproved(certificationId, locale);
	}

}
