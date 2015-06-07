package com.daugherty.e2c.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Chinese translation of a Supplier (profile). This is also part of the overall Supplier approval process.
 */
public class ProfileTranslation extends Entity {

    private static final long serialVersionUID = 1L;

    public static final String COMPANY_DESCRIPTION_SERIAL_PROPERTY = "companyDescription";
    public static final String COMPANY_DESCRIPTION_TRANSLATION_SERIAL_PROPERTY = "companyDescriptionTranslation";

    private String publicId;
    private Long snapshotId;
    private final String companyDescription;
    private final String companyDescriptionTranslation;

    public ProfileTranslation(Long id, String publicId, Long snapshotId, String companyDescription, String companyDescriptionTranslation) {
        super(id);
        this.publicId = publicId;
        this.snapshotId = snapshotId;
        this.companyDescription = companyDescription;
        this.companyDescriptionTranslation = companyDescriptionTranslation;
    }
    
    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public String getCompanyDescriptionTranslation() {
        return companyDescriptionTranslation;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        ProfileTranslation rhs = (ProfileTranslation) obj;
        builder.append(snapshotId, rhs.getSnapshotId());
    }

    @Override
    protected int hashCodeMultiplier() {
        return 23;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(snapshotId);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("snapshotId", snapshotId).append("companyDescription", companyDescription)
                .append("companyDescriptionTranslation", companyDescriptionTranslation);
    }
}
