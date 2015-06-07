package com.daugherty.e2c.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An individual who is a contact point for an organization.
 */
public class Contact extends ValueObject {

    private static final long serialVersionUID = 1L;

    private final String title;
    private final Gender gender;
    private String firstName;
    private String lastName;
    private String country;
    private Language language;
    private final String province;
    private String emailAddress;
    private final String skypeRefId;
    private final String msnRefId;
    private final String icqRefId;
    private final String businessTelephoneNumber;
    private final String ipAddress;
    private final Date registrationDate;

    public Contact(String firstName, String lastName, String country, String province, String emailAddress,
            String ipAddress, Language language, Date registrationDate) {
        this(null, null, firstName, lastName, country, province, emailAddress, null, null, null, null, ipAddress,
                language, registrationDate);
    }

    public Contact(String title, Gender gender, String firstName, String lastName, String country, String province,
            String emailAddress, String skypeRefId, String msnRefId, String icqRefId, String businessTelephoneNumber,
            String ipAddress, Language language, Date registrationDate) {
        this.title = title;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.province = province;
        this.emailAddress = emailAddress == null ? null : emailAddress.trim();
        this.skypeRefId = skypeRefId;
        this.msnRefId = msnRefId;
        this.icqRefId = icqRefId;
        this.businessTelephoneNumber = businessTelephoneNumber;
        this.ipAddress = ipAddress;
        this.language = language;
        this.registrationDate = registrationDate;
    }

    public String getTitle() {
        return title;
    }

    public Gender getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getSkypeRefId() {
        return skypeRefId;
    }

    public String getMsnRefId() {
        return msnRefId;
    }

    public String getIcqRefId() {
        return icqRefId;
    }

    public String getBusinessTelephoneNumber() {
        return businessTelephoneNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    @Override
    protected void addFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        Contact rhs = (Contact) obj;
        builder.append(title, rhs.title).append(gender, rhs.gender).append(firstName, rhs.firstName)
                .append(lastName, rhs.lastName).append(country, rhs.country).append(province, rhs.province)
                .append(emailAddress, rhs.emailAddress).append(skypeRefId, rhs.skypeRefId)
                .append(msnRefId, rhs.msnRefId).append(icqRefId, rhs.icqRefId)
                .append(businessTelephoneNumber, rhs.businessTelephoneNumber);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 1001;
    }

    @Override
    protected void addFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(title).append(gender).append(firstName).append(lastName).append(country).append(province)
                .append(emailAddress).append(skypeRefId).append(msnRefId).append(icqRefId);
    }

    @Override
    protected void addFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("title", title).append("gender", gender).append("firstName", firstName)
                .append("lastName", lastName).append("country", country).append("province", province)
                .append("emailAddress", emailAddress).append("skypeRefId", skypeRefId).append("msnRefId", msnRefId)
                .append("icqRefId", icqRefId).append("businessTelephoneNumber", businessTelephoneNumber)
                .append("ipAddress", ipAddress).append("language", language);
    }

}
