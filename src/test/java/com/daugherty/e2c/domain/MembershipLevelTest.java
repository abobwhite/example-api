package com.daugherty.e2c.domain;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class MembershipLevelTest {

    @Test
    public void validateErrorWhenNumberOfProductsIsNull() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.TEN, 6, null, null, null, null, null,
                null, null, null, 0, null, null, null, null, null, null, null);

        ValidationError errors = membershipLevel.validate();

        assertTrue(errors.getErrors().containsEntry(MembershipLevel.PRODUCT_COUNT_SERIAL_PROPERTY,
                Validatable.PRODUCT_COUNT_REQUIRED));
    }

    @Test
    public void validateErrorWhenNumberOfProductsExceedsMax() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.TEN, 6, 151, null, null, null, null,
                null, null, null, 0, null, null, null, null, null, null, null);

        ValidationError errors = membershipLevel.validate();

        assertTrue(errors.getErrors().containsEntry(MembershipLevel.PRODUCT_COUNT_SERIAL_PROPERTY,
                Validatable.PRODUCT_COUNT_LENGTH));
    }

    @Test
    public void validateErrorWhenNumberOfHotProductsIsNull() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.TEN, 6, null, null, null, null, null,
                null, null, null, 0, null, null, null, null, null, null, null);

        ValidationError errors = membershipLevel.validate();

        assertTrue(errors.getErrors().containsEntry(MembershipLevel.HOT_PRODUCT_COUNT_SERIAL_PROPERTY,
                Validatable.HOT_PRODUCT_COUNT_REQUIRED));
    }

    @Test
    public void validateErrorWhenNumberOfHotProductsExceedsMax() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.TEN, 6, null, null, null, null, 151,
                null, null, null, 0, null, null, null, null, null, null, null);

        ValidationError errors = membershipLevel.validate();

        assertTrue(errors.getErrors().containsEntry(MembershipLevel.HOT_PRODUCT_COUNT_SERIAL_PROPERTY,
                Validatable.HOT_PRODUCT_COUNT_LENGTH));
    }

    @Test
    public void validateErrorWhenAdditionalProductImageCountIsNull() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.TEN, 6, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = membershipLevel.validate();

        assertTrue(errors.getErrors().containsEntry(MembershipLevel.ADDITIONAL_PRODUCT_IMAGE_COUNT_SERIAL_PROPERTY,
                Validatable.ADDITIONAL_PRODUCT_IMAGE_COUNT_REQUIRED));
    }

    @Test
    public void validateErrorWhenAdditionalProductImageCountExceedsMax() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.TEN, 6, null, null, null, null, null,
                null, null, null, 3, null, null, null, null, null, null, null);

        ValidationError errors = membershipLevel.validate();

        assertTrue(errors.getErrors().containsEntry(MembershipLevel.ADDITIONAL_PRODUCT_IMAGE_COUNT_SERIAL_PROPERTY,
                Validatable.ADDITIONAL_PRODUCT_IMAGE__COUNT_LENGTH));
    }

    @Test
    public void validateErrorWhenPriceIsNull() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, null, 6, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null);

        ValidationError errors = membershipLevel.validate();

        assertTrue(errors.getErrors().containsEntry(MembershipLevel.PRICE_SERIAL_PROPERTY, Validatable.PRICE_REQUIRED));
    }
}
