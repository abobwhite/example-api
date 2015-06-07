package com.daugherty.e2c.business.mapper;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.service.json.JsonInquiry;
import com.google.common.collect.Lists;

@Component
public class InquiryMapper {
    @Inject
    DocumentUrlFactory documentUrlFactory;
    @Inject
    private PartyMapper partyMapper;
    
    public Inquiry toNewDomainObject(JsonInquiry json, Locale locale) {
        return new Inquiry(null, partyMapper.toExistingDomainObject(json.getOriginator().getId(), json.getOriginator(), locale), json.getProductIds(), 
                json.getSubject(), json.getBody(), messageTagDomainObjects(json.getMessageTags()));
    }
    
    private List<MessageTag> messageTagDomainObjects(List<String> messageTags) {
        List<MessageTag> domainMessageTags = Lists.newArrayList();
        for (String messageTag : messageTags) {
            domainMessageTags.add(MessageTag.findByName(messageTag));
        }
        return domainMessageTags;
    }
}
