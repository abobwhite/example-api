package com.daugherty.e2c.service.json;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.MembershipLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Lists;

/**
 * Marshalls/unmarshalls a Membership and Membership Level domain object to/from JSON.
 */
@JsonRootName(value = "membership")
public class JsonMembership {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("supplierId")
    private String supplierId;
    @JsonProperty(Membership.EFFECTIVE_DATE_SERIAL_PROPERTY)
    private Date effectiveDate;
    @JsonProperty(Membership.EXPIRATION_DATE_SERIAL_PROPERTY)
    private Date expirationDate;
    @JsonProperty("purchaseDate")
    private Date purchaseDate;
    @JsonProperty("translationsRemaining")
    private Integer translationsRemaining;
    @JsonProperty("hotProductCount")
    private Integer hotProductCount;
    @JsonProperty("productCount")
    private Integer productCount;
    @JsonProperty("basePrice")
    private BigDecimal basePrice;
    @JsonProperty(Membership.PURCHASE_PRICE_SERIAL_PROPERTY)
    private BigDecimal purchasePrice;
    @JsonProperty(Membership.PAYMENT_AMOUNT_SERIAL_PROPERTY)
    private BigDecimal paymentAmount;
    @JsonProperty("approvalStatus")
    private String approvalStatus;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("paymentType")
    private String paymentType;
    @JsonProperty("paymentInvoice")
    private String paymentInvoice;
    @JsonProperty("paypalLink")
    private String paypalLink;
    @JsonProperty("earlyRenewalDiscount")
    private BigDecimal earlyRenewalDiscount;
    @JsonProperty("upgradeCredit")
    private BigDecimal upgradeCredit;
    @JsonProperty("discounts")
    private List<JsonMembershipDiscount> discounts = Lists.newArrayList();
    @JsonProperty("productSummary")
    private Long productSummary;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty(MembershipLevel.PROFILE_PUBLIC_SERIAL_PROPERTY)
    private Boolean profilePublic;
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
    @JsonProperty("term")
    private Integer term;
    @JsonProperty("messageCount")
    private Integer messageCount;
    @JsonProperty("marketAnalysis")
    private Boolean marketAnalysis;
    @JsonProperty("buyerSearch")
    private Boolean buyerSearch;
    @JsonProperty("logisticsAssistance")
    private Boolean logisticsAssistance;

    public JsonMembership() {

    }

