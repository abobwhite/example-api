package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageType;
import com.daugherty.e2c.service.json.JsonMessage;

@Component
public class MessageMapper {
    @Inject
    private PartyMapper partyMapper;
    @Inject
    private InteractionMapper interactionMapper;
    
    public Message toNewDomainObject(JsonMessage json, Locale locale) {
        return new Message(json.getSubject(), partyMapper.toExistingDomainObject(json.getOtherParty().getId(), json.getOtherParty(), locale), 
                interactionMapper.toNewDomainObject(json.getId(), json.getInteractions().get(0), locale), MessageType.valueOf(json.getMessageType()));
    }
}
