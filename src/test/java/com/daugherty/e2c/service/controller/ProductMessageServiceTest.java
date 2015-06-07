package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jasypt.util.numeric.IntegerNumberEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.ProductMessageAccessor;
import com.daugherty.e2c.business.mapper.ProductMessageMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonInteraction;
import com.daugherty.e2c.service.json.JsonMessage;
import com.daugherty.e2c.service.json.JsonParty;
import com.daugherty.e2c.service.json.JsonProductMessage;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProductMessageServiceTest {

    private static final Company BUYER_COMPANY = new Company("East India Tea Company", null, null, null, null, null,
            null, null, null, null, null);
    private static final Company SUPPLIER_COMPANY = new Company("Megadodo Publications", null, null, null, null, null,
            null, null, null, null, null);
    private static final Party BUYER = new Party(586L, "jKNzKB04", null, BUYER_COMPANY, ApprovalStatus.APPROVED, PartyType.BUYER,
            null, null);
    private static final Party SUPPLIER = new Party(666L, "pBVQwo0b", null, SUPPLIER_COMPANY, ApprovalStatus.APPROVED,
            PartyType.SUPPLIER, null, null);

    private static final ProductMessage UNFLAGGED_MESSAGE = new ProductMessage(2L, "GpP8xPem", "unflagged", SUPPLIER, false,
            new Date(), false);

    @Mock
    private ProductMessageAccessor productMessageAccessor;
    @Mock
    private Mutator<ProductMessage> productMessageMutator;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private IntegerNumberEncryptor entityIdNumberEncryptor;
    @Mock
    private Principal principal;
    @Mock
    private ProductMessageMapper productMessageMapper;

    @InjectMocks
    private final ProductMessageService service = new ProductMessageService();

    @Captor
    private ArgumentCaptor<Filter<ProductMessage>> messageFilterCaptor;
    @Captor
    private ArgumentCaptor<Filter<Interaction>> interactionFilterCaptor;

    @Test
    public void createMessageDelegatesToMessageMutator() throws Exception {
        JsonProductMessage requestMessage = new JsonProductMessage();
        requestMessage.setSubject("subject");
        JsonParty originator = new JsonParty();
        originator.setId("jKNzKB04");
        originator.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        originator.setPartyType(PartyType.BUYER.toString());
        requestMessage.setOtherParty(originator);
        requestMessage.setProductIds(Lists.newArrayList(242L));

        JsonInteraction interaction = new JsonInteraction();
        interaction.setBody("body");
        JsonParty jsonSender = new JsonParty();
        jsonSender.setId("jKNzKB04");
        jsonSender.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        jsonSender.setPartyType(PartyType.BUYER.toString());
        interaction.setSender(jsonSender);
        JsonParty jsonReceiver = new JsonParty();
        jsonReceiver.setId("pBVQwo0b");
        jsonReceiver.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        jsonReceiver.setPartyType(PartyType.SUPPLIER.toString());
        interaction.setReceiver(jsonReceiver);
        requestMessage.setInteractions(Lists.newArrayList(interaction));

        ProductMessage persistedMessage = new ProductMessage(42L, "jKNz4P4q", "subject", BUYER, false, new Date(), false);
        persistedMessage.setId(42L);
        when(productMessageMapper.toNewDomainObject(requestMessage, Locale.ENGLISH)).thenReturn(persistedMessage);
        when(productMessageMutator.create(productMessageMapper.toNewDomainObject(requestMessage, Locale.ENGLISH))).thenReturn(persistedMessage);

        JsonMessage responseMessage = service.createMessage(requestMessage, Locale.ENGLISH);

        assertThat(responseMessage.getId(), is("jKNz4P4q"));
    }

    @Test
    public void retrieveMessageDelegatesToMessageAccessor() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(1L));
        when(productMessageAccessor.load(UNFLAGGED_MESSAGE.getPublicId(), Locale.ENGLISH)).thenReturn(UNFLAGGED_MESSAGE);

        JsonMessage jsonMessage = service.retrieveMessage(UNFLAGGED_MESSAGE.getPublicId(), principal, Locale.ENGLISH);

        assertThat(jsonMessage, is(notNullValue()));
        assertThat(jsonMessage.getId(), is(UNFLAGGED_MESSAGE.getPublicId()));
    }

    private RememberMeAuthenticationToken buildAuthenticationToken(Long partyId) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.SUPPLIER, Role.BUYER);
        E2CUser user = new E2CUser("username", "password", true, 0, authorities, new Party(partyId), true);

        return new RememberMeAuthenticationToken("e2c", user, authorities);
    }

}
