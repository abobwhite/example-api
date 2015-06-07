package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class GeneralCertificationMutatorTest {
	
	@Rule
    public ExpectedException expectedException = ExpectedException.none();
	
	@Mock
	private CertificationWriteDao certificationWriteDao;
	@Mock
    private ApprovalStateTransitionVisitor changeDataVisitor;
	
	@InjectMocks
	private final Mutator<Certification> generalCertificationMutator = new GeneralCertificationMutator();
	
	@Test
    public void updateValidCertificationUpdatesDatabase() {
		
		Long certificationId = 666L;
		Long productId = 51L;
		Calendar calendar = Calendar.getInstance();
		calendar.set(2013, 8, 18, 0, 0);
		
		Certification testCertification = new Certification(
				certificationId,
				"StandardUPD",
				"cert-numberUPD",
				"www.cert-linkUPD.com",
				productId,
				"issued-byUPD",
				calendar.getTimeInMillis(),
				"scope-range!UPD",
				ApprovalStatus.DRAFT,
				13,
				779L				
				);
		
		when(certificationWriteDao.update(testCertification)).thenReturn(testCertification);

        Certification mutatedCertification = generalCertificationMutator.update(testCertification);

        assertThat(mutatedCertification, is(notNullValue()));
        verify(certificationWriteDao).update(testCertification);    
	}
	
	@Test
	public void verifyThatInsertThrowsUnsupportedOperationException(){
		
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
		generalCertificationMutator.create(testCertification);		
	}
	
	@Test
	public void verifyDelete(){
		Long certificationId = 666L;
		generalCertificationMutator.delete(certificationId);
		verify(certificationWriteDao).delete(certificationId);
	}
}
