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
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.service.json.JsonBuyer;
import com.daugherty.e2c.service.json.JsonInteraction;
import com.daugherty.e2c.service.json.JsonProductMessage;
import com.daugherty.e2c.service.json.JsonSupplier;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProductMessageMapperTest {
    @Mock
    private PartyMapper partyMapper;
    @Mock
    private InteractionMapper interactionMapper;
    
    @InjectMocks
    private ProductMessageMapper productMessageMapper = new ProductMessageMapper();
    
    @Test
    public void toNewDomainObjectPopulatesSubsetOfFields() throws Exception {
        JsonProductMessage jsonMessage = new JsonProductMessage();
        jsonMessage.setSubject("subject");
        JsonBuyer jsonOriginator = new JsonBuyer();
        jsonOriginator.setId("jKNzKB04");
        jsonOriginator.setPartyType(PartyType.BUYER.toString());
        jsonOriginator.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        jsonMessage.setOtherParty(jsonOriginator);
        jsonMessage.setProductIds(Lists.newArrayList(242L));

        JsonInteraction jsonInteraction = new JsonInteraction();
        jsonInteraction.setBody("body");
        jsonInteraction.setSender(jsonOriginator);
        JsonSupplier jsonReceiver = new JsonSupplier();
        jsonReceiver.setId("pBVQwo0b");
        jsonReceiver.setPartyType(PartyType.SUPPLIER.toString());
        jsonReceiver.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        jsonInteraction.setReceiver(jsonReceiver);
        jsonMessage.setInteractions(Lists.newArrayList(jsonInteraction));
        
        Party originator = new Party(586L, "jKNzKB04", null, null, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);
        Party receiver = new Party(666L, "pBVQwo0b", null, null, ApprovalStatus.APPROVED, PartyType.SUPPLIER, null, null);
        Interaction interaction = new Interaction(null, jsonInteraction.getBody(), originator, receiver);
        
        when(partyMapper.toExistingDomainObject("jKNzKB04", jsonOriginator, Locale.ENGLISH)).thenReturn(originator);
        when(interactionMapper.toNewDomainObject(null, jsonInteraction, Locale.ENGLISH)).thenReturn(interaction);

        ProductMessage entityMessage = productMessageMapper.toNewDomainObject(jsonMessage, Locale.ENGLISH);

        assertThat(entityMessage.getId(), is(nullValue()));
        assertThat(entityMessage.getSubject(), is(jsonMessage.getSubject()));
        assertThat(entityMessage.getOtherParty().getPublicId(), is(jsonMessage.getOtherParty().getId()));
        assertThat(entityMessage.getOtherParty().getPartyType().toString(), is(jsonMessage.getOtherParty()
                .getPartyType()));
        assertThat(entityMessage.getProductIds().toString(), is(jsonMessage.getProductIds().toString()));
        assertThat(entityMessage.isFlagged(), is(false));
        assertThat(entityMessage.getLastInteraction().getMessage(), is(nullValue()));
        assertThat(entityMessage.getLastInteraction().getBody(), is(jsonInteraction.getBody()));
        assertThat(entityMessage.getLastInteraction().getSender().getPublicId(), is(jsonInteraction.getSender().getId()));
        assertThat(entityMessage.getLastInteraction().getReceiver().getPublicId(), is(jsonInteraction.getReceiver().getId()));
        assertThat(entityMessage.getLastInteraction().getSentTime(), is(nullValue()));
        assertThat(entityMessage.getLastInteraction().isRead(), is(false));
        assertThat(entityMessage.isCompletelyRead(), is(false));
    }
}
