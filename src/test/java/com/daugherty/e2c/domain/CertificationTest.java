package com.daugherty.e2c.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CertificationTest {
	
	@Test
	public void validateStandardTooLong(){
		Certification testCertification = new Certification(null, 
				"1234567890123456789012345678901", "5678X", 
				"www.whocares.???", 42L, 
				"bob", 
				1234567890000L, 
				"scopey", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error = testCertification.validate();
		
		assertTrue(error.getErrors().containsEntry(Certification.STANDARD_SERIAL_PROPERTY, Validatable.CERTIFICATION_STANDARD_LENGTH));
	}
	
	@Test
	public void validateCertNumberTooLong(){
		Certification testCertification = new Certification(null, 
				"standard", "1234567890123456789012345678901", 
				"www.whocares.???", 42L, 
				"bob", 
				1234567890000L, 
				"scopey", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error = testCertification.validate();
		
		assertTrue(error.getErrors().containsEntry(
				Certification.CERTIFICATE_NUMBER_SERIAL_PROPERTY, Validatable.CERTIFICATION_CERTIFICATE_NUMBER_LENGTH));
	}
	
	@Test
	public void validateLinkTooLong(){
		Certification testCertification = new Certification(null, 
				"abc", "5678X", 
				"www.12345678901234567890123456789012345678901234567890.???", 42L, 
				"bob", 
				1234567890000L, 
				"scopey", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error = testCertification.validate();
		
		assertTrue(error.getErrors().containsEntry(
				Certification.LINK_SERIAL_PROPERTY, Validatable.CERTIFICATION_LINK_LENGTH));
	}
	
	@Test
	public void validateIssuedByTooLong(){
		Certification testCertification = new Certification(null, 
				"abc", "5678X", 
				"www.some-site.???", 42L, 
				"abcdefghijklmnopqrstuvwxyz78901", 
				1234567890000L, 
				"scopey", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error = testCertification.validate();
		
		assertTrue(error.getErrors().containsEntry(
				Certification.ISSUED_BY_SERIAL_PROPERTY, Validatable.CERTIFICATION_ISSUED_BY_LENGTH));
	}
	
	@Test
	public void validateScopeRangeTooLong(){
		Certification testCertification = new Certification(null, 
				"abc", "5678X", 
				"www.some-site.???", 42L, 
				"bob", 
				1234567890000L, 
				"abcdefghijklmnopqrstuvwxyz78901", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error = testCertification.validate();
		
		assertTrue(error.getErrors().containsEntry(
				Certification.SCOPE_RANGE_SERIAL_PROPERTY, Validatable.CERTIFICATION_SCOPE_RANGE_LENGTH));
	}
	
	@Test
	public void validateStandardRequired(){
		Certification testCertification = new Certification(null, 
				null, "5678X", 
				"www.some-site.???", 42L, 
				"bob", 
				1234567890000L, 
				"scopeRange", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error = testCertification.validate();
		
		assertTrue(error.getErrors().containsEntry(
				Certification.STANDARD_SERIAL_PROPERTY, Validatable.CERTIFICATION_STANDARD_REQUIRED));
		
		Certification testCertification2 = new Certification(null, 
				"", "5678X", 
				"www.some-site.???", 42L, 
				"bob", 
				1234567890000L, 
				"scopeRange", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error2 = testCertification2.validate();
		
		assertTrue(error2.getErrors().containsEntry(
				Certification.STANDARD_SERIAL_PROPERTY, Validatable.CERTIFICATION_STANDARD_REQUIRED));
	}
	
	@Test
	public void validateCertificateNumberRequired(){
		Certification testCertification = new Certification(null, 
				"certificationStandard", null, 
				"www.some-site.???", 42L, 
				"bob", 
				1234567890000L, 
				"scopeRange", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error = testCertification.validate();
		
		assertTrue(error.getErrors().containsEntry(
				Certification.CERTIFICATE_NUMBER_SERIAL_PROPERTY, Validatable.CERTIFICATION_CERTIFICATE_NUMBER_REQUIRED));
		
		Certification testCertification2 = new Certification(null, 
				"certificationStandard", "", 
				"www.some-site.???", 42L, 
				"bob", 
				1234567890000L, 
				"scopeRange", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error2 = testCertification2.validate();
		
		assertTrue(error2.getErrors().containsEntry(
				Certification.CERTIFICATE_NUMBER_SERIAL_PROPERTY, Validatable.CERTIFICATION_CERTIFICATE_NUMBER_REQUIRED));
	}
	
	@Test
	public void validIssueDateRequired(){
		Certification testCertification = new Certification(null, 
				"certificationStandard", "certificateNbr", 
				"www.some-site.???", 42L, 
				"bob", 
				null, 
				"scopeRange", 
				ApprovalStatus.DRAFT, 12,
				null 
		);
		
		ValidationError error = testCertification.validate();
		
		assertTrue(error.getErrors().containsEntry(
				Certification.ISSUE_DATE_SERIAL_PROPERTY, Validatable.CERTIFICATION_ISSUED_DATE_REQUIRED));
	}
}
