package com.daugherty.e2c.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class InteractionTest {

    @Test
    public void validateBodyHasErrorWhenNull() {
        Interaction interaction = new Interaction(null, null, null, null);

        ValidationError errors = interaction.validate();

        assertTrue(errors.getErrors().containsEntry(Interaction.BODY_SERIAL_PROPERTY,
                Validatable.INTERACTION_BODY_REQUIRED));
    }

    @Test
    public void validateBodyHasErrorWhenBlank() {
        Interaction interaction = new Interaction(null, "  ", null, null);

        ValidationError errors = interaction.validate();

        assertTrue(errors.getErrors().containsEntry(Interaction.BODY_SERIAL_PROPERTY,
                Validatable.INTERACTION_BODY_REQUIRED));
    }

    @Test
    public void validateBodyHasErrorWhenTooLong() {
        Interaction interaction = new Interaction(null, StringUtils.repeat("A", 4001), null, null);

        ValidationError errors = interaction.validate();

        assertTrue(errors.getErrors().containsEntry(Interaction.BODY_SERIAL_PROPERTY,
                Validatable.INTERACTION_BODY_LENGTH));
    }

    @Test
    public void validateBodyHasNoErrorWhenNotBlankAndWithinLengthLimit() {
        Interaction interaction = new Interaction(null, StringUtils.repeat("A", 4000), null, null);

        ValidationError errors = interaction.validate();

        assertFalse(errors.getErrors().containsEntry(Interaction.BODY_SERIAL_PROPERTY,
                Validatable.INTERACTION_BODY_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Interaction.BODY_SERIAL_PROPERTY,
                Validatable.INTERACTION_BODY_LENGTH));
    }

    @Test
    public void validateSenderHasErrorWhenNull() {
        Interaction interaction = new Interaction(null, null, null, null);

        ValidationError errors = interaction.validate();

        assertTrue(errors.getErrors().containsEntry(Interaction.SENDER_SERIAL_PROPERTY,
                Validatable.INTERACTION_SENDER_REQUIRED));
    }

    @Test
    public void validateSenderHasNoErrorWhenNotNull() {
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", null, company, null, PartyType.BUYER, null, null);

        Interaction interaction = new Interaction(null, null, party, null);

        ValidationError errors = interaction.validate();

        assertFalse(errors.getErrors().containsEntry(Interaction.SENDER_SERIAL_PROPERTY,
                Validatable.INTERACTION_SENDER_REQUIRED));
    }

    @Test
    public void validateReceiverHasErrorWhenNull() {
        Interaction interaction = new Interaction(null, null, null, null);

        ValidationError errors = interaction.validate();

        assertTrue(errors.getErrors().containsEntry(Interaction.RECEIVER_SERIAL_PROPERTY,
                Validatable.INTERACTION_RECEIVER_REQUIRED));
    }

    @Test
    public void validateReceiverHasNoErrorWhenNotNull() {
        Company company = new Company("supplier", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(666L, "pBVQwo0b", null, company, null, PartyType.SUPPLIER, null, null);

        Interaction interaction = new Interaction(null, null, null, party);

        ValidationError errors = interaction.validate();

        assertFalse(errors.getErrors().containsEntry(Interaction.RECEIVER_SERIAL_PROPERTY,
                Validatable.INTERACTION_RECEIVER_REQUIRED));
    }

}
