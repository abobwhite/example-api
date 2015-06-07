package com.daugherty.e2c.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class BuyerTest {

    @Test
    public void validateProvinceCanBeNullWhenCountryPopulated() {
        Buyer buyer = new Buyer(null, new Contact(null, null, null, null, "country", null, null, null, null, null,
                null, null, null, new Date()), null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.PROVINCE_SERIAL_PROPERTY, Validatable.BUYER_PROVINCE));
    }

    @Test
    public void validateProvinceHasErrorWhenNull() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, "中国", null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Buyer.PROVINCE_SERIAL_PROPERTY, Validatable.BUYER_PROVINCE));
    }

    @Test
    public void validateProvinceHasErrorWhenEmpty() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, "中国", "", null, null, null, null, null, null,
                null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Buyer.PROVINCE_SERIAL_PROPERTY, Validatable.BUYER_PROVINCE));
    }

    @Test
    public void validateProvinceHasNoErrorWhenNotNull() {
        Buyer buyer = new Buyer(null, new Contact(null, null, null, null, "中国", Buyer.PROVINCE_SERIAL_PROPERTY, null,
                null, null, null, null, null, null, new Date()), null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.PROVINCE_SERIAL_PROPERTY, Validatable.BUYER_PROVINCE));
    }

    @Test
    public void validateTitleHasNoErrorWhenNotNull() {
        Buyer buyer = new Buyer(null, null, new Contact("Mr.", null, null, null, null, null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.TITLE_SERIAL_PROPERTY, Validatable.BUYER_TITLE_REQUIRED));
    }

    @Test
    public void validateTitleHasErrorWhenNullAndNotUnprofiled() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Buyer.TITLE_SERIAL_PROPERTY, Validatable.BUYER_TITLE_REQUIRED));
    }

    @Test
    public void validateTitleHasNoErrorWhenNullAndUnprofiled() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.UNPROFILED, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.TITLE_SERIAL_PROPERTY, Validatable.BUYER_TITLE_REQUIRED));
    }

    @Test
    public void validateBusinessTelephoneHasNoErrorWhenExactly20Characters() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null,
                StringUtils.repeat("M", 20), null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.BUSINESS_PHONE_SERIAL_PROPERTY,
                Validatable.PARTY_BUSINESS_TELEPHONE_LENGTH));
    }

    @Test
    public void validateBusinessTelephoneHasErrorWhenMoreThan20Characters() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null,
                StringUtils.repeat("M", 21), null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Buyer.BUSINESS_PHONE_SERIAL_PROPERTY,
                Validatable.PARTY_BUSINESS_TELEPHONE_LENGTH));
    }

    @Test
    public void validateCompanyNameChineseHasNoErrorWhenNotNull() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, "Chinese Name", null, null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.CHINESE_COMPANY_NAME_PROPERTY,
                Validatable.BUYER_CHINESE_COMPANY_NAME_REQUIRED));
    }

    @Test
    public void validateCompanyNameChinsesHasErrorWhenNullAndNotUnprofiled() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, null, null, null, null, null, null, null,
                null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Buyer.CHINESE_COMPANY_NAME_PROPERTY,
                Validatable.BUYER_CHINESE_COMPANY_NAME_REQUIRED));
    }

    @Test
    public void validateCompanyNameChinsesHasNoErrorWhenNullAndUnprofiled() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, null, null, null, null, null, null, null, null, null,
                null), ApprovalStatus.UNPROFILED, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.CHINESE_COMPANY_NAME_PROPERTY,
                Validatable.BUYER_CHINESE_COMPANY_NAME_REQUIRED));
    }

    @Test
    public void validateCompanyNameChinsesHasNoErrorWhenLengthLessThan50() {
        Buyer buyer = new Buyer(null, null, null, new Company(null, "Chinese Name", null, null, null, null, null, null, null,
                null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Buyer.CHINESE_COMPANY_NAME_PROPERTY,
                Validatable.BUYER_CHINESE_COMPANY_NAME_LENGTH));
    }

    @Test
    public void validateCompanyNameChinsesHasErrorWhenLengthExceeds50() {
        Buyer buyer = new Buyer(null, null, null, new Company(null,
                "Chinese NameChinese NameChinese NameChinese NameChinese NameChinese NameChinese NameChinese Name",
                null, null, null, null, null, null, null, null, null), ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertTrue(errors.getErrors().containsEntry(Buyer.CHINESE_COMPANY_NAME_PROPERTY,
                Validatable.BUYER_CHINESE_COMPANY_NAME_LENGTH));
    }

    @Test
    public void validateGenderHasNoErrorWhenNullAndNotUnprofiled() {
        Buyer buyer = new Buyer(null, null, new Contact(null, null, null, null, null, null, null, null, null, null, null,
                null, null, new Date()), null, ApprovalStatus.DRAFT, null, null, null);

        ValidationError errors = buyer.validate();

        assertFalse(errors.getErrors().containsEntry(Supplier.GENDER_SERIAL_PROPERTY,
                Validatable.SUPPLIER_GENDER_REQUIRED));
    }

}