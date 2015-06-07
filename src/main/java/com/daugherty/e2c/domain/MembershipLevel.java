package com.daugherty.e2c.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a set of capabilities/services available to a Supplier.
 */
public class MembershipLevel extends Entity implements Validatable {

    private static final long serialVersionUID = 1L;

    public static final String LEVEL_SERIAL_PROPERTY = "level";
    public static final String PRICE_SERIAL_PROPERTY = "price";
    public static final String MONTHS_OF_TERMS_SERIAL_PROPERTY = "monthsOfTerm";
    public static final String PRODUCT_COUNT_SERIAL_PROPERTY = "productCount";
    public static final String MESSAGE_COUNT_SERIAL_PROPERTY = "messageCount";
    public static final String TRANSLATION_COUNT_SERIAL_PROPERTY = "translationCount";
    public static final String PROFILE_PUBLIC_SERIAL_PROPERTY = "profilePublic";
    public static final String HOT_PRODUCT_COUNT_SERIAL_PROPERTY = "hotProductCount";
    public static final String INCLUDED_IN_PRODUCT_ALERTS_SERIAL_PROPERTY = "includedInProductAlerts";
    public static final String SUPPLIER_MESSAGING_ENABLED_SERIAL_PROPERTY = "supplierMessagingEnabled";
    public static final String EXPORT_TUTORIAL_ACCESSIBLE_SERIAL_PROPERTY = "exportTutorialAccessible";
    public static final String ADDITIONAL_PRODUCT_IMAGE_COUNT_SERIAL_PROPERTY = "additionalProductImageCount";
    public static final String VERIFIABLE_BY_THIRD_PARTY_SERIAL_PROPERTY = "verifiableByThirdParty";
    public static final String ADVANCED_WEB_AND_MAIL_CAPABILITY_ENABLED_PROPERTY = "advancedWebAndMailCapabilityEnabled";
    public static final String VIDEO_UPLOADABLE_SERIAL_PROPERTY = "videoUploadable";
    public static final String CONTACT_CHINA_DIRECT_SERIAL_PROPERTY = "contactChinaDirect";
    public static final String MARKET_ANALYSIS_SERIAL_PROPERTY = "marketAnalysis";
    public static final String BUYER_SEARCH_SERIAL_PROPERTY = "buyerSearch";
    public static final String LOGISTICS_ASSISTANCE_SERIAL_PROPERTY = "logisticsAssistance";
    public static final String PERSONALIZED_SERVICE = "personalizedService";
    public static final String TARGETED_MARKETING = "targetedMarketing";

    private static final Integer MAX_PRODUCT_COUNT = 150;
    private static final Integer MAX_HOT_PRODUCT_COUNT = 150;
    private static final Integer MAX_ADDITIONAL_PRODUCT_IMAGE_COUNT = 2;

    private final Integer value;
    private BigDecimal price;
    private final Integer monthsOfTerm;
    private final Integer productCount;
    private final Integer messageCount;
    private final Integer translationCount;
    private final Boolean profilePublic;
    private final Integer hotProductCount;
    private final Boolean includedInProductAlerts;
    private final Boolean supplierMessagingEnabled;
    private final Boolean exportTutorialAccessible;
    private final Integer additionalProductImageCount;
    private final Boolean verifiableByThirdParty;
    private final Boolean advancedWebAndMailCapabilityEnabled;
    private final Boolean videoUploadable;
    private final Boolean contactChinaDirect;
    private final Boolean marketAnalysis;
    private final Boolean buyerSearch;
    private final Boolean logisticsAssistance;
    private final Boolean personalizedService;
    private final Boolean targetedMarketing;

    /**
     * Constructor for existing MembershipLevel instances.
     */
    public MembershipLevel(Long id, Integer value, BigDecimal price, Integer monthsOfTerm, Integer productCount,
            Integer messageCount, Integer translationCount, Boolean profilePublic, Integer hotProductCount,
            Boolean includedInProductAlerts, Boolean supplierMessagingEnabled, Boolean exportTutorialAccessible,
            Integer additionalProductImageCount, Boolean verifiableByThirdParty,
            Boolean advancedWebAndMailCapabilityEnabled, Boolean videoUploadable, Boolean contactChinaDirect,
            Boolean marketAnalysis, Boolean buyerSearch, Boolean logisticsAssistance) {
        super(id);
        this.value = value;
        this.price = price;
        this.monthsOfTerm = monthsOfTerm;
        this.productCount = productCount;
        this.messageCount = messageCount;
        this.translationCount = translationCount;
        this.profilePublic = profilePublic;
        this.hotProductCount = hotProductCount;
        this.includedInProductAlerts = includedInProductAlerts;
        this.supplierMessagingEnabled = supplierMessagingEnabled;
        this.exportTutorialAccessible = exportTutorialAccessible;
        this.additionalProductImageCount = additionalProductImageCount;
        this.verifiableByThirdParty = verifiableByThirdParty;
        this.advancedWebAndMailCapabilityEnabled = advancedWebAndMailCapabilityEnabled;
        this.videoUploadable = videoUploadable;
        this.contactChinaDirect = contactChinaDirect;
        this.marketAnalysis = marketAnalysis;
        this.buyerSearch = buyerSearch;
        this.logisticsAssistance = logisticsAssistance;
        this.personalizedService = Boolean.TRUE;
        this.targetedMarketing = Boolean.TRUE;
    }

