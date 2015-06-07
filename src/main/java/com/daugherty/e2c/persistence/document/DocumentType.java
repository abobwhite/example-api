package com.daugherty.e2c.persistence.document;

public enum DocumentType {
    BUYER_LICENSE("BLI-"), BUYER_LOGO("BLO-"), SUPPLIER_LICENSE("SLI-"), SUPPLIER_LOGO("SLO-"), SUPPLIER_VIDEO("SVD-"), PRODUCT_IMAGE(
            "PRI-"), CERTIFICATION_DOCUMENT("CDO-"), MESSAGE_ATTACHEMNT("MEA-");

    private String type;

    private DocumentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}