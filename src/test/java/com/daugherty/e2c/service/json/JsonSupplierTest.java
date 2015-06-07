package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
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
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.UrlBuilder;
import com.daugherty.e2c.security.Role;
import com.google.common.collect.Lists;

public class JsonSupplierTest {

    private final DocumentUrlFactory documentUrlFactory = new DocumentUrlFactory(new UrlBuilder("buyer.e2c",
            "global.e2c", "", ""), "/test/");

    @Test
    public void newSupplierConstructionWithNewDomainObjectPopulatesSubsetOfFieldsExcludingUserInformation() {
        Supplier entitySupplier = new Supplier(new PendingUser("username", null, null), new Contact("first", "last",
                "country", null, "email", "123.456.789.012", Language.ENGLISH, new Date()), null);

        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH);

        assertThat(jsonSupplier.getUsername(), is(nullValue()));
        assertThat(jsonSupplier.getPassword(), is(nullValue()));
        assertThat(jsonSupplier.getPasswordConfirmation(), is(nullValue()));
        assertThat(jsonSupplier.getFirstName(), is(entitySupplier.getContact().getFirstName()));
        assertThat(jsonSupplier.getLastName(), is(entitySupplier.getContact().getLastName()));
        assertThat(jsonSupplier.getCountry(), is(entitySupplier.getContact().getCountry()));
        assertThat(jsonSupplier.getEmail(), is(entitySupplier.getContact().getEmailAddress()));
        assertThat(jsonSupplier.getIpAddress(), is(entitySupplier.getContact().getIpAddress()));
        assertThat(jsonSupplier.getLanguage(), is(entitySupplier.getContact().getLanguage().getDisplayName()));
    }

    @Test
    public void existingSupplierConstructionWithFullyPopulatedDomainObjectPopulatesAllFieldsExceptUsername() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);

        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH);

        assertThat(jsonSupplier.getId(), is(entitySupplier.getPublicId()));
        assertThat(jsonSupplier.getUsername(), is(nullValue()));
        assertThat(jsonSupplier.getPassword(), is(nullValue()));
        assertThat(jsonSupplier.getPasswordConfirmation(), is(nullValue()));
        assertThat(jsonSupplier.getGender(), is(entitySupplier.getContact().getGender().getReadableName()));
        assertThat(jsonSupplier.getFirstName(), is(entitySupplier.getContact().getFirstName()));
        assertThat(jsonSupplier.getLastName(), is(entitySupplier.getContact().getLastName()));
        assertThat(jsonSupplier.getCountry(), is(entitySupplier.getContact().getCountry()));
        assertThat(jsonSupplier.getEmail(), is(entitySupplier.getContact().getEmailAddress()));
        assertThat(jsonSupplier.getSkypeRefId(), is(entitySupplier.getContact().getSkypeRefId()));
        assertThat(jsonSupplier.getMsnRefId(), is(entitySupplier.getContact().getMsnRefId()));
        assertThat(jsonSupplier.getIcqRefId(), is(entitySupplier.getContact().getIcqRefId()));
        assertThat(jsonSupplier.getEnglishCompanyName(), is(entitySupplier.getCompany().getEnglishName()));
        assertThat(jsonSupplier.getDescription(), is(entitySupplier.getCompany().getDescription()));
        assertThat(jsonSupplier.getNumberOfEmployees(), is(entitySupplier.getCompany().getNumberOfEmployees()));
        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
        assertThat(jsonSupplier.getYearEstablished(), is(entitySupplier.getCompany().getYearEstablished()));
        assertThat(jsonSupplier.getTotalAnnualSales(), is(entitySupplier.getCompany().getTotalAnnualSales()));
        assertThat(jsonSupplier.getLogoUrl(), is("//global.e2c/test/" + entitySupplier.getCompany().getLogoRefId()));
        assertThat(jsonSupplier.getVideoUrl(), is(entitySupplier.getCompany().getVideoRefId()));
        assertThat(jsonSupplier.getIpAddress(), is(entitySupplier.getContact().getIpAddress()));
        assertThat(jsonSupplier.getLanguage(), is(entitySupplier.getContact().getLanguage().getDisplayName()));
        assertThat(jsonSupplier.getLinks().get("products").split("\\?_=")[0],
                is("suppliers/jKNz4P4q/products"));
        assertThat(jsonSupplier.getLinks().get("certification").split("\\?_=")[0],
                is("certifications/1234"));
        assertThat(jsonSupplier.getLinks().get("messageSummary").split("\\?_=")[0],
                is("suppliers/jKNz4P4q/messageSummary"));

        assertThat(jsonSupplier.getBusinessTypes().size(), is(2));
        assertThat(jsonSupplier.getBusinessTypes().get(0), is(BusinessType.AGENT.getName()));
        assertThat(jsonSupplier.getBusinessTypes().get(1), is(BusinessType.GOVERNMENT.getName()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAndChatIdentitiesForAnonymousUsers() {
        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory, Locale.ENGLISH, null);

        assertThat(jsonSupplier.getEmail(), is(nullValue()));
        assertThat(jsonSupplier.getSkypeRefId(), is(nullValue()));
        assertThat(jsonSupplier.getMsnRefId(), is(nullValue()));
        assertThat(jsonSupplier.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedAdmin() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);

        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH, buildAuthentication(Role.ADMIN, null, null));

        assertThat(jsonSupplier.getEmail(), is(entitySupplier.getContact().getEmailAddress()));
        assertThat(jsonSupplier.getSkypeRefId(), is(entitySupplier.getContact().getSkypeRefId()));
        assertThat(jsonSupplier.getMsnRefId(), is(entitySupplier.getContact().getMsnRefId()));
        assertThat(jsonSupplier.getIcqRefId(), is(entitySupplier.getContact().getIcqRefId()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedBuyer() {
        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory,
                Locale.ENGLISH, buildAuthentication(Role.BUYER, 586L, "jKNzKB04"));

        assertThat(jsonSupplier.getEmail(), is(nullValue()));
        assertThat(jsonSupplier.getSkypeRefId(), is(nullValue()));
        assertThat(jsonSupplier.getMsnRefId(), is(nullValue()));
        assertThat(jsonSupplier.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedBuyerModerator() {
        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory,
                Locale.ENGLISH, buildAuthentication(Role.BUYER_MODERATOR, null, null));

        assertThat(jsonSupplier.getEmail(), is(nullValue()));
        assertThat(jsonSupplier.getSkypeRefId(), is(nullValue()));
        assertThat(jsonSupplier.getMsnRefId(), is(nullValue()));
        assertThat(jsonSupplier.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnoreEmailAndChatIdentitiesForAuthenticatedSupplierMatchingThisOne() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 42L, "jKNz4P4q"));

        assertThat(jsonSupplier.getEmail(), is(entitySupplier.getContact().getEmailAddress()));
        assertThat(jsonSupplier.getSkypeRefId(), is(entitySupplier.getContact().getSkypeRefId()));
        assertThat(jsonSupplier.getMsnRefId(), is(entitySupplier.getContact().getMsnRefId()));
        assertThat(jsonSupplier.getIcqRefId(), is(entitySupplier.getContact().getIcqRefId()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedSupplierNotMatchingThisOne() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 586L, "jKNzKB04"));

        assertThat(jsonSupplier.getEmail(), is(nullValue()));
        assertThat(jsonSupplier.getSkypeRefId(), is(nullValue()));
        assertThat(jsonSupplier.getMsnRefId(), is(nullValue()));
        assertThat(jsonSupplier.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnoreEmailAndChatIdentitiesForAuthenticatedSupplierModerator() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER_MODERATOR, null, null));

        assertThat(jsonSupplier.getEmail(), is(entitySupplier.getContact().getEmailAddress()));
        assertThat(jsonSupplier.getSkypeRefId(), is(entitySupplier.getContact().getSkypeRefId()));
        assertThat(jsonSupplier.getMsnRefId(), is(entitySupplier.getContact().getMsnRefId()));
        assertThat(jsonSupplier.getIcqRefId(), is(entitySupplier.getContact().getIcqRefId()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAndChatIdentitiesForAuthenticatedTranslator() {
        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory,
                Locale.ENGLISH, buildAuthentication(Role.TRANSLATOR, null, null));

        assertThat(jsonSupplier.getEmail(), is(nullValue()));
        assertThat(jsonSupplier.getSkypeRefId(), is(nullValue()));
        assertThat(jsonSupplier.getMsnRefId(), is(nullValue()));
        assertThat(jsonSupplier.getIcqRefId(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAnonymousUsers() {
        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory,
                Locale.ENGLISH, null);

        assertThat(jsonSupplier.getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAnonymousUsers() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(true);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH, null);

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedAdmin() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);

        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory,
                Locale.ENGLISH, buildAuthentication(Role.ADMIN, null, null));

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedAdmin() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(true);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.ADMIN, null, null));

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedBuyer() {
        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory,
                Locale.ENGLISH, buildAuthentication(Role.BUYER, 586L, "jKNzKB04"));

        assertThat(jsonSupplier.getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedBuyer() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(true);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.BUYER, null, null));

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedBuyerModerator() {
        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory,
                Locale.ENGLISH, buildAuthentication(Role.BUYER_MODERATOR, null, null));

        assertThat(jsonSupplier.getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedBuyerModerator() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(true);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.BUYER_MODERATOR, null, null));

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnoreNonPublicWebsiteForAuthenticatedSupplierMatchingThisOne() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 42L, "jKNz4P4q"));

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedSupplierNotMatchingThisOne() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 586L, "jKNzKB04"));

        assertThat(jsonSupplier.getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedSupplierNotMatchingThisOne() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(true);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 586L, "jKNzKB04"));

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnoreNonPublicWebsiteForAuthenticatedSupplierModerator() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(false);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER_MODERATOR, null, null));

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedTranslator() {
        JsonSupplier jsonSupplier = new JsonSupplier(buildFullyPopulatedDomainObject(false), documentUrlFactory,
                Locale.ENGLISH, buildAuthentication(Role.TRANSLATOR, null, null));

        assertThat(jsonSupplier.getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedTranslator() {
        Supplier entitySupplier = buildFullyPopulatedDomainObject(true);
        JsonSupplier jsonSupplier = new JsonSupplier(entitySupplier, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.TRANSLATOR, null, null));

        assertThat(jsonSupplier.getWebsite(), is(entitySupplier.getCompany().getWebsite()));
    }

    private Supplier buildFullyPopulatedDomainObject(boolean advancedWebAndMailCapabilityEnabled) {
        Membership membership = new Membership(1L, new MembershipLevel(1L, 1, BigDecimal.valueOf(10), 6, 10, 20, 20,
                false, 10, false, false, false, 10, false, advancedWebAndMailCapabilityEnabled, false, true,
                false, false, false));

        Contact contact = new Contact(null, Gender.MALE, "first", "last", "country", null, "email", "skype", "msn",
                "icq", null, "123.123.123.123", Language.ENGLISH, new Date());
        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.AGENT, BusinessType.GOVERNMENT);
        Company company = new Company("companyName", null, "description", businessTypes, "1-10", "website", 2013,
                "200-300", null, "logoRefId", "videoRefId");
        Supplier supplier = new Supplier(42L, "jKNz4P4q", contact, company, ApprovalStatus.APPROVED, 13, 586L,
                "exportLicenseRefId", null, 1234L);
        supplier.getMemberships().add(membership);

        return supplier;
    }

    private Authentication buildAuthentication(String role, Long partyId, String publicPartyId) {
        E2CUser user = new E2CUser("username", "password", true, 0, AuthorityUtils.createAuthorityList(role),
                new Party(partyId, publicPartyId), true);

        return new UsernamePasswordAuthenticationToken(user, null);
    }

}
