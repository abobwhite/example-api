package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.service.json.JsonAnonymous;

@Component
public class AnonymousMapper extends PartyMapper{
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    
    public Anonymous toNewDomainObject(JsonAnonymous json, Locale locale){
        return new Anonymous(toContactDomainObject(json, null, null, locale), toCompanyDomainObject(json, null, null, null, documentUrlFactory, locale));
    }
}
