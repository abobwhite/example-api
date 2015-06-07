package com.daugherty.e2c.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The task that a Supplier Moderator must perform before new/updated Supplier profile, Product or Certification
 * information is visible on the site.
 */
public class SupplierApproval extends Entity implements Approvable {

    private static final long serialVersionUID = 1L;

    public static final String TITLE_SERIAL_PROPERTY = "title";
    public static final String EMAIL_SERIAL_PROPERTY = "emailAddress";
    public static final String SUPPLIER_SERIAL_PROPERTY = "supplier";
    public static final String TYPE_SERIAL_PROPERTY = "approvalType";
    public static final String STATUS_SERIAL_PROPERTY = "approvalStatus";
    public static final String VERSION_SERIAL_PROPERTY = "version";
    public static final String LAST_UPDATED_BY_SERIAL_PROPERTY = "lastUpdatedBy";
    public static final String LAST_UPDATED_AT_SERIAL_PROPERTY = "lastUpdatedAt";

    public static final String PROFILE_TYPE = "Profile";
    public static final String PRODUCT_TYPE = "Product";
    public static final String CERTIFICATION_TYPE = "Certification";

    private String publicId;
    private final String title;
    private Supplier supplier;
    private final String type;
    private ApprovalStatus approvalStatus;
    private Integer version;
    private Long snapshotId;
    private final String lastUpdatedBy;
    private final Date lastUpdatedAt;

    public SupplierApproval(Long id, String publicId, String title, Supplier supplier, String type,
            ApprovalStatus approvalStatus, Integer version, Long snapshotId, String lastUpdatedBy, Date lastUpdatedAt) {
        super(id);
        this.publicId = publicId;
        this.title = title;
        this.supplier = supplier;
        this.type = type;
        this.approvalStatus = approvalStatus;
        this.version = version;
        this.snapshotId = snapshotId;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getTitle() {
        return title;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public String getType() {
        return type;
    }

    @Override
    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Long getSnapshotId() {
        return snapshotId;
    }

    @Override
    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    @Override
    public void visit(ApprovalStateTransitionVisitor stateTransitionVisitor) {
        version = stateTransitionVisitor.getNewVersion(version, approvalStatus);
        approvalStatus = stateTransitionVisitor.getNewApprovalStatus(approvalStatus);
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        SupplierApproval rhs = (SupplierApproval) obj;
        builder.append(approvalStatus, rhs.approvalStatus).append(version, rhs.version)
                .append(snapshotId, rhs.snapshotId).append(lastUpdatedBy, rhs.lastUpdatedBy)
                .append(lastUpdatedAt, rhs.lastUpdatedAt);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 11;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(approvalStatus).append(version).append(snapshotId).append(lastUpdatedBy).append(lastUpdatedAt);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("approvalStatus", approvalStatus).append("version", version).append("snapshotId", snapshotId)
                .append("lastUpdatedBy", lastUpdatedBy).append("lastUpdatedAt", lastUpdatedAt);
    }

}
