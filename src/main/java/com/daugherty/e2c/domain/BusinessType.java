package com.daugherty.e2c.domain;

/**
 * Represents a Business Type
 */
public enum BusinessType {
    AGENT(1L, "Agent"), //
    PROCUREMENT_OFFICE(2L, "Procurement Office"), //
    GOVERNMENT(3L, "Government and Related Organization"), //
    TRADING(4L, "Trading Company"), //
    BUSINESS_SERVICE(5L, "Business Service"), //
    DISTRIBUTOR_WHOLESALER(6L, "Distributor/Wholesaler"), //
    MANUFACTURER(7L, "Manufacturer");

    private Long id;
    private String name;

    private BusinessType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static BusinessType findById(Long id) {
        for (BusinessType businessType : BusinessType.values()) {
            if (businessType.getId().equals(id)) {
                return businessType;
            }
        }
        throw new IllegalArgumentException(id + " is not a valid BusinessType Id");
    }

    public static BusinessType findByName(String name) {
        for (BusinessType businessType : BusinessType.values()) {
            if (businessType.getName().equals(name)) {
                return businessType;
            }
        }
        throw new IllegalArgumentException(name + " is not a valid BusinessType Name");
    }

    @Override
    public String toString() {
        return name;
    }
}
