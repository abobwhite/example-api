package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.collect.Sets;

public class DiscountTest {

    @Test
    public void constructor() {
        Discount coupon = new Discount(1L, "DOLLAR", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
                "100 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.NEW));

        assertThat(coupon.getId(), is(1L));
        assertThat(coupon.getCode(), is("DOLLAR"));
        assertThat(coupon.getAmountType(), is(DiscountAmountType.DOLLAR));
        assertThat(coupon.getAmount(), is(BigDecimal.valueOf(100L)));
        assertThat(coupon.getDescription(), is("100 Dollars Off"));
        assertThat(coupon.getMembershipLevels().iterator().next(), is(2));
        assertThat(coupon.getDiscountTypes().iterator().next(), is(SubscriptionType.NEW));
    }

    @Test
    public void validateCodeHasErrorWhenEmpty() {
        Discount discount = new Discount(null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.CODE_SERIAL_PROPERTY, Validatable.DISCOUNT_CODE_REQUIRED));
    }

    @Test
    public void validateCodeHasErrorWhenLengthExceeds45() {
        Discount discount = new Discount(null, StringUtils.repeat("F", 46), null, null, null, null, null, null, null,
                null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.CODE_SERIAL_PROPERTY, Validatable.DISCOUNT_CODE_LENGTH));
    }

    @Test
    public void validateAmountTypeHasErrorWhenEmpty() {
        Discount discount = new Discount(null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.TYPE_SERIAL_PROPERTY,
                Validatable.DISCOUNT_AMOUNT_TYPE_REQUIRED));
    }

    @Test
    public void validateAmountHasErrorWhenEmpty() {
        Discount discount = new Discount(null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.DISCOUNT_AMOUNT_SERIAL_PROPERTY,
                Validatable.DISCOUNT_AMOUNT_REQUIRED));
    }

    @Test
    public void validateAmountHasErrorWhenExceeds100AndAmountTypeIsPercentEmpty() {
        Discount discount = new Discount(null, null, DiscountAmountType.PERCENT, BigDecimal.valueOf(100.1), null, null,
                null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.DISCOUNT_AMOUNT_SERIAL_PROPERTY,
                Validatable.DISCOUNT_PERCENT_AMOUNT_INVALID));
    }

    @Test
    public void validateDescriptionHasErrorWhenEmpty() {
        Discount discount = new Discount(null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.DESCRIPTION_SERIAL_PROPERTY,
                Validatable.DISCOUNT_DESCRIPTION_REQUIRED));
    }

    @Test
    public void validateDescriptionHasErrorWhenLengthExceeds45() {
        Discount discount = new Discount(null, null, null, null, StringUtils.repeat("F", 46), null, null, null, null,
                null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.DESCRIPTION_SERIAL_PROPERTY,
                Validatable.DISCOUNT_DESCRIPTION_LENGTH));
    }

    @Test
    public void validateEffectiveDateHasErrorWhenEmpty() {
        Discount discount = new Discount(null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.EFFECTIVE_SERIAL_PROPERTY,
                Validatable.DISCOUNT_EFFECTIVE_DATE_REQUIRED));
    }

    @Test
    public void validateExpirationDateHasErrorWhenEmpty() {
        Discount discount = new Discount(null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.EXPIRATION_SERIAL_PROPERTY,
                Validatable.DISCOUNT_EXPIRATION_DATE_REQUIRED));
    }

    @Test
    public void validateExpirationDateHasErrorWhenBeforeEffectiveDate() {
        Discount discount = new Discount(null, null, null, null, null, null, null, new DateTime().toDate(),
                new DateTime().minusDays(1).toDate(), null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.EXPIRATION_SERIAL_PROPERTY,
                Validatable.DISCOUNT_EXPIRATION_DATE_INVALID));
    }

    @Test
    public void validateMembershipLevelsHasErrorWhenEmpty() {
        Discount discount = new Discount(null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.MEMBERSHIP_LEVEL_SERIAL_PROPERTY,
                Validatable.DISCOUNT_MEMBERSHIP_LEVEL_REQUIRED));
    }

    @Test
    public void validateSubscriptionTypesHasErrorWhenEmpty() {
        Discount discount = new Discount(null, null, null, null, null, null, null, null, null, null, null);

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().containsEntry(Discount.SUBSCRIPTION_TYPE_SERIAL_PROPERTY,
                Validatable.DISCOUNT_SUBSCRIPTION_TYPE_REQUIRED));
    }

    @Test
    public void validateDiscountHasNoErrorsWhenValid() {
        Discount discount = new Discount(1L, "DOLLAR", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
                "100 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.NEW));

        ValidationError errors = discount.validate();

        assertTrue(errors.getErrors().isEmpty());
    }

}
