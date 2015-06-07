package com.daugherty.e2c.domain;

/**
 * Defines operations for Domain Objects that can validate their own information.
 */
public interface Validatable {

    // Pending User message keys
    static final String PENDING_USER_PREFIX = "pendingUser.";
    static final String PENDING_USER_USERNAME = PENDING_USER_PREFIX + "username.doesNotMatch";
    static final String PENDING_USER_PASSWORD = PENDING_USER_PREFIX + "password.doesNotMatch";

    // Party message keys
    static final String PARTY_PREFIX = "party.";
    static final String PARTY_COUNTRY = PARTY_PREFIX + "country.required";
    static final String PARTY_EMAIL_REQUIRED = PARTY_PREFIX + "email.required";
    static final String PARTY_EMAIL_FORMAT = PARTY_PREFIX + "email.format";
    static final String PARTY_EMAIL_NOT_UNIQUE = PARTY_PREFIX + "email.notUnique";
    static final String PARTY_FIRST_NAME_REQUIRED = PARTY_PREFIX + "firstName.required";
    static final String PARTY_FIRST_NAME_LENGTH = PARTY_PREFIX + "firstName.length";
    static final String PARTY_LAST_NAME_REQUIRED = PARTY_PREFIX + "lastName.required";
    static final String PARTY_LAST_NAME_LENGTH = PARTY_PREFIX + "lastName.length";
    static final String PARTY_SKYPE_LENGTH = PARTY_PREFIX + "skype.length";
    static final String PARTY_MSN_LENGTH = PARTY_PREFIX + "msn.length";
    static final String PARTY_ICQ_LENGTH = PARTY_PREFIX + "icq.length";
    static final String PARTY_ENGLISH_COMPANY_NAME_REQUIRED = PARTY_PREFIX + "englishCompanyName.required";
    static final String PARTY_ENGLISH_COMPANY_NAME_LENGTH = PARTY_PREFIX + "englishCompanyName.length";
    static final String PARTY_COMPANY_DESCRIPTION_REQUIRED = PARTY_PREFIX + "description.required";
    static final String PARTY_COMPANY_DESCRIPTION_LENGTH = PARTY_PREFIX + "description.length";
    static final String PARTY_BUSINESS_TYPES_REQUIRED = PARTY_PREFIX + "businessTypes.required";
    static final String PARTY_BUSINESS_TYPES_LENGTH = PARTY_PREFIX + "businessTypes.length";
    static final String PARTY_WEBSITE_LENGTH = PARTY_PREFIX + "website.length";
    static final String PARTY_BUSINESS_TELEPHONE_REQUIRED = PARTY_PREFIX + "businessPhone.required";
    static final String PARTY_BUSINESS_TELEPHONE_LENGTH = PARTY_PREFIX + "businessPhone.length";

    // Buyer message keys
    static final String BUYER_PREFIX = "buyer.";
    static final String BUYER_PROVINCE = BUYER_PREFIX + "province.required";
    static final String BUYER_TITLE_REQUIRED = BUYER_PREFIX + "title.required";

    static final String BUYER_CHINESE_COMPANY_NAME_REQUIRED = BUYER_PREFIX + "chineseCompanyName.required";
    static final String BUYER_CHINESE_COMPANY_NAME_LENGTH = BUYER_PREFIX + "chineseCompanyName.length";

    // Supplier message keys
    static final String SUPPLIER_PREFIX = "supplier.";
    static final String SUPPLIER_GENDER_REQUIRED = SUPPLIER_PREFIX + "gender.required";

    // Anonymous message keys
    static final String ANONYMOUS_PREFIX = "anonymous.";
    static final String ANONYMOUS_EMAIL_ADDRESS_IS_ALEADY_USED = ANONYMOUS_PREFIX + "email.notUnique";

    // Employee message keys
    static final String EMPLOYEE_PREFIX = "employee.";
    static final String EMPLOYEE_EMAIL_ADDRESS_IS_ALEADY_USED = EMPLOYEE_PREFIX + "email.notUnique";

