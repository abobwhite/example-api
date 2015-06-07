package com.daugherty.e2c.domain;

/**
 * Represents a Type of Purchase Order.
 */
public enum MessageType {

    PRODUCT("Product"), BUYLEAD("Buy Lead");

    private String type;

    private MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static MessageType findByType(String type) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getType().equals(type)) {
                return messageType;
            }
        }
        throw new IllegalArgumentException(type + " is not a valid Message Type");
    }

    @Override
    public String toString() {
        return type;
    }

}
