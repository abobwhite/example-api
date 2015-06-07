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
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonBuyer;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyerMapperTest {
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    
    @Mock
    private Hashids hashids;
    
    @InjectMocks
    private BuyerMapper buyerMapper = new BuyerMapper();
    
  @Test
  public void transformNewTransferObjectPopulatesSubsetOfFields() throws Exception {
      JsonBuyer jsonBuyer = new JsonBuyer();
      jsonBuyer.setUsername("username");
      jsonBuyer.setPassword("password");
      jsonBuyer.setPasswordConfirmation("passwordConfirmation");
      jsonBuyer.setFirstName("first");
      jsonBuyer.setLastName("last");
      jsonBuyer.setCountry("country");
      jsonBuyer.setProvince("province");
      jsonBuyer.setEmail("email");

      Buyer entityBuyer = buyerMapper.toNewDomainObject(jsonBuyer, null);

      assertThat(entityBuyer.getPendingUser().getUsername(), is(jsonBuyer.getUsername()));
      assertThat(entityBuyer.getPendingUser().getPassword(), is(jsonBuyer.getPassword()));
      assertThat(entityBuyer.getPendingUser().getPasswordConfirmation(), is(jsonBuyer.getPasswordConfirmation()));
      assertThat(entityBuyer.getContact().getFirstName(), is(jsonBuyer.getFirstName()));
      assertThat(entityBuyer.getContact().getLastName(), is(jsonBuyer.getLastName()));
      assertThat(entityBuyer.getContact().getCountry(), is(jsonBuyer.getCountry()));
      assertThat(entityBuyer.getContact().getProvince(), is(jsonBuyer.getProvince()));
      assertThat(entityBuyer.getContact().getEmailAddress(), is(jsonBuyer.getEmail()));
  }

  @Test
  public void transformExistingTransferObjectPopulatesAllFieldsExceptUsernameAndSnapshotId() throws Exception {
      JsonBuyer jsonBuyer = new JsonBuyer();
      jsonBuyer.setFirstName("first");
      jsonBuyer.setLastName("last");
      jsonBuyer.setEmail("email");
      jsonBuyer.setSkypeRefId("skypeId");
      jsonBuyer.setMsnRefId("msdId");
      jsonBuyer.setIcqRefId("icqId");
      jsonBuyer.setEnglishCompanyName("english name");
      jsonBuyer.setDescription("description");
      jsonBuyer.setLogoUrl("logoUrl");
      jsonBuyer.setWebsite("http://website");
      jsonBuyer.setYearEstablished(2013);
      jsonBuyer.setNumberOfEmployees("1-10");
      jsonBuyer.setTotalAnnualSales("200-300");
      jsonBuyer.setBusinessTypes(Lists.newArrayList(BusinessType.AGENT.getName(), BusinessType.GOVERNMENT.getName()));
      jsonBuyer.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getName());
      jsonBuyer.setVersion(13);
      jsonBuyer.setCountry("country");
      // Buyer-specific fields
      jsonBuyer.setTitle("Doctor");
      jsonBuyer.setBusinessTelephoneNumber("2468013579");
      jsonBuyer.setChineseCompanyName("chinese name");
      jsonBuyer.setProvince("province");
      jsonBuyer.setTotalImportAmount("100-150");
      jsonBuyer.setImportLicenseUrl("licenseUrl");
      
      when(documentUrlFactory.removeDocumentUrl(jsonBuyer.getImportLicenseUrl(), Locale.CHINESE)).thenReturn("licenseUrl");
      when(documentUrlFactory.removeDocumentUrl(jsonBuyer.getLogoUrl(), Locale.CHINESE)).thenReturn("logoUrl");
      when(hashids.decode("jKNz4P4q")).thenReturn(new long[]{42L});

      Buyer entityBuyer = buyerMapper.toExistingDomainObject("jKNz4P4q", jsonBuyer, Locale.CHINESE);

      assertThat(entityBuyer.getId(), is(42L));
      assertThat(entityBuyer.getPendingUser(), is(nullValue()));
      assertThat(entityBuyer.getContact().getFirstName(), is(jsonBuyer.getFirstName()));
      assertThat(entityBuyer.getContact().getLastName(), is(jsonBuyer.getLastName()));
      assertThat(entityBuyer.getContact().getEmailAddress(), is(jsonBuyer.getEmail()));
      assertThat(entityBuyer.getContact().getSkypeRefId(), is(jsonBuyer.getSkypeRefId()));
      assertThat(entityBuyer.getContact().getMsnRefId(), is(jsonBuyer.getMsnRefId()));
      assertThat(entityBuyer.getContact().getIcqRefId(), is(jsonBuyer.getIcqRefId()));
      assertThat(entityBuyer.getCompany().getEnglishName(), is(jsonBuyer.getEnglishCompanyName()));
      assertThat(entityBuyer.getCompany().getDescription(), is(jsonBuyer.getDescription()));
      assertThat(entityBuyer.getCompany().getLogoRefId(), is(jsonBuyer.getLogoUrl()));
      assertThat(entityBuyer.getCompany().getWebsite(), is(jsonBuyer.getWebsite()));
      assertThat(entityBuyer.getCompany().getYearEstablished(), is(jsonBuyer.getYearEstablished()));
      assertThat(entityBuyer.getCompany().getNumberOfEmployees(), is(jsonBuyer.getNumberOfEmployees()));
      assertThat(entityBuyer.getCompany().getTotalAnnualSales(), is(jsonBuyer.getTotalAnnualSales()));
      assertThat(entityBuyer.getCompany().getBusinessTypes().size(), is(jsonBuyer.getBusinessTypes().size()));
      assertThat(entityBuyer.getCompany().getBusinessTypes().get(0).getName(), is(jsonBuyer.getBusinessTypes().get(0)));
      assertThat(entityBuyer.getCompany().getBusinessTypes().get(1).getName(), is(jsonBuyer.getBusinessTypes().get(1)));
      assertThat(entityBuyer.getApprovalStatus().getName(), is(jsonBuyer.getApprovalStatus()));
      assertThat(entityBuyer.getVersion(), is(jsonBuyer.getVersion()));
      assertThat(entityBuyer.getContact().getCountry(), is(jsonBuyer.getCountry()));
      // Buyer-specific fields
      assertThat(entityBuyer.getContact().getTitle(), is(jsonBuyer.getTitle()));
      assertThat(entityBuyer.getContact().getBusinessTelephoneNumber(), is(jsonBuyer.getBusinessTelephoneNumber()));
      assertThat(entityBuyer.getCompany().getChineseName(), is(jsonBuyer.getChineseCompanyName()));
      assertThat(entityBuyer.getContact().getProvince(), is(jsonBuyer.getProvince()));
      assertThat(entityBuyer.getCompany().getTotalImportAmount(), is(jsonBuyer.getTotalImportAmount()));
      assertThat(entityBuyer.getImportLicenseRefId(), is(jsonBuyer.getImportLicenseUrl()));
  }

}
