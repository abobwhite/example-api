package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

public class BuyLeadTest {

    @Test
    public void constructor() {
        Party party = new Party(1L);
        ProductCategory productCategory = new ProductCategory(10L);
        Date effectiveDate = new DateTime().toDate();
        Date expirationDate = new DateTime().plusDays(7).toDate();
        BuyLead buyLead = new BuyLead(party, productCategory, effectiveDate, expirationDate);

        assertThat(buyLead.getRequester(), is(party));
        assertThat(buyLead.getProductCategory(), is(productCategory));
        assertThat(buyLead.getEffectiveDate(), is(effectiveDate));
        assertThat(buyLead.getExpirationDate(), is(expirationDate));
    }

    @Test
    public void validatePartyHasErrorWhenEmpty() {
        BuyLead buyLead = new BuyLead(null, null, null, null);

        ValidationError errors = buyLead.validate();

        assertTrue(errors.getErrors().containsEntry(BuyLead.PARTY_SERIAL_PROPERTY, Validatable.BUY_LEAD_PARTY_REQUIRED));
    }

    @Test
    public void validateProductCategoryHasErrorWhenLengthExceeds45() {
        BuyLead buyLead = new BuyLead(null, null, null, null);

        ValidationError errors = buyLead.validate();

        assertTrue(errors.getErrors().containsEntry(BuyLead.PRODUCT_CATEGORY_SERIAL_PROPERTY,
                Validatable.BUY_LEAD_PRODUCT_CATGEORY_REQUIRED));
    }

    @Test
    public void validateEffectiveDateHasErrorWhenEmpty() {
        BuyLead buyLead = new BuyLead(null, null, null, null);

        ValidationError errors = buyLead.validate();

        assertTrue(errors.getErrors().containsEntry(BuyLead.EFFECTIVE_DATE_SERIAL_PROPERTY,
                Validatable.BUY_LEAD_EFFECTIVE_DATE_REQUIRED));
    }

    @Test
    public void validateExpirationDateHasErrorWhenEmpty() {
        BuyLead buyLead = new BuyLead(null, null, null, null);

        ValidationError errors = buyLead.validate();

        assertTrue(errors.getErrors().containsEntry(BuyLead.EXPIRATION_DATE_SERIAL_PROPERTY,
                Validatable.BUY_LEAD_EXPIRATION_DATE_REQUIRED));
    }
}
