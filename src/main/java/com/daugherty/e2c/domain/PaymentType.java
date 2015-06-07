package com.daugherty.e2c.domain;

/**
 * Represents a Type of Purchase Order.
 */
public enum PaymentType {

    NONE("None"), PAYPAL("PayPal"), EFT("EFT");

    private String type;

    private PaymentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static PaymentType findByType(String type) {
        for (PaymentType discountType : PaymentType.values()) {
            if (discountType.getType().equals(type)) {
                return discountType;
            }
        }
        throw new IllegalArgumentException(type + " is not a valid Payment Type description");
    }

    @Override
    public String toString() {
        return type;
    }

}
