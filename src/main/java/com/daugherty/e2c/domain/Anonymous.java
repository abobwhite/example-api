package com.daugherty.e2c.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An Anonymous party of the system
 */
public class Anonymous extends Party {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor for new Anonymous instances.
     */
    public Anonymous(Contact contact, Company company) {
        this(null, null, contact, company, ApprovalStatus.UNPROFILED, 1, null);
    }

    /**
     * Constructor for existing Anonymous instances.
     */
    public Anonymous(Long id, String publicId, Contact contact, Company company, ApprovalStatus approvalStatus, Integer version,
            Long snapshotId) {
        super(id, publicId, contact, company, approvalStatus, PartyType.ANONYMOUS, version, snapshotId);
    }

    @Override
    public ValidationError validate() {
        ValidationError error = new ValidationError();

        validateFirstName(error);
        validateLastName(error);
        validateCompanyEnglishName(error);
        validateEmail(error);
        validateWebsite(error);
        validateCountry(error);
        validateProvince(error);
        validateBusinessTelephoneNumber(error);

        return error;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        super.addEntityFieldsToEqualsBuilder(builder, obj);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        super.addEntityFieldsToHashCodeBuilder(builder);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        super.addEntityFieldsToToStringBuilder(builder);
    }
}
