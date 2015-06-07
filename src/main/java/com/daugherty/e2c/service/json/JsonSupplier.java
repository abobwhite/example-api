package com.daugherty.e2c.service.json;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Transport object representing a supplier domain object
 */
@JsonRootName(value = "supplier")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonSupplier extends JsonParty {

    @JsonProperty(Supplier.GENDER_SERIAL_PROPERTY)
    private String gender;
    @JsonProperty("exportLicenseUrl")
    private String exportLicenseUrl;
    @JsonProperty("companyVideoUrl")
    private String videoUrl;
    @JsonProperty("memberships")
    private List<JsonMembership> memberships;
    @JsonProperty("provisionalMembership")
    private Long provisionalMembershipId;
    @JsonProperty("links")
    protected Map<String, String> links;

    public JsonSupplier() {
    }

    public JsonSupplier(String supplierId) {
        setId(supplierId);
    }

    public JsonSupplier(Supplier supplier, DocumentUrlFactory documentUrlFactory, Locale locale) {
        super(supplier, documentUrlFactory, locale);
        setExportLicenseUrl(documentUrlFactory.createDocumentUrl(supplier.getExportLicenseRefId(), locale));
        setUpMemberships(supplier.getMemberships());
        setProvisionalMembership(supplier.getProvisionalMembershipId());
        setLinks(supplier.getCertificationId());
    }

    public JsonSupplier(Supplier supplier, DocumentUrlFactory documentUrlFactory, Locale locale, Principal principal) {
        this(supplier, documentUrlFactory, locale);
        secureDataFor(principal, supplier);
    }

    @Override
    protected void setContactFields(Contact contact) {
        super.setContactFields(contact);
        if (contact.getGender() != null) {
            setGender(contact.getGender().getReadableName());
        }
    }

    @Override
    protected void setCompanyFields(Company company, DocumentUrlFactory documentUrlFactory, Locale locale) {
        super.setCompanyFields(company, documentUrlFactory, locale);
        setVideoUrl(company.getVideoRefId());
    }

    private void secureDataFor(Principal principal, Supplier supplier) {
        if (principal == null || isNotSupplierModeratorOrThisSupplier(principal)) {
            setEmail(null);
            setSkypeRefId(null);
            setMsnRefId(null);
            setIcqRefId(null);
            if (Boolean.FALSE.equals(supplier.isAdvancedWebAndMailCapabilityEnabled())) {
                setWebsite(null);
            }
        }
    }

    private boolean isNotSupplierModeratorOrThisSupplier(Principal principal) {
        E2CUser user = (E2CUser) ((Authentication) principal).getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (Role.SUPPLIER_MODERATOR.equals(authority.getAuthority()) || Role.ADMIN.equals(authority.getAuthority())) {
                return false;
            } else if (Role.SUPPLIER.equals(authority.getAuthority())) {
                return !Objects.equal(getId(), user.getParty().getPublicId());
            }
        }
        return true;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExportLicenseUrl() {
        return exportLicenseUrl;
    }

    public void setExportLicenseUrl(String exportLicenseUrl) {
        this.exportLicenseUrl = exportLicenseUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Map<String, String> getLinks() {
        return links;
    }
    
    public List<JsonMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<JsonMembership> memberships) {
        this.memberships = memberships;
    }

    protected void setLinks(Long certificationId) {
        Long now = new Date().getTime();

        links = Maps.newHashMap();
        links.put("products", "suppliers/" + getId() + "/products?_=" + now);
        if (certificationId == null || certificationId == 0L) {
            links.put("certification", null);
        } else {
            links.put("certification", "certifications/" + certificationId + "?_=" + now);
        }
        links.put("messageSummary", "suppliers/" + getId() + "/messageSummary");
    }

    private void setUpMemberships(List<Membership> memberships) {
        List<JsonMembership> jsonMemberships = Lists.newArrayList();
        if (!memberships.isEmpty()) {
            for (Membership membership : memberships) {
                jsonMemberships.add(new JsonMembership(membership, this.getId()));
            }

            this.memberships = jsonMemberships;
        }

    }

    public Long getProvisionalMembership() {
        return provisionalMembershipId;
    }

    public void setProvisionalMembership(Long provisionalMembershipId) {
        this.provisionalMembershipId = provisionalMembershipId;
    }
}
