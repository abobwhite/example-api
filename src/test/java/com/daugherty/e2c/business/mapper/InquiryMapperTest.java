package com.daugherty.e2c.business.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.service.json.JsonBuyer;
import com.daugherty.e2c.service.json.JsonInquiry;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class InquiryMapperTest {
    @Mock
    private PartyMapper partyMapper;
    
    @InjectMocks
    private InquiryMapper inquiryMapper = new InquiryMapper();
    
    @Test
    public void toNewDomainObjectPopulatesAllFields() {
        JsonInquiry jsonInquiry = new JsonInquiry();
        JsonBuyer originator = new JsonBuyer();
        originator.setId("jKNz4P4q");
        originator.setPartyType(PartyType.BUYER.toString());
        originator.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        jsonInquiry.setOriginator(originator);
        jsonInquiry.setProductIds(Lists.newArrayList(1L, 2L, 3L));
        jsonInquiry.setSubject("subject");
        jsonInquiry.setBody("body");
        jsonInquiry.setMessageTags(Lists.newArrayList(MessageTag.SPECIFICATIONS.getName(),MessageTag.DELIVERY_TIME.getName()));
        
        Party originatorEntity = new Party(42L, "jKNz4P4q", null, null, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);
        
        when(partyMapper.toExistingDomainObject(originator.getId(), originator, Locale.ENGLISH)).thenReturn(originatorEntity);

        Inquiry entityInquiry = inquiryMapper.toNewDomainObject(jsonInquiry, Locale.ENGLISH);

        assertThat(entityInquiry.getId(), is(nullValue()));
        assertThat(entityInquiry.getOriginator().getPublicId(), is(originator.getId()));
        assertThat(entityInquiry.getOriginator().getPartyType().toString(), is(originator.getPartyType()));
        assertThat(entityInquiry.getProductIds(), is(jsonInquiry.getProductIds()));
        assertThat(entityInquiry.getSubject(), is(jsonInquiry.getSubject()));
        assertThat(entityInquiry.getBody(), is(jsonInquiry.getBody()));
        assertThat(entityInquiry.getMessageTags().size(), is(jsonInquiry.getMessageTags().size()));
        assertThat(entityInquiry.getMessageTags().get(0).getName(), is(jsonInquiry.getMessageTags().get(0)));
        assertThat(entityInquiry.getMessageTags().get(1).getName(), is(jsonInquiry.getMessageTags().get(1)));
    }
}
