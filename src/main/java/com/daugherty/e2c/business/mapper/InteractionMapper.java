package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonInteraction;

@Component
public class InteractionMapper {
    @Inject
    private PartyMapper partyMapper;
    @Inject
    private Hashids hashids;
    
    public Interaction toNewDomainObject(String publicMessageId, JsonInteraction json, Locale locale) {
        Long messageId = publicMessageId != null ? hashids.decode(publicMessageId)[0] : null;
        Message message = new Message(messageId, publicMessageId);
        return new Interaction(message, json.getBody(), partyMapper.toExistingDomainObject(json.getSender().getId(), json.getSender(), locale),
                partyMapper.toExistingDomainObject(json.getReceiver().getId(), json.getReceiver(), locale));
    }
}
