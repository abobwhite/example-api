package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;

@RunWith(MockitoJUnitRunner.class)
public class LatestBuyerAccessorTest {
    @Mock
    private BuyerReadDao buyerReadDao;
    @Mock
    private PartyBusinessTypeReadDao buyerBusinessTypeReadDao;

    @InjectMocks
    private final BuyerAccessor buyerAccessor = new LatestBuyerAccessor();

    @Test
    public void loadFindsExistingBuyerByRefId() {
        Long buyerId = 42L;
        String publicBuyerId = "jKNz4P4q";
        Long snapshotId = 43L;

        List<BusinessType> businessTypes = new ArrayList<BusinessType>();
        businessTypes.add(BusinessType.AGENT);
        businessTypes.add(BusinessType.BUSINESS_SERVICE);
        Contact contact = new Contact("title", null, "first_name", "last_name", "country", "province", "email_address",
                "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123", Language.CHINESE,
                new Date());
        Company company = new Company("english_name", "chinese_name", "description", businessTypes, "1-10", "website",
                2013, "200-300", "100-150", "logo_ref_id", "video_ref_id");
        Buyer buyer = new Buyer(buyerId, publicBuyerId, contact, company, ApprovalStatus.DRAFT, 1, snapshotId, "import_license_ref_id");

        when(buyerBusinessTypeReadDao.findBySnapshotId(snapshotId)).thenReturn(businessTypes);
        when(buyerReadDao.loadLatest(buyerId)).thenReturn(buyer);

        Buyer persistedBuyer = buyerAccessor.load(buyerId, Locale.ENGLISH);

        assertThat(persistedBuyer, is(buyer));
        assertThat(persistedBuyer.getContact(), is(contact));
        assertThat(persistedBuyer.getCompany(), is(company));
        assertThat(persistedBuyer.getCompany().getBusinessTypes(), is(businessTypes));
    }
}
