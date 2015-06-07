package com.daugherty.e2c.business.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonParty;

@Component
public class PartyMapper {
    @Inject
    protected Hashids hashids;
    @Inject
    protected DocumentUrlFactory documentUrlFactory;
    
    public Party toExistingDomainObject(String publicId, JsonParty json, Locale locale) {
        Long partyId = hashids.decode(publicId)[0];
        return new Party(partyId, publicId, toContactDomainObject(json, null, null, locale), toCompanyDomainObject(json, null, null, null, 
                documentUrlFactory, locale), ApprovalStatus.findByName(json.getApprovalStatus()), PartyType.findByType(json.getPartyType()), 
                json.getVersion(), null);
    }
    
    protected PendingUser toPendingUserObject(JsonParty json) {
        return new PendingUser(json.getUsername(), json.getPassword(), json.getPasswordConfirmation());
    }
    
    protected Company toCompanyDomainObject(JsonParty json, String chineseCompanyName, String totalImportAmount, String videoUrl, DocumentUrlFactory documentUrlFactory, Locale locale) {
        return new Company(json.getEnglishCompanyName(), chineseCompanyName, json.getDescription(),
                toBusinessTypeDomainObjects(json.getBusinessTypes()), json.getNumberOfEmployees(), json.getWebsite(),
                json.getYearEstablished(), json.getTotalAnnualSales(), totalImportAmount, documentUrlFactory.removeDocumentUrl(json.getLogoUrl(),
                        locale), videoUrl);
    }
    
    protected Contact toContactDomainObject(JsonParty json, String title, String gender, Locale locale) {
        return new Contact(title, verifyGenderEnumValue(gender), json.getFirstName(), json.getLastName(), json.getCountry(), json.getProvince(),
                json.getEmail(), json.getSkypeRefId(), json.getMsnRefId(), json.getIcqRefId(), json.getBusinessTelephoneNumber(), json.getIpAddress(),
                verifyLanguageEnumValue(locale), json.getRegistrationDate());
    }
    
    private List<BusinessType> toBusinessTypeDomainObjects(List<String> businessTypes) {
        List<BusinessType> domainBusinessTypes = new ArrayList<BusinessType>();
        if (!CollectionUtils.isEmpty(businessTypes)) {
            for (String businessTypeName : businessTypes) {
                domainBusinessTypes.add(BusinessType.findByName(businessTypeName));
            }
        }
        return domainBusinessTypes;
    }
    
    private Language verifyLanguageEnumValue(Locale locale) {
        return locale == null ? null : Language.findByAbbreviation(locale.getLanguage());
    }
    
    private Gender verifyGenderEnumValue(String gender) {
        return StringUtils.isBlank(gender) ? null : Gender.findByReadableName(gender);
    }
}
