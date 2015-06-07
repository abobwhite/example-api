package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.UrlBuilder;
import com.daugherty.e2c.security.Role;
import com.google.common.collect.Lists;

public class JsonBuyerTest {

    private final DocumentUrlFactory documentUrlFactory = new DocumentUrlFactory(new UrlBuilder("buyer.e2c",
            "global.e2c", "", ""), "/test/");

    @Test
    public void newBuyerConstructionWithNewDomainObjectPopulatesSubsetOfFieldsExcludingUserInformation() {
        Buyer entityBuyer = new Buyer(new PendingUser("username", null, null), new Contact("first", "last", "country",
                "province", "email", "123.456.789.012", Language.CHINESE, new Date()), null);

        JsonBuyer jsonBuyer = new JsonBuyer(entityBuyer, documentUrlFactory, Locale.CHINESE);

        assertThat(jsonBuyer.getUsername(), is(nullValue()));
        assertThat(jsonBuyer.getPassword(), is(nullValue()));
        assertThat(jsonBuyer.getPasswordConfirmation(), is(nullValue()));
        assertThat(jsonBuyer.getFirstName(), is(entityBuyer.getContact().getFirstName()));
        assertThat(jsonBuyer.getLastName(), is(entityBuyer.getContact().getLastName()));
        assertThat(jsonBuyer.getCountry(), is(entityBuyer.getContact().getCountry()));
        assertThat(jsonBuyer.getProvince(), is(entityBuyer.getContact().getProvince()));
        assertThat(jsonBuyer.getEmail(), is(entityBuyer.getContact().getEmailAddress()));
        assertThat(jsonBuyer.getIpAddress(), is(entityBuyer.getContact().getIpAddress()));
        assertThat(jsonBuyer.getLanguage(), is(entityBuyer.getContact().getLanguage().getDisplayName()));
    }

    @Test
    public void existingBuyerConstructionWithFullyPopulatedDomainObjectPopulatesAllFields() {
        Buyer entityBuyer = buildFullyPopulatedDomainObject();

        JsonBuyer jsonBuyer = new JsonBuyer(entityBuyer, documentUrlFactory, Locale.CHINESE);

        assertThat(jsonBuyer.getId(), is(entityBuyer.getPublicId()));
        assertThat(jsonBuyer.getUsername(), is(nullValue()));
        assertThat(jsonBuyer.getPassword(), is(nullValue()));
        assertThat(jsonBuyer.getPasswordConfirmation(), is(nullValue()));
        assertThat(jsonBuyer.getTitle(), is(entityBuyer.getContact().getTitle()));
        assertThat(jsonBuyer.getFirstName(), is(entityBuyer.getContact().getFirstName()));
        assertThat(jsonBuyer.getLastName(), is(entityBuyer.getContact().getLastName()));
        assertThat(jsonBuyer.getCountry(), is(entityBuyer.getContact().getCountry()));
        assertThat(jsonBuyer.getProvince(), is(entityBuyer.getContact().getProvince()));
        assertThat(jsonBuyer.getEmail(), is(entityBuyer.getContact().getEmailAddress()));
        assertThat(jsonBuyer.getSkypeRefId(), is(entityBuyer.getContact().getSkypeRefId()));
        assertThat(jsonBuyer.getMsnRefId(), is(entityBuyer.getContact().getMsnRefId()));
        assertThat(jsonBuyer.getIcqRefId(), is(entityBuyer.getContact().getIcqRefId()));
        assertThat(jsonBuyer.getBusinessTelephoneNumber(), is(entityBuyer.getContact().getBusinessTelephoneNumber()));
        assertThat(jsonBuyer.getEnglishCompanyName(), is(entityBuyer.getCompany().getEnglishName()));
        assertThat(jsonBuyer.getChineseCompanyName(), is(entityBuyer.getCompany().getChineseName()));
        assertThat(jsonBuyer.getDescription(), is(entityBuyer.getCompany().getDescription()));
        assertThat(jsonBuyer.getNumberOfEmployees(), is(entityBuyer.getCompany().getNumberOfEmployees()));
        assertThat(jsonBuyer.getWebsite(), is(entityBuyer.getCompany().getWebsite()));
        assertThat(jsonBuyer.getYearEstablished(), is(entityBuyer.getCompany().getYearEstablished()));
        assertThat(jsonBuyer.getTotalAnnualSales(), is(entityBuyer.getCompany().getTotalAnnualSales()));
        assertThat(jsonBuyer.getTotalImportAmount(), is(entityBuyer.getCompany().getTotalImportAmount()));
        assertThat(jsonBuyer.getLogoUrl(), is("http://buyer.e2c/test/" + entityBuyer.getCompany().getLogoRefId()));
        assertThat(jsonBuyer.getIpAddress(), is(entityBuyer.getContact().getIpAddress()));
        assertThat(jsonBuyer.getLanguage(), is(entityBuyer.getContact().getLanguage().getDisplayName()));
        assertThat(jsonBuyer.getLinks().get("messageSummary").split("\\?_=")[0], is("buyers/jKNz4P4q/messageSummary"));

        assertThat(jsonBuyer.getBusinessTypes().size(), is(2));
        assertThat(jsonBuyer.getBusinessTypes().get(0), is(BusinessType.AGENT.getName()));
        assertThat(jsonBuyer.getBusinessTypes().get(1), is(BusinessType.GOVERNMENT.getName()));
    }

