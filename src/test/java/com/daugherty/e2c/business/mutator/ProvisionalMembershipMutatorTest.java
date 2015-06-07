package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateMidnight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.sender.MembershipRenewalMailSender;
import com.daugherty.e2c.mail.sender.MembershipUpgradeMailSender;
import com.daugherty.e2c.mail.sender.NewMembershipMailSender;
import com.daugherty.e2c.persistence.data.MembershipDiscountWriteDao;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;
import com.daugherty.e2c.persistence.data.MembershipWriteDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class ProvisionalMembershipMutatorTest {
    @Mock
    private MembershipLevelReadDao membershipLevelReadDao;
    @Mock
    private MembershipWriteDao membershipWriteDao;
    @Mock
    private MembershipDiscountWriteDao membershipDiscountWriteDao;
    @Mock
    private Accessor<Membership> latestPaidSupplierMembershipAccessor;
    @Mock
    private Accessor<Supplier> latestSupplierAccessor;
    @Mock
    private NewMembershipMailSender newMembershipMailSender;
    @Mock
    private MembershipUpgradeMailSender upgradeMembershipMailSender;
    @Mock
    private MembershipRenewalMailSender renewalMembershipMailSender;
    @Mock
    private Supplier supplier;

    @InjectMocks
    private ProvisionalMembershipMutator provisionalMembershipMutator = new ProvisionalMembershipMutator();

    @Test
    public void createMembershipDelegatesToDao() {
        DateMidnight purchaseDate = new DateMidnight().minusMonths(3);
        DateMidnight effectiveDate = new DateMidnight().minusMonths(3);
        DateMidnight expirationDate = new DateMidnight().plusMonths(1);

        Discount discount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO, "ONGOING", true,
                false, new Date(), new Date(), Sets.newHashSet(2), Sets.newHashSet(SubscriptionType.NEW));

        MembershipDiscount newMembershipDiscount = new MembershipDiscount(discount, BigDecimal.ZERO);

        MembershipLevel membershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647,
                600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership newMembership = new Membership(null, 42L, membershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate.toDate(), effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO,
                Lists.newArrayList(newMembershipDiscount));

        when(membershipLevelReadDao.loadByValue(2)).thenReturn(membershipLevel);

        Membership persistedMembership = provisionalMembershipMutator.create(newMembership);

        verify(membershipWriteDao).insert(newMembership);
        verify(membershipDiscountWriteDao).updateMembershipDiscounts(1L, Lists.newArrayList(newMembershipDiscount));

        assertThat(persistedMembership.getSupplierId(), is(42L));
        assertThat(persistedMembership.getLevel().getValue(), is(2));
        assertThat(persistedMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(persistedMembership.getPurchasePrice().doubleValue(), is(0.00));
        assertThat(persistedMembership.getEffectiveDate(), is(effectiveDate.toDate()));
        assertThat(persistedMembership.getExpirationDate(), is(expirationDate.toDate()));
    }

    @Test
    public void updateMembershipDelegatesToDao() {
        DateMidnight purchaseDate = new DateMidnight().minusMonths(3);
        DateMidnight effectiveDate = new DateMidnight().minusMonths(3);
        DateMidnight expirationDate = new DateMidnight().plusMonths(1);

        Discount discount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO, "ONGOING", true,
                false, new Date(), new Date(), Sets.newHashSet(2), Sets.newHashSet(SubscriptionType.NEW));

        MembershipDiscount updateMembershipDiscount = new MembershipDiscount(discount, BigDecimal.ZERO);

        MembershipLevel membershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647,
                600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership updateMembership = new Membership(1L, 42L, membershipLevel, ApprovalStatus.PROVISIONAL, 1,
                purchaseDate.toDate(), effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO,
                Lists.newArrayList(updateMembershipDiscount));

        when(membershipLevelReadDao.loadByValue(2)).thenReturn(membershipLevel);

        Membership persistedMembership = provisionalMembershipMutator.update(updateMembership);

        verify(membershipWriteDao).update(updateMembership);
        verifyNoMoreInteractions(membershipWriteDao);
        verify(membershipDiscountWriteDao).updateMembershipDiscounts(1L, Lists.newArrayList(updateMembershipDiscount));
        verifyZeroInteractions(latestPaidSupplierMembershipAccessor);

        assertThat(persistedMembership.getSupplierId(), is(42L));
        assertThat(persistedMembership.getLevel().getValue(), is(2));
        assertThat(persistedMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(persistedMembership.getPurchasePrice().doubleValue(), is(0.00));
        assertThat(persistedMembership.getEffectiveDate(), is(effectiveDate.toDate()));
        assertThat(persistedMembership.getExpirationDate(), is(expirationDate.toDate()));
    }

    @Test
    public void updateMembershipDelegatesToDaoAndDoesNotUpdateExistingMembershipWhenNoneExists() {
        DateMidnight purchaseDate = new DateMidnight().minusMonths(3);
        DateMidnight effectiveDate = new DateMidnight().minusMonths(3);
        DateMidnight expirationDate = new DateMidnight().plusMonths(1);

        Discount discount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO, "ONGOING", true,
                false, new Date(), new Date(), Sets.newHashSet(2), Sets.newHashSet(SubscriptionType.NEW));

        MembershipDiscount updateMembershipDiscount = new MembershipDiscount(discount, BigDecimal.ZERO);

        MembershipLevel membershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647,
                600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership updateMembership = new Membership(1L, 42L, membershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate.toDate(), effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO,
                Lists.newArrayList(updateMembershipDiscount));

        when(membershipLevelReadDao.loadByValue(2)).thenReturn(membershipLevel);
        when(latestSupplierAccessor.load(42L, Locale.ENGLISH)).thenReturn(supplier);
        when(latestPaidSupplierMembershipAccessor.load(42L, Locale.ENGLISH)).thenThrow(
                new EmptyResultDataAccessException(1));

        Membership persistedMembership = provisionalMembershipMutator.update(updateMembership);

        verify(membershipWriteDao).update(updateMembership);
        verifyNoMoreInteractions(membershipWriteDao);
        verify(membershipDiscountWriteDao).updateMembershipDiscounts(1L, Lists.newArrayList(updateMembershipDiscount));
        verifyZeroInteractions(newMembershipMailSender);

        assertThat(persistedMembership.getSupplierId(), is(42L));
        assertThat(persistedMembership.getLevel().getValue(), is(2));
        assertThat(persistedMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(persistedMembership.getPurchasePrice().doubleValue(), is(0.00));
        assertThat(persistedMembership.getEffectiveDate(), is(effectiveDate.toDate()));
        assertThat(persistedMembership.getExpirationDate(), is(expirationDate.toDate()));
    }

    @Test
    public void updateMembershipDelegatesToDaoAndDoesUpdateExistingMembershipWhenExistsAndNotExpired() {
        DateMidnight purchaseDate = new DateMidnight();
        DateMidnight effectiveDate = new DateMidnight();
        DateMidnight expirationDate = new DateMidnight().plusMonths(1);

        Discount discount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO, "ONGOING", true,
                false, new Date(), new Date(), Sets.newHashSet(2), Sets.newHashSet(SubscriptionType.NEW));

        MembershipDiscount existingMembershipDiscount = new MembershipDiscount(discount, BigDecimal.ZERO);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, 42L, existingMembershipLevel, ApprovalStatus.PAID, 1,
                new DateMidnight().minusMonths(2).toDate(), new DateMidnight().minusMonths(2).toDate(),
                new DateMidnight().plusMonths(2).toDate(), BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE,
                null, BigDecimal.ZERO, BigDecimal.ZERO, Lists.newArrayList(existingMembershipDiscount));

        MembershipDiscount updateMembershipDiscount = new MembershipDiscount(discount, BigDecimal.ZERO);

        MembershipLevel membershipLevel = new MembershipLevel(101L, 3, new BigDecimal("1000.00"), 6, 5, 2147483647,
                600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership updateMembership = new Membership(1L, 42L, membershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate.toDate(), effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO,
                Lists.newArrayList(updateMembershipDiscount));

        when(membershipLevelReadDao.loadByValue(3)).thenReturn(membershipLevel);
        when(latestSupplierAccessor.load(42L, Locale.ENGLISH)).thenReturn(supplier);
        when(latestPaidSupplierMembershipAccessor.load(42L, Locale.ENGLISH)).thenReturn(existingMembership);

        Membership persistedMembership = provisionalMembershipMutator.update(updateMembership);

        verify(membershipWriteDao, times(2)).update(Mockito.any(Membership.class));
        verify(membershipDiscountWriteDao, times(2)).updateMembershipDiscounts(1L,
                Lists.newArrayList(existingMembershipDiscount));

        assertThat(persistedMembership.getSupplierId(), is(42L));
        assertThat(persistedMembership.getLevel().getValue(), is(3));
        assertThat(persistedMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(persistedMembership.getPurchasePrice().doubleValue(), is(0.00));
        assertThat(persistedMembership.getEffectiveDate(), is(effectiveDate.toDate()));
        assertThat(persistedMembership.getExpirationDate(), is(expirationDate.toDate()));

        assertThat(existingMembership.getExpirationDate(), is(new DateMidnight(effectiveDate).minusDays(1).toDate()));
    }

    @Test
    public void updateMembershipDelegatesToDaoAndSendsRenewalEmail() {
        DateMidnight purchaseDate = new DateMidnight();
        DateMidnight effectiveDate = new DateMidnight();
        DateMidnight expirationDate = new DateMidnight().plusMonths(1);

        Discount discount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO, "ONGOING", true,
                false, new Date(), new Date(), Sets.newHashSet(2), Sets.newHashSet(SubscriptionType.NEW));

        MembershipDiscount existingMembershipDiscount = new MembershipDiscount(discount, BigDecimal.ZERO);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, 42L, existingMembershipLevel, ApprovalStatus.PAID, 1,
                new DateMidnight().minusMonths(2).toDate(), new DateMidnight().minusMonths(2).toDate(),
                new DateMidnight().plusMonths(2).toDate(), BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE,
                null, BigDecimal.ZERO, BigDecimal.ZERO, Lists.newArrayList(existingMembershipDiscount));

        MembershipDiscount updateMembershipDiscount = new MembershipDiscount(discount, BigDecimal.ZERO);

        MembershipLevel membershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647,
                600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership updateMembership = new Membership(1L, 42L, membershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate.toDate(), effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO,
                Lists.newArrayList(updateMembershipDiscount));

        when(membershipLevelReadDao.loadByValue(2)).thenReturn(membershipLevel);
        when(latestSupplierAccessor.load(42L, Locale.ENGLISH)).thenReturn(supplier);
        when(latestPaidSupplierMembershipAccessor.load(42L, Locale.ENGLISH)).thenReturn(existingMembership);

        Membership persistedMembership = provisionalMembershipMutator.update(updateMembership);

        verify(membershipWriteDao, times(1)).update(Mockito.any(Membership.class));
        verify(membershipDiscountWriteDao, times(1)).updateMembershipDiscounts(1L,
                Lists.newArrayList(existingMembershipDiscount));
        verify(renewalMembershipMailSender, times(1)).sendEmail(supplier, membershipLevel);

        assertThat(persistedMembership.getSupplierId(), is(42L));
        assertThat(persistedMembership.getLevel().getValue(), is(2));
        assertThat(persistedMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(persistedMembership.getPurchasePrice().doubleValue(), is(0.00));
        assertThat(persistedMembership.getEffectiveDate(), is(effectiveDate.toDate()));
        assertThat(persistedMembership.getExpirationDate(), is(expirationDate.toDate()));
    }
}
