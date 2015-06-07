package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class ProductCertificationMutatorTest {
	
	@Rule
    public ExpectedException expectedException = ExpectedException.none();
	
	@Mock
	public CertificationWriteDao certificationWriteDao;
	
	@Mock
    private ApprovalStateTransitionVisitor changeDataVisitor;
	
	@InjectMocks
	public CertificationMutator productCertificationMutator = new ProductCertificationMutator();
	
	@Test
	public void verifyThatDeleteThrowsUnsupportedOperationException(){
		expectedException.expect(UnsupportedOperationException.class);
		CertificationMutator certificationMutator = new ProductCertificationMutator();
		certificationMutator.delete(0L);		
	}
	
	@Test
	public void verifyThatUpdateThrowsUnsupportedOperationException(){
		
		Long certificationId = 666L;
		Calendar calendar = Calendar.getInstance();
		calendar.set(2013, 8, 18, 0, 0);
		
		Certification testCertification = new Certification(
				certificationId,
				"StandardUPD",
				"cert-numberUPD",
				"www.cert-linkUPD.com",
				null,
				"issued-byUPD",
				calendar.getTimeInMillis(),
				"scope-range!UPD",
				ApprovalStatus.DRAFT,
				13,
				779L				
				);
		
		expectedException.expect(UnsupportedOperationException.class);
		productCertificationMutator.update(testCertification);		
	}
	
	@Test
	public void verifyThatCreateHitsDao(){
		Certification certification = new Certification(42L, "standard",
				"certificateNbr", "http://localhost:????", 43L, "issuedBy", 1234567890L, "scopey",
				ApprovalStatus.APPROVED, 7, 100L);
		when(certificationWriteDao.insertProductCertification(certification)).thenReturn(certification);
		Certification createdCertification = productCertificationMutator.create(certification);
		assertThat(certification.getId(), is(createdCertification.getId()));
		assertThat(certification.getStandard(), is(createdCertification.getStandard()));
		assertThat(certification.getCertificateNumber(), is(createdCertification.getCertificateNumber()));
		assertThat(certification.getLink(), is(createdCertification.getLink()));
		assertThat(certification.getSupplierOrProductId(), is(createdCertification.getSupplierOrProductId()));
		assertThat(certification.getIssuedBy(), is(createdCertification.getIssuedBy()));
		assertThat(certification.getIssuedDate(), is(createdCertification.getIssuedDate()));
		assertThat(certification.getScopeRange(), is(createdCertification.getScopeRange()));
		assertThat(certification.getApprovalStatus(), is(createdCertification.getApprovalStatus()));
		assertThat(certification.getVersion(), is(createdCertification.getVersion()));
		assertThat(certification.getSnapshotId(), is(createdCertification.getSnapshotId()));
	}
}