    @Test
    public void existingBuyerConstructionShouldIgnoreEmailAndChatIdentitiesForAnonymousUsers() {
        JsonBuyer jsonBuyer = new JsonBuyer(buildFullyPopulatedDomainObject(), documentUrlFactory, Locale.ENGLISH, null);

        assertThat(jsonBuyer.getEmail(), is(nullValue()));
        assertThat(jsonBuyer.getSkypeRefId(), is(nullValue()));
        assertThat(jsonBuyer.getMsnRefId(), is(nullValue()));
        assertThat(jsonBuyer.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingBuyerConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedAdmin() {
        JsonBuyer jsonBuyer = new JsonBuyer(buildFullyPopulatedDomainObject(), documentUrlFactory, Locale.CHINESE,
                buildAuthentication(Role.ADMIN, null, null));

        assertThat(jsonBuyer.getEmail(), is(nullValue()));
        assertThat(jsonBuyer.getSkypeRefId(), is(nullValue()));
        assertThat(jsonBuyer.getMsnRefId(), is(nullValue()));
        assertThat(jsonBuyer.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingBuyerConstructionShouldNotIgnoreEmailAndChatIdentitiesForAuthenticatedBuyerMatchingThisOne() {
        Buyer entityBuyer = buildFullyPopulatedDomainObject();
        JsonBuyer jsonBuyer = new JsonBuyer(entityBuyer, documentUrlFactory, Locale.CHINESE, buildAuthentication(
                Role.BUYER, 42L, "jKNz4P4q"));

        assertThat(jsonBuyer.getEmail(), is(entityBuyer.getContact().getEmailAddress()));
        assertThat(jsonBuyer.getSkypeRefId(), is(entityBuyer.getContact().getSkypeRefId()));
        assertThat(jsonBuyer.getMsnRefId(), is(entityBuyer.getContact().getMsnRefId()));
        assertThat(jsonBuyer.getIcqRefId(), is(entityBuyer.getContact().getIcqRefId()));
    }

    @Test
    public void existingBuyerConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedBuyerNotMatchingThisOne() {
        Buyer entityBuyer = buildFullyPopulatedDomainObject();
        JsonBuyer jsonBuyer = new JsonBuyer(entityBuyer, documentUrlFactory, Locale.CHINESE, buildAuthentication(
                Role.BUYER, 586L, "jKNzKB04"));

        assertThat(jsonBuyer.getEmail(), is(nullValue()));
        assertThat(jsonBuyer.getSkypeRefId(), is(nullValue()));
        assertThat(jsonBuyer.getMsnRefId(), is(nullValue()));
        assertThat(jsonBuyer.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingBuyerConstructionShouldNotIgnoreEmailAndChatIdentitiesForAuthenticatedBuyerModerator() {
        Buyer entityBuyer = buildFullyPopulatedDomainObject();
        JsonBuyer jsonBuyer = new JsonBuyer(entityBuyer, documentUrlFactory, Locale.CHINESE, buildAuthentication(
                Role.BUYER_MODERATOR, null, null));

        assertThat(jsonBuyer.getEmail(), is(entityBuyer.getContact().getEmailAddress()));
        assertThat(jsonBuyer.getSkypeRefId(), is(entityBuyer.getContact().getSkypeRefId()));
        assertThat(jsonBuyer.getMsnRefId(), is(entityBuyer.getContact().getMsnRefId()));
        assertThat(jsonBuyer.getIcqRefId(), is(entityBuyer.getContact().getIcqRefId()));
    }

    @Test
    public void existingBuyerConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedSupplier() {
        JsonBuyer jsonBuyer = new JsonBuyer(buildFullyPopulatedDomainObject(), documentUrlFactory, Locale.CHINESE,
                buildAuthentication(Role.SUPPLIER, 586L, "jKNzKB04"));

        assertThat(jsonBuyer.getEmail(), is(nullValue()));
        assertThat(jsonBuyer.getSkypeRefId(), is(nullValue()));
        assertThat(jsonBuyer.getMsnRefId(), is(nullValue()));
        assertThat(jsonBuyer.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingBuyerConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedSupplierModerator() {
        JsonBuyer jsonBuyer = new JsonBuyer(buildFullyPopulatedDomainObject(), documentUrlFactory, Locale.CHINESE,
                buildAuthentication(Role.SUPPLIER_MODERATOR, null, null));

        assertThat(jsonBuyer.getEmail(), is(nullValue()));
        assertThat(jsonBuyer.getSkypeRefId(), is(nullValue()));
        assertThat(jsonBuyer.getMsnRefId(), is(nullValue()));
        assertThat(jsonBuyer.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingBuyerConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedTranslator() {
        JsonBuyer jsonBuyer = new JsonBuyer(buildFullyPopulatedDomainObject(), documentUrlFactory, Locale.CHINESE,
                buildAuthentication(Role.TRANSLATOR, null, null));

        assertThat(jsonBuyer.getEmail(), is(nullValue()));
        assertThat(jsonBuyer.getSkypeRefId(), is(nullValue()));
        assertThat(jsonBuyer.getMsnRefId(), is(nullValue()));
        assertThat(jsonBuyer.getIcqRefId(), is(nullValue()));
    }

    private Buyer buildFullyPopulatedDomainObject() {
        Contact contact = new Contact("title", null, "first", "last", "country", "province", "email", "skype", "msn",
                "icq", "mobile", "123.123.123.123", Language.CHINESE, new Date());
        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.AGENT, BusinessType.GOVERNMENT);
        Company company = new Company("englishCompanyName", "chineseCompanyName", "description", businessTypes, "1-10",
                "website", 2013, "200-300", "100-150", "logoRefId", null);
        return new Buyer(42L, "jKNz4P4q", contact, company, ApprovalStatus.APPROVED, 13, 586L, "exportLicenseRefId");
    }

    private Authentication buildAuthentication(String role, Long partyId, String publicPartyId) {
        E2CUser user = new E2CUser("username", "password", true, 0, AuthorityUtils.createAuthorityList(role),
                new Party(partyId, publicPartyId), true);

        return new UsernamePasswordAuthenticationToken(user, null);
    }

}
