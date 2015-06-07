package com.daugherty.e2c.persistence.data;

import java.util.Locale;

import com.daugherty.e2c.domain.Certification;

public interface CertificationReadDao {

	Certification loadLatest(Long certificationId, Locale locale);
	Certification loadApproved(Long certificationId, Locale locale);
	
}
