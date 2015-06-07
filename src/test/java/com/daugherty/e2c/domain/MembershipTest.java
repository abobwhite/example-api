package com.daugherty.e2c.domain;

import static org.junit.Assert.assertTrue;

import org.joda.time.DateMidnight;
import org.junit.Test;

public class MembershipTest {

    @Test
    public void validateErrorWhenPurchasePriceIsNull() {
        Membership membership = new Membership(42L, null, new MembershipLevel(1), null, null, null, null, null, null,
                null, null, null, null, null, null);

        ValidationError errors = membership.validate();

        assertTrue(errors.getErrors().containsEntry(Membership.PURCHASE_PRICE_SERIAL_PROPERTY,
                Validatable.PURCHASE_PRICE_REQUIRED));
    }

    @Test
    public void validateErrorWhenPaymentAmountIsNull() {
        Membership membership = new Membership(42L, null, new MembershipLevel(1), ApprovalStatus.PAID, null, null,
                null, null, null, null, null, null, null, null, null);

        ValidationError errors = membership.validate();

        assertTrue(errors.getErrors().containsEntry(Membership.PAYMENT_AMOUNT_SERIAL_PROPERTY,
                Validatable.PAYMENT_AMOUNT_REQUIRED));
    }

    @Test
    public void validateErrorWhenEffectiveDateIsNull() {
        Membership membership = new Membership(42L, null, new MembershipLevel(1), null, null, null, null, null, null,
                null, null, null, null, null, null);

        ValidationError errors = membership.validate();

        assertTrue(errors.getErrors().containsEntry(Membership.EFFECTIVE_DATE_SERIAL_PROPERTY,
                Validatable.EFFECTIVE_DATE_REQUIRED));
    }

    @Test
    public void validateErrorWhenEffectiveDateIsAfterExpirationDate() {
        DateMidnight effectiveDate = new DateMidnight().plusDays(1);
        DateMidnight expirationDate = new DateMidnight();

        Membership membership = new Membership(42L, null, new MembershipLevel(1), null, null, null,
                effectiveDate.toDate(), expirationDate.toDate(), null, null, null, null, null, null, null);

        ValidationError errors = membership.validate();

        assertTrue(errors.getErrors().containsEntry(Membership.EFFECTIVE_DATE_SERIAL_PROPERTY,
                Validatable.EFFECTIVE_DATE_INVALID));
    }
}
