package com.daugherty.e2c.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents an image for a Product.
 */
public class ProductImage extends Entity {

    private static final long serialVersionUID = 1L;

    private final String productImageLink;
    private final Boolean primary;
    private final Long productSnapshotId;

    public ProductImage(String productImageLink, Boolean primary) {
        this(null, null, productImageLink, primary);
    }

    public ProductImage(Long productSnapshotId, String productImageLink, Boolean primary) {
        this(null, productSnapshotId, productImageLink, primary);
    }

    public ProductImage(Long id, Long productSnapshotId, String productImageLink, Boolean primary) {
        super(id);
        this.productSnapshotId = productSnapshotId;
        this.productImageLink = productImageLink;
        this.primary = primary;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public Boolean isPrimary() {
        return primary;
    }

    public Long getProductSnapshotId() {
        return productSnapshotId;
    }

    @Override
    protected int hashCodeMultiplier() {
        return 19;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        ProductImage rhs = (ProductImage) obj;
        builder.append(getId(), rhs.getId()).append(productImageLink, rhs.productImageLink)
                .append(primary, rhs.primary).append(productSnapshotId, rhs.productSnapshotId);

    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(getId()).append(productImageLink).append(primary).append(productSnapshotId);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("id", getId()).append("productImageLink", productImageLink).append("primary", primary)
                .append("productSnapshotId", productSnapshotId);
    }
}
