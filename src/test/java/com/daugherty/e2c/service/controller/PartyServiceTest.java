package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.PartyFilter;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.service.json.JsonParties;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class PartyServiceTest {

    @Mock
    private Accessor<Party> partyAccessor;
    @Mock
    private Mutator<Party> partyMutator;
    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @InjectMocks
    private PartyService partyService = new PartyService();

    @Captor
    private ArgumentCaptor<Filter<Party>> filterCaptor;

    @Test
    public void retrieveUsersWithEmptyArgumentsDelegatesToAccessor() {
        Contact contact = new Contact("title", null, "first_name", "last_name", "country", "province", "email_address",
                "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123", Language.CHINESE,
                new Date());
        Company company = new Company("english_name", "chinese_name", "description",
                Lists.newArrayList(BusinessType.AGENT), "1-10", "website", 2013, "200-300", "100-150", "logo_ref_id",
                null);
        Party party = new Party(586L, "jKNzKB04", contact, company, ApprovalStatus.APPROVED, PartyType.BUYER, null,
                null);

        when(partyAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(party));

        JsonParties jsonParties = partyService.retrieveParties("username", PartyType.BUYER.toString(), "",
                Boolean.FALSE, 1, 250, Locale.ENGLISH);

        verify(partyAccessor).find(filterCaptor.capture());
        Filter<Party> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(PartyFilter.class)));
        assertThat(filter.getStringCriterion(PartyFilter.USERNAME_PREFIX), is("username"));
        assertThat(filter.getStringCriterion(PartyFilter.PARTY_TYPE), is(PartyType.BUYER.toString()));
        assertThat(filter.getSortBy(), is(""));
        assertThat(filter.isSortDescending(), is(false));
        assertThat(filter.getStartItem(), is(1));
        assertThat(filter.getCount(), is(250));

        assertThat(jsonParties, is(notNullValue()));
        assertThat(jsonParties.size(), is(1));
        assertThat(jsonParties.get(0).getId(), is("jKNzKB04"));
        assertThat(jsonParties.get(0).getPartyType(), is(PartyType.BUYER.toString()));
    }

    @Test
    public void deletePartyDelegatesToMutator() {
        partyService.deleteParty("id");

        verify(partyMutator).delete("id");
    }
}
