package com.daugherty.e2c.domain;

/**
 * Represents a Type of Purchase Order.
 */
public enum PartyType {

    BUYER("Buyer"), SUPPLIER("Supplier"), ANONYMOUS("Anonymous"), INTERNAL("Internal");

    private String type;

    private PartyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static PartyType findByType(String type) {
        for (PartyType partyType : PartyType.values()) {
            if (partyType.getType().equals(type)) {
                return partyType;
            }
        }
        throw new IllegalArgumentException(type + " is not a valid Party Type");
    }

    @Override
    public String toString() {
        return type;
    }

}
