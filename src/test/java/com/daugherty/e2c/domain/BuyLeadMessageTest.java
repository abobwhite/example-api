package com.daugherty.e2c.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class BuyLeadMessageTest {

    @Test
    public void validateProductCategoryIdHasErrorWhenEmpty() {
        BuyLeadMessage message = new BuyLeadMessage(null, null, null, null);

        ValidationError errors = message.validate();

        assertTrue(errors.getErrors().containsEntry(BuyLeadMessage.BUY_LEAD_SERIAL_PROPERTY,
                Validatable.MESSAGE_BUY_LEAD_REQUIRED));
    }

    @Test
    public void validateProductCategoryIdHasNoErrorWhenNotEmpty() {
        BuyLeadMessage message = new BuyLeadMessage(null, null, new BuyLead(1L, new Party(10L), new ProductCategory(
                100L), new Date(), new Date()), null);

        ValidationError errors = message.validate();

        assertFalse(errors.getErrors().containsEntry(BuyLeadMessage.BUY_LEAD_SERIAL_PROPERTY,
                Validatable.MESSAGE_BUY_LEAD_REQUIRED));
    }

}
