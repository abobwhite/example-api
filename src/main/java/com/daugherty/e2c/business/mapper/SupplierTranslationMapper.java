package com.daugherty.e2c.business.mapper;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.service.json.JsonSupplierTranslation;

@Component
public class SupplierTranslationMapper {
    public JsonSupplierTranslation fromExistingDomainObject(SupplierTranslation translation) {
        JsonSupplierTranslation json = new JsonSupplierTranslation();
        json.setId(translation.getPublicId());
        json.setApprovalStatus(translation.getApprovalStatus().getName());
        json.setTitle(translation.getTitle());
        json.setTranslationType(translation.getType());
        json.setTranslated(translation.isTranslated() ? "Yes" : "No");
        json.setLastUpdatedBy(translation.getLastUpdatedBy());
        json.setLastUpdatedAt(translation.getLastUpdatedAt());
        return json;
    }
}
