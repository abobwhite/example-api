package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class JsonInquiryTest {

    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @Test
    public void unsubmittedInquiryConstructionWithNewDomainObjectPopulatesSubsetOfFields() throws Exception {
        Inquiry entityInquiry = new Inquiry(42L, Lists.newArrayList(242L, 808L));

        JsonInquiry jsonInquiry = new JsonInquiry(entityInquiry, documentUrlFactory, Locale.ENGLISH);

        assertThat(jsonInquiry.getId(), is(42L));
        assertThat(jsonInquiry.getOriginator(), is(nullValue()));
        assertThat(jsonInquiry.getSubject(), is(nullValue()));
        assertThat(jsonInquiry.getBody(), is(nullValue()));
        assertThat(jsonInquiry.getProductIds().size(), is(entityInquiry.getProductIds().size()));
        assertThat(jsonInquiry.getProductIds().get(0), is(entityInquiry.getProductIds().get(0)));
        assertThat(jsonInquiry.getProductIds().get(1), is(entityInquiry.getProductIds().get(1)));
        assertThat(jsonInquiry.getMessageTags().size(), is(0));
    }

    @Test
    public void existingInquiryConstructionWithFullyPopulatedDomainObjectPopulatesAllFields() throws Exception {
        Company buyerCompany = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(11L, "g3Vl8VbW", null, buyerCompany, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);

        Inquiry entityInquiry = new Inquiry(42L, buyer, Lists.newArrayList(242L, 808L), "subject", "body",
                Lists.newArrayList(MessageTag.DELIVERY_TIME, MessageTag.INSPECTION_CERTIFICATE));

        JsonInquiry jsonInquiry = new JsonInquiry(entityInquiry, documentUrlFactory, Locale.ENGLISH);

        assertThat(jsonInquiry.getId(), is(42L));
        assertThat(jsonInquiry.getOriginator().getId(), is(entityInquiry.getOriginator().getPublicId()));
        assertThat(jsonInquiry.getOriginator().getEnglishCompanyName(), is(entityInquiry.getOriginator().getCompany()
                .getEnglishName()));
        assertThat(jsonInquiry.getOriginator().getPartyType(), is(entityInquiry.getOriginator().getPartyType()
                .toString()));
        assertThat(jsonInquiry.getSubject(), is(entityInquiry.getSubject()));
        assertThat(jsonInquiry.getBody(), is(entityInquiry.getBody()));
        assertThat(jsonInquiry.getProductIds().size(), is(entityInquiry.getProductIds().size()));
        assertThat(jsonInquiry.getProductIds().get(0), is(entityInquiry.getProductIds().get(0)));
        assertThat(jsonInquiry.getProductIds().get(1), is(entityInquiry.getProductIds().get(1)));
        assertThat(jsonInquiry.getMessageTags().size(), is(entityInquiry.getMessageTags().size()));
        assertThat(jsonInquiry.getMessageTags().get(0), is(entityInquiry.getMessageTags().get(0).getName()));
        assertThat(jsonInquiry.getMessageTags().get(1), is(entityInquiry.getMessageTags().get(1).getName()));
        assertThat(jsonInquiry.getSubmissionTime(), is(entityInquiry.getSubmissionTime()));
    }
}
