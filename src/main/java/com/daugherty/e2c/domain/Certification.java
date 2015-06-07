package com.daugherty.e2c.domain;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;

public class Certification extends Entity implements Validatable, Approvable {

    private static final long serialVersionUID = 1L;

    public static final String CERTIFICATION_ID_SERIAL_PROPERTY = "id";
    public static final String APPROVAL_STATUS_SERIAL_PROPERTY = "approvalStatus";
    public static final String VERSION_SERIAL_PROPERTY = "version";
    public static final String SNAPSHOT_ID_SERIAL_PROPERTY = "snapshotId";
    public static final String STANDARD_SERIAL_PROPERTY = "standard";
    public static final String CERTIFICATE_NUMBER_SERIAL_PROPERTY = "certificateNumber";
    public static final String LINK_SERIAL_PROPERTY = "link";
    public static final String SUPPLIER_OR_PRODUCT_ID = "supplierOrProductId";
    public static final String ISSUED_BY_SERIAL_PROPERTY = "issuedBy";
    public static final String ISSUE_DATE_SERIAL_PROPERTY = "issuedDate";
    public static final String SCOPE_RANGE_SERIAL_PROPERTY = "scopeRange";

    private ApprovalStatus approvalStatus = ApprovalStatus.UNPROFILED;
    private Integer version = 1;
    private Long snapshotId;

    private final String standard;
    private final String certificateNumber;
    private String link;
    private final Long supplierOrProductId;
    private final String issuedBy;
    private Long issuedDate; // WTF Why is this a Long, not a Date?
    private final String scopeRange;

    // constructor for new Certification instances
    public Certification() {
        this(null, null, null, null, null, null, System.currentTimeMillis(), null, ApprovalStatus.UNPROFILED, 1, null);
    }

    // constructor for existing Certification instances
    public Certification(Long id, String standard, String certificateNumber, String link, Long supplierOrProductId,
            String issuedBy, Long issueDate, String scopeRange, ApprovalStatus approvalStatus, Integer version,
            Long snapshotId) {

        super(id);

        this.standard = standard;
        this.certificateNumber = certificateNumber;
        this.link = link;
        this.supplierOrProductId = supplierOrProductId;
        this.issuedBy = issuedBy;
        if (issueDate != null) {
            issuedDate = DateUtils.truncate(new java.util.Date(issueDate), Calendar.DAY_OF_MONTH).getTime();
        }
        this.scopeRange = scopeRange;
        this.approvalStatus = approvalStatus;
        this.version = version;
        this.snapshotId = snapshotId;
    }

    public String getStandard() {
        return standard;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public Long getIssuedDate() {
        return issuedDate;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public Long getSupplierOrProductId() {
        return supplierOrProductId;
    }

    public String getScopeRange() {
        return scopeRange;
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
    public ValidationError validate() {
        ValidationError error = new ValidationError();

        validateStandard(error);
        validateCertificateNumber(error);
        validateLink(error);
        validateIssuedBy(error);
        validateIssuedDate(error);
        validateScopeRange(error);

        return error;
    }

    private void validateStandard(ValidationError error) {
        if (StringUtils.isEmpty(standard)) {
            error.add(STANDARD_SERIAL_PROPERTY, CERTIFICATION_STANDARD_REQUIRED);
        }

        if (standard != null && standard.length() > 30) {
            error.add(STANDARD_SERIAL_PROPERTY, CERTIFICATION_STANDARD_LENGTH);
        }
    }

    private void validateCertificateNumber(ValidationError error) {
        if (StringUtils.isEmpty(certificateNumber)) {
            error.add(CERTIFICATE_NUMBER_SERIAL_PROPERTY, CERTIFICATION_CERTIFICATE_NUMBER_REQUIRED);
        }

        if (certificateNumber != null && certificateNumber.length() > 30) {
            error.add(CERTIFICATE_NUMBER_SERIAL_PROPERTY, CERTIFICATION_CERTIFICATE_NUMBER_LENGTH);
        }
    }

    private void validateLink(ValidationError error) {
        if (link != null && link.length() > 50) {
            error.add(LINK_SERIAL_PROPERTY, CERTIFICATION_LINK_LENGTH);
        }
    }

    private void validateIssuedBy(ValidationError error) {
        if (issuedBy != null && issuedBy.length() > 30) {
            error.add(ISSUED_BY_SERIAL_PROPERTY, CERTIFICATION_ISSUED_BY_LENGTH);
        }
    }

    private void validateIssuedDate(ValidationError error) {
        if (issuedDate == null) {
            error.add(ISSUE_DATE_SERIAL_PROPERTY, CERTIFICATION_ISSUED_DATE_REQUIRED);
        }
    }

    private void validateScopeRange(ValidationError error) {
        if (scopeRange != null && scopeRange.length() > 30) {
            error.add(SCOPE_RANGE_SERIAL_PROPERTY, CERTIFICATION_SCOPE_RANGE_LENGTH);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {

        Certification rhs = (Certification) obj;
        builder.append(approvalStatus, rhs.getApprovalStatus()).append(version, rhs.getVersion())
                .append(snapshotId, rhs.getSnapshotId()).append(standard, rhs.getStandard())
                .append(certificateNumber, rhs.getCertificateNumber()).append(link, rhs.getLink())
                .append(supplierOrProductId, rhs.getSupplierOrProductId()).append(issuedBy, rhs.getIssuedBy())
                .append(issuedDate, rhs.getIssuedDate()).append(scopeRange, rhs.getScopeRange());

    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(approvalStatus).append(version).append(snapshotId).append(standard).append(certificateNumber)
                .append(link).append(supplierOrProductId).append(issuedBy).append(issuedDate).append(scopeRange);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append(APPROVAL_STATUS_SERIAL_PROPERTY, approvalStatus).append(VERSION_SERIAL_PROPERTY, version)
                .append(SNAPSHOT_ID_SERIAL_PROPERTY, snapshotId).append(STANDARD_SERIAL_PROPERTY, standard)
                .append(CERTIFICATE_NUMBER_SERIAL_PROPERTY, certificateNumber).append(LINK_SERIAL_PROPERTY, link)
                .append(SUPPLIER_OR_PRODUCT_ID, supplierOrProductId).append(ISSUED_BY_SERIAL_PROPERTY, issuedBy)
                .append(ISSUE_DATE_SERIAL_PROPERTY, issuedDate).append(SCOPE_RANGE_SERIAL_PROPERTY, scopeRange);

    }

    @Override
    protected int hashCodeMultiplier() {
        return 13;
    }

}
