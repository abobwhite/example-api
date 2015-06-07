package com.daugherty.e2c.business.mapper;

import java.security.Principal;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.service.json.JsonSupplier;

/**
 * Marshalls/unmarshalls a Supplier domain object to/from JSON.
 */
@Component
public class SupplierMapper extends PartyMapper{
    
    public Supplier toNewDomainObject(JsonSupplier json, Locale locale){
        return new Supplier(toPendingUserObject(json), toContactDomainObject(json, null, json.getGender(), locale), toCompanyDomainObject(json, null, null, 
                json.getVideoUrl(), documentUrlFactory, locale));
    }
    
    public Supplier toExistingDomainObject(String publicId, JsonSupplier json, Locale locale){
        Long partyId = hashids.decode(publicId)[0];
        return new Supplier(partyId, publicId, toContactDomainObject(json, null, json.getGender(), null), toCompanyDomainObject(json, null, null, 
                json.getVideoUrl(), documentUrlFactory, locale), ApprovalStatus.findByName(json.getApprovalStatus()), json.getVersion(), null,
                documentUrlFactory.removeDocumentUrl(json.getExportLicenseUrl(), locale), null, null);
    }
    
    public JsonSupplier fromDomainObject(Supplier supplier, Locale locale, Principal principal){
        return new JsonSupplier(supplier, documentUrlFactory, locale, principal);
    }
    
    public JsonSupplier fromDomainObject(Supplier supplier, Locale locale){
        return new JsonSupplier(supplier, documentUrlFactory, locale);
    }
}
