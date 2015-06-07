package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
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
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;

@RunWith(MockitoJUnitRunner.class)
public class JsonInteractionTest {

    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @Test
    public void existingInteractionConstructionWithFullyPopulatedDomainObjectPopulatesAllFields() throws Exception {
        Company buyerCompany = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, buyerCompany, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);

        Company supplierCompany = new Company("supplier", null, null, null, null, null, null, null, null, null, null);
        Party supplier = new Party(666L, "pBVQwo0b", null, supplierCompany, ApprovalStatus.APPROVED, PartyType.SUPPLIER, null, null);

        Interaction entityInteraction = new Interaction(421L, "body", buyer, supplier, new Date(), true);

        JsonInteraction jsonInteraction = new JsonInteraction(entityInteraction, documentUrlFactory, Locale.ENGLISH);

        assertThat(jsonInteraction.getId(), is(421L));
        assertThat(jsonInteraction.getBody(), is(entityInteraction.getBody()));
        assertThat(jsonInteraction.getSender().getId(), is(entityInteraction.getSender().getPublicId()));
        assertThat(jsonInteraction.getSender().getEnglishCompanyName(), is(entityInteraction.getSender().getCompany()
                .getEnglishName()));
        assertThat(jsonInteraction.getSender().getPartyType(), is(entityInteraction.getSender().getPartyType()
                .toString()));
        assertThat(jsonInteraction.getReceiver().getId(), is(entityInteraction.getReceiver().getPublicId()));
        assertThat(jsonInteraction.getReceiver().getEnglishCompanyName(), is(entityInteraction.getReceiver()
                .getCompany().getEnglishName()));
        assertThat(jsonInteraction.getReceiver().getPartyType(), is(entityInteraction.getReceiver().getPartyType()
                .toString()));
        assertThat(jsonInteraction.getSentTime(), is(entityInteraction.getSentTime()));
        assertThat(jsonInteraction.getRead(), is(entityInteraction.isRead()));
    }
}
