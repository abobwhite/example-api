package com.daugherty.e2c.service.json;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Marshalls/unmarshalls a Coupon domain object to/from JSON.
 */
@JsonRootName(value = "discount")
public class JsonDiscount {
    @JsonProperty("id")
    private Long id;
    @JsonProperty(Discount.CODE_SERIAL_PROPERTY)
    private String code;
    @JsonProperty(Discount.DESCRIPTION_SERIAL_PROPERTY)
    private String description;
    @JsonProperty(Discount.TYPE_SERIAL_PROPERTY)
    private String type;
    @JsonProperty(Discount.DISCOUNT_AMOUNT_SERIAL_PROPERTY)
    private BigDecimal discountAmount;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty(Discount.ONGOING_SERIAL_PROPERTY)
    private Boolean ongoing;
    @JsonProperty(Discount.SPECIAL_SERIAL_PROPERTY)
    private Boolean special;
    @JsonProperty(Discount.EFFECTIVE_SERIAL_PROPERTY)
    private Date effectiveDate;
    @JsonProperty(Discount.EXPIRATION_SERIAL_PROPERTY)
    private Date expirationDate;
    @JsonProperty(Discount.MEMBERSHIP_LEVEL_SERIAL_PROPERTY)
    private List<Integer> membershipLevels;
    @JsonProperty(Discount.SUBSCRIPTION_TYPE_SERIAL_PROPERTY)
    private List<String> subscriptionTypes;

    public JsonDiscount() {
    }

    public JsonDiscount(Discount discount) {
        this(discount.getId(), discount.getCode(), discount.getDescription(),
                discount.getAmountType().getDescription(), discount.getAmount(), null, discount.getOngoing(),
                discount.getSpecial(), discount.getEffectiveDate(), discount.getExpirationDate(), Lists
                        .newArrayList(discount.getMembershipLevels()), Lists.newArrayList(discount.getDiscountTypes()));
    }

    public JsonDiscount(Long id, String code, String description, String type, BigDecimal discountAmount,
            BigDecimal amount, Boolean ongoing, Boolean special, Date effectiveDate,
            Date expirationDate, List<Integer> membershipLevels, List<SubscriptionType> subscriptionTypes) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.type = type;
        this.discountAmount = discountAmount;
        this.amount = amount;
        this.ongoing = ongoing;
        this.special = special;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.membershipLevels = membershipLevels;
        setUpSubscriptionTypes(subscriptionTypes);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
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

    public List<Integer> getMembershipLevels() {
        return membershipLevels;
    }

    public void setMembershipLevels(List<Integer> membershipLevels) {
        this.membershipLevels = membershipLevels;
    }

    public List<String> getSubscriptionTypes() {
        return subscriptionTypes;
    }

    public void setSubscriptionTypes(List<String> subscriptionTypes) {
        this.subscriptionTypes = subscriptionTypes;
    }

    public Discount toDomainObject(Long discountId) {
        return new Discount(discountId, code, DiscountAmountType.findByDescription(getType()), discountAmount,
                description, ongoing, special, effectiveDate, expirationDate, Sets.newHashSet(membershipLevels),
                Sets.newHashSet(toSubscriptionTypeDomainObjects(subscriptionTypes)));
    }

    private void setUpSubscriptionTypes(List<SubscriptionType> subscriptionTypes) {
        List<String> types = Lists.newArrayList();

        for (SubscriptionType subscriptionType : subscriptionTypes) {
            types.add(subscriptionType.getName());
        }

        this.subscriptionTypes = types;
    }

    public List<SubscriptionType> toSubscriptionTypeDomainObjects(List<String> subscriptionTypes) {
        List<SubscriptionType> domainSubscriptionTypes = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(subscriptionTypes)) {
            for (String subscriptionTypeName : subscriptionTypes) {
                domainSubscriptionTypes.add(SubscriptionType.findByName(subscriptionTypeName));
            }
        }
        return domainSubscriptionTypes;
    }

}
