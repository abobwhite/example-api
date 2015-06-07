package com.daugherty.e2c.service.json;

import java.util.Date;

import com.daugherty.e2c.domain.SupplierTranslation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a SupplierTranslation domain object to/from JSON.
 */
@JsonRootName(value = "supplierTranslation")
public class JsonSupplierTranslation {

    @JsonProperty("id")
    private String id;
    @JsonProperty(SupplierTranslation.STATUS_SERIAL_PROPERTY)
    private String approvalStatus;
    @JsonProperty(SupplierTranslation.TITLE_SERIAL_PROPERTY)
    private String title;
    @JsonProperty(SupplierTranslation.TYPE_SERIAL_PROPERTY)
    private String translationType;
    @JsonProperty(SupplierTranslation.TRANSLATED_SERIAL_PROPERTY)
    private String translated;
    @JsonProperty(SupplierTranslation.LAST_UPDATED_BY_SERIAL_PROPERTY)
    private String lastUpdatedBy;
    @JsonProperty(SupplierTranslation.LAST_UPDATED_AT_SERIAL_PROPERTY)
    private Date lastUpdatedAt;

    public JsonSupplierTranslation() {
        
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

    public String getTranslationType() {
        return translationType;
    }

    public void setTranslationType(String translationType) {
        this.translationType = translationType;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
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
