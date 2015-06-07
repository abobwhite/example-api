package com.daugherty.e2c.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Chinese translation of Supplier information.
 */
public class SupplierTranslation extends Entity implements Approvable {

    private static final long serialVersionUID = 1L;

    public static final String TITLE_SERIAL_PROPERTY = "title";
    public static final String TYPE_SERIAL_PROPERTY = "translationType";
    public static final String STATUS_SERIAL_PROPERTY = "approvalStatus";
    public static final String TRANSLATED_SERIAL_PROPERTY = "translated";
    public static final String LAST_UPDATED_BY_SERIAL_PROPERTY = "lastUpdatedBy";
    public static final String LAST_UPDATED_AT_SERIAL_PROPERTY = "lastUpdatedAt";

    public static final String PROFILE_TYPE = "Profile";
    public static final String PRODUCT_TYPE = "Product";
    public static final String CERTIFICATION_TYPE = "Certification";

    private String publicId;
    private final String title;
    private ApprovalStatus approvalStatus;
    private int version;
    private Long snapshotId;
    private final boolean translated;
    private final String lastUpdatedBy;
    private final Date lastUpdatedAt;
    private final String type;

    public SupplierTranslation(Long id, String publicId, String title, ApprovalStatus approvalStatus, Integer version, Long snapshotId,
            String type, boolean translated, String lastUpdatedBy, Date lastUpdatedAt) {
        super(id);
        this.publicId = publicId;
        this.title = title;
        this.approvalStatus = approvalStatus;
        this.version = version;
        this.snapshotId = snapshotId;
        this.translated = translated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = lastUpdatedAt;
        this.type = type;
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

    public boolean isTranslated() {
        return translated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
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

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        SupplierTranslation rhs = (SupplierTranslation) obj;
        builder.append(approvalStatus, rhs.approvalStatus).append(version, rhs.version)
                .append(snapshotId, rhs.snapshotId).append(lastUpdatedBy, rhs.lastUpdatedBy)
                .append(lastUpdatedAt, rhs.lastUpdatedAt);
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

    @Override
    protected int hashCodeMultiplier() {
        return 13;
    }
}
