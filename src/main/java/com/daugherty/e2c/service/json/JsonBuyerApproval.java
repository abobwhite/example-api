package com.daugherty.e2c.service.json;

import java.util.Date;

import com.daugherty.e2c.domain.BuyerApproval;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a SupplierApproval domain object to/from JSON.
 */
@JsonRootName(value = "buyerApproval")
public class JsonBuyerApproval {

    @JsonProperty("id")
    private String id;
    @JsonProperty(BuyerApproval.STATUS_SERIAL_PROPERTY)
    private String approvalStatus;
    @JsonProperty(BuyerApproval.TITLE_SERIAL_PROPERTY)
    private String title;
    @JsonProperty(BuyerApproval.EMAIL_SERIAL_PROPERTY)
    private String email;
    @JsonProperty(BuyerApproval.VERSION_SERIAL_PROPERTY)
    private Integer version;
    @JsonProperty(BuyerApproval.LAST_UPDATED_BY_SERIAL_PROPERTY)
    private String lastUpdatedBy;
    @JsonProperty(BuyerApproval.LAST_UPDATED_AT_SERIAL_PROPERTY)
    private Date lastUpdatedAt;

    public JsonBuyerApproval() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public final String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        this.email = email;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

}
