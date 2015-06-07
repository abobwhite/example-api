package com.daugherty.e2c.business.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.service.json.JsonParty;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class PartyMapperTest {
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    
    @InjectMocks
    private PartyMapper partyMapper = new PartyMapper();
    
    @Test
    public void transformNewTransferObjectPopulatesCompanyFields() {
        JsonParty jsonParty = new JsonParty();
        jsonParty.setEnglishCompanyName("english company name");
        jsonParty.setDescription("description");
        jsonParty.setBusinessTypes(Lists.newArrayList(BusinessType.AGENT.getName(), BusinessType.BUSINESS_SERVICE.getName()));
        jsonParty.setNumberOfEmployees("101 - 500");
        jsonParty.setWebsite("www.e2cRocks.com");
        jsonParty.setYearEstablished(2013);
        jsonParty.setTotalAnnualSales("$1,000,000");
        
        when(documentUrlFactory.removeDocumentUrl(jsonParty.getLogoUrl(),Locale.ENGLISH)).thenReturn("logoUrl");

        Company company = partyMapper.toCompanyDomainObject(jsonParty, "chinese company name", "total imports", "video url", documentUrlFactory, Locale.ENGLISH);

        assertThat(company.getEnglishName(), is(jsonParty.getEnglishCompanyName()));
        assertThat(company.getDescription(), is(jsonParty.getDescription()));
        assertTrue(company.getBusinessTypes().contains(BusinessType.AGENT));
        assertTrue(company.getBusinessTypes().contains(BusinessType.BUSINESS_SERVICE));
        assertThat(company.getNumberOfEmployees(), is(jsonParty.getNumberOfEmployees()));
        assertThat(company.getWebsite(), is("http://" + jsonParty.getWebsite()));
        assertThat(company.getYearEstablished(), is(jsonParty.getYearEstablished()));
        assertThat(company.getTotalAnnualSales(), is(jsonParty.getTotalAnnualSales()));
        assertThat(company.getLogoRefId(), is("logoUrl"));
        assertThat(company.getChineseName(), is("chinese company name"));
        assertThat(company.getTotalImportAmount(), is("total imports"));
        assertThat(company.getVideoRefId(), is("video url"));
    }
    
    @Test
    public void transformNewTransferObjectPopulatesContactFields() {
        JsonParty jsonParty = new JsonParty();
        jsonParty.setFirstName("Bob");
        jsonParty.setLastName("White");
        jsonParty.setCountry("New Zealand");
        jsonParty.setProvince("province");
        jsonParty.setEmail("bob.white@quail.com");
        jsonParty.setSkypeRefId("skypppee");
        jsonParty.setMsnRefId("msn is old");
        jsonParty.setIcqRefId("ICQ?");
        jsonParty.setBusinessTelephoneNumber("8675309");
        jsonParty.setIpAddress("192.94.40.41");
        jsonParty.setRegistrationDate(new Date());

        Contact contact = partyMapper.toContactDomainObject(jsonParty, "title", "Male", Locale.ENGLISH);

        assertThat(contact.getFirstName(), is(jsonParty.getFirstName()));
        assertThat(contact.getLastName(), is(jsonParty.getLastName()));
        assertThat(contact.getCountry(), is(jsonParty.getCountry()));
        assertThat(contact.getProvince(), is(jsonParty.getProvince()));
        assertThat(contact.getEmailAddress(), is(jsonParty.getEmail()));
        assertThat(contact.getSkypeRefId(), is(jsonParty.getSkypeRefId()));
        assertThat(contact.getMsnRefId(), is(jsonParty.getMsnRefId()));
        assertThat(contact.getIcqRefId(), is(jsonParty.getIcqRefId()));
        assertThat(contact.getBusinessTelephoneNumber(), is(jsonParty.getBusinessTelephoneNumber()));
        assertThat(contact.getIpAddress(), is(jsonParty.getIpAddress()));
        assertThat(contact.getRegistrationDate(), is(jsonParty.getRegistrationDate()));
        assertThat(contact.getTitle(), is("title"));
        assertThat(contact.getGender(), is(Gender.findByReadableName("Male")));
    }
    
    @Test
    public void transformNewTransferObjectPopulatesPendingUser() {
        JsonParty jsonParty = new JsonParty();
        jsonParty.setUsername("bobwhite");
        jsonParty.setPassword("B0bWh1t3Qua1l");
        jsonParty.setPasswordConfirmation("B0bWh1t3Qua1l");

        PendingUser pendingUser = partyMapper.toPendingUserObject(jsonParty);

        assertThat(pendingUser.getUsername(), is(jsonParty.getUsername()));
        assertThat(pendingUser.getPassword(), is(jsonParty.getPassword()));
        assertThat(pendingUser.getPasswordConfirmation(), is(jsonParty.getPasswordConfirmation()));
    }
}
