package com.daugherty.e2c.business.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.service.json.JsonProfileTranslation;

@Component
public class ProfileTranslationMapper {
    public ProfileTranslation toExistingDomainObject(Supplier supplier, JsonProfileTranslation profileTranslation) {
        return new ProfileTranslation(supplier.getId(), supplier.getPublicId(), supplier.getSnapshotId(), profileTranslation.getCompanyDescription(),
                profileTranslation.getCompanyDescriptionTranslation());
    }
    
    public JsonProfileTranslation fromExistingDomainObject(ProfileTranslation translation, Map<String, String> links) {
        JsonProfileTranslation json = new JsonProfileTranslation();
        json.setId(translation.getPublicId());
        json.setCompanyDescription(translation.getCompanyDescription());
        json.setCompanyDescriptionTranslation(translation.getCompanyDescriptionTranslation());
        json.setLinks(links);
        
        return json;
    }
}
