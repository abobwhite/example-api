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
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

@RunWith(MockitoJUnitRunner.class)
public class LatestSupplierAccessorTest {
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private PartyBusinessTypeReadDao businessTypeReadDao;

    @InjectMocks
    private final LatestSupplierAccessor supplierAccessor = new LatestSupplierAccessor();

    @Test
    public void loadFindsExistingSupplierWhoDoesNotHaveMembership() {
        Long supplierId = 42L;
        String publicSupplierId = "jKNz4P4q";
        Long snapshotId = 43L;

        List<BusinessType> businessTypes = new ArrayList<BusinessType>();
        businessTypes.add(BusinessType.AGENT);
        businessTypes.add(BusinessType.BUSINESS_SERVICE);
        when(businessTypeReadDao.findBySnapshotId(snapshotId)).thenReturn(businessTypes);

        Contact contact = new Contact("title", null, "first_name", "last_name", "country", "province", "email_address",
                "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123", Language.ENGLISH,
                new Date());
        Company company = new Company("english_name", "chinese_name", "description", businessTypes, "1-10", "website",
                2013, "200-300", "100-150", "logo_ref_id", "video_ref_id");
        Supplier supplier = new Supplier(supplierId, publicSupplierId, contact, company, ApprovalStatus.DRAFT, 1, snapshotId,
                "import_license_ref_id", null, null);
        when(supplierReadDao.loadLatest(supplierId, Locale.ENGLISH)).thenReturn(supplier);

        Supplier persistedSupplier = supplierAccessor.load(supplierId, Locale.ENGLISH);

        assertThat(persistedSupplier, is(supplier));
        assertThat(persistedSupplier.getContact(), is(contact));
        assertThat(persistedSupplier.getCompany(), is(company));
        assertThat(persistedSupplier.getCompany().getBusinessTypes(), is(businessTypes));
    }

    @Test
    public void loadFindsExistingSupplierWhoAlreadyHasMembership() {
        Long supplierId = 42L;
        String publicSupplierId = "jKNz4P4q";
        Long snapshotId = 43L;

        List<BusinessType> businessTypes = new ArrayList<BusinessType>();
        businessTypes.add(BusinessType.AGENT);
        businessTypes.add(BusinessType.BUSINESS_SERVICE);
        when(businessTypeReadDao.findBySnapshotId(snapshotId)).thenReturn(businessTypes);

        Contact contact = new Contact("title", null, "first_name", "last_name", "country", "province", "email_address",
                "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123", Language.ENGLISH,
                new Date());
        Company company = new Company("english_name", "chinese_name", "description", businessTypes, "1-10", "website",
                2013, "200-300", "100-150", "logo_ref_id", "video_ref_id");
        Supplier supplier = new Supplier(supplierId, publicSupplierId, contact, company, ApprovalStatus.DRAFT, 1, snapshotId,
                "import_license_ref_id", null, null);
        when(supplierReadDao.loadLatest(supplierId, Locale.ENGLISH)).thenReturn(supplier);

        Supplier persistedSupplier = supplierAccessor.load(supplierId, Locale.ENGLISH);

        assertThat(persistedSupplier, is(supplier));
        assertThat(persistedSupplier.getContact(), is(contact));
        assertThat(persistedSupplier.getCompany(), is(company));
        assertThat(persistedSupplier.getCompany().getBusinessTypes(), is(businessTypes));
    }

}
