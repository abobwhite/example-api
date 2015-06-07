package com.daugherty.e2c.service.json;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/1/14
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.UserMembership;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Lists;

/**
 * Marshalls/unmarshalls a Membership and Membership Level domain object to/from JSON.
 */
@JsonRootName(value = "userMembership")
public class JsonUserMembership{
    //TODO: extend JsonMembership once we make memberships have string(public) ids also
    //  Then remove these properties
    @JsonProperty("id")
    private Long id;
    @JsonProperty("supplierId")
    private String publicSupplierId;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("effectiveDate")
    private Date effectiveDate;
    @JsonProperty("expirationDate")
    private Date expirationDate;
    @JsonProperty("purchaseDate")
    private Date purchaseDate;
    @JsonProperty("translationsRemaining")
    private Integer translationsRemaining;
    @JsonProperty("hotProductCount")
    private Integer hotProductCount;
    @JsonProperty("basePrice")
    private BigDecimal basePrice;
    @JsonProperty("purchasePrice")
    private BigDecimal purchasePrice;
    @JsonProperty("paymentAmount")
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
    private List<JsonDiscount> discounts = Lists.newArrayList();
    @JsonProperty("productSummary")
    private Long productSummary;
    
    @JsonProperty(UserMembership.USERNAME_SERIAL_PROPERTY)
    private String username;
    @JsonProperty(UserMembership.FIRST_NAME_SERIAL_PROPERTY)
    private String firstName;
    @JsonProperty(UserMembership.LAST_NAME_SERIAL_PROPERTY)
    private String lastName;
    @JsonProperty(UserMembership.EMAIL_SERIAL_PROPERTY)
    private String email;
    @JsonProperty(UserMembership.COMPANY_NAME_SERIAL_PROPERTY)
    private String englishCompanyName;
    @JsonProperty(UserMembership.APPROVAL_STATUS_SERIAL_PROPERTY)
    private String userApprovalStatus;

    public JsonUserMembership() {

    }

    public JsonUserMembership(UserMembership userMembership, String publicSupplierId) {
        this.id = userMembership.getId();
        this.publicSupplierId = publicSupplierId;
        this.level = userMembership.getLevel().getValue();
        this.effectiveDate = userMembership.getEffectiveDate();
        this.expirationDate = userMembership.getExpirationDate();
        this.purchaseDate = userMembership.getPurchaseDate();
        this.translationsRemaining = userMembership.getLevel().getTranslationCount();
        this.hotProductCount = userMembership.getLevel().getHotProductCount();
        this.basePrice = userMembership.getLevel().getPrice();
        this.purchasePrice = userMembership.getPurchasePrice();
        this.paymentAmount = userMembership.getPaymentAmount();
        this.approvalStatus = userMembership.getApprovalStatus().getName();
        this.version = userMembership.getVersion();
        this.paymentType = userMembership.getPaymentType().getType();
        this.paymentInvoice = userMembership.getPaymentInvoice();
        this.earlyRenewalDiscount = userMembership.getEarlyRenewalDiscount();
        this.upgradeCredit = userMembership.getUpgradeCredit();
        setUpDiscounts(userMembership);
        setProductSummary(userMembership.getId());
        
        setUsername(userMembership.getUserName());
        setFirstName(userMembership.getFirstName());
        setLastName(userMembership.getLastName());
        setEmail(userMembership.getEmailAddress());
        setEnglishCompanyName(userMembership.getCompanyNameEnglish());
        setUserApprovalStatus(userMembership.getUserApprovalStatus());
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierId() {
        return publicSupplierId;
    }

    public void setPublicSupplierId(String publicSupplierId) {
        this.publicSupplierId = publicSupplierId;
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

    public List<JsonDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<JsonDiscount> discounts) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnglishCompanyName() {
        return englishCompanyName;
    }

    public void setEnglishCompanyName(String englishCompanyName) {
        this.englishCompanyName = englishCompanyName;
    }

    public String getUserApprovalStatus() {
        return userApprovalStatus;
    }

    public void setUserApprovalStatus(String approvalStatus) {
        this.userApprovalStatus = approvalStatus;
    }

    private void setUserApprovalStatus(ApprovalStatus approvalStatus) {
        if (approvalStatus != null)
            setUserApprovalStatus(approvalStatus.getName());
    }
    
    private void setUpDiscounts(Membership membership) {
        BigDecimal total = membership.getLevel().getPrice();

        for (MembershipDiscount membershipDiscount : membership.getMembershipDiscounts()) {
            JsonDiscount discount = new JsonDiscount(membershipDiscount.getId(), membershipDiscount.getDiscount()
                    .getCode(), String.valueOf(membershipDiscount.getDiscount().getAmount().intValue()),
                    membershipDiscount.getDiscount().getAmountType().getDescription(), membershipDiscount.getDiscount()
                            .getAmount(), membershipDiscount.getAmount(), membershipDiscount.getDiscount().getOngoing(),
                    membershipDiscount.getDiscount().getSpecial(), membershipDiscount.getDiscount().getEffectiveDate(),
                    membershipDiscount.getDiscount().getExpirationDate(), Lists.newArrayList(membershipDiscount
                            .getDiscount().getMembershipLevels()), Lists.newArrayList(membershipDiscount.getDiscount()
                            .getDiscountTypes()));

            total = total.subtract(membershipDiscount.getAmount());

            this.getDiscounts().add(discount);

        }
    }
}
