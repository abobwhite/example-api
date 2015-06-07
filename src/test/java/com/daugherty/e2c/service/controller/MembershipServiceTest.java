package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.PayPalLinkBuilder;
import com.daugherty.e2c.business.mapper.MembershipMapper;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.service.json.JsonMembership;

@RunWith(MockitoJUnitRunner.class)
public class MembershipServiceTest {
    @Mock
    private Accessor<Membership> membershipAccessor;
    @Mock
    private Mutator<Membership> membershipMutator;
    @Mock
    private PayPalLinkBuilder payPalLinkBuilder;
    @Mock
    private MembershipMapper membershipMapper;

    @InjectMocks
    private final MembershipService service = new MembershipService();

    @Captor
    private ArgumentCaptor<JsonMembership> jsonMembershipArgumentCaptor;

    @Test
    public void retrieveSupplierMembershipDelegatesToAccessor() {
        long membershipId = 43L;
        Membership domainObject = new Membership(42L, new MembershipLevel(1L, 1, null, 6, 5, 2147483647, 600, true, 0,
                false, false, false, 0, false, false, false, false, false, false, false));

        when(membershipAccessor.load(membershipId, Locale.ENGLISH)).thenReturn(domainObject);
        JsonMembership jsonMembership = new JsonMembership(domainObject, "jKNz4P4q");
        when(membershipMapper.fromDomainObject(domainObject)).thenReturn(jsonMembership);
        when(payPalLinkBuilder.build(jsonMembership, Locale.ENGLISH)).thenReturn("some_link");

        JsonMembership transferObject = service.retrieveMembership(membershipId, null, Locale.ENGLISH);

        assertThat(transferObject.getSupplierId(), is("jKNz4P4q"));
        assertThat(transferObject.getLevel(), is(domainObject.getLevel().getValue()));
        assertThat(transferObject.getEffectiveDate(), is(domainObject.getEffectiveDate()));
        assertThat(transferObject.getExpirationDate(), is(domainObject.getExpirationDate()));
        assertThat(transferObject.getTranslationsRemaining(), is(domainObject.getLevel().getTranslationCount()));
    }

    @Test
    public void updateSupplierMembershipDelegatesToMutator() {
        long membershipId = 43L;
        Membership domainObject = new Membership(42L, new MembershipLevel(1L, 1, null, 6, 5, 2147483647, 600, true, 0,
                false, false, false, 0, false, false, false, false, false, false, false));

        JsonMembership jsonMembership = new JsonMembership(domainObject, "jKNz4P4q");

        when(membershipMapper.toExistingDomainObject(42L, jsonMembership)).thenReturn(domainObject);
        when(membershipMapper.fromDomainObject(domainObject)).thenReturn(jsonMembership);
        when(payPalLinkBuilder.build(jsonMembership, Locale.ENGLISH)).thenReturn("some_link");
        when(membershipMutator.update(Mockito.any(Membership.class))).thenReturn(domainObject);

        JsonMembership transferObject = service.updateMembership(membershipId, jsonMembership, Locale.ENGLISH);

        assertThat(transferObject.getSupplierId(), is("jKNz4P4q"));
        assertThat(transferObject.getLevel(), is(domainObject.getLevel().getValue()));
        assertThat(transferObject.getEffectiveDate(), is(domainObject.getEffectiveDate()));
        assertThat(transferObject.getExpirationDate(), is(domainObject.getExpirationDate()));
        assertThat(transferObject.getTranslationsRemaining(), is(domainObject.getLevel().getTranslationCount()));
    }
}
