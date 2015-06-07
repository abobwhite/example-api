package com.daugherty.e2c.domain;

/**
 * Represents a Message tag.
 */
public enum MessageTag {
    SPECIFICATIONS(1L, "Specifications"), //
    COMPANY_OVERVIEW(2L, "Company Overview"), //
    INSPECTION_CERTIFICATE(3L, "Inspection Certificate"), //
    MINIMUM_ORDER_QUANTITY(4L, "Minimum Order Quantity"), //
    FOB_PRICE(5L, "FOB Price"), //
    DELIVERY_TIME(6L, "Delivery Time");

    private Long id;
    private String name;

    private MessageTag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static MessageTag findById(Long id) {
        for (MessageTag messageTag : MessageTag.values()) {
            if (messageTag.getId().equals(id)) {
                return messageTag;
            }
        }
        throw new IllegalArgumentException(id + " is not a valid MessageTag Id");
    }

    public static MessageTag findByName(String name) {
        for (MessageTag messageTag : MessageTag.values()) {
            if (messageTag.getName().equals(name)) {
                return messageTag;
            }
        }
        throw new IllegalArgumentException(name + " is not a valid MessageTag Name");
    }

    @Override
    public String toString() {
        return name;
    }
}
