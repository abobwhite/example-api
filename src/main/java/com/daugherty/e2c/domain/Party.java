package com.daugherty.e2c.domain;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.CollectionUtils;

/**
 * An organization that participates in an import-export relationship.
 */
public class Party extends Entity implements Approvable, Validatable {

    private static final long serialVersionUID = 1L;

    public static final String EMAIL_SERIAL_PROPERTY = "email";
    public static final String FIRST_NAME_SERIAL_PROPERTY = "firstName";
    public static final String LAST_NAME_SERIAL_PROPERTY = "lastName";
    public static final String COUNTRY_SERIAL_PROPERTY = "country";
    public static final String SKYPE_SERIAL_PROPERTY = "skypeRefId";
    public static final String MSN_SERIAL_PROPERTY = "msnRefId";
    public static final String ICQ_SERIAL_PROPERTY = "icqRefId";
    public static final String ENGLISH_COMPANY_NAME_SERIAL_PROPERTY = "englishCompanyName";
    public static final String COMPANY_DESCRIPTION_SERIAL_PROPERTY = "description";
    public static final String BUSINESS_TYPES_SERIAL_PROPERTY = "businessTypes";
    public static final String LANGUAGE_SERIAL_PROPERTY = "language";
    public static final String WEBSITE_SERIAL_PROPERTY = "website";
    public static final String REGISTRATION_DATE = "registrationDate";
    public static final String BUSINESS_PHONE_SERIAL_PROPERTY = "businessTelephoneNumber";
    public static final String PARTY_TYPE_SERIAL_PROPERTY = "partyType";
    public static final String PROVINCE_SERIAL_PROPERTY = "province";

    private static final String PERSONAL_NAME_EMAIL_FORMAT = "{0} {1} <{2}>";
    private static final String CHINA = "中国";

    public static String buildEmailWithPersonalName(String firstName, String lastName, String emailAddress) {
        if (firstName == null || lastName == null) {
            return emailAddress;
        }

        return MessageFormat.format(PERSONAL_NAME_EMAIL_FORMAT, firstName, lastName, emailAddress);
    }

    private String publicId;
    private PartyType partyType;
    private PendingUser pendingUser;
    private Contact contact;
    private Company company;
    private ApprovalStatus approvalStatus = ApprovalStatus.UNPROFILED;
    private Integer version = 1;
    private Long snapshotId;
    protected String username;

    public Party(Long id) {
        super(id);
    }

    public Party(Long id, String publicId) {
        super(id);
        this.publicId = publicId;
    }

    /**
     * Constructor for new Party instances.
     */
    public Party(PendingUser pendingUser, Contact contact, Company company, PartyType partyType) {
        this(null, null, contact, company, ApprovalStatus.UNPROFILED, partyType, 1, null);
        this.pendingUser = pendingUser;
    }

    /**
     * Constructor for existing Party instances.
     */
    public Party(Long id, String publicId, Contact contact, Company company, ApprovalStatus approvalStatus,
            PartyType partyType, Integer version, Long snapshotId) {
        super(id);
        this.publicId = publicId;
        this.contact = contact;
        this.company = company;
        this.approvalStatus = approvalStatus;
        this.partyType = partyType;
        this.version = version;
        this.snapshotId = snapshotId;
    }

    public PendingUser getPendingUser() {
        return pendingUser;
    }

    public Contact getContact() {
        return contact;
    }

    public Company getCompany() {
        return company;
    }

    public String getEmailWithPersonalName() {
        return buildEmailWithPersonalName(contact.getFirstName(), contact.getLastName(), contact.getEmailAddress());
    }

    public Boolean isAdvancedWebAndMailCapabilityEnabled() {
        return false;
    }

    @Override
    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public PartyType getPartyType() {
        return partyType;
    }

    public void setPartyType(PartyType partyType) {
        this.partyType = partyType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

        if (ApprovalStatus.UNPROFILED.equals(getApprovalStatus()) && pendingUser != null) {
            error.add(pendingUser.validate());
        } else {
            validateFirstName(error);
            validateLastName(error);
            validateSkypeId(error);
            validateMsnId(error);
            validateIcqId(error);
            validateCompanyEnglishName(error);
            validateCompanyDescription(error);
            validateBusinessTypes(error);
            validateWebsite(error);
        }

        // Always perform these validations

        validateCountry(error);
        validateEmail(error);

        return error;
    }

    protected void validateEmail(ValidationError errors) {
        // TODO Length???
        if (contact == null || StringUtils.isEmpty(contact.getEmailAddress())) {
            errors.add(EMAIL_SERIAL_PROPERTY, PARTY_EMAIL_REQUIRED);
        } else {
            if (!contact.getEmailAddress().matches("^[^\\s]+@[^.\\s]+(\\.[^.\\s]+)*$")) {
                errors.add(EMAIL_SERIAL_PROPERTY, PARTY_EMAIL_FORMAT);
            }
        }
    }

    protected void validateFirstName(ValidationError errors) {
        if (contact == null || StringUtils.isEmpty(contact.getFirstName())) {
            errors.add(FIRST_NAME_SERIAL_PROPERTY, PARTY_FIRST_NAME_REQUIRED);
        }
        if (contact == null || StringUtils.isEmpty(contact.getFirstName()) || contact.getFirstName().length() > 30) {
            errors.add(FIRST_NAME_SERIAL_PROPERTY, PARTY_FIRST_NAME_LENGTH);
        }
    }

