package com.daugherty.e2c.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ProductTest {

    @Test
    public void validateNameHasErrorWhenNull() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.NAME_SERIAL_PROPERTY, Validatable.PRODUCT_NAME_REQUIRED));
    }

    @Test
    public void validateNameHasErrorWhenBlank() {
        Product product = new Product(new ProductInformation(null, "  ", null, null, null, null, null, null, null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.NAME_SERIAL_PROPERTY, Validatable.PRODUCT_NAME_REQUIRED));
    }

    @Test
    public void validateNameHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, StringUtils.repeat("A", 61), null, null, null, null,
                null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.NAME_SERIAL_PROPERTY, Validatable.PRODUCT_NAME_LENGTH));
    }

    @Test
    public void validateNameHasNoErrorWhenNotBlankAndWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, StringUtils.repeat("A", 60), null, null, null, null,
                null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.NAME_SERIAL_PROPERTY, Validatable.PRODUCT_NAME_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Product.NAME_SERIAL_PROPERTY, Validatable.PRODUCT_NAME_LENGTH));
    }

    @Test
    public void validateDescriptionHasErrorWhenNull() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PRODUCT_DESCRIPTION_REQUIRED));
    }

    @Test
    public void validateDescriptionHasErrorWhenBlank() {
        Product product = new Product(new ProductInformation(null, null, "  ", null, null, null, null, null, null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PRODUCT_DESCRIPTION_REQUIRED));
    }

    @Test
    public void validateDescriptionHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, StringUtils.repeat("A", 2001), null, null,
                null, null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PRODUCT_DESCRIPTION_LENGTH));
    }

    @Test
    public void validateDescriptionHasNoErrorWhenNotBlankAndWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, StringUtils.repeat("A", 2000), null, null,
                null, null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PRODUCT_DESCRIPTION_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Product.DESCRIPTION_SERIAL_PROPERTY,
                Validatable.PRODUCT_DESCRIPTION_LENGTH));
    }

    @Test
    public void validatePaymentTermsHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, null, StringUtils.repeat("A", 121), null,
                null, null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.PAYMENT_TERMS_SERIAL_PROPERTY,
                Validatable.PRODUCT_PAYMENT_TERMS_LENGTH));
    }

    @Test
    public void validatePaymentTermsHasNoErrorWhenWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, null, StringUtils.repeat("A", 120), null,
                null, null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.PAYMENT_TERMS_SERIAL_PROPERTY,
                Validatable.PRODUCT_PAYMENT_TERMS_LENGTH));
    }

    @Test
    public void validateMinimumOrderHasErrorWhenNull() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.MINIMUM_ORDER_SERIAL_PROPERTY,
                Validatable.PRODUCT_MINIMUM_ORDER_REQUIRED));
    }

    @Test
    public void validateMinimumOrderHasErrorWhenBlank() {
        Product product = new Product(new ProductInformation(null, null, null, null, "  ", null, null, null, null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.MINIMUM_ORDER_SERIAL_PROPERTY,
                Validatable.PRODUCT_MINIMUM_ORDER_REQUIRED));
    }

    @Test
    public void validateMinimumOrderHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, null, null, StringUtils.repeat("A", 21), null,
                null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.MINIMUM_ORDER_SERIAL_PROPERTY,
                Validatable.PRODUCT_MINIMUM_ORDER_LENGTH));
    }

    @Test
    public void validateMinimumOrderHasNoErrorWhenNotBlankAndWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, null, null, StringUtils.repeat("A", 20), null,
                null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.MINIMUM_ORDER_SERIAL_PROPERTY,
                Validatable.PRODUCT_MINIMUM_ORDER_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Product.MINIMUM_ORDER_SERIAL_PROPERTY,
                Validatable.PRODUCT_MINIMUM_ORDER_LENGTH));
    }

    @Test
    public void validateFOBPortHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, StringUtils.repeat("A", 76),
                null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.FREIGHT_ON_BOARD_PORT_SERIAL_PROPERTY,
                Validatable.PRODUCT_FOB_PORT_LENGTH));
    }

    @Test
    public void validateFOBPortHasNoErrorWhenWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, StringUtils.repeat("A", 75),
                null, null, null, null, null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.FREIGHT_ON_BOARD_PORT_SERIAL_PROPERTY,
                Validatable.PRODUCT_FOB_PORT_LENGTH));
    }

    @Test
    public void validateFOBPriceHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, StringUtils.repeat(
                "A", 101), null, null, null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.FREIGHT_ON_BOARD_PRICE_SERIAL_PROPERTY,
                Validatable.PRODUCT_FOB_PRICE_LENGTH));
    }

    @Test
    public void validateFOBPriceHasNoErrorWhenWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, StringUtils.repeat(
                "A", 100), null, null, null, null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.FREIGHT_ON_BOARD_PRICE_SERIAL_PROPERTY,
                Validatable.PRODUCT_FOB_PRICE_LENGTH));
    }

    @Test
    public void validateCountryHasErrorWhenNull() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.COUNTRY_SERIAL_PROPERTY,
                Validatable.PRODUCT_COUNTRY_REQUIRED));
    }

    @Test
    public void validateCountryHasErrorWhenBlank() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, "  ", null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.COUNTRY_SERIAL_PROPERTY,
                Validatable.PRODUCT_COUNTRY_REQUIRED));
    }

    @Test
    public void validateCountryHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null,
                StringUtils.repeat("A", 76), null, null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors()
                .containsEntry(Product.COUNTRY_SERIAL_PROPERTY, Validatable.PRODUCT_COUNTRY_LENGTH));
    }

    @Test
    public void validateCountryHasNoErrorWhenNotBlankAndWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null,
                StringUtils.repeat("A", 75), null, null, null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.COUNTRY_SERIAL_PROPERTY,
                Validatable.PRODUCT_COUNTRY_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Product.COUNTRY_SERIAL_PROPERTY,
                Validatable.PRODUCT_COUNTRY_LENGTH));
    }

    @Test
    public void validateLeadTimeHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null,
                StringUtils.repeat("A", 21), null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.LEAD_TIME_SERIAL_PROPERTY,
                Validatable.PRODUCT_LEAD_TIME_LENGTH));
    }

    @Test
    public void validateLeadTimeHasNoErrorWhenWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null,
                StringUtils.repeat("A", 20), null, null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.LEAD_TIME_SERIAL_PROPERTY,
                Validatable.PRODUCT_LEAD_TIME_LENGTH));
    }

    @Test
    public void validateModelNumberHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                StringUtils.repeat("A", 31), null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.MODEL_NUMBER_SERIAL_PROPERTY,
                Validatable.PRODUCT_MODEL_NUMBER_LENGTH));
    }

    @Test
    public void validateModelNumberHasNoErrorWhenWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                StringUtils.repeat("A", 30), null), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.MODEL_NUMBER_SERIAL_PROPERTY,
                Validatable.PRODUCT_MODEL_NUMBER_LENGTH));
    }

    @Test
    public void validateSpecificationsHasErrorWhenTooLong() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, StringUtils.repeat("A", 2001)), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.SPECIFICATIONS_SERIAL_PROPERTY,
                Validatable.PRODUCT_SPECIFICATIONS_LENGTH));
    }

    @Test
    public void validateSpecificationsHasNoErrorWhenWithinLengthLimit() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, StringUtils.repeat("A", 2000)), null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.SPECIFICATIONS_SERIAL_PROPERTY,
                Validatable.PRODUCT_SPECIFICATIONS_LENGTH));
    }

    @Test
    public void validateMetaTagsHasErrorWhenTooLong() {
        Product product = new Product(42L, new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), new ProductMetadata(null, null, StringUtils.repeat("A", 101), null, null, null, null,
                new Date()), null, null, null, null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.META_TAGS_SERIAL_PROPERTY,
                Validatable.PRODUCT_META_TAGS_LENGTH));
    }

    @Test
    public void validateMetaTagsHasNoErrorWhenWithinLengthLimit() {
        Product product = new Product(42L, new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), new ProductMetadata(null, null, StringUtils.repeat("A", 100), null, null, null, null,
                new Date()), null, null, null, null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.META_TAGS_SERIAL_PROPERTY,
                Validatable.PRODUCT_META_TAGS_LENGTH));
    }

    @Test
    public void validateKeywordsHasErrorWhenTooLong() {
        Product product = new Product(42L, new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), new ProductMetadata(null, null, null, StringUtils.repeat("A", 101), null, null, null,
                new Date()), null, null, null, null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.KEYWORDS_SERIAL_PROPERTY,
                Validatable.PRODUCT_KEYWORDS_LENGTH));
    }

    @Test
    public void validateKeywordsHasNoErrorWhenWithinLengthLimit() {
        Product product = new Product(42L, new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), new ProductMetadata(null, null, null, StringUtils.repeat("A", 100), null, null, null,
                new Date()), null, null, null, null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.KEYWORDS_SERIAL_PROPERTY,
                Validatable.PRODUCT_KEYWORDS_LENGTH));
    }

    @Test
    public void validateCategoriesHasErrorWhenListNull() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), null);

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.CATEGORIES_SERIAL_PROPERTY,
                Validatable.PRODUCT_CATEGORIES_REQUIRED));
    }

    @Test
    public void validateCategoriesHasErrorWhenListEmpty() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), Lists.<ProductCategory> newArrayList());

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.CATEGORIES_SERIAL_PROPERTY,
                Validatable.PRODUCT_CATEGORIES_REQUIRED));
    }

    @Test
    public void validateCategoriesHasErrorWhenTooMany() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), Lists.newArrayList(new ProductCategory(1L), new ProductCategory(2L), new ProductCategory(
                3L), new ProductCategory(4L)));

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors().containsEntry(Product.CATEGORIES_SERIAL_PROPERTY,
                Validatable.PRODUCT_CATEGORIES_LENGTH));
    }

    @Test
    public void validateCategoriesHasNoErrorWhenWithinCountLimit() {
        Product product = new Product(new ProductInformation(null, null, null, null, null, null, null, null, null,
                null, null), Lists.newArrayList(new ProductCategory(1L), new ProductCategory(2L), new ProductCategory(
                3L)));

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.CATEGORIES_SERIAL_PROPERTY,
                Validatable.PRODUCT_CATEGORIES_REQUIRED));
        assertFalse(errors.getErrors().containsEntry(Product.CATEGORIES_SERIAL_PROPERTY,
                Validatable.PRODUCT_CATEGORIES_LENGTH));
    }

    @Test
    public void validateImagesHasNoErrorWhenListNullAndIdDoesNotExist() {
        Product product = new Product(null, null);

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.IMAGES_SERIAL_PROPERTY,
                Validatable.PRODUCT_IMAGES_REQUIRED));
    }

    @Test
    public void validateImagesHasNoErrorWhenListEmptyAndIdDoesNotExist() {
        Product product = new Product(null, null);
        product.setImages(Lists.<ProductImage> newArrayList());

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.IMAGES_SERIAL_PROPERTY,
                Validatable.PRODUCT_IMAGES_REQUIRED));
    }

    @Test
    public void validateImagesHasErrorWhenListHasItemsAndIdDoesNotExist() {
        Product product = new Product(null, null);
        product.setImages(Lists.newArrayList(new ProductImage(242L, "productImageLink", true)));

        ValidationError errors = product.validate();

        assertTrue(errors.getErrors()
                .containsEntry(Product.IMAGES_SERIAL_PROPERTY, Validatable.PRODUCT_IMAGES_ORPHANED));
    }

    @Test
    public void validateImagesHasNoErrorWhenListHasItemsAndIdExists() {
        Product product = new Product(null, null);
        product.setId(42L);
        product.setImages(Lists.newArrayList(new ProductImage(242L, "productImageLink", true)));

        ValidationError errors = product.validate();

        assertFalse(errors.getErrors().containsEntry(Product.IMAGES_SERIAL_PROPERTY,
                Validatable.PRODUCT_IMAGES_REQUIRED));
    }

}
