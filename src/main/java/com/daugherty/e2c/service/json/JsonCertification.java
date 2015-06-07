package com.daugherty.e2c.service.json;

import java.security.Principal;
import java.util.Date;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Certification;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "certification")
public class JsonCertification {

    @JsonProperty(Certification.CERTIFICATION_ID_SERIAL_PROPERTY)
    private Long id;
    @JsonProperty(Certification.STANDARD_SERIAL_PROPERTY)
    private String standard;
    @JsonProperty(Certification.CERTIFICATE_NUMBER_SERIAL_PROPERTY)
    private String certificateNumber;
    @JsonProperty(Certification.LINK_SERIAL_PROPERTY)
    private String link;
    @JsonProperty(Certification.ISSUED_BY_SERIAL_PROPERTY)
    private String issuedBy;
    @JsonProperty(Certification.ISSUE_DATE_SERIAL_PROPERTY)
    private Date issuedDate;
    @JsonProperty(Certification.SCOPE_RANGE_SERIAL_PROPERTY)
    private String scopeRange;
    @JsonProperty(Certification.APPROVAL_STATUS_SERIAL_PROPERTY)
    private String approvalStatus;
    @JsonProperty(Certification.VERSION_SERIAL_PROPERTY)
    private Integer version;
    @JsonProperty(Certification.SNAPSHOT_ID_SERIAL_PROPERTY)
    private Long snapshotId;

    public JsonCertification() {
    }

    public JsonCertification(Certification certification, DocumentUrlFactory documentUrlFactory, Locale locale,
            Principal principal) {
        setId(certification.getId());
        setStandard(certification.getStandard());
        setCertificateNumber(certification.getCertificateNumber());
        if (principal != null) {
            setLink(documentUrlFactory.createDocumentUrl(certification.getLink(), locale));
        }
        setIssuedBy(certification.getIssuedBy());
        setIssuedDate(new Date(certification.getIssuedDate()));
        setScopeRange(certification.getScopeRange());
        setApprovalStatus(certification.getApprovalStatus().getName());
        setVersion(certification.getVersion());
        setSnapshotId(certification.getSnapshotId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issueDate) {
        issuedDate = issueDate;
    }

    public String getScopeRange() {
        return scopeRange;
    }

    public void setScopeRange(String scopeRange) {
        this.scopeRange = scopeRange;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }
}
