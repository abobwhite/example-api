package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class JsonProductMessageTest {

    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @Test
    public void existingMessageConstructionWithFullyPopulatedDomainObjectPopulatesAllFields() throws Exception {
        Company buyerCompany = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, buyerCompany, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);

        ProductMessage entityMessage = new ProductMessage(42L, "jKNz4P4q", "subject", buyer, true, new Date(), true);
        entityMessage.addProductIds(Lists.newArrayList(242L, 808L));

        JsonProductMessage jsonMessage = new JsonProductMessage(entityMessage, documentUrlFactory, Locale.ENGLISH);

        assertThat(jsonMessage.getId(), is("jKNz4P4q"));
        assertThat(jsonMessage.getSubject(), is(entityMessage.getSubject()));
        assertThat(jsonMessage.getOtherParty().getId(), is(entityMessage.getOtherParty().getPublicId()));
        assertThat(jsonMessage.getOtherParty().getEnglishCompanyName(), is(entityMessage.getOtherParty().getCompany()
                .getEnglishName()));
        assertThat(jsonMessage.getOtherParty().getPartyType(), is(entityMessage.getOtherParty().getPartyType()
                .toString()));
        assertThat(jsonMessage.getProductIds(), is(nullValue()));
        assertThat(jsonMessage.getInteractions(), is(nullValue()));
        assertThat(jsonMessage.getFlagged(), is(entityMessage.isFlagged()));
        assertThat(jsonMessage.getMostRecentInteractionTime(), is(entityMessage.getLastInteraction().getSentTime()));
        assertThat(jsonMessage.getLinks().get("interactions").split("\\?_=")[0], is("messages/jKNz4P4q/interactions"));
        assertThat(jsonMessage.getLinks().get("products"), is("products?productIds=242,808"));
        assertThat(jsonMessage.getAllInteractionsRead(), is(entityMessage.isCompletelyRead()));
    }
}