    /**
     * Constructor for existing MembershipLevel instances.
     */
    public MembershipLevel(Integer value, BigDecimal price, Integer monthsOfTerm, Integer productCount,
            Integer messageCount, Integer translationCount, Boolean profilePublic, Integer hotProductCount,
            Boolean includedInProductAlerts, Boolean supplierMessagingEnabled, Boolean exportTutorialAccessible,
            Integer additionalProductImageCount, Boolean verifiableByThirdParty,
            Boolean advancedWebAndMailCapabilityEnabled, Boolean videoUploadable, Boolean contactChinaDirect,
            Boolean marketAnalysis, Boolean buyerSearch, Boolean logisticsAssistance) {

        this(null, value, price, monthsOfTerm, productCount, messageCount, translationCount, profilePublic,
                hotProductCount, includedInProductAlerts, supplierMessagingEnabled, exportTutorialAccessible,
                additionalProductImageCount, verifiableByThirdParty, advancedWebAndMailCapabilityEnabled,
                videoUploadable, contactChinaDirect, marketAnalysis, buyerSearch, logisticsAssistance);
    }

    public MembershipLevel(Integer value) {
        this(null, value, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null);
    }

    public Integer getValue() {
        return value;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getMonthsOfTerm() {
        return monthsOfTerm;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public Integer getTranslationCount() {
        return translationCount;
    }

    public Boolean isProfilePublic() {
        return profilePublic;
    }

    public Integer getHotProductCount() {
        return hotProductCount;
    }

    public Boolean isIncludedInProductAlerts() {
        return includedInProductAlerts;
    }

    public Boolean isSupplierMessagingEnabled() {
        return supplierMessagingEnabled;
    }

    public Boolean isExportTutorialAccessible() {
        return exportTutorialAccessible;
    }

    public Integer getAdditionalProductImageCount() {
        return additionalProductImageCount;
    }

    public Boolean isVerifiableByThirdParty() {
        return verifiableByThirdParty;
    }

    public Boolean isAdvancedWebAndMailCapabilityEnabled() {
        return advancedWebAndMailCapabilityEnabled;
    }

    public Boolean isVideoUploadable() {
        return videoUploadable;
    }

    public Boolean isContactChinaDirect() {
        return contactChinaDirect;
    }

    public Boolean isMarketAnalysis() {
        return marketAnalysis;
    }

    public Boolean isBuyerSearch() {
        return buyerSearch;
    }

    public Boolean isLogisticsAssistance() {
        return logisticsAssistance;
    }

    public Boolean hasPersonalizedService() {
        return personalizedService;
    }

    public Boolean hasTargetedMarketing() {
        return targetedMarketing;
    }

    public ApprovalStatus getDefaultApprovalStatus() {
        return ApprovalStatus.PROVISIONAL;
    }

    @Override
    public ValidationError validate() {
        ValidationError error = new ValidationError();

        validateNumberOfProducts(error);
        validateNumberOfHotProducts(error);
        validateNumberOfAdditionalProducts(error);
        validatePrice(error);

        return error;
    }

    private void validateNumberOfProducts(ValidationError errors) {
        if (productCount == null) {
            errors.add(PRODUCT_COUNT_SERIAL_PROPERTY, PRODUCT_COUNT_REQUIRED);
        }

        if (productCount != null && productCount > MAX_PRODUCT_COUNT) {
            errors.add(PRODUCT_COUNT_SERIAL_PROPERTY, PRODUCT_COUNT_LENGTH);
        }
    }

    private void validateNumberOfHotProducts(ValidationError errors) {
        if (hotProductCount == null) {
            errors.add(HOT_PRODUCT_COUNT_SERIAL_PROPERTY, HOT_PRODUCT_COUNT_REQUIRED);
        }

        if (hotProductCount != null && hotProductCount > MAX_HOT_PRODUCT_COUNT) {
            errors.add(HOT_PRODUCT_COUNT_SERIAL_PROPERTY, HOT_PRODUCT_COUNT_LENGTH);
        }
    }

    private void validateNumberOfAdditionalProducts(ValidationError errors) {
        if (additionalProductImageCount == null) {
            errors.add(ADDITIONAL_PRODUCT_IMAGE_COUNT_SERIAL_PROPERTY, ADDITIONAL_PRODUCT_IMAGE_COUNT_REQUIRED);
        }

        if (additionalProductImageCount != null && additionalProductImageCount > MAX_ADDITIONAL_PRODUCT_IMAGE_COUNT) {
            errors.add(ADDITIONAL_PRODUCT_IMAGE_COUNT_SERIAL_PROPERTY, ADDITIONAL_PRODUCT_IMAGE__COUNT_LENGTH);
        }
    }

    private void validatePrice(ValidationError errors) {
        if (price == null) {
            errors.add(PRICE_SERIAL_PROPERTY, PRICE_REQUIRED);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        MembershipLevel rhs = (MembershipLevel) obj;
        builder.append(value, rhs.value);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 5;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(value);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("value", value).append("price", price).append("monthsOfTerm", monthsOfTerm)
                .append("productCount", productCount).append("messageCount", messageCount)
                .append("translationCount", translationCount).append("profilePublic", profilePublic)
                .append("hotProductCount", hotProductCount).append("includedInProductAlerts", includedInProductAlerts)
                .append("supplierMessagingEnabled", supplierMessagingEnabled)
                .append("exportTutorialAccessible", exportTutorialAccessible)
                .append("additonalImageProductCount", additionalProductImageCount)
                .append("verifiableByThirdParty", verifiableByThirdParty)
                .append("advancedWebAndMailCapabilityEnabled", advancedWebAndMailCapabilityEnabled)
                .append("videoUploadable", videoUploadable).append("contactChinaDirect", contactChinaDirect)
                .append("personalizedService", personalizedService).append("targetedMarketing", targetedMarketing);
    }

}
