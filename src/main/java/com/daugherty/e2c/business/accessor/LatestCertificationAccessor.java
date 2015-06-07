package com.daugherty.e2c.business.accessor;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationReadDao;

@Service("latestCertificationAccessor")
public class LatestCertificationAccessor extends CertificationAccessor {
	@Inject
    private CertificationReadDao certificationReadDao;

	@Override
	public Certification doLoad(Long certificationId, Locale locale) {
		return certificationReadDao.loadLatest(certificationId, locale);
	}

}
