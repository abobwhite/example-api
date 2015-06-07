package com.daugherty.e2c.domain;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * A non-Chinese organization that sells and exports things.
 */
public class Supplier extends Party {

    private static final long serialVersionUID = 1L;

    public static final String GENDER_SERIAL_PROPERTY = "gender";

    private Long certificationId;
    private String exportLicenseRefId;
    private List<Membership> memberships = Lists.newArrayList();
    private Membership provisionalMembership;

    public Supplier(Long id) {
        super(id);
    }

    /**
     * Constructor for new Supplier instances.
     */
    public Supplier(PendingUser pendingUser, Contact contact, Company company) {
        super(pendingUser, contact, company, PartyType.SUPPLIER);
    }

    /**
     * Constructor for existing Supplier instances.
     */
    public Supplier(Long id, String publicId, Contact contact, Company company, ApprovalStatus approvalStatus, Integer version,
            Long snapshotId, String exportLicenseRefId, Membership provisionalMembership, Long certificationId) {
        super(id, publicId, contact, company, approvalStatus, PartyType.SUPPLIER, version, snapshotId);
        this.exportLicenseRefId = exportLicenseRefId;
        this.certificationId = certificationId;
        this.provisionalMembership = provisionalMembership;
    }

    public String getExportLicenseRefId() {
        return exportLicenseRefId;
    }

    public void setExportLicenseRefId(String exportLicenseRefId) {
        this.exportLicenseRefId = exportLicenseRefId;
    }

    public Membership getMembership() {
        if (memberships.isEmpty()) {
            return null;
        } else {
            Date now = new Date();
            for (Membership membership : memberships) {
                if (membership.getEffectiveDate().getTime() <= now.getTime()
                        && now.getTime() <= membership.getExpirationDate().getTime()) {
                    return membership;
                }
            }
        }

        return Collections.max(memberships, new MembershipExpirationDateComparator());
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public Long getMembershipId() {
        Membership membership = getMembership();
        return membership == null ? null : membership.getId();
    }

    public Membership getProvisionalMembership() {
        return provisionalMembership;
    }

    public Long getProvisionalMembershipId() {
        return provisionalMembership == null ? null : provisionalMembership.getId();
    }

    public Integer getMembershipLevel() {
        Membership membership = getMembership();
        return membership == null ? null : membership.getLevel().getValue();
    }

    public Boolean isAdvancedWebAndMailCapabilityEnabled() {
        Membership membership = getMembership();
        return membership == null ? null : membership.getLevel().isAdvancedWebAndMailCapabilityEnabled();
    }

    public Long getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(Long certificationId) {
        this.certificationId = certificationId;
    }

    @Override
    public ValidationError validate() {
        ValidationError errors = super.validate();

        validateCountry(errors);

        if (!ApprovalStatus.UNPROFILED.equals(getApprovalStatus())) {
            validateGender(errors);
            validateBusinessTelephoneNumber(errors);
        }

        return errors;
    }

    private void validateGender(ValidationError errors) {
        if (getContact() == null || getContact().getGender() == null) {
            errors.add(GENDER_SERIAL_PROPERTY, SUPPLIER_GENDER_REQUIRED);
        }
    }

    protected void validateBusinessTelephoneNumber(ValidationError errors) {
        if (getContact() == null || StringUtils.isEmpty(getContact().getBusinessTelephoneNumber())) {
            errors.add(BUSINESS_PHONE_SERIAL_PROPERTY, PARTY_BUSINESS_TELEPHONE_REQUIRED);
        }

        if (getContact() == null || StringUtils.isEmpty(getContact().getBusinessTelephoneNumber())
                || getContact().getBusinessTelephoneNumber().length() > 20) {
            errors.add(BUSINESS_PHONE_SERIAL_PROPERTY, PARTY_BUSINESS_TELEPHONE_LENGTH);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        super.addEntityFieldsToEqualsBuilder(builder, obj);
        Supplier rhs = (Supplier) obj;
        builder.append(exportLicenseRefId, rhs.exportLicenseRefId).append(memberships, rhs.memberships)
                .append(provisionalMembership, rhs.provisionalMembership).append(certificationId, rhs.certificationId);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        super.addEntityFieldsToHashCodeBuilder(builder);
        builder.append(exportLicenseRefId).append(memberships).append(provisionalMembership).append(certificationId);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        super.addEntityFieldsToToStringBuilder(builder);
        builder.append("exportLicenseRefId", exportLicenseRefId).append("memberships", memberships)
                .append("provisionalMembership", provisionalMembership).append("certificationId", certificationId);
    }
}
