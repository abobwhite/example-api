package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.persistence.data.CertificationReadDao;

@RunWith(MockitoJUnitRunner.class)
public class CertificationAccessorTest {

    @Mock
    private CertificationReadDao certificationReadDao;

    @InjectMocks
    private final CertificationAccessor latestCertificationAccessor = new LatestCertificationAccessor();

    @Test
    public void loadFindsExistingCertificationByCertificationId() {
        Long certificationId = 777L;
        Long supplierId = 21L;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 17, 0, 0);

        Certification certification = new Certification(certificationId, "Standard", "cert-number",
                "www.cert-link.com", supplierId, "issued-by", calendar.getTimeInMillis(), "scope-range!",
                ApprovalStatus.DRAFT, 13, 779L);

        when(certificationReadDao.loadLatest(certificationId, Locale.ENGLISH)).thenReturn(certification);

        Certification persistedCertification = latestCertificationAccessor.load(certificationId, Locale.ENGLISH);

        assertThat(certification, is(persistedCertification));
    }

}
