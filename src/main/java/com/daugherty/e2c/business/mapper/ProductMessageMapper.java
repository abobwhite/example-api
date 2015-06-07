package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.service.json.JsonProductMessage;

@Component
public class ProductMessageMapper {
    @Inject
    private PartyMapper partyMapper;
    @Inject
    private InteractionMapper interactionMapper;
    
    public ProductMessage toNewDomainObject(JsonProductMessage json, Locale locale) {
        return new ProductMessage(json.getSubject(), partyMapper.toExistingDomainObject(json.getOtherParty().getId(), json.getOtherParty(), locale), 
                json.getProductIds().get(0), interactionMapper.toNewDomainObject(json.getId(), json.getInteractions().get(0), locale));
    }
}