    public JsonMembership(Membership membership, String publicPartyId) {
        this.id = membership.getId();
        this.supplierId = publicPartyId;
        this.level = membership.getLevel().getValue();
        this.effectiveDate = membership.getEffectiveDate();
        this.expirationDate = membership.getExpirationDate();
        this.purchaseDate = membership.getPurchaseDate();
        this.translationsRemaining = membership.getLevel().getTranslationCount();
        this.hotProductCount = membership.getLevel().getHotProductCount();
        this.productCount = membership.getLevel().getProductCount();
        this.basePrice = membership.getLevel().getPrice();
        this.purchasePrice = membership.getPurchasePrice();
        this.paymentAmount = membership.getPaymentAmount();
        this.approvalStatus = membership.getApprovalStatus().getName();
        this.version = membership.getVersion();
        this.paymentType = membership.getPaymentType().getType();
        this.paymentInvoice = membership.getPaymentInvoice();
        this.earlyRenewalDiscount = membership.getEarlyRenewalDiscount();
        this.upgradeCredit = membership.getUpgradeCredit();
        this.profilePublic = membership.getLevel().isProfilePublic();
        this.includedInProductAlerts = membership.getLevel().isIncludedInProductAlerts();
        this.supplierMessagingEnabled = membership.getLevel().isSupplierMessagingEnabled();
        this.exportTutorialAccessible = membership.getLevel().isExportTutorialAccessible();
        this.additionalProductImageCount = membership.getLevel().getAdditionalProductImageCount();
        this.verifiableByThirdParty = membership.getLevel().isVerifiableByThirdParty();
        this.advancedWebAndMailCapabilityEnabled = membership.getLevel().isAdvancedWebAndMailCapabilityEnabled();
        this.videoUploadable = membership.getLevel().isVideoUploadable();
        this.contactChinaDirect = membership.getLevel().isContactChinaDirect();
        this.term = membership.getLevel().getMonthsOfTerm();
        this.messageCount = membership.getLevel().getMessageCount();
        this.marketAnalysis = membership.getLevel().isMarketAnalysis();
        this.buyerSearch = membership.getLevel().isBuyerSearch();
        this.logisticsAssistance = membership.getLevel().isLogisticsAssistance();
        setUpDiscounts(membership);
        setProductSummary(membership.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getTranslationsRemaining() {
        return translationsRemaining;
    }

    public void setTranslationsRemaining(Integer translationsRemaining) {
        this.translationsRemaining = translationsRemaining;
    }

    public final Integer getHotProductCount() {
        return hotProductCount;
    }

    public final void setHotProductCount(Integer hotProductCount) {
        this.hotProductCount = hotProductCount;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentInvoice() {
        return paymentInvoice;
    }

    public void setPaymentInvoice(String paymentInvoice) {
        this.paymentInvoice = paymentInvoice;
    }

    public String getPaypalLink() {
        return paypalLink;
    }

    public void setPaypalLink(String paypalLink) {
        this.paypalLink = paypalLink;
    }

    public List<JsonMembershipDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<JsonMembershipDiscount> discounts) {
        this.discounts = discounts;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getEarlyRenewalDiscount() {
        return earlyRenewalDiscount;
    }

    public void setEarlyRenewalDiscount(BigDecimal earlyRenewalDiscount) {
        this.earlyRenewalDiscount = earlyRenewalDiscount;
    }

    public BigDecimal getUpgradeCredit() {
        return upgradeCredit;
    }

    public void setUpgradeCredit(BigDecimal upgradeCredit) {
        this.upgradeCredit = upgradeCredit;
    }

    public final Long getProductSummary() {
        return productSummary;
    }

    public final void setProductSummary(Long productSummary) {
        this.productSummary = productSummary;
    }

    public Boolean getProfilePublic() {
        return profilePublic;
    }

    public void setProfilePublic(Boolean profilePublic) {
        this.profilePublic = profilePublic;
    }

    public Boolean getIncludedInProductAlerts() {
        return includedInProductAlerts;
    }

    public void setIncludedInProductAlerts(Boolean includedInProductAlerts) {
        this.includedInProductAlerts = includedInProductAlerts;
    }

    public Boolean getSupplierMessagingEnabled() {
        return supplierMessagingEnabled;
    }

    public void setSupplierMessagingEnabled(Boolean supplierMessagingEnabled) {
        this.supplierMessagingEnabled = supplierMessagingEnabled;
    }

    public Boolean getExportTutorialAccessible() {
        return exportTutorialAccessible;
    }

    public void setExportTutorialAccessible(Boolean exportTutorialAccessible) {
        this.exportTutorialAccessible = exportTutorialAccessible;
    }

    public Integer getAdditionalProductImageCount() {
        return additionalProductImageCount;
    }

    public void setAdditionalProductImageCount(Integer additionalProductImageCount) {
        this.additionalProductImageCount = additionalProductImageCount;
    }

    public Boolean getVerifiableByThirdParty() {
        return verifiableByThirdParty;
    }

    public void setVerifiableByThirdParty(Boolean verifiableByThirdParty) {
        this.verifiableByThirdParty = verifiableByThirdParty;
    }

    public Boolean getAdvancedWebAndMailCapabilityEnabled() {
        return advancedWebAndMailCapabilityEnabled;
    }

    public void setAdvancedWebAndMailCapabilityEnabled(Boolean advancedWebAndMailCapabilityEnabled) {
        this.advancedWebAndMailCapabilityEnabled = advancedWebAndMailCapabilityEnabled;
    }

    public Boolean getVideoUploadable() {
        return videoUploadable;
    }

    public void setVideoUploadable(Boolean videoUploadable) {
        this.videoUploadable = videoUploadable;
    }

    public Boolean getContactChinaDirect() {
        return contactChinaDirect;
    }

    public void setContactChinaDirect(Boolean contactChinaDirect) {
        this.contactChinaDirect = contactChinaDirect;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Boolean getMarketAnalysis() {
        return marketAnalysis;
    }

    public void setMarketAnalysis(Boolean marketAnalysis) {
        this.marketAnalysis = marketAnalysis;
    }

    public Boolean getBuyerSearch() {
        return buyerSearch;
    }

    public void setBuyerSearch(Boolean buyerSearch) {
        this.buyerSearch = buyerSearch;
    }

    public Boolean getLogisticsAssistance() {
        return logisticsAssistance;
    }

    public void setLogisticsAssistance(Boolean logisticsAssistance) {
        this.logisticsAssistance = logisticsAssistance;
    }

    private void setUpDiscounts(Membership membership) {
        for (MembershipDiscount membershipDiscount : membership.getMembershipDiscounts()) {
            JsonMembershipDiscount discount = new JsonMembershipDiscount(membershipDiscount.getId(), membershipDiscount.getDiscount().getId(), membershipDiscount.getDiscount()
                    .getCode(), String.valueOf(membershipDiscount.getDiscount().getAmount().intValue()),
                    membershipDiscount.getDiscount().getAmountType().getDescription(), membershipDiscount.getDiscount()
                            .getAmount(), membershipDiscount.getAmount(), membershipDiscount.getDiscount().getOngoing(),
                    membershipDiscount.getDiscount().getSpecial(), membershipDiscount.getDiscount().getEffectiveDate(),
                    membershipDiscount.getDiscount().getExpirationDate(), Lists.newArrayList(membershipDiscount
                            .getDiscount().getMembershipLevels()), Lists.newArrayList(membershipDiscount.getDiscount()
                            .getDiscountTypes()), membership.getId());

            this.getDiscounts().add(discount);

        }
    }
}
