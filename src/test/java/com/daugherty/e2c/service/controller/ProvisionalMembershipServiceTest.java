package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
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
import com.daugherty.e2c.business.MembershipFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.PayPalLinkBuilder;
import com.daugherty.e2c.business.accessor.ProvisionalMembershipAccessor;
import com.daugherty.e2c.business.mapper.ProvisionalMembershipMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.sender.ElectronicFundTransferMailSender;
import com.daugherty.e2c.service.json.JsonMembership;
import com.daugherty.e2c.service.json.JsonProvisionalMembership;

@RunWith(MockitoJUnitRunner.class)
public class ProvisionalMembershipServiceTest {

    @Mock
    private Accessor<Membership> membershipAccessor;
    @Mock
    private MembershipFactory membershipFactory;
    @Mock
    private Accessor<MembershipLevel> membershipLevelAccessor;
    @Mock
    private Mutator<Membership> provisionalMembershipMutator;
    @Mock
    private PayPalLinkBuilder payPalLinkBuilder;
    @Mock
    private ElectronicFundTransferMailSender electronicFundTransferMailSender;
    @Mock
    private Accessor<Supplier> latestSupplierAccessor;
    @Mock
    private ProvisionalMembershipMapper provisionalMembershipMapper;
    @Mock
    private ProvisionalMembershipAccessor provisionalMembershipAccessor;

    @InjectMocks
    private final ProvisionalMembershipService service = new ProvisionalMembershipService();

    @Captor
    private ArgumentCaptor<JsonMembership> jsonMembershipArgumentCaptor;

    @Test
    public void retrieveProvisionalSupplierMembershipDelegatesToMembershipAccessor() {
        long provisionalMembershipId = 1L;
        Membership domainObject = new Membership(42L, new MembershipLevel(1L, 1, null, 6, 5, 2147483647, 600, true, 0,
                false, false, false, 0, false, false, false, false, false, false, false));
        JsonProvisionalMembership jsonMembership = new JsonProvisionalMembership(domainObject, "jKNz4P4q");

        when(provisionalMembershipMapper.fromDomainObject(domainObject)).thenReturn(jsonMembership);
        when(membershipAccessor.load(provisionalMembershipId, Locale.ENGLISH)).thenReturn(domainObject);

        JsonMembership transferObject = service.retrieveMembership(provisionalMembershipId, Locale.ENGLISH);

        assertThat(transferObject.getId(), is(0L));
        assertThat(transferObject.getSupplierId(), is("jKNz4P4q"));
        assertThat(transferObject.getLevel(), is(domainObject.getLevel().getValue()));
        assertThat(transferObject.getEffectiveDate(), is(domainObject.getEffectiveDate()));
        assertThat(transferObject.getExpirationDate(), is(domainObject.getExpirationDate()));
        assertThat(transferObject.getTranslationsRemaining(), is(domainObject.getLevel().getTranslationCount()));
    }

    @Test
    public void createMembershipDelegatesToMembershipMutator() {
        long supplierId = 42L;
        String pubicSupplierId = "jKNz4P4q";
        Membership membership = new Membership(supplierId, new MembershipLevel(1L, 1, null, 6, 5, 2147483647, 600,
                true, 0, false, false, false, 0, false, false, false, false, false, false, false));
        JsonProvisionalMembership jsonMembership = new JsonProvisionalMembership(membership, pubicSupplierId);

        when(provisionalMembershipAccessor.load(pubicSupplierId, Locale.ENGLISH)).thenReturn(null);
        when(membershipFactory.build(Mockito.any(Membership.class))).thenReturn(membership);
        when(provisionalMembershipMapper.fromDomainObject(membership)).thenReturn(jsonMembership);
        when(provisionalMembershipMutator.create(Mockito.any(Membership.class))).thenReturn(membership);

        JsonMembership responseMembership = service.createMembership(jsonMembership);

        assertThat(responseMembership.getSupplierId(), is(pubicSupplierId));
        verify(provisionalMembershipMutator, never()).update(membership);
    }

