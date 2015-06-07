package com.daugherty.e2c.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

public class InquiryTest {

    @Test
    public void validateProductIdsHasErrorWhenNull() {
        Inquiry inquiry = new Inquiry(null, null, null, null, null, null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors().containsEntry(Inquiry.PRODUCT_IDS_SERIAL_PROPERTY,
                Validatable.MESSAGE_PRODUCT_IDS_REQUIRED));
    }

    @Test
    public void validateProductIdsHasErrorWhenEmpty() {
        Inquiry inquiry = new Inquiry(null, null, Lists.<Long> newArrayList(), null, null, null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors().containsEntry(Inquiry.PRODUCT_IDS_SERIAL_PROPERTY,
                Validatable.MESSAGE_PRODUCT_IDS_REQUIRED));
    }

    @Test
    public void validateProductIdsHasNoErrorWhenNotEmpty() {
        Inquiry inquiry = new Inquiry(null, null, Lists.newArrayList(242L), null, null, null);

        ValidationError errors = inquiry.validate();

        assertFalse(errors.getErrors().containsEntry(Inquiry.PRODUCT_IDS_SERIAL_PROPERTY,
                Validatable.MESSAGE_PRODUCT_IDS_REQUIRED));
    }

    @Test
    public void validateOriginatorHasErrorWhenNull() {
        Inquiry inquiry = new Inquiry(null, null, null, null, null, null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors().containsEntry(Inquiry.ORIGINATOR_SERIAL_PROPERTY,
                Validatable.MESSAGE_ORIGINATOR_REQUIRED));
    }

    @Test
    public void validateOriginatorHasNoErrorWhenNotNull() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        Inquiry inquiry = new Inquiry(null, party, null, null, null, null);

        ValidationError errors = inquiry.validate();

        assertFalse(errors.getErrors().containsEntry(Inquiry.ORIGINATOR_SERIAL_PROPERTY,
                Validatable.MESSAGE_ORIGINATOR_REQUIRED));
    }

    @Test
    public void validateSubjectHasErrorWhenNull() {
        Inquiry inquiry = new Inquiry(null, null, null, null, null, null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors().containsEntry(Inquiry.SUBJECT_SERIAL_PROPERTY,
                Validatable.MESSAGE_SUBJECT_REQUIRED));
    }

    @Test
    public void validateSubjectHasErrorWhenBlank() {
        Inquiry inquiry = new Inquiry(null, null, null, "  ", null, null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors().containsEntry(Inquiry.SUBJECT_SERIAL_PROPERTY,
                Validatable.MESSAGE_SUBJECT_REQUIRED));
    }

    @Test
    public void validateSubjectHasErrorWhenTooLong() {
        Inquiry inquiry = new Inquiry(null, null, null, StringUtils.repeat("A", 151), null, null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors()
                .containsEntry(Inquiry.SUBJECT_SERIAL_PROPERTY, Validatable.MESSAGE_SUBJECT_LENGTH));
    }

    @Test
    public void validateSubjectHasNoErrorWhenNotBlankAndWithinLengthLimit() {
        Inquiry inquiry = new Inquiry(null, null, null, StringUtils.repeat("A", 150), null, null);

        ValidationError errors = inquiry.validate();

        assertFalse(errors.getErrors().containsEntry(Inquiry.SUBJECT_SERIAL_PROPERTY,
                Validatable.MESSAGE_SUBJECT_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Inquiry.SUBJECT_SERIAL_PROPERTY,
                Validatable.MESSAGE_SUBJECT_LENGTH));
    }

    @Test
    public void validateBodyHasErrorWhenNull() {
        Inquiry inquiry = new Inquiry(null, null, null, null, null, null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors()
                .containsEntry(Inquiry.BODY_SERIAL_PROPERTY, Validatable.INTERACTION_BODY_REQUIRED));
    }

    @Test
    public void validateBodyHasErrorWhenBlank() {
        Inquiry inquiry = new Inquiry(null, null, null, null, "  ", null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors()
                .containsEntry(Inquiry.BODY_SERIAL_PROPERTY, Validatable.INTERACTION_BODY_REQUIRED));
    }

    @Test
    public void validateBodyHasErrorWhenTooLong() {
        Inquiry inquiry = new Inquiry(null, null, null, null, StringUtils.repeat("A", 4001), null);

        ValidationError errors = inquiry.validate();

        assertTrue(errors.getErrors().containsEntry(Inquiry.BODY_SERIAL_PROPERTY, Validatable.INTERACTION_BODY_LENGTH));
    }

    @Test
    public void validateBodyHasNoErrorWhenNotBlankAndWithinLengthLimit() {
        Inquiry inquiry = new Inquiry(null, null, null, null, StringUtils.repeat("A", 4000), null);

        ValidationError errors = inquiry.validate();

        assertFalse(errors.getErrors().containsEntry(Inquiry.BODY_SERIAL_PROPERTY,
                Validatable.INTERACTION_BODY_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Inquiry.BODY_SERIAL_PROPERTY, Validatable.INTERACTION_BODY_LENGTH));
    }

}