    protected void validateLastName(ValidationError errors) {
        if (contact == null || StringUtils.isEmpty(contact.getLastName())) {
            errors.add(LAST_NAME_SERIAL_PROPERTY, PARTY_LAST_NAME_REQUIRED);
        }
        if (contact == null || StringUtils.isEmpty(contact.getLastName()) || contact.getLastName().length() > 30) {
            errors.add(LAST_NAME_SERIAL_PROPERTY, PARTY_LAST_NAME_LENGTH);
        }
    }

    protected void validateCountry(ValidationError errors) {
        if (getContact() == null || StringUtils.isEmpty(getContact().getCountry())) {
            errors.add(COUNTRY_SERIAL_PROPERTY, PARTY_COUNTRY);
        }
    }

    protected void validateProvince(ValidationError errors) {
        if (!errors.getErrors().containsEntry(COUNTRY_SERIAL_PROPERTY, Validatable.PARTY_COUNTRY)) {
            if (CHINA.equals(getContact().getCountry()) && StringUtils.isEmpty(getContact().getProvince())) {
                errors.add(PROVINCE_SERIAL_PROPERTY, BUYER_PROVINCE);
            }
        }
    }

    private void validateSkypeId(ValidationError errors) {
        if (getContact() == null
                || (StringUtils.isNotEmpty(getContact().getSkypeRefId()) && getContact().getSkypeRefId().length() > 32)) {
            errors.add(SKYPE_SERIAL_PROPERTY, PARTY_SKYPE_LENGTH);
        }
    }

    private void validateMsnId(ValidationError errors) {
        if (getContact() == null
                || (StringUtils.isNotEmpty(getContact().getMsnRefId()) && getContact().getMsnRefId().length() > 75)) {
            errors.add(MSN_SERIAL_PROPERTY, PARTY_MSN_LENGTH);
        }
    }

    private void validateIcqId(ValidationError errors) {
        if (getContact() == null
                || (StringUtils.isNotEmpty(getContact().getIcqRefId()) && getContact().getIcqRefId().length() > 75)) {
            errors.add(ICQ_SERIAL_PROPERTY, PARTY_ICQ_LENGTH);
        }
    }

    protected void validateCompanyEnglishName(ValidationError errors) {
        if (getCompany() == null || StringUtils.isEmpty(getCompany().getEnglishName())) {
            errors.add(ENGLISH_COMPANY_NAME_SERIAL_PROPERTY, PARTY_ENGLISH_COMPANY_NAME_REQUIRED);
        }
        if (getCompany() == null || StringUtils.isEmpty(getCompany().getEnglishName())
                || getCompany().getEnglishName().length() > 60) {
            errors.add(ENGLISH_COMPANY_NAME_SERIAL_PROPERTY, PARTY_ENGLISH_COMPANY_NAME_LENGTH);
        }
    }

    private void validateCompanyDescription(ValidationError errors) {
        if (getCompany() == null || StringUtils.isEmpty(getCompany().getDescription())) {
            errors.add(COMPANY_DESCRIPTION_SERIAL_PROPERTY, PARTY_COMPANY_DESCRIPTION_REQUIRED);
        }
        if (getCompany() == null || StringUtils.isEmpty(getCompany().getDescription())
                || getCompany().getDescription().length() > 4000) {
            errors.add(COMPANY_DESCRIPTION_SERIAL_PROPERTY, PARTY_COMPANY_DESCRIPTION_LENGTH);
        }
    }

    private void validateBusinessTypes(ValidationError errors) {
        if (getCompany() == null || CollectionUtils.isEmpty(getCompany().getBusinessTypes())) {
            errors.add(BUSINESS_TYPES_SERIAL_PROPERTY, PARTY_BUSINESS_TYPES_REQUIRED);
        }
        if (getCompany() == null || CollectionUtils.isEmpty(getCompany().getBusinessTypes())
                || getCompany().getBusinessTypes().size() > 3) {
            errors.add(BUSINESS_TYPES_SERIAL_PROPERTY, PARTY_BUSINESS_TYPES_LENGTH);
        }
    }

    protected void validateWebsite(ValidationError errors) {
        if (getCompany() == null
                || (StringUtils.isNotEmpty(getCompany().getWebsite()) && getCompany().getWebsite().length() > 70)) {
            errors.add(WEBSITE_SERIAL_PROPERTY, PARTY_WEBSITE_LENGTH);
        }
    }

    protected void validateBusinessTelephoneNumber(ValidationError errors) {
        if (getContact() != null && !StringUtils.isEmpty(getContact().getBusinessTelephoneNumber())
                && getContact().getBusinessTelephoneNumber().length() > 20) {
            errors.add(BUSINESS_PHONE_SERIAL_PROPERTY, PARTY_BUSINESS_TELEPHONE_LENGTH);
        }
    }

    public boolean isBuyer() {
        return PartyType.BUYER.equals(partyType);
    }

    public boolean isAnonymous() {
        return PartyType.ANONYMOUS.equals(partyType);
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        Party rhs = (Party) obj;
        builder.append(contact, rhs.contact).append(company, rhs.company);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 1;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(contact).append(company);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("pendingUser", pendingUser).append("contact", contact).append("company", company)
                .append("approvalStatus", approvalStatus).append("version", version).append("snapshotId", snapshotId);
    }
}
