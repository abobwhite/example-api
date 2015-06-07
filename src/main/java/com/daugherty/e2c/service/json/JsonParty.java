package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Base class for Buyer and Supplier JSON serialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("party")
public class JsonParty {

    @JsonProperty("id")
    private String id;
    @JsonProperty(PendingUser.USERNAME_SERIAL_PROPERTY)
    private String username;
    @JsonProperty(PendingUser.PASSWORD_SERIAL_PROPERTY)
    private String password;
    @JsonProperty(PendingUser.PASSWORD_CONFIRMATION_SERIAL_PROPERTY)
    private String passwordConfirmation;
    @JsonProperty(Party.FIRST_NAME_SERIAL_PROPERTY)
    private String firstName;
    @JsonProperty(Party.LAST_NAME_SERIAL_PROPERTY)
    private String lastName;
    @JsonProperty(Party.EMAIL_SERIAL_PROPERTY)
    private String email;
    @JsonProperty(Party.SKYPE_SERIAL_PROPERTY)
    private String skypeRefId;
    @JsonProperty(Party.MSN_SERIAL_PROPERTY)
    private String msnRefId;
    @JsonProperty(Party.ICQ_SERIAL_PROPERTY)
    private String icqRefId;
    @JsonProperty(Party.ENGLISH_COMPANY_NAME_SERIAL_PROPERTY)
    private String englishCompanyName;
    @JsonProperty(Party.COMPANY_DESCRIPTION_SERIAL_PROPERTY)
    private String description;
    @JsonProperty("logoUrl")
    private String logoUrl;
    @JsonProperty("logoThumbnailUrl")
    private String logoThumbnailUrl;
    @JsonProperty(Party.WEBSITE_SERIAL_PROPERTY)
    private String website;
    @JsonProperty("yearEstablished")
    private Integer yearEstablished;
    @JsonProperty("numberOfEmployees")
    private String numberOfEmployees;
    @JsonProperty("totalAnnualSales")
    private String totalAnnualSales;
    @JsonProperty("businessTypes")
    private List<String> businessTypes;
    @JsonProperty("approvalStatus")
    private String approvalStatus;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty(Party.COUNTRY_SERIAL_PROPERTY)
    private String country;
    @JsonProperty("ipAddress")
    private String ipAddress;
    @JsonProperty(Party.LANGUAGE_SERIAL_PROPERTY)
    private String language;
    @JsonProperty(Party.REGISTRATION_DATE)
    private Date registrationDate;
    @JsonProperty(Party.BUSINESS_PHONE_SERIAL_PROPERTY)
    private String businessTelephoneNumber;
    @JsonProperty(Party.PARTY_TYPE_SERIAL_PROPERTY)
    private String partyType;
    @JsonProperty(Party.PROVINCE_SERIAL_PROPERTY)
    private String province;

    public JsonParty() {
        super();
    }

    public JsonParty(Party party, DocumentUrlFactory documentUrlFactory, Locale locale) {
        setId(party.getPublicId());
        setApprovalStatus(party.getApprovalStatus().getName());
        setVersion(party.getVersion());
        setPartyType(party.getPartyType() == null ? null : party.getPartyType().getType());
        setUsername(party.getUsername());

        Contact contact = party.getContact();
        if (contact != null) {
            setContactFields(contact);
        }

        Company company = party.getCompany();
        if (company != null) {
            setCompanyFields(company, documentUrlFactory, locale);
        }
    }

    protected void setContactFields(Contact contact) {
        setFirstName(contact.getFirstName());
        setLastName(contact.getLastName());
        setEmail(contact.getEmailAddress());
        setSkypeRefId(contact.getSkypeRefId());
        setMsnRefId(contact.getMsnRefId());
        setIcqRefId(contact.getIcqRefId());
        setCountry(contact.getCountry());
        setProvince(contact.getProvince());
        setIpAddress(contact.getIpAddress());
        setLanguage(getLanguage(contact));
        setRegistrationDate(contact.getRegistrationDate());
        setBusinessTelephoneNumber(contact.getBusinessTelephoneNumber());
    }

    private String getLanguage(Contact contact) {
        return contact.getLanguage() == null ? null : contact.getLanguage().getDisplayName();
    }

    protected void setCompanyFields(Company company, DocumentUrlFactory documentUrlFactory, Locale locale) {
        setEnglishCompanyName(company.getEnglishName());
        setDescription(company.getDescription());
        setLogoUrl(documentUrlFactory.createDocumentUrl(company.getLogoRefId(), locale));
        setLogoThumbnailUrl(documentUrlFactory.createE2CDocumentThumbnailUrl(company.getLogoRefId(), locale));
        setWebsite(company.getWebsite());
        setYearEstablished(company.getYearEstablished());
        setNumberOfEmployees(company.getNumberOfEmployees());
        setTotalAnnualSales(company.getTotalAnnualSales());
        setBusinessTypes(serializeBusinessTypes(company.getBusinessTypes()));
    }

    protected List<String> serializeBusinessTypes(List<BusinessType> businessTypes) {
        List<String> jsonBusinessTypes = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(businessTypes)) {
            for (BusinessType businessType : businessTypes) {
                jsonBusinessTypes.add(businessType.getName());
            }
        }
        return jsonBusinessTypes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkypeRefId() {
        return skypeRefId;
    }

    public void setSkypeRefId(String skypeRefId) {
        this.skypeRefId = skypeRefId;
    }

    public String getMsnRefId() {
        return msnRefId;
    }

    public void setMsnRefId(String msnRefId) {
        this.msnRefId = msnRefId;
    }

    public String getIcqRefId() {
        return icqRefId;
    }

    public void setIcqRefId(String icqRefId) {
        this.icqRefId = icqRefId;
    }

    public String getEnglishCompanyName() {
        return englishCompanyName;
    }

    public void setEnglishCompanyName(String englishCompanyName) {
        this.englishCompanyName = englishCompanyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoThumbnailUrl() {
        return logoThumbnailUrl;
    }

    public void setLogoThumbnailUrl(String logoThumbnailUrl) {
        this.logoThumbnailUrl = logoThumbnailUrl;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getYearEstablished() {
        return yearEstablished;
    }

    public void setYearEstablished(Integer yearEstablished) {
        this.yearEstablished = yearEstablished;
    }

    public String getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(String numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getTotalAnnualSales() {
        return totalAnnualSales;
    }

    public void setTotalAnnualSales(String totalAnnualSales) {
        this.totalAnnualSales = totalAnnualSales;
    }

    public List<String> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(List<String> businessTypes) {
        this.businessTypes = businessTypes;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getBusinessTelephoneNumber() {
        return businessTelephoneNumber;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public void setBusinessTelephoneNumber(String businessTelephoneNumber) {
        this.businessTelephoneNumber = businessTelephoneNumber;
    }

    // TODO: Get rid of EVERYTHING below this comment in favor of mappers

    protected List<BusinessType> toBusinessTypeDomainObjects(List<String> businessTypes) {
        List<BusinessType> domainBusinessTypes = new ArrayList<BusinessType>();
        if (!CollectionUtils.isEmpty(businessTypes)) {
            for (String businessTypeName : businessTypes) {
                domainBusinessTypes.add(BusinessType.findByName(businessTypeName));
            }
        }
        return domainBusinessTypes;
    }

    protected Language verifyLanguageEnumValue(Locale locale) {
        return locale == null ? null : Language.findByAbbreviation(locale.getLanguage());
    }
}
