package com.daugherty.e2c.service.json;

import java.util.Date;

import com.daugherty.e2c.domain.SupplierApproval;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a SupplierApproval domain object to/from JSON.
 */
@JsonRootName(value = "supplierApproval")
public class JsonSupplierApproval {

    @JsonProperty("id")
    private String id;
    @JsonProperty(SupplierApproval.STATUS_SERIAL_PROPERTY)
    private String approvalStatus;
    @JsonProperty(SupplierApproval.TITLE_SERIAL_PROPERTY)
    private String title;
    @JsonProperty(SupplierApproval.SUPPLIER_SERIAL_PROPERTY)
    private JsonSupplier supplier;
    @JsonProperty(SupplierApproval.TYPE_SERIAL_PROPERTY)
    private String approvalType;
    @JsonProperty(SupplierApproval.VERSION_SERIAL_PROPERTY)
    private Integer version;
    @JsonProperty(SupplierApproval.LAST_UPDATED_BY_SERIAL_PROPERTY)
    private String lastUpdatedBy;
    @JsonProperty(SupplierApproval.LAST_UPDATED_AT_SERIAL_PROPERTY)
    private Date lastUpdatedAt;

    public JsonSupplierApproval() {

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

    public JsonSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(JsonSupplier supplier) {
        this.supplier = supplier;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
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
