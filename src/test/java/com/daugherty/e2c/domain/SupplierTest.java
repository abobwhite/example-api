package com.daugherty.e2c.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateMidnight;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class SupplierTest {

    @Test
    public void validateCountryHasErrorWhenNull() {
        Supplier supplier = new Supplier(null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, null, null, null), null);

        ValidationError errors = supplier.validate();

        assertTrue(errors.getErrors().containsEntry(Supplier.COUNTRY_SERIAL_PROPERTY, Validatable.PARTY_COUNTRY));
    }

    @Test
    public void validateCountryHasErrorWhenEmpty() {
        Supplier supplier = new Supplier(null, new Contact(null, null, null, null, "", null, null, null, null, null,
                null, null, null, null), null);

        ValidationError errors = supplier.validate();

        assertTrue(errors.getErrors().containsEntry(Supplier.COUNTRY_SERIAL_PROPERTY, Validatable.PARTY_COUNTRY));
    }

    @Test
    public void validateCountryHasNoErrorWhenNotNull() {
        Supplier supplier = new Supplier(null, new Contact(null, null, null, null, Supplier.COUNTRY_SERIAL_PROPERTY,
                null, null, null, null, null, null, null, null, null), null);

        ValidationError errors = supplier.validate();

        assertFalse(errors.getErrors().containsEntry(Supplier.COUNTRY_SERIAL_PROPERTY, Validatable.PARTY_COUNTRY));
    }

    @Test
    public void validateProvinceHasNoErrorWhenNull() {
        Supplier supplier = new Supplier(null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, null, null, null), null);

        ValidationError errors = supplier.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.PROVINCE_SERIAL_PROPERTY, Validatable.BUYER_PROVINCE));
    }

    @Test
    public void validateProvinceHasNoErrorWhenEmpty() {
        Supplier supplier = new Supplier(null, new Contact(null, null, null, null, null, "", null, null, null, null,
                null, null, null, null), null);

        ValidationError errors = supplier.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.PROVINCE_SERIAL_PROPERTY, Validatable.BUYER_PROVINCE));
    }

    @Test
    public void validateGenderHasNoErrorWhenNotNull() {
        Supplier supplier = new Supplier(null, null, new Contact(null, Gender.MALE, null, null, null, null, null, null, null,
                null, null, null, null, null), null, ApprovalStatus.DRAFT, null, null, null, null, null);

        ValidationError errors = supplier.validate();

        assertFalse(errors.getErrors().containsEntry(Supplier.GENDER_SERIAL_PROPERTY,
                Validatable.SUPPLIER_GENDER_REQUIRED));
    }

    @Test
    public void validateGenderHasErrorWhenNullAndNotUnprofiled() {
        Supplier supplier = new Supplier(null, null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, null, null, null), null, ApprovalStatus.DRAFT, null, null, null, null, null);

        ValidationError errors = supplier.validate();

        assertTrue(errors.getErrors().containsEntry(Supplier.GENDER_SERIAL_PROPERTY,
                Validatable.SUPPLIER_GENDER_REQUIRED));
    }

    @Test
    public void validateGenderHasNoErrorWhenNullAndUnprofiled() {
        Supplier supplier = new Supplier(null, null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, null, null, null), null, ApprovalStatus.UNPROFILED, null, null, null, null, null);

        ValidationError errors = supplier.validate();

        assertFalse(errors.getErrors().containsEntry(Supplier.GENDER_SERIAL_PROPERTY,
                Validatable.SUPPLIER_GENDER_REQUIRED));
    }

    @Test
    public void validateTitleHasNoErrorWhenNullAndNotUnprofiled() {
        Supplier supplier = new Supplier(null, null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, null, null, null), null, ApprovalStatus.DRAFT, null, null, null, null, null);

        ValidationError errors = supplier.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.TITLE_SERIAL_PROPERTY, Validatable.BUYER_TITLE_REQUIRED));
    }

    @Test
    public void validateBusinessTelephoneHasRequiredErrorWhenNull() {
        Supplier supplier = new Supplier(null, null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, null, null, null), null, ApprovalStatus.DRAFT, null, null, null, null, null);

        ValidationError errors = supplier.validate();

        assertTrue(errors.getErrors().containsEntry(Supplier.BUSINESS_PHONE_SERIAL_PROPERTY,
                Validatable.PARTY_BUSINESS_TELEPHONE_REQUIRED));
    }

    @Test
    public void validateBusinessTelephoneHasErrorWhenMoreThan21Characters() {
        Supplier supplier = new Supplier(null, null, new Contact(null, null, null, null, null, null, null, null, null, null,
                null, StringUtils.repeat("M", 21), null, null), null, ApprovalStatus.DRAFT, null, null, null, null,
                null);

        ValidationError errors = supplier.validate();

        assertTrue(errors.getErrors().containsEntry(Supplier.BUSINESS_PHONE_SERIAL_PROPERTY,
                Validatable.PARTY_BUSINESS_TELEPHONE_LENGTH));
    }

    @Test
    public void validateCompanyNameChinsesHasNoErrorWhenNullAndNotUnprofiled() {
        Supplier supplier = new Supplier(null, null, null, new Company(null, null, null, null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null, null, null);

        ValidationError errors = supplier.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.CHINESE_COMPANY_NAME_PROPERTY,
                Validatable.BUYER_CHINESE_COMPANY_NAME_REQUIRED));
    }

    @Test
    public void getMembershipReturnsNullWhenEmpty() {
        Supplier supplier = new Supplier(null, null, null, new Company(null, null, null, null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null, null, null);

        assertNull(supplier.getMembership());
    }

    @Test
    public void getMembershipReturnsCurrentActiveMembership() {
        Supplier supplier = new Supplier(null, null, null, new Company(null, null, null, null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null, null, null);

        DateMidnight purchaseDate = new DateMidnight().minusMonths(3);
        DateMidnight effectiveDate = new DateMidnight().minusMonths(3);
        DateMidnight expirationDate = new DateMidnight().minusMonths(2);

        Membership expiredMembership = new Membership(1L, 100L, null, ApprovalStatus.PAID, 1, purchaseDate.toDate(),
                effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO, BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);

        Membership activeMembership = new Membership(1L, 100L, null, ApprovalStatus.PAID, 1, purchaseDate.toDate(),
                expirationDate.plusDays(1).toDate(), expirationDate.plusDays(1).plusMonths(4).toDate(),
                BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);

        supplier.getMemberships().add(expiredMembership);
        supplier.getMemberships().add(activeMembership);

        assertThat(supplier.getMembership(), CoreMatchers.is(activeMembership));
    }

    @Test
    public void getMembershipReturnsLastExpiredMembershipWhenAllAreExpired() {
        Supplier supplier = new Supplier(null, null, null, new Company(null, null, null, null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null, null, null);

        DateMidnight purchaseDate = new DateMidnight().minusMonths(3);
        DateMidnight effectiveDate = new DateMidnight().minusMonths(3);
        DateMidnight expirationDate = new DateMidnight().minusMonths(2);

        Membership expiredMembership1 = new Membership(1L, 100L, null, ApprovalStatus.PAID, 1, purchaseDate.toDate(),
                effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO, BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);

        Membership expiredMembership2 = new Membership(1L, 100L, null, ApprovalStatus.PAID, 1, purchaseDate.toDate(),
                expirationDate.plusDays(1).toDate(), expirationDate.plusDays(7).toDate(), BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);

        supplier.getMemberships().add(expiredMembership1);
        supplier.getMemberships().add(expiredMembership2);

        assertThat(supplier.getMembership(), CoreMatchers.is(expiredMembership2));
    }

}
