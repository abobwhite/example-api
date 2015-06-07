package com.daugherty.e2c.service.json;

import java.math.BigDecimal;

import com.daugherty.e2c.domain.MembershipLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a Membership Level domain object to/from JSON.
 */
@JsonRootName("membershipLevel")
public class JsonMembershipLevel {

    @JsonProperty("id")
    private Long id;
    @JsonProperty(MembershipLevel.LEVEL_SERIAL_PROPERTY)
    private Integer level;
    @JsonProperty(MembershipLevel.PRICE_SERIAL_PROPERTY)
    private BigDecimal price;
    @JsonProperty(MembershipLevel.MONTHS_OF_TERMS_SERIAL_PROPERTY)
    private Integer monthsOfTerm;
    @JsonProperty(MembershipLevel.PRODUCT_COUNT_SERIAL_PROPERTY)
    private Integer productCount;
    @JsonProperty(MembershipLevel.MESSAGE_COUNT_SERIAL_PROPERTY)
    private Integer messageCount;
    @JsonProperty(MembershipLevel.TRANSLATION_COUNT_SERIAL_PROPERTY)
    private Integer translationCount;
    @JsonProperty(MembershipLevel.PROFILE_PUBLIC_SERIAL_PROPERTY)
    private Boolean profilePublic;
    @JsonProperty(MembershipLevel.HOT_PRODUCT_COUNT_SERIAL_PROPERTY)
    private Integer hotProductCount;
    @JsonProperty(MembershipLevel.INCLUDED_IN_PRODUCT_ALERTS_SERIAL_PROPERTY)
    private Boolean includedInProductAlerts;
    @JsonProperty(MembershipLevel.SUPPLIER_MESSAGING_ENABLED_SERIAL_PROPERTY)
    private Boolean supplierMessagingEnabled;
    @JsonProperty(MembershipLevel.EXPORT_TUTORIAL_ACCESSIBLE_SERIAL_PROPERTY)
    private Boolean exportTutorialAccessible;
    @JsonProperty(MembershipLevel.ADDITIONAL_PRODUCT_IMAGE_COUNT_SERIAL_PROPERTY)
    private Integer additionalProductImageCount;
    @JsonProperty(MembershipLevel.VERIFIABLE_BY_THIRD_PARTY_SERIAL_PROPERTY)
    private Boolean verifiableByThirdParty;
    @JsonProperty(MembershipLevel.ADVANCED_WEB_AND_MAIL_CAPABILITY_ENABLED_PROPERTY)
    private Boolean advancedWebAndMailCapabilityEnabled;
    @JsonProperty(MembershipLevel.VIDEO_UPLOADABLE_SERIAL_PROPERTY)
    private Boolean videoUploadable;
    @JsonProperty(MembershipLevel.CONTACT_CHINA_DIRECT_SERIAL_PROPERTY)
    private Boolean contactChinaDirect;
    @JsonProperty(MembershipLevel.MARKET_ANALYSIS_SERIAL_PROPERTY)
    private Boolean marketAnalysis;
    @JsonProperty(MembershipLevel.BUYER_SEARCH_SERIAL_PROPERTY)
    private Boolean buyerSearch;
    @JsonProperty(MembershipLevel.LOGISTICS_ASSISTANCE_SERIAL_PROPERTY)
    private Boolean logisticsAssistance;
    @JsonProperty(MembershipLevel.PERSONALIZED_SERVICE)
    private Boolean personalizedService;
    @JsonProperty(MembershipLevel.TARGETED_MARKETING)
    private Boolean targetedMarketing;

    public JsonMembershipLevel() {
    }

    public JsonMembershipLevel(MembershipLevel membershipLevel) {
        this.id = membershipLevel.getId();
        this.level = membershipLevel.getValue();
        this.price = membershipLevel.getPrice();
        this.monthsOfTerm = membershipLevel.getMonthsOfTerm();
        this.productCount = membershipLevel.getProductCount();
        this.messageCount = membershipLevel.getMessageCount();
        this.translationCount = membershipLevel.getTranslationCount();
        this.profilePublic = membershipLevel.isProfilePublic();
        this.hotProductCount = membershipLevel.getHotProductCount();
        this.includedInProductAlerts = membershipLevel.isIncludedInProductAlerts();
        this.supplierMessagingEnabled = membershipLevel.isSupplierMessagingEnabled();
        this.exportTutorialAccessible = membershipLevel.isExportTutorialAccessible();
        this.additionalProductImageCount = membershipLevel.getAdditionalProductImageCount();
        this.verifiableByThirdParty = membershipLevel.isVerifiableByThirdParty();
        this.advancedWebAndMailCapabilityEnabled = membershipLevel.isAdvancedWebAndMailCapabilityEnabled();
        this.videoUploadable = membershipLevel.isVideoUploadable();
        this.contactChinaDirect = membershipLevel.isContactChinaDirect();
        this.marketAnalysis = membershipLevel.isMarketAnalysis();
        this.buyerSearch = membershipLevel.isBuyerSearch();
        this.logisticsAssistance = membershipLevel.isLogisticsAssistance();
        this.personalizedService = membershipLevel.hasPersonalizedService();
        this.targetedMarketing = membershipLevel.hasTargetedMarketing();
    }

    public Long getId() {
        return id;
    }

    public Integer getLevel() {
        return level;
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

    public Boolean getProfilePublic() {
        return profilePublic;
    }

    public Integer getHotProductCount() {
        return hotProductCount;
    }

    public Boolean getIncludedInProductAlerts() {
        return includedInProductAlerts;
    }

    public Boolean getSupplierMessagingEnabled() {
        return supplierMessagingEnabled;
    }

    public Boolean getExportTutorialAccessible() {
        return exportTutorialAccessible;
    }

    public Integer getAdditionalProductImageCount() {
        return additionalProductImageCount;
    }

    public Boolean getVerifiableByThirdParty() {
        return verifiableByThirdParty;
    }

    public Boolean getAdvancedWebAndMailCapabilityEnabled() {
        return advancedWebAndMailCapabilityEnabled;
    }

    public Boolean getVideoUploadable() {
        return videoUploadable;
    }

    public Boolean getContactChinaDirect() {
        return contactChinaDirect;
    }

    public Boolean getMarketAnalysis() {
        return marketAnalysis;
    }

    public Boolean getBuyerSearch() {
        return buyerSearch;
    }

    public Boolean getLogisticsAssistance() {
        return logisticsAssistance;
    }

    public Boolean getPersonalizedService() {
        return personalizedService;
    }

    public Boolean getTargetedMarketing() {
        return targetedMarketing;
    }

    public MembershipLevel toNewDomainObject() {
        return new MembershipLevel(level, price, monthsOfTerm, productCount, messageCount, translationCount,
                profilePublic, hotProductCount, includedInProductAlerts, supplierMessagingEnabled,
                exportTutorialAccessible, additionalProductImageCount, verifiableByThirdParty,
                advancedWebAndMailCapabilityEnabled, videoUploadable, contactChinaDirect, marketAnalysis, buyerSearch,
                logisticsAssistance);
    }
}