    @Test
    public void createMembershipDelegatesToMembershipMutatorUpdatesIfProvisionalAlreadyExistsForSupplier() {
        long supplierId = 42L;
        String pubicSupplierId = "jKNz4P4q";

        MembershipLevel originalLevel = new MembershipLevel(1L, 1, new BigDecimal(988), 6, 5, 2147483647, 600, true, 0,
                false, false, false, 0, false, false, false, false, false, false, false);

        Membership originalMembership = new Membership(1L, supplierId, originalLevel, ApprovalStatus.PROVISIONAL, 1,
                new Date(), new Date(), new Date(), BigDecimal.TEN, BigDecimal.TEN, 1L, PaymentType.NONE, null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        Membership membership = new Membership(supplierId, new MembershipLevel(1L, 1, null, 6, 5, 2147483647, 600,
                true, 0, false, false, false, 0, false, false, false, false, false, false, false));
        JsonProvisionalMembership jsonMembership = new JsonProvisionalMembership(membership, "jKNz4P4q");

        when(provisionalMembershipAccessor.load(pubicSupplierId, Locale.ENGLISH)).thenReturn(originalMembership);
        when(provisionalMembershipMapper.toExistingDomainObject(supplierId, jsonMembership)).thenReturn(membership);
        when(membershipFactory.build(Mockito.any(Membership.class))).thenReturn(membership);
        when(provisionalMembershipMapper.fromDomainObject(membership)).thenReturn(jsonMembership);
        when(provisionalMembershipMutator.update(membership)).thenReturn(membership);

        service.createMembership(jsonMembership);

        assertThat(jsonMembership.getSupplierId(), is(pubicSupplierId));
        verify(provisionalMembershipMutator, never()).create(membership);
    }

    @Test
    public void updateMembership_provisionalWithPaymentTypeToPayPal() {
        Long membershipId = 1L;
        long supplierId = 42L;
        String payPalLink = "some_link";

        MembershipLevel level = new MembershipLevel(1L, 2, null, 6, 5, 2147483647, 600, true, 0, false, false, false,
                0, false, false, false, false, false, false, false);

        Membership domainObject = new Membership(membershipId, supplierId, level, ApprovalStatus.PROVISIONAL, 1,
                new Date(), new Date(), new Date(), BigDecimal.TEN, BigDecimal.TEN, 1L, PaymentType.PAYPAL, null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        JsonProvisionalMembership jsonProvisionalMembership = new JsonProvisionalMembership(domainObject, "jKNz4P4q");

        when(membershipAccessor.load(1L, Locale.ENGLISH)).thenReturn(domainObject);
        when(provisionalMembershipMapper.toExistingDomainObject(membershipId, jsonProvisionalMembership)).thenReturn(
                domainObject);
        when(provisionalMembershipMapper.fromDomainObject(domainObject)).thenReturn(jsonProvisionalMembership);
        when(provisionalMembershipMutator.update(Mockito.any(Membership.class))).thenReturn(domainObject);
        when(payPalLinkBuilder.build(jsonProvisionalMembership, Locale.ENGLISH)).thenReturn(payPalLink);

        JsonMembership transferObject = service.updateMembership(membershipId, jsonProvisionalMembership,
                Locale.ENGLISH);

        assertThat(transferObject.getId(), is(membershipId));
        assertThat(transferObject.getSupplierId(), is("jKNz4P4q"));
        assertThat(transferObject.getLevel(), is(domainObject.getLevel().getValue()));
        assertThat(transferObject.getEffectiveDate(), is(domainObject.getEffectiveDate()));
        assertThat(transferObject.getExpirationDate(), is(domainObject.getExpirationDate()));
        assertThat(transferObject.getTranslationsRemaining(), is(domainObject.getLevel().getTranslationCount()));
        assertThat(transferObject.getPaypalLink(), is(payPalLink));

    }

    @Test
    public void updateMembership_provisionalWithPaymentTypeToEFT() {
        Long membershipId = 1L;
        long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(1L, 2, BigDecimal.TEN, 6, 5, 2147483647, 600, true, 0, false,
                false, false, 0, false, false, false, false, false, false, false);

        Membership domainObject = new Membership(membershipId, supplierId, level, ApprovalStatus.PROVISIONAL, 1,
                new Date(), new Date(), new Date(), BigDecimal.TEN, BigDecimal.TEN, 1L, PaymentType.EFT, null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        JsonProvisionalMembership jsonProvisionalMembership = new JsonProvisionalMembership(domainObject, "jKNz4P4q");

        Supplier supplier = mock(Supplier.class);
        Contact contact = mock(Contact.class);
        when(contact.getFirstName()).thenReturn("John");
        when(contact.getLastName()).thenReturn("Doe");
        when(contact.getEmailAddress()).thenReturn("john.doe@xyz.com");

        when(supplier.getContact()).thenReturn(contact);
        when(provisionalMembershipMapper.toExistingDomainObject(membershipId, jsonProvisionalMembership)).thenReturn(
                domainObject);
        when(provisionalMembershipMapper.fromDomainObject(domainObject)).thenReturn(jsonProvisionalMembership);
        when(provisionalMembershipMutator.update(Mockito.any(Membership.class))).thenReturn(domainObject);
        when(latestSupplierAccessor.load(domainObject.getSupplierId(), Locale.ENGLISH)).thenReturn(supplier);

        JsonMembership transferObject = service.updateMembership(membershipId, jsonProvisionalMembership,
                Locale.ENGLISH);

        assertThat(transferObject.getId(), is(membershipId));
        assertThat(transferObject.getSupplierId(), is("jKNz4P4q"));
        assertThat(transferObject.getLevel(), is(domainObject.getLevel().getValue()));
        assertThat(transferObject.getEffectiveDate(), is(domainObject.getEffectiveDate()));
        assertThat(transferObject.getExpirationDate(), is(domainObject.getExpirationDate()));
        assertThat(transferObject.getTranslationsRemaining(), is(domainObject.getLevel().getTranslationCount()));
        verify(electronicFundTransferMailSender).send(String.valueOf(domainObject.getLevel().getValue()),
                String.valueOf(domainObject.getPurchasePrice().doubleValue()), "John, Doe", contact.getEmailAddress(),
                Language.ENGLISH);
    }

}
