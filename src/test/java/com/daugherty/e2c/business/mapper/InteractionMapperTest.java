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
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonBuyer;
import com.daugherty.e2c.service.json.JsonInteraction;
import com.daugherty.e2c.service.json.JsonSupplier;

@RunWith(MockitoJUnitRunner.class)
public class InteractionMapperTest {
    @Mock
    private PartyMapper partyMapper;
    @Mock
    private Hashids hashids;
    
    @InjectMocks
    private InteractionMapper interactionMapper = new InteractionMapper();
    
    @Test
    public void toNewDomainObjectPopulatesSubsetOfFields() throws Exception {
        JsonInteraction jsonInteraction = new JsonInteraction();
        jsonInteraction.setBody("body");
        JsonBuyer jsonSender = new JsonBuyer();
        jsonSender.setId("jKNzKB04");
        jsonSender.setPartyType(PartyType.BUYER.toString());
        jsonSender.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        jsonInteraction.setSender(jsonSender);
        JsonSupplier jsonReceiver = new JsonSupplier();
        jsonReceiver.setId("pBVQwo0b");
        jsonReceiver.setPartyType(PartyType.SUPPLIER.toString());
        jsonReceiver.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        jsonInteraction.setReceiver(jsonReceiver);
        
        Party sender = new Party(586L, "jKNzKB04", null, null, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);
        Party receiver = new Party(666L, "pBVQwo0b", null, null, ApprovalStatus.APPROVED, PartyType.SUPPLIER, null, null);
        when(partyMapper.toExistingDomainObject(jsonSender.getId(), jsonSender, Locale.ENGLISH)).thenReturn(sender);
        when(partyMapper.toExistingDomainObject(jsonReceiver.getId(), jsonReceiver, Locale.ENGLISH)).thenReturn(receiver);
        when(hashids.decode("jKNz4P4q")).thenReturn(new long[]{42L});

        Interaction entityInteraction = interactionMapper.toNewDomainObject("jKNz4P4q", jsonInteraction, Locale.ENGLISH);

        assertThat(entityInteraction.getId(), is(nullValue()));
        assertThat(entityInteraction.getMessage(), is(new Message(42L)));
        assertThat(entityInteraction.getBody(), is(jsonInteraction.getBody()));
        assertThat(entityInteraction.getSender().getPublicId(), is(jsonInteraction.getSender().getId()));
        assertThat(entityInteraction.getSender().getPartyType().toString(), is(jsonInteraction.getSender()
                .getPartyType()));
        assertThat(entityInteraction.getReceiver().getPublicId(), is(jsonInteraction.getReceiver().getId()));
        assertThat(entityInteraction.getReceiver().getPartyType().toString(), is(jsonInteraction.getReceiver()
                .getPartyType()));
        assertThat(entityInteraction.getSentTime(), is(nullValue()));
        assertThat(entityInteraction.isRead(), is(false));
    }
}