    // Certification message keys
    static final String CERTIFICATION_PREFIX = "certification.";
    static final String CERTIFICATION_STANDARD_LENGTH = CERTIFICATION_PREFIX + "standard.length";
    static final String CERTIFICATION_STANDARD_REQUIRED = CERTIFICATION_PREFIX + "standard.required";
    static final String CERTIFICATION_CERTIFICATE_NUMBER_LENGTH = CERTIFICATION_PREFIX + "certificateNumber.length";
    static final String CERTIFICATION_CERTIFICATE_NUMBER_REQUIRED = CERTIFICATION_PREFIX + "certificateNumber.required";
    static final String CERTIFICATION_LINK_LENGTH = CERTIFICATION_PREFIX + "link.length";
    static final String CERTIFICATION_ISSUED_BY_LENGTH = CERTIFICATION_PREFIX + "issuedBy.length";
    static final String CERTIFICATION_ISSUED_DATE_REQUIRED = CERTIFICATION_PREFIX + "issuedDate.required";
    static final String CERTIFICATION_SCOPE_RANGE_LENGTH = CERTIFICATION_PREFIX + "scopeRange.length";

    // Product message keys
    static final String PRODUCT_PREFIX = "product.";
    static final String PRODUCT_NAME_REQUIRED = PRODUCT_PREFIX + "name.required";
    static final String PRODUCT_NAME_LENGTH = PRODUCT_PREFIX + "name.length";
    static final String PRODUCT_DESCRIPTION_REQUIRED = PRODUCT_PREFIX + "description.required";
    static final String PRODUCT_DESCRIPTION_LENGTH = PRODUCT_PREFIX + "description.length";
    static final String PRODUCT_PAYMENT_TERMS_LENGTH = PRODUCT_PREFIX + "paymentTerms.length";
    static final String PRODUCT_MINIMUM_ORDER_REQUIRED = PRODUCT_PREFIX + "minimumOrder.required";
    static final String PRODUCT_MINIMUM_ORDER_LENGTH = PRODUCT_PREFIX + "minimumOrder.length";
    static final String PRODUCT_FOB_PORT_LENGTH = PRODUCT_PREFIX + "freightOnBoardPort.length";
    static final String PRODUCT_FOB_PRICE_LENGTH = PRODUCT_PREFIX + "freightOnBoardPrice.length";
    static final String PRODUCT_COUNTRY_REQUIRED = PRODUCT_PREFIX + "country.required";
    static final String PRODUCT_COUNTRY_LENGTH = PRODUCT_PREFIX + "country.length";
    static final String PRODUCT_MODEL_NUMBER_LENGTH = PRODUCT_PREFIX + "modelNumber.length";
    static final String PRODUCT_LEAD_TIME_LENGTH = PRODUCT_PREFIX + "leadTime.length";
    static final String PRODUCT_SPECIFICATIONS_LENGTH = PRODUCT_PREFIX + "specifications.length";
    static final String PRODUCT_KEYWORDS_LENGTH = PRODUCT_PREFIX + "keywords.length";
    static final String PRODUCT_KEYWORDS_REQUIRED = PRODUCT_PREFIX + "keywords.required";
    static final String PRODUCT_META_TAGS_LENGTH = PRODUCT_PREFIX + "metaTags.length";
    static final String PRODUCT_META_TAGS_REQUIRED = PRODUCT_PREFIX + "metaTags.required";
    static final String PRODUCT_CATEGORIES_REQUIRED = PRODUCT_PREFIX + "categories.required";
    static final String PRODUCT_CATEGORIES_LENGTH = PRODUCT_PREFIX + "categories.length";
    static final String PRODUCT_IMAGES_REQUIRED = PRODUCT_PREFIX + "images.required";
    static final String PRODUCT_IMAGES_ORPHANED = PRODUCT_PREFIX + "images.orphaned";
    static final String PRODUCT_IMAGES_LENGTH = PRODUCT_PREFIX + "images.length";
    static final String PRODUCT_PUBLICATIONS_LENGTH = PRODUCT_PREFIX + "publications.length";
    static final String PRODUCT_HOTS_LENGTH = PRODUCT_PREFIX + "hots.length";

