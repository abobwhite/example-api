package com.daugherty.e2c.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ProductMessageTest {

    @Test
    public void validateSubjectHasErrorWhenNull() {
    	ProductMessage message = new ProductMessage(null, null, null, null);

        ValidationError errors = message.validate();

        assertTrue(errors.getErrors().containsEntry(Message.SUBJECT_SERIAL_PROPERTY,
                Validatable.MESSAGE_SUBJECT_REQUIRED));
    }

    @Test
    public void validateSubjectHasErrorWhenBlank() {
    	ProductMessage message = new ProductMessage("  ", null, null, null);

        ValidationError errors = message.validate();

        assertTrue(errors.getErrors().containsEntry(Message.SUBJECT_SERIAL_PROPERTY,
                Validatable.MESSAGE_SUBJECT_REQUIRED));
    }

    @Test
    public void validateSubjectHasErrorWhenTooLong() {
    	ProductMessage message = new ProductMessage(StringUtils.repeat("A", 151), null, null, null);

        ValidationError errors = message.validate();

        assertTrue(errors.getErrors()
                .containsEntry(Message.SUBJECT_SERIAL_PROPERTY, Validatable.MESSAGE_SUBJECT_LENGTH));
    }

    @Test
    public void validateSubjectHasNoErrorWhenNotBlankAndWithinLengthLimit() {
    	ProductMessage message = new ProductMessage(StringUtils.repeat("A", 150), null, null, null);

        ValidationError errors = message.validate();

        assertFalse(errors.getErrors().containsEntry(Message.SUBJECT_SERIAL_PROPERTY,
                Validatable.MESSAGE_SUBJECT_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Message.SUBJECT_SERIAL_PROPERTY,
                Validatable.MESSAGE_SUBJECT_LENGTH));
    }

    @Test
    public void validateOriginatorHasErrorWhenNull() {
    	ProductMessage message = new ProductMessage(null, null, null, null);

        ValidationError errors = message.validate();

        assertTrue(errors.getErrors().containsEntry(Message.OTHER_PARTY_SERIAL_PROPERTY,
                Validatable.MESSAGE_ORIGINATOR_REQUIRED));
    }

    @Test
    public void validateOriginatorHasNoErrorWhenNotNull() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        ProductMessage message = new ProductMessage(null, party, null, null);

        ValidationError errors = message.validate();

        assertFalse(errors.getErrors().containsEntry(Message.OTHER_PARTY_SERIAL_PROPERTY,
                Validatable.MESSAGE_ORIGINATOR_REQUIRED));
    }

    @Test
    public void validateProductIdsHasErrorWhenEmpty() {
    	ProductMessage message = new ProductMessage(null, null, null, null);

        ValidationError errors = message.validate();

        assertTrue(errors.getErrors().containsEntry(ProductMessage.PRODUCT_IDS_SERIAL_PROPERTY,
                Validatable.MESSAGE_PRODUCT_IDS_REQUIRED));
    }

    @Test
    public void validateProductIdsHasNoErrorWhenNotEmpty() {
    	ProductMessage message = new ProductMessage(null, null, 242L, null);

        ValidationError errors = message.validate();

        assertFalse(errors.getErrors().containsEntry(ProductMessage.PRODUCT_IDS_SERIAL_PROPERTY,
                Validatable.MESSAGE_PRODUCT_IDS_REQUIRED));
    }

    @Test
    public void validateHasNoErrorsWhenInteractionIsNull() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        ProductMessage message = new ProductMessage("subject", party, 242L, null);

        ValidationError errors = message.validate();

        assertTrue(errors.getErrors().isEmpty());
    }

    @Test
    public void validateInteractionPropagatesAnyValidationErrors() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        ProductMessage message = new ProductMessage("subject", party, 242L, new Interaction(null, null, null, null));

        ValidationError errors = message.validate();

        assertTrue(errors.getErrors().containsEntry(Interaction.BODY_SERIAL_PROPERTY,
                Validatable.INTERACTION_BODY_REQUIRED));
    }

}
