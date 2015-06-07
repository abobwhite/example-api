package com.daugherty.e2c.service.json;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

/**
 * Marshalls/unmarshalls a Buyer domain object to/from JSON.
 */
@JsonRootName(value = "buyer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonBuyer extends JsonParty {

    @JsonProperty(Buyer.TITLE_SERIAL_PROPERTY)
    private String title;
    @JsonProperty(Buyer.CHINESE_COMPANY_NAME_PROPERTY)
    private String chineseCompanyName;
    @JsonProperty(Buyer.PROVINCE_SERIAL_PROPERTY)
    private String province;
    @JsonProperty("totalImportAmount")
    private String totalImportAmount;
    @JsonProperty("importLicenseUrl")
    private String importLicenseUrl;
    @JsonProperty("links")
    private Map<String, String> links;

    public JsonBuyer() {
    }

    public JsonBuyer(Buyer buyer, DocumentUrlFactory documentUrlFactory, Locale locale) {
        super(buyer, documentUrlFactory, locale);
        setImportLicenseUrl(documentUrlFactory.createDocumentUrl(buyer.getImportLicenseRefId(), locale));
        setLinks();
    }

    public JsonBuyer(Buyer buyer, DocumentUrlFactory documentUrlFactory, Locale locale, Principal principal) {
        this(buyer, documentUrlFactory, locale);
        secureDataFor(principal);
    }

    @Override
    protected void setContactFields(Contact contact) {
        super.setContactFields(contact);
        setTitle(contact.getTitle());
    }

    @Override
    protected void setCompanyFields(Company company, DocumentUrlFactory documentUrlFactory, Locale locale) {
        super.setCompanyFields(company, documentUrlFactory, locale);
        setChineseCompanyName(company.getChineseName());
        setTotalImportAmount(company.getTotalImportAmount());
    }

    private void secureDataFor(Principal principal) {
        if (principal == null || isNotBuyerModeratorOrThisBuyer(principal)) {
            setEmail(null);
            setSkypeRefId(null);
            setMsnRefId(null);
            setIcqRefId(null);
        }
    }

    private boolean isNotBuyerModeratorOrThisBuyer(Principal principal) {
        E2CUser user = (E2CUser) ((Authentication) principal).getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (Role.BUYER_MODERATOR.equals(authority.getAuthority())) {
                return false;
            } else if (Role.BUYER.equals(authority.getAuthority())) {
                return !Objects.equal(getId(), user.getParty().getPublicId());
            }
        }
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImportLicenseUrl() {
        return importLicenseUrl;
    }

    public void setImportLicenseUrl(String importLicenseUrl) {
        this.importLicenseUrl = importLicenseUrl;
    }

    public String getChineseCompanyName() {
        return chineseCompanyName;
    }

    public void setChineseCompanyName(String chineseCompanyName) {
        this.chineseCompanyName = chineseCompanyName;
    }

    public String getTotalImportAmount() {
        return totalImportAmount;
    }

    public void setTotalImportAmount(String totalImportAmount) {
        this.totalImportAmount = totalImportAmount;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public final void setLinks() {
        links = Maps.newHashMap();
        links.put("messageSummary", "buyers/" + getId() + "/messageSummary");
    }
}