    // User message keys
    static final String USER_PREFIX = "user.";
    static final String USER_USERNAME_HAS_SPACES = USER_PREFIX + "username.hasSpaces";
    static final String USER_USERNAME_LENGTH = USER_PREFIX + "username.length";
    static final String USER_USERNAME_NOT_ENABLED = USER_PREFIX + "username.notEnabled";
    static final String USER_USERNAME_NOT_FOUND = USER_PREFIX + "username.notFound";
    static final String USER_USERNAME_NOT_UNIQUE = USER_PREFIX + "username.notUnique";
    static final String USER_USERNAME_REQUIRED = USER_PREFIX + "username.required";
    static final String USER_PASSWORD_HAS_SPACES = USER_PREFIX + "password.hasSpaces";
    static final String USER_PASSWORD_LENGTH = USER_PREFIX + "password.length";
    static final String USER_PASSWORD_NOT_MATCH = USER_PREFIX + "password.notMatched";
    static final String USER_PASSWORD_REQUIRED = USER_PREFIX + "password.required";
    static final String USER_EMAIL_NOT_FOUND = USER_PREFIX + "emailAddress.notFound";
    static final String USER_OLD_PASSWORD_NOT_MATCH = USER_PREFIX + "oldPassword.notMatched";

    // Product Search message keys
    static final String PRODUCT_SEARCH_PREFIX = "search.";
    static final String PRODUCT_SEARCH_TERM_LENGTH = PRODUCT_SEARCH_PREFIX + "searchTerm.length";

    // Message message keys
    static final String MESSAGE_PREFIX = "message.";
    static final String MESSAGE_SUBJECT_REQUIRED = MESSAGE_PREFIX + "subject.required";
    static final String MESSAGE_SUBJECT_LENGTH = MESSAGE_PREFIX + "subject.length";
    static final String MESSAGE_ORIGINATOR_REQUIRED = MESSAGE_PREFIX + "originator.required";
    static final String MESSAGE_ORIGINATOR_NEEDS_UPGRADE = MESSAGE_PREFIX + "originator.needsUpgrade";
    static final String MESSAGE_PRODUCT_IDS_REQUIRED = MESSAGE_PREFIX + "productIds.required";
    static final String MESSAGE_BUY_LEAD_REQUIRED = MESSAGE_PREFIX + "buyLead.required";
    static final String MESSAGE_ATTACHMENTS_LENGTH = MESSAGE_PREFIX + "attachments.length";

    // Interaction message keys
    static final String INTERACTION_PREFIX = "interaction.";
    static final String INTERACTION_BODY_REQUIRED = INTERACTION_PREFIX + "body.required";
    static final String INTERACTION_BODY_LENGTH = INTERACTION_PREFIX + "body.length";
    static final String INTERACTION_SENDER_REQUIRED = INTERACTION_PREFIX + "sender.required";
    static final String INTERACTION_RECEIVER_REQUIRED = INTERACTION_PREFIX + "receiver.required";

