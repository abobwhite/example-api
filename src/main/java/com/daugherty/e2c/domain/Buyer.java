package com.daugherty.e2c.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A Chinese organization that buys and import things.
 */
public class Buyer extends Party {

    private static final long serialVersionUID = 1L;

    public static final String TITLE_SERIAL_PROPERTY = "title";
    public static final String CHINESE_COMPANY_NAME_PROPERTY = "chineseCompanyName";

    private String importLicenseRefId;

    /**
     * Constructor for new Buyer instances.
     */
    public Buyer(PendingUser pendingUser, Contact contact, Company company) {
        super(pendingUser, contact, company, PartyType.BUYER);
    }

    /**
     * Constructor for existing Buyer instances.
     */
    public Buyer(Long id, String publicId, Contact contact, Company company, ApprovalStatus approvalStatus, Integer version,
            Long snapshotId, String importLicenseRefId) {
        super(id, publicId, contact, company, approvalStatus, PartyType.BUYER, version, snapshotId);
        this.importLicenseRefId = importLicenseRefId;
    }

    public String getImportLicenseRefId() {
        return importLicenseRefId;
    }

    public void setImportLicenseRefId(String importLicenseRefId) {
        this.importLicenseRefId = importLicenseRefId;
    }

    @Override
    public ValidationError validate() {
        ValidationError error = super.validate();

        if (!ApprovalStatus.UNPROFILED.equals(getApprovalStatus())) {
            validateCountry(error);
            validateProvince(error);
            validateTitle(error);
            validateBusinessTelephoneNumber(error);
            validateCompanyChineseName(error);
        }

        return error;
    }

    private void validateTitle(ValidationError errors) {
        if (getContact() == null || StringUtils.isEmpty(getContact().getTitle())) {
            errors.add(TITLE_SERIAL_PROPERTY, BUYER_TITLE_REQUIRED);
        }
    }

    private void validateCompanyChineseName(ValidationError errors) {
        if (getCompany() == null || StringUtils.isEmpty(getCompany().getChineseName())) {
            errors.add(CHINESE_COMPANY_NAME_PROPERTY, BUYER_CHINESE_COMPANY_NAME_REQUIRED);
        }
        if (getCompany() == null || StringUtils.isEmpty(getCompany().getChineseName())
                || getCompany().getChineseName().length() > 50) {
            errors.add(CHINESE_COMPANY_NAME_PROPERTY, BUYER_CHINESE_COMPANY_NAME_LENGTH);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        super.addEntityFieldsToEqualsBuilder(builder, obj);
        Buyer rhs = (Buyer) obj;
        builder.append(importLicenseRefId, rhs.importLicenseRefId);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        super.addEntityFieldsToHashCodeBuilder(builder);
        builder.append(importLicenseRefId);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        super.addEntityFieldsToToStringBuilder(builder);
        builder.append("importLicenseRefId", importLicenseRefId);
    }
}
