package com.daugherty.e2c.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Sets;

/**
 * Represents the Discount Domain Object
 */
public class Discount extends Entity implements Validatable {
    private static final long serialVersionUID = 1L;

    public static final String CODE_SERIAL_PROPERTY = "code";
    public static final String TYPE_SERIAL_PROPERTY = "type";
    public static final String DISCOUNT_AMOUNT_SERIAL_PROPERTY = "discountAmount";
    public static final String DESCRIPTION_SERIAL_PROPERTY = "description";
    public static final String EFFECTIVE_SERIAL_PROPERTY = "effectiveDate";
    public static final String EXPIRATION_SERIAL_PROPERTY = "expirationDate";
    public static final String ONGOING_SERIAL_PROPERTY = "ongoing";
    public static final String SPECIAL_SERIAL_PROPERTY = "special";
    public static final String MEMBERSHIP_LEVEL_SERIAL_PROPERTY = "membershipLevels";
    public static final String SUBSCRIPTION_TYPE_SERIAL_PROPERTY = "subscriptionTypes";

    private String code;
    private DiscountAmountType amountType;
    private BigDecimal amount;
    private String description;
    private Boolean ongoing;
    private Boolean special;
    private Date effectiveDate;
    private Date expirationDate;
    private Set<Integer> membershipLevels = Sets.newHashSet();
    private Set<SubscriptionType> discountTypes = Sets.newHashSet();

    public Discount(Long id) {
        super(id);
    }

    public Discount(Long id, String code, DiscountAmountType amountType, BigDecimal amount, String description,
            Boolean ongoing, Boolean special, Date effectiveDate, Date expirationDate) {
        this(id, code, amountType, amount, description, ongoing, special, effectiveDate, expirationDate,
                new HashSet<Integer>(), new HashSet<SubscriptionType>());
    }

    public Discount(Long id, String code, DiscountAmountType amountType, BigDecimal amount, String description,
            Boolean ongoing, Boolean special, Date effectiveDate, Date expirationDate, Set<Integer> membershipLevels,
            Set<SubscriptionType> discountTypes) {
        super(id);
        this.code = code;
        this.amountType = amountType;
        this.amount = amount;
        this.description = description;
        this.ongoing = ongoing;
        this.special = special;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.membershipLevels = membershipLevels;
        this.discountTypes = discountTypes;
    }

    @Override
    public ValidationError validate() {
        ValidationError error = new ValidationError();

        // Always perform these validations
        validateCode(error);
        validateAmountType(error);
        validateAmount(error);
        validateDescription(error);
        validateEffectiveDate(error);
        validateExpirationDate(error);
        validateMembershipLevels(error);
        validateSubscriptionTypes(error);

        return error;
    }

    private void validateCode(ValidationError errors) {
        if (code == null || StringUtils.isEmpty(code)) {
            errors.add(CODE_SERIAL_PROPERTY, DISCOUNT_CODE_REQUIRED);
        }
        if (code == null || StringUtils.isEmpty(code) || code.length() > 45) {
            errors.add(CODE_SERIAL_PROPERTY, DISCOUNT_CODE_LENGTH);
        }
    }

    private void validateAmountType(ValidationError errors) {
        if (amountType == null) {
            errors.add(TYPE_SERIAL_PROPERTY, DISCOUNT_AMOUNT_TYPE_REQUIRED);
        }
    }

    private void validateAmount(ValidationError errors) {
        if (amount == null) {
            errors.add(DISCOUNT_AMOUNT_SERIAL_PROPERTY, DISCOUNT_AMOUNT_REQUIRED);
        }

        if (amountType != null && DiscountAmountType.PERCENT.equals(amountType)
                && BigDecimal.valueOf(100).doubleValue() < amount.doubleValue()) {
            errors.add(DISCOUNT_AMOUNT_SERIAL_PROPERTY, DISCOUNT_PERCENT_AMOUNT_INVALID);
        }
    }

    private void validateDescription(ValidationError errors) {
        if (description == null || StringUtils.isEmpty(description)) {
            errors.add(DESCRIPTION_SERIAL_PROPERTY, DISCOUNT_DESCRIPTION_REQUIRED);
        }
        if (description == null || StringUtils.isEmpty(description) || description.length() > 45) {
            errors.add(DESCRIPTION_SERIAL_PROPERTY, DISCOUNT_DESCRIPTION_LENGTH);
        }
    }

    private void validateEffectiveDate(ValidationError errors) {
        if (effectiveDate == null) {
            errors.add(EFFECTIVE_SERIAL_PROPERTY, DISCOUNT_EFFECTIVE_DATE_REQUIRED);
        }
    }

    private void validateExpirationDate(ValidationError errors) {
        if (expirationDate == null) {
            errors.add(EXPIRATION_SERIAL_PROPERTY, DISCOUNT_EXPIRATION_DATE_REQUIRED);
        }
        if (effectiveDate != null && expirationDate != null && effectiveDate.after(expirationDate)) {
            errors.add(EXPIRATION_SERIAL_PROPERTY, DISCOUNT_EXPIRATION_DATE_INVALID);
        }
    }

    private void validateMembershipLevels(ValidationError errors) {
        if (membershipLevels == null || membershipLevels.isEmpty()) {
            errors.add(MEMBERSHIP_LEVEL_SERIAL_PROPERTY, DISCOUNT_MEMBERSHIP_LEVEL_REQUIRED);
        }
    }

    private void validateSubscriptionTypes(ValidationError errors) {
        if (discountTypes == null || discountTypes.isEmpty()) {
            errors.add(SUBSCRIPTION_TYPE_SERIAL_PROPERTY, DISCOUNT_SUBSCRIPTION_TYPE_REQUIRED);
        }
    }

    public String getCode() {
        return code;
    }

    public DiscountAmountType getAmountType() {
        return amountType;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getOngoing() {
        return ongoing;
    }

    public Boolean getSpecial() {
        return special;
    }

    public Set<Integer> getMembershipLevels() {
        return membershipLevels;
    }

    public Set<SubscriptionType> getDiscountTypes() {
        return discountTypes;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        Discount rhs = (Discount) obj;
        builder.append(code, rhs.getCode()).append(amountType, rhs.getAmountType()).append(amount, rhs.getAmount())
                .append(description, rhs.getDescription()).append(ongoing, rhs.getOngoing())
                .append(special, rhs.getSpecial()).append(membershipLevels, rhs.getMembershipLevels());
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(code).append(amountType).append(amount).append(description).append(special).append(ongoing)
                .append(membershipLevels);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("code", code).append("type", amountType).append("amount", amount).append("special", special)
                .append("ongoing", ongoing).append("description", description)
                .append("membershipLevels", membershipLevels).append("discountTypes", discountTypes);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 3;
    }

}
