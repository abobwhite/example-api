package com.daugherty.e2c.domain;

/**
 * Represents a Type of Catgeory Type.
 */
public enum CategoryType {

    PRODUCT("Product"), SERVICE("Service");

    private String type;

    private CategoryType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static CategoryType findByType(String type) {
        for (CategoryType partyType : CategoryType.values()) {
            if (partyType.getType().equals(type)) {
                return partyType;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return type;
    }

}
