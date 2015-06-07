package com.daugherty.e2c.business.mapper;

import java.security.Principal;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.service.json.JsonBuyer;

/**
 * Marshalls/unmarshalls a Buyer domain object to/from JSON.
 */
@Component
public class BuyerMapper extends PartyMapper {
    
    public Buyer toNewDomainObject(JsonBuyer json, Locale locale){
        return new Buyer(toPendingUserObject(json), toContactDomainObject(json, json.getChineseCompanyName(), null, locale), 
                toCompanyDomainObject(json, json.getChineseCompanyName(), json.getTotalImportAmount(), null, documentUrlFactory, locale));
    }
    
    public Buyer toExistingDomainObject(String publicId, JsonBuyer json, Locale locale){
        Long partyId = hashids.decode(publicId)[0];
        return new Buyer(partyId, publicId, toContactDomainObject(json, json.getTitle(), null, locale), 
                toCompanyDomainObject(json, json.getChineseCompanyName(), json.getTotalImportAmount(), null, documentUrlFactory, locale), 
                ApprovalStatus.findByName(json.getApprovalStatus()), json.getVersion(), null, 
                documentUrlFactory.removeDocumentUrl(json.getImportLicenseUrl(), locale));
    }
    
    public JsonBuyer fromDomainObject(Buyer buyer, Locale locale, Principal principal){
        return new JsonBuyer(buyer, documentUrlFactory, locale, principal);
    }
    
    public JsonBuyer fromDomainObject(Buyer buyer, Locale locale){
        return new JsonBuyer(buyer, documentUrlFactory, locale);
    }
    
    
}
