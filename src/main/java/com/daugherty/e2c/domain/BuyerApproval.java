package com.daugherty.e2c.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The task that a Buyer Moderator must perform before new/updated Buyer profile information is visible on the site.
 */
public class BuyerApproval extends Entity implements Approvable {

    private static final long serialVersionUID = 2L;

    public static final String TITLE_SERIAL_PROPERTY = "title";
    public static final String EMAIL_SERIAL_PROPERTY = "emailAddress";
    public static final String STATUS_SERIAL_PROPERTY = "approvalStatus";
    public static final String VERSION_SERIAL_PROPERTY = "version";
    public static final String LAST_UPDATED_BY_SERIAL_PROPERTY = "lastUpdatedBy";
    public static final String LAST_UPDATED_AT_SERIAL_PROPERTY = "lastUpdatedAt";

    private String publicId;
    private final String title;
    private final String firstName;
    private final String lastName;
    private final String email;
    private ApprovalStatus approvalStatus;
    private Integer version;
    private Long snapshotId;
    private final String lastUpdatedBy;
    private final Date lastUpdatedAt;

    public BuyerApproval(Long id, String publicId, String title, String firstName, String lastName, String email,
            ApprovalStatus approvalStatus, Integer version, Long snapshotId, String lastUpdatedBy, Date lastUpdatedAt) {
        super(id);
        this.publicId = publicId;
        this.title = title;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public final String getEmail() {
        return email;
    }

    public String getEmailWithPersonalName() {
        return Party.buildEmailWithPersonalName(firstName, lastName, email);
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
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
        BuyerApproval rhs = (BuyerApproval) obj;
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
        return 21;
    }
}
