package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.BuyLeadMessageAccessor;
import com.daugherty.e2c.business.mapper.BuyLeadMessageMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonBuyLead;
import com.daugherty.e2c.service.json.JsonBuyLeadMessage;
import com.daugherty.e2c.service.json.JsonInteraction;
import com.daugherty.e2c.service.json.JsonMessage;
import com.daugherty.e2c.service.json.JsonParty;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyLeadMessageServiceTest {

    private static final Company BUYER_COMPANY = new Company("East India Tea Company", null, null, null, null, null,
            null, null, null, null, null);
    private static final Company SUPPLIER_COMPANY = new Company("Megadodo Publications", null, null, null, null, null,
            null, null, null, null, null);
    private static final Party BUYER = new Party(586L, "jKNzKB04", null, BUYER_COMPANY, ApprovalStatus.APPROVED, PartyType.BUYER,
            null, null);
    private static final Party SUPPLIER = new Party(666L, "pBVQwo0b", null, SUPPLIER_COMPANY, ApprovalStatus.APPROVED,
            PartyType.SUPPLIER, null, null);

    private static final BuyLeadMessage UNFLAGGED_MESSAGE = new BuyLeadMessage(2L, "GpP8xPem", "unflagged", SUPPLIER, false,
            new Date(), false, new BuyLead(BUYER, new ProductCategory(10L), new Date(), new Date()));

    @Mock
    private BuyLeadMessageAccessor buyLeadMessageAccessor;
    @Mock
    private Mutator<BuyLeadMessage> buyLeadMessageMutator;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private Principal principal;
    @Mock
    private BuyLeadMessageMapper buyLeadMessageMapper;

    @InjectMocks
    private final BuyLeadMessageService service = new BuyLeadMessageService();

    @Test
    public void createMessageDelegatesToMessageMutator() throws Exception {
        JsonParty receiver = new JsonParty();
        receiver.setId("p2VGOVMw");
        receiver.setPartyType(PartyType.BUYER.toString());
        receiver.setApprovalStatus(ApprovalStatus.APPROVED.getName());

        JsonBuyLead jsonBuyLead = new JsonBuyLead();
        jsonBuyLead.setId(2L);
        jsonBuyLead.setRequester(receiver);
        jsonBuyLead.setCategory(2807L);

        JsonBuyLeadMessage message = new JsonBuyLeadMessage();
        message.setSubject("Inquiry about Passenger Transportation Services");
        JsonParty originator = new JsonParty();
        originator.setId("Y40dgNWM");
        originator.setPartyType(PartyType.SUPPLIER.toString());
        originator.setApprovalStatus(ApprovalStatus.APPROVED.getName());
        message.setOtherParty(originator);
        message.setLead(jsonBuyLead);

        JsonInteraction interaction = new JsonInteraction();
        interaction.setBody("I need some transportation?");
        interaction.setSender(originator);

        interaction.setReceiver(receiver);
        message.setInteractions(Lists.newArrayList(interaction));
        
        Party originatorEntity = new Party(21L, "Y40dgNWM", null, null, ApprovalStatus.APPROVED, PartyType.SUPPLIER, null, null);
        Party receiverEntity = new Party(28L, "p2VGOVMw", null, null, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);
        BuyLead buyLead = new BuyLead(jsonBuyLead.getId(), originatorEntity, new ProductCategory(jsonBuyLead.getCategory()), null, null);
        Interaction interactionEntity = new Interaction(null, interaction.getBody(), originatorEntity, receiverEntity);
        
        BuyLeadMessage messageEntity = new BuyLeadMessage(message.getSubject(), originatorEntity, buyLead, interactionEntity);

        when(buyLeadMessageMapper.toNewDomainObject(message, Locale.ENGLISH)).thenReturn(messageEntity);
        when(buyLeadMessageMutator.create(buyLeadMessageMapper.toNewDomainObject(message, Locale.ENGLISH))).thenReturn(UNFLAGGED_MESSAGE);

        JsonMessage responseMessage = service.createMessage(message, Locale.ENGLISH);

        assertThat(responseMessage.getId(), is("GpP8xPem"));
    }

    @Test
    public void retrieveMessageDelegatesToMessageAccessor() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(1L));
        when(buyLeadMessageAccessor.load(UNFLAGGED_MESSAGE.getPublicId(), Locale.ENGLISH)).thenReturn(UNFLAGGED_MESSAGE);

        JsonBuyLeadMessage jsonMessage = service.retrieveMessage(UNFLAGGED_MESSAGE.getPublicId(), principal, Locale.ENGLISH);

        assertThat(jsonMessage, is(notNullValue()));
        assertThat(jsonMessage.getId(), is(UNFLAGGED_MESSAGE.getPublicId()));
    }

    private RememberMeAuthenticationToken buildAuthenticationToken(Long partyId) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.SUPPLIER, Role.BUYER);
        E2CUser user = new E2CUser("username", "password", true, 0, authorities, new Party(partyId), true);

        return new RememberMeAuthenticationToken("e2c", user, authorities);
    }

}
