package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

public class PartyTest {

    @Test
    public void getEmailWithPersonalNameCombinesFirstAndLastNameWithEmailAddress() throws Exception {
        TestParty party = new TestParty(null, new Contact(null, null, "first", "last", null, null,
                "person@company.com", null, null, null, null, null, null, new Date()), null);
        assertThat(party.getEmailWithPersonalName(), is("first last <person@company.com>"));
    }

    @Test
    public void validateHasNoErrorsWhenPendingUserIsNull() {
        TestParty party = new TestParty(null, null, null);

        ValidationError errors = party.validate();

        assertFalse(errors.getErrors().containsKey(PendingUser.USERNAME_SERIAL_PROPERTY));
    }

    @Test
    public void validatePendingUserPropagatesAnyValidationErrors() {
        TestParty party = new TestParty(new PendingUser(null, null, null), null, null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_REQUIRED));
    }

    @Test
    public void validateEmailHasErrorWhenNull() {
        TestParty party = new TestParty(null, null, null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_REQUIRED));
    }

    @Test
    public void validateEmailHasErrorWhenEmpty() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null, "", null, null, null,
                null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_REQUIRED));
    }

    @Test
    public void validateEmailHasNoErrorWhenNotNull() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null, "person@company.com",
                null, null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertFalse(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_REQUIRED));
    }

    @Test
    public void validateEmailWithoutDotHasError() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null, "personatdotcom", null,
                null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_FORMAT));
    }

    @Test
    public void validateEmailWithSpaceHasError() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null, "person @test.com", null,
                null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_FORMAT));
    }

    @Test
    public void validateEmailWithTrailingPeriodHasError() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null, "person@test.", null,
                null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_FORMAT));
    }

    @Test
    public void validateEmailMatchingCorrectFormatHasNoError() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null,
                "person@subdomain.domain.com", null, null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertFalse(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_FORMAT));
    }

    @Test
    public void validateEmailStripsOffLeadingAndTrailingSpacesAndHasNoErrors() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null, " person@company.com ",
                null, null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertThat(party.getContact().getEmailAddress(), is("person@company.com"));
        assertFalse(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_FORMAT));
    }

    @Test
    public void validateFirstNameHasErrorWhenNull() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.FIRST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_FIRST_NAME_REQUIRED));
    }

    @Test
    public void validateFirstNameHasErrorWhenEmpty() {
        TestParty party = new TestParty(null, new Contact(null, null, "", null, null, null, null, null, null, null,
                null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.FIRST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_FIRST_NAME_REQUIRED));
    }

    @Test
    public void validateFirstNameHasNoErrorWhenNotNull() {
        TestParty party = new TestParty(null, new Contact(null, null, "firstName", null, null, null, null, null, null,
                null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertFalse(errors.getErrors().containsEntry(Party.FIRST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_FIRST_NAME_REQUIRED));
    }

    @Test
    public void validateFirstNameHasErrorWhenLengthExceeds30() {
        TestParty party = new TestParty(null, new Contact(null, null, StringUtils.repeat("F", 31), null, null, null,
                null, null, null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.FIRST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_FIRST_NAME_LENGTH));
    }

    @Test
    public void validateFirstNameHasNoErrorWhenLengthIs30() {
        TestParty party = new TestParty(null, new Contact(null, null, StringUtils.repeat("F", 30), null, null, null,
                null, null, null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertFalse(errors.getErrors().containsEntry(Party.FIRST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_FIRST_NAME_LENGTH));
    }

    @Test
    public void validateLastNameHasErrorWhenNull() {
        TestParty party = new TestParty(null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.LAST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_LAST_NAME_REQUIRED));
    }

    @Test
    public void validateLastNameHasErrorWhenEmpty() {
        TestParty party = new TestParty(null, new Contact(null, null, null, "", null, null, null, null, null, null,
                null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors().containsEntry(Party.LAST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_LAST_NAME_REQUIRED));
    }

    @Test
    public void validateLastNameHasNoErrorWhenNotNull() {
        TestParty party = new TestParty(null, new Contact(null, null, null, "last", null, null, null, null, null, null,
                null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertFalse(errors.getErrors().containsEntry(Party.LAST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_LAST_NAME_REQUIRED));
    }

    @Test
    public void validateLastNameHasErrorWhenLengthExceeds30() {
        TestParty party = new TestParty(null, new Contact(null, null, null, StringUtils.repeat("L", 31), null, null,
                null, null, null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertTrue(errors.getErrors()
                .containsEntry(Party.LAST_NAME_SERIAL_PROPERTY, Validatable.PARTY_LAST_NAME_LENGTH));
    }

    @Test
    public void validateLastNameHasNoErrorWhenLengthIs30() {
        TestParty party = new TestParty(null, new Contact(null, null, null, StringUtils.repeat("L", 30), null, null,
                null, null, null, null, null, null, null, new Date()), null);

        ValidationError errors = party.validate();

        assertFalse(errors.getErrors().containsEntry(Party.LAST_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_LAST_NAME_LENGTH));
    }

    @Test
    public void validateSkypeIdHasNoLengthErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.SKYPE_SERIAL_PROPERTY, Validatable.PARTY_SKYPE_LENGTH));
    }

    @Test
    public void validateSkypeIdHasNoLengthErrorWhenLengthIs32() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null,
                StringUtils.repeat("S", 32), null, null, null, null, null, new Date()), null, ApprovalStatus.DRAFT,
                null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.SKYPE_SERIAL_PROPERTY, Validatable.PARTY_SKYPE_LENGTH));
    }

    @Test
    public void validateSkypeIdHasLengthErrorWhenLengthGreaterThan32() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null,
                StringUtils.repeat("S", 33), null, null, null, null, null, new Date()), null, ApprovalStatus.DRAFT,
                null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Party.SKYPE_SERIAL_PROPERTY, Validatable.PARTY_SKYPE_LENGTH));
    }

    public void validateMSNIdHasNoLengthErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.MSN_SERIAL_PROPERTY, Validatable.PARTY_SKYPE_LENGTH));
    }

    @Test
    public void validateMSNIdHasNoLengthErrorWhenLengthIs75() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, StringUtils.repeat(
                "M", 75), null, null, null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.MSN_SERIAL_PROPERTY, Validatable.PARTY_MSN_LENGTH));
    }

    @Test
    public void validateMSNIdHasLengthErrorWhenLengthGreaterThan75() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, StringUtils.repeat(
                "M", 76), null, null, null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);
        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Party.MSN_SERIAL_PROPERTY, Validatable.PARTY_MSN_LENGTH));
    }

    public void validateICQIdHasNoLengthErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.ICQ_SERIAL_PROPERTY, Validatable.PARTY_ICQ_LENGTH));
    }

    @Test
    public void validateICQIdHasNoLengthErrorWhenLengthIs75() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null,
                StringUtils.repeat("I", 75), null, null, null, new Date()), null, ApprovalStatus.DRAFT, null, null,
                null);

        ValidationError errors = buyer.validate();
        assertFalse(errors.getErrors().containsEntry(Party.ICQ_SERIAL_PROPERTY, Validatable.PARTY_ICQ_LENGTH));
    }

    @Test
    public void validateICQIdHasLengthErrorWhenLengthGreaterThan75() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null,
                StringUtils.repeat("I", 76), null, null, null, new Date()), null, ApprovalStatus.DRAFT, null, null,
                null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Party.ICQ_SERIAL_PROPERTY, Validatable.PARTY_ICQ_LENGTH));
    }

    @Test
    public void validateCompanyNameEnglishHasNoErrorWhenNotNull() {
        Buyer buyer = new Buyer(null, null, null, new Company("English Name", null, null, null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.ENGLISH_COMPANY_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_ENGLISH_COMPANY_NAME_REQUIRED));
    }

    @Test
    public void validateCompanyNameEnglishHasErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, null, null, null, null, null, null, null,
                null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Party.ENGLISH_COMPANY_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_ENGLISH_COMPANY_NAME_REQUIRED));
    }

    @Test
    public void validateCompanyNameEnglishHasNoErrorWhenLengthExactly60() {
        Buyer buyer = new Buyer(null, null, null, new Company(StringUtils.repeat("N", 60), null, null, null, null, null,
                null, null, null, null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.ENGLISH_COMPANY_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_ENGLISH_COMPANY_NAME_LENGTH));
    }

    @Test
    public void validateCompanyNameEnglishHasErrorWhenLengthExceeds60() {
        Buyer buyer = new Buyer(null, null, null, new Company(StringUtils.repeat("N", 61), null, null, null, null, null,
                null, null, null, null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Party.ENGLISH_COMPANY_NAME_SERIAL_PROPERTY,
                Validatable.PARTY_ENGLISH_COMPANY_NAME_LENGTH));
    }

    @Test
    public void validateCompanyDescriptionHasNoErrorWhenNotNull() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, "Description", null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.COMPANY_DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PARTY_COMPANY_DESCRIPTION_REQUIRED));
    }

    @Test
    public void validateCompanyDescriptionHasErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, null, null, null, null, null, null, null,
                null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Party.COMPANY_DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PARTY_COMPANY_DESCRIPTION_REQUIRED));
    }

    @Test
    public void validateCompanyDescriptionHasNoErrorWhenNotNullAndLengthIsLessThan4000() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, "Description", null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.COMPANY_DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PARTY_COMPANY_DESCRIPTION_LENGTH));
    }

    @Test
    public void validateCompanyDescriptionHasNoErrorWhenNotNullAndLengthIs4000() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, StringUtils.repeat("D", 4000), null, null, null,
                null, null, null, null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();
        assertFalse(errors.getErrors().containsEntry(Party.COMPANY_DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PARTY_COMPANY_DESCRIPTION_LENGTH));
    }

    @Test
    public void validateCompanyDescriptionHasErrorWhenNotNullAndLengthIsGreaterThan4000() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, StringUtils.repeat("D", 4001), null, null, null,
                null, null, null, null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();
        assertTrue(errors.getErrors().containsEntry(Party.COMPANY_DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PARTY_COMPANY_DESCRIPTION_LENGTH));
    }

    @Test
    public void validateBusinessTypesHasNoErrorWhenNotNull() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, Lists.newArrayList(BusinessType.AGENT), null,
                null, null, null, null, null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();
        assertFalse(errors.getErrors().containsEntry(Party.BUSINESS_TYPES_SERIAL_PROPERTY,
                Validatable.PARTY_BUSINESS_TYPES_REQUIRED));
    }

    @Test
    public void validateBusinessTypeHasErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, null, null, null, null, null, null, null,
                null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();
        assertTrue(errors.getErrors().containsEntry(Party.BUSINESS_TYPES_SERIAL_PROPERTY,
                Validatable.PARTY_BUSINESS_TYPES_REQUIRED));
    }

    @Test
    public void validateBusinessTypeHasErrorWhenExceeds3() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, Lists.newArrayList(BusinessType.AGENT,
                BusinessType.BUSINESS_SERVICE, BusinessType.DISTRIBUTOR_WHOLESALER, BusinessType.MANUFACTURER), null,
                null, null, null, null, null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();
        assertTrue(errors.getErrors().containsEntry(Party.BUSINESS_TYPES_SERIAL_PROPERTY,
                Validatable.PARTY_BUSINESS_TYPES_LENGTH));
    }

    @Test
    public void validateCountryHasNoErrorWhenNotNull() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, "country", null, null, null, null, null,
                null, null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.COUNTRY_SERIAL_PROPERTY, Validatable.PARTY_COUNTRY));
    }

    @Test
    public void validateCountryHasErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Party.COUNTRY_SERIAL_PROPERTY, Validatable.PARTY_COUNTRY));
    }

    @Test
    public void validateWebsiteHasNoLengthErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, null, null, null, null, null, null, null,
                null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.WEBSITE_SERIAL_PROPERTY, Validatable.PARTY_WEBSITE_LENGTH));
    }

    @Test
    public void validateWebsiteHasNoLengthErrorWhenLengthIs70() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, null, null, StringUtils.repeat("W",
                70 - UrlBuilder.HTTP_PREFIX.length()), null, null, null, null, null), ApprovalStatus.DRAFT, null, null,
                null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Party.WEBSITE_SERIAL_PROPERTY, Validatable.PARTY_WEBSITE_LENGTH));
    }

    @Test
    public void validateWebsiteHasLengthErrorWhenLengthGreaterThan70() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, null, null, StringUtils.repeat("W",
                71 - UrlBuilder.HTTP_PREFIX.length()), null, null, null, null, null), ApprovalStatus.DRAFT, null, null,
                null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Party.WEBSITE_SERIAL_PROPERTY, Validatable.PARTY_WEBSITE_LENGTH));
    }

    @Test
    public void visitTransitionsState() throws Exception {
        TestParty party = new TestParty(null, null, null);

        party.visit(new ApprovalStateTransitionVisitor() {
            @Override
            public int getNewVersion(int currentVersion, ApprovalStatus currentApprovalStatus) {
                return 13;
            }

            @Override
            public ApprovalStatus getNewApprovalStatus(ApprovalStatus currentApprovalStatus) {
                return ApprovalStatus.DRAFT;
            }
        });

        assertThat(party.getVersion(), is(13));
        assertThat(party.getApprovalStatus(), is(ApprovalStatus.DRAFT));
    }

    private class TestParty extends Party {
        private static final long serialVersionUID = 1L;

        public TestParty(PendingUser pendingUser, Contact contact, Company company) {
            super(pendingUser, contact, company, PartyType.ANONYMOUS);
        }

    }

}
