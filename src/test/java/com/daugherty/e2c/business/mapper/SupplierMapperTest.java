package com.daugherty.e2c.business.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonSupplier;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SupplierMapperTest {
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    
    @Mock
    private Hashids hashids;
    
    @InjectMocks
    private SupplierMapper supplierMapper = new SupplierMapper();
    
    @Test
    public void transformNewTransferObjectPopulatesSubsetOfFields() {
        JsonSupplier jsonSupplier = new JsonSupplier();
        jsonSupplier.setUsername("username");
        jsonSupplier.setPassword("password");
        jsonSupplier.setPasswordConfirmation("passwordConfirmation");
        jsonSupplier.setFirstName("first");
        jsonSupplier.setLastName("last");
        jsonSupplier.setCountry("country");
        jsonSupplier.setEmail("email");
        jsonSupplier.setIpAddress("123.456.789.012");

        Supplier entitySupplier = supplierMapper.toNewDomainObject(jsonSupplier, Locale.ENGLISH);

        assertThat(entitySupplier.getPendingUser().getUsername(), is(jsonSupplier.getUsername()));
        assertThat(entitySupplier.getPendingUser().getPassword(), is(jsonSupplier.getPassword()));
        assertThat(entitySupplier.getPendingUser().getPasswordConfirmation(), is(jsonSupplier.getPasswordConfirmation()));
        assertThat(entitySupplier.getContact().getFirstName(), is(jsonSupplier.getFirstName()));
        assertThat(entitySupplier.getContact().getLastName(), is(jsonSupplier.getLastName()));
        assertThat(entitySupplier.getContact().getCountry(), is(jsonSupplier.getCountry()));
        assertThat(entitySupplier.getContact().getEmailAddress(), is(jsonSupplier.getEmail()));
        assertThat(entitySupplier.getContact().getIpAddress(), is(jsonSupplier.getIpAddress()));
        assertThat(entitySupplier.getContact().getLanguage(), is(Language.ENGLISH));
    }
    
    @Test
    public void transformExistingTransferObjectPopulatesAllFieldsExceptUsernameAndSnapshotId() throws Exception {
        JsonSupplier jsonSupplier = new JsonSupplier();
        jsonSupplier.setFirstName("first");
        jsonSupplier.setLastName("last");
        jsonSupplier.setEmail("email");
        jsonSupplier.setSkypeRefId("skypeId");
        jsonSupplier.setMsnRefId("msdId");
        jsonSupplier.setIcqRefId("icqId");
        jsonSupplier.setEnglishCompanyName("english name");
        jsonSupplier.setDescription("description");
        jsonSupplier.setLogoUrl("logoUrl");
        jsonSupplier.setWebsite("http://website");
        jsonSupplier.setYearEstablished(2013);
        jsonSupplier.setNumberOfEmployees("1-10");
        jsonSupplier.setTotalAnnualSales("200-300");
        jsonSupplier.setBusinessTypes(Lists.newArrayList(BusinessType.AGENT.getName(), BusinessType.GOVERNMENT.getName()));
        jsonSupplier.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getName());
        jsonSupplier.setVersion(13);
        jsonSupplier.setCountry("country");
        // Supplier-specific fields
        jsonSupplier.setGender("Male");
        jsonSupplier.setExportLicenseUrl("licenseUrl");
        
        when(documentUrlFactory.removeDocumentUrl(jsonSupplier.getExportLicenseUrl(), Locale.ENGLISH)).thenReturn("licenseUrl");
        when(documentUrlFactory.removeDocumentUrl(jsonSupplier.getLogoUrl(), Locale.ENGLISH)).thenReturn("logoUrl");
        when(hashids.decode("jKNz4P4q")).thenReturn(new long[]{42L});

        Supplier entitySupplier = supplierMapper.toExistingDomainObject("jKNz4P4q", jsonSupplier, Locale.ENGLISH);

        assertThat(entitySupplier.getId(), is(42L));
        assertThat(entitySupplier.getPendingUser(), is(nullValue()));
        assertThat(entitySupplier.getContact().getFirstName(), is(jsonSupplier.getFirstName()));
        assertThat(entitySupplier.getContact().getLastName(), is(jsonSupplier.getLastName()));
        assertThat(entitySupplier.getContact().getEmailAddress(), is(jsonSupplier.getEmail()));
        assertThat(entitySupplier.getContact().getSkypeRefId(), is(jsonSupplier.getSkypeRefId()));
        assertThat(entitySupplier.getContact().getMsnRefId(), is(jsonSupplier.getMsnRefId()));
        assertThat(entitySupplier.getContact().getIcqRefId(), is(jsonSupplier.getIcqRefId()));
        assertThat(entitySupplier.getCompany().getEnglishName(), is(jsonSupplier.getEnglishCompanyName()));
        assertThat(entitySupplier.getCompany().getDescription(), is(jsonSupplier.getDescription()));
        assertThat(entitySupplier.getCompany().getLogoRefId(), is(jsonSupplier.getLogoUrl()));
        assertThat(entitySupplier.getCompany().getWebsite(), is(jsonSupplier.getWebsite()));
        assertThat(entitySupplier.getCompany().getYearEstablished(), is(jsonSupplier.getYearEstablished()));
        assertThat(entitySupplier.getCompany().getNumberOfEmployees(), is(jsonSupplier.getNumberOfEmployees()));
        assertThat(entitySupplier.getCompany().getTotalAnnualSales(), is(jsonSupplier.getTotalAnnualSales()));
        assertThat(entitySupplier.getCompany().getBusinessTypes().size(), is(jsonSupplier.getBusinessTypes().size()));
        assertThat(entitySupplier.getCompany().getBusinessTypes().get(0).getName(), is(jsonSupplier.getBusinessTypes().get(0)));
        assertThat(entitySupplier.getCompany().getBusinessTypes().get(1).getName(), is(jsonSupplier.getBusinessTypes().get(1)));
        assertThat(entitySupplier.getApprovalStatus().getName(), is(jsonSupplier.getApprovalStatus()));
        assertThat(entitySupplier.getVersion(), is(jsonSupplier.getVersion()));
        assertThat(entitySupplier.getContact().getCountry(), is(jsonSupplier.getCountry()));
        // Supplier-specific fields
        assertThat(entitySupplier.getContact().getGender().getReadableName(), is(jsonSupplier.getGender()));
        assertThat(entitySupplier.getExportLicenseRefId(), is(jsonSupplier.getExportLicenseUrl()));
    }

}
