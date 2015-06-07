package com.daugherty.e2c.domain;

/**
 * Represents a Type of Purchase Order.
 */
public enum DiscountAmountType {

    DOLLAR("D", "Dollar"), PERCENT("P", "Percent");

    private String code;
    private String description;

    private DiscountAmountType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static DiscountAmountType findByCode(String code) {
        for (DiscountAmountType discountType : DiscountAmountType.values()) {
            if (discountType.getCode().equals(code)) {
                return discountType;
            }
        }
        throw new IllegalArgumentException(code + " is not a valid Discount Amount Type code");
    }

    public static DiscountAmountType findByDescription(String description) {
        for (DiscountAmountType discountType : DiscountAmountType.values()) {
            if (discountType.getDescription().equals(description)) {
                return discountType;
            }
        }
        throw new IllegalArgumentException(description + " is not a valid Discount Amount Type description");
    }

    @Override
    public String toString() {
        return code;
    }

}
