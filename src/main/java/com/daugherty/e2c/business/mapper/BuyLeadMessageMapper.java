package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.service.json.JsonBuyLeadMessage;

@Component
public class BuyLeadMessageMapper {
    @Inject
    private PartyMapper partyMapper;
    @Inject
    private BuyLeadMapper buyLeadMapper;
    @Inject
    private InteractionMapper interationMapper;
    
    public BuyLeadMessage toNewDomainObject(JsonBuyLeadMessage json, Locale locale) {
        return new BuyLeadMessage(json.getSubject(), partyMapper.toExistingDomainObject(json.getOtherParty().getId(), json.getOtherParty(), locale),
                buyLeadMapper.toNewDomainObject(json.getLead()), interationMapper.toNewDomainObject(json.getId(), json.getInteractions().get(0), locale));
    }
}
