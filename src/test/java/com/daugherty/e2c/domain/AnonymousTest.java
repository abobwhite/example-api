package com.daugherty.e2c.domain;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class AnonymousTest {

    @Test
    public void validateBusinessTelephoneHasErrorWhenMoreThan21Characters() {
        Contact contact = new Contact(null, null, "First", "Name", "Country", null, "emailAddres@email.com", null,
                null, null, StringUtils.repeat("M", 21), null, null, null);
        Company company = new Company("English Company Name", null, null, null, null, "website@website.com", null,
                null, null, null, null);

        Anonymous anonymousParty = new Anonymous(contact, company);

        ValidationError errors = anonymousParty.validate();

        assertTrue(errors.getErrors().containsEntry(Supplier.BUSINESS_PHONE_SERIAL_PROPERTY,
                Validatable.PARTY_BUSINESS_TELEPHONE_LENGTH));
    }

    @Test
    public void isValidWhenAllRequiredFieldsArePresent() {
        Contact contact = new Contact(null, null, "First", "Name", "Country", null, "emailAddres@email.com", null,
                null, null, "+12347439", null, null, null);
        Company company = new Company("English Company Name", null, null, null, null, "website@website.com", null,
                null, null, null, null);

        Anonymous anonymousParty = new Anonymous(contact, company);

        ValidationError validationError = anonymousParty.validate();

        assertTrue(validationError.getErrors().isEmpty());
    }

}
