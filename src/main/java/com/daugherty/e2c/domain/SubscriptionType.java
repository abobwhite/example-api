package com.daugherty.e2c.domain;

/**
 * Represents a Subscription Type
 */
public enum SubscriptionType {
    RENEW(1L, "Renew"), //
    UPGRADE(2L, "Upgrade"), //
    NEW(3L, "New");

    private Long id;
    private String name;

    private SubscriptionType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static SubscriptionType findById(Long id) {
        for (SubscriptionType businessType : SubscriptionType.values()) {
            if (businessType.getId().equals(id)) {
                return businessType;
            }
        }
        throw new IllegalArgumentException(id + " is not a valid Subscription Type Id");
    }

    public static SubscriptionType findByName(String name) {
        for (SubscriptionType businessType : SubscriptionType.values()) {
            if (businessType.getName().equals(name)) {
                return businessType;
            }
        }
        throw new IllegalArgumentException(name + " is not a valid Subscription Type Name");
    }

    @Override
    public String toString() {
        return name;
    }
}