    // Membership message keys
    static final String MEMBERSHIP_PREFIX = "membership.";
    static final String MEMBERSHIP_LEVEL_NOT_ALLOWED = MEMBERSHIP_PREFIX + "level.invalid";
    static final String PRODUCT_COUNT_REQUIRED = MEMBERSHIP_PREFIX + "productCount.required";
    static final String PRODUCT_COUNT_LENGTH = MEMBERSHIP_PREFIX + "productCount.length";
    static final String HOT_PRODUCT_COUNT_REQUIRED = MEMBERSHIP_PREFIX + "hotProductCount.required";
    static final String HOT_PRODUCT_COUNT_LENGTH = MEMBERSHIP_PREFIX + "hotProductCount.length";
    static final String ADDITIONAL_PRODUCT_IMAGE_COUNT_REQUIRED = MEMBERSHIP_PREFIX
            + "additionalProductImageCount.required";
    static final String ADDITIONAL_PRODUCT_IMAGE__COUNT_LENGTH = MEMBERSHIP_PREFIX
            + "additionalProductImageCount.length";
    static final String PRICE_REQUIRED = MEMBERSHIP_PREFIX + "price.required";
    static final String PURCHASE_PRICE_REQUIRED = MEMBERSHIP_PREFIX + "purchasePrice.required";
    static final String PAYMENT_AMOUNT_REQUIRED = MEMBERSHIP_PREFIX + "paymentAmount.required";
    static final String EFFECTIVE_DATE_REQUIRED = MEMBERSHIP_PREFIX + "effectiveDate.required";
    static final String EFFECTIVE_DATE_INVALID = MEMBERSHIP_PREFIX + "effectiveDate.invalid";
    static final String EFFECTIVE_DATE_INVALID_RANGE = MEMBERSHIP_PREFIX + "effectiveDate.invalidRange";
    static final String EXPIRATION_DATE_INVALID_RANGE = MEMBERSHIP_PREFIX + "expirationDate.invalidRange";

    // Discount message keys
    static final String DISCOUNT_PREFIX = "discount.";
    static final String DISCOUNT_INVALID = DISCOUNT_PREFIX + "invalid";
    static final String DISCOUNT_CODE_REQUIRED = DISCOUNT_PREFIX + "code.required";
    static final String DISCOUNT_CODE_LENGTH = DISCOUNT_PREFIX + "code.length";
    static final String DISCOUNT_AMOUNT_TYPE_REQUIRED = DISCOUNT_PREFIX + "amountType.required";
    static final String DISCOUNT_AMOUNT_REQUIRED = DISCOUNT_PREFIX + "amount.required";
    static final String DISCOUNT_PERCENT_AMOUNT_INVALID = DISCOUNT_PREFIX + "percentAmount.invalid";
    static final String DISCOUNT_DESCRIPTION_REQUIRED = DISCOUNT_PREFIX + "description.required";
    static final String DISCOUNT_DESCRIPTION_LENGTH = DISCOUNT_PREFIX + "description.length";
    static final String DISCOUNT_EFFECTIVE_DATE_REQUIRED = DISCOUNT_PREFIX + "effectiveDate.required";
    static final String DISCOUNT_EXPIRATION_DATE_REQUIRED = DISCOUNT_PREFIX + "expirationDate.required";
    static final String DISCOUNT_EXPIRATION_DATE_INVALID = DISCOUNT_PREFIX + "expirationDate.invalid";
    static final String DISCOUNT_MEMBERSHIP_LEVEL_REQUIRED = DISCOUNT_PREFIX + "membershipLevels.required";
    static final String DISCOUNT_SUBSCRIPTION_TYPE_REQUIRED = DISCOUNT_PREFIX + "subscriptionTypes.required";

    // Buy Lead message keys
    static final String BUY_LEAD_PREFIX = "buyLead.";
    static final String BUY_LEAD_PARTY_REQUIRED = BUY_LEAD_PREFIX + "requestor.required";
    static final String BUY_LEAD_PRODUCT_CATGEORY_REQUIRED = BUY_LEAD_PREFIX + "category.required";
    static final String BUY_LEAD_EFFECTIVE_DATE_REQUIRED = BUY_LEAD_PREFIX + "effectiveDate.required";
    static final String BUY_LEAD_EXPIRATION_DATE_REQUIRED = BUY_LEAD_PREFIX + "expirationDate.required";
    static final String BUY_LEAD_ALREADY_EXISTS = BUY_LEAD_PREFIX + "exists";

    // Image Document message keys
    static final String IMAGE_PREFIX = "image.";
    static final String INVALID_IMAGE = IMAGE_PREFIX + "invalid";

    ValidationError validate();

}
