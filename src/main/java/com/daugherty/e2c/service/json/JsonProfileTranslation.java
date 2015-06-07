package com.daugherty.e2c.service.json;

import java.util.Map;

import com.daugherty.e2c.domain.ProfileTranslation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a ProfileTranslation domain object to/from JSON.
 */
@JsonRootName(value = "profileTranslation")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonProfileTranslation {

    @JsonProperty("id")
    private String id;
    @JsonProperty(ProfileTranslation.COMPANY_DESCRIPTION_SERIAL_PROPERTY)
    private String companyDescription;
    @JsonProperty(ProfileTranslation.COMPANY_DESCRIPTION_TRANSLATION_SERIAL_PROPERTY)
    private String companyDescriptionTranslation;
    @JsonProperty("links")
    private Map<String, String> links;
    @JsonProperty("translationType")
    public static final String type = "Profile";

    public JsonProfileTranslation() {
        
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescrption) {
        companyDescription = companyDescrption;
    }

    public String getCompanyDescriptionTranslation() {
        return companyDescriptionTranslation;
    }

    public void setCompanyDescriptionTranslation(String companyDescriptionTranslation) {
        this.companyDescriptionTranslation = companyDescriptionTranslation;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public static String getType() {
        return type;
    }
}
