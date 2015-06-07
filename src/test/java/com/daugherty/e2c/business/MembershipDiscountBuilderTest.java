package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.persistence.data.DiscountReadDao;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class MembershipDiscountBuilderTest {
    @Mock
    private MembershipLevelReadDao membershipLevelReadDao;
    @Mock
    private DiscountReadDao discountReadDao;
    @Mock
    private QueryCriteria discountQueryCriteria;
    @Mock
    private QueryCriteria couponDiscountQueryCriteria;

    @InjectMocks
    private MembershipDiscountBuilder membershipDiscountBuilder = new MembershipDiscountBuilder();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void applyDiscountForNewSubscriptionWithCoupon() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Discount discount = new Discount(1L, "Dollar1", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
                "100 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.NEW));

        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);
        when(
                discountReadDao.createDiscountQueryCriteria("Dollar1", null, 2, SubscriptionType.NEW.getName(), false,
                        null, false, 1, 250, Locale.ENGLISH)).thenReturn(discountQueryCriteria);
        when(discountReadDao.findDiscounts(discountQueryCriteria)).thenReturn(Lists.newArrayList(discount));

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level), null, level,
                "Dollar1");

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice(), is(new BigDecimal("1000.00")));
        assertThat(builtMembership.getMembershipDiscounts().size(), is(1));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getAmount().doubleValue(), is(100.00));
    }

    @Test(expected = ValidationException.class)
    public void applyDiscountForNewSubscriptionWithInvalidCouponThrowsException() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);
        when(
                discountReadDao.createDiscountQueryCriteria("THISISINVALID", null, 2, SubscriptionType.NEW.getName(),
                        false, null, false, 1, 250, Locale.ENGLISH)).thenReturn(discountQueryCriteria);
        when(discountReadDao.findDiscounts(discountQueryCriteria)).thenReturn(null);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level), null, level,
                "Dollar1");

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice(), is(new BigDecimal("1000.00")));
        assertThat(builtMembership.getMembershipDiscounts().size(), is(0));
    }

    @Test
    public void applyDiscountForNewSubscriptionWithCouponThatTakesPurchasePriceNegativeThrowsException() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("code", Validatable.DISCOUNT_INVALID));

        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Discount discount = new Discount(1L, "Dollar1001", DiscountAmountType.DOLLAR, BigDecimal.valueOf(1001L),
                "1001 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.NEW));

        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);
        when(
                discountReadDao.createDiscountQueryCriteria("Dollar1001", null, 2, SubscriptionType.NEW.getName(),
                        false, null, false, 1, 250, Locale.ENGLISH)).thenReturn(discountQueryCriteria);
        when(discountReadDao.findDiscounts(discountQueryCriteria)).thenReturn(Lists.newArrayList(discount));

        membershipDiscountBuilder.build(new Membership(supplierId, level), null, level, "Dollar1001");
    }

    @Test
    public void applyRenewalDiscountForRenewSubscription() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addMonths(new Date(), 3), Calendar.DAY_OF_MONTH);

        Membership existingMembership = new Membership(1L, supplierId, level, ApprovalStatus.PAID, 1, purchaseDate,
                effectiveDate, expirationDate, BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE, null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        when(discountReadDao.findRenewalDiscountByMonthsRemaining(2, 3)).thenReturn(BigDecimal.TEN);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, null);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(900.00));
        assertThat(builtMembership.getEarlyRenewalDiscount().doubleValue(), is(100.00));
    }

    @Test
    public void applyRenewalDiscountAndCouponDiscountForRenewSubscriptionAndCoupon() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addMonths(new Date(), 3), Calendar.DAY_OF_MONTH);

        Membership existingMembership = new Membership(1L, supplierId, level, ApprovalStatus.PAID, 1, purchaseDate,
                effectiveDate, expirationDate, BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE, null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        Discount dollarDiscount = new Discount(4L, "DOLLAR1", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L), "",
                Boolean.TRUE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.RENEW));

        when(
                discountReadDao.createDiscountQueryCriteria("DOLLAR1", null, 2, SubscriptionType.RENEW.getName(),
                        false, null, false, 1, 250, Locale.ENGLISH)).thenReturn(couponDiscountQueryCriteria);
        when(discountReadDao.findDiscounts(couponDiscountQueryCriteria)).thenReturn(Lists.newArrayList(dollarDiscount));

        when(discountReadDao.findRenewalDiscountByMonthsRemaining(2, 3)).thenReturn(BigDecimal.TEN);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, "DOLLAR1");

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(800.00));
        assertThat(builtMembership.getEarlyRenewalDiscount().doubleValue(), is(100.00));
    }

    @Test
    public void discountAppliedWithRenewalSubscriptionAtLowerLevel() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addMonths(new Date(), 3), Calendar.DAY_OF_MONTH);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 3, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE,
                null, BigDecimal.ZERO, BigDecimal.ZERO);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Discount dollarDiscount = new Discount(4L, "DOLLAR1", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L), "",
                Boolean.TRUE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.RENEW));
        when(
                discountReadDao.createDiscountQueryCriteria("DOLLAR1", null, 2, SubscriptionType.RENEW.getName(),
                        false, null, false, 1, 250, Locale.ENGLISH)).thenReturn(couponDiscountQueryCriteria);
        when(discountReadDao.findDiscounts(couponDiscountQueryCriteria)).thenReturn(Lists.newArrayList(dollarDiscount));

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, "DOLLAR1");

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(900.00));
    }

    @Test
    public void renewSubscriptionWithOngoingCouponUsesPreviousMembershipDiscountedPrice() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

        Discount existingDiscount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO,
                "ONGOING", true, false, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.RENEW));

        MembershipDiscount existingMembershipDiscount = new MembershipDiscount(existingDiscount, BigDecimal.ZERO);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, new BigDecimal("1000.00"), BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO,
                Lists.newArrayList(existingMembershipDiscount));
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, null);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getAmount().doubleValue(), is(0.00));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getDiscount().getCode(), is("ONGOING123"));
    }

    @Test
    public void renewSubscriptionWithOngoingCouponUsesPreviousMembershipDiscountedPriceWithPriceIncease() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1200.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

        Discount existingDiscount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO,
                "ONGOING", true, false, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.RENEW));

        MembershipDiscount existingMembershipDiscount = new MembershipDiscount(existingDiscount, BigDecimal.ZERO);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.valueOf(990), BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.TEN, BigDecimal.ZERO, Lists.newArrayList(existingMembershipDiscount));
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, null);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1200.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getAmount().doubleValue(), is(200.00));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getDiscount().getCode(), is("ONGOING123"));
    }

    @Test
    public void renewSubscriptionWithOngoingCouponDoesNotApplyWhenMembershipLevelChanges() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 3, new BigDecimal("1200.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

        Discount existingDiscount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO,
                "ONGOING", true, false, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.RENEW));

        MembershipDiscount existingMembershipDiscount = new MembershipDiscount(existingDiscount, BigDecimal.ZERO);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE,
                null, BigDecimal.ZERO, BigDecimal.ZERO, Lists.newArrayList(existingMembershipDiscount));
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, null);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(3));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1200.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(1200.00));
        assertTrue(builtMembership.getMembershipDiscounts().isEmpty());
    }

    @Test
    public void renewSubscriptionWithSpecialCouponUsesPreviousMembershipDiscountedPrice() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

        Discount specialDiscount = new Discount(4L, "SPECIAL123", DiscountAmountType.DOLLAR, BigDecimal.valueOf(0), "",
                Boolean.FALSE, Boolean.TRUE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.RENEW));

        when(
                discountReadDao.createDiscountQueryCriteria("SPECIAL123", null, 2, SubscriptionType.RENEW.getName(),
                        false, null, false, 1, 250, Locale.ENGLISH)).thenReturn(couponDiscountQueryCriteria);
        when(discountReadDao.findDiscounts(couponDiscountQueryCriteria))
                .thenReturn(Lists.newArrayList(specialDiscount));

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("800.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, new BigDecimal("800.00"), BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, "SPECIAL123");

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(800.00));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getAmount().doubleValue(), is(200.00));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getDiscount().getCode(), is("SPECIAL123"));
    }

    @Test
    public void renewSubscriptionWithSpecialCouponUsesPreviousMembershipDiscountedPriceAndAddsAddionalDiscount() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

        Discount specialDiscount = new Discount(4L, "SPECIAL123", DiscountAmountType.PERCENT, BigDecimal.valueOf(10),
                "", Boolean.FALSE, Boolean.TRUE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.RENEW));

        when(
                discountReadDao.createDiscountQueryCriteria("SPECIAL123", null, 2, SubscriptionType.RENEW.getName(),
                        false, null, false, 1, 250, Locale.ENGLISH)).thenReturn(couponDiscountQueryCriteria);
        when(discountReadDao.findDiscounts(couponDiscountQueryCriteria))
                .thenReturn(Lists.newArrayList(specialDiscount));

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("800.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, new BigDecimal("800.00"), BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, "SPECIAL123");

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(720.00));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getAmount().doubleValue(), is(280.00));
        assertThat(builtMembership.getMembershipDiscounts().get(0).getDiscount().getCode(), is("SPECIAL123"));
    }

    @Test
    public void upgadeSubscriptionWithExistingMembershipGivesUpgradeCredit() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 3, new BigDecimal("1200.00"), 12, 5, 2147483647, 600, true,
                5, false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addMonths(new Date(), 3), Calendar.DAY_OF_MONTH);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 12, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.valueOf(1000), new BigDecimal("1000.00"), 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);
        when(membershipLevelReadDao.loadByValue(3L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, null);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(3));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1200.00));
        assertThat(builtMembership.getPurchasePrice(), E2CMatchers.equalToWithinTolerance(BigDecimal.valueOf(700), 10L));
        assertThat(builtMembership.getUpgradeCredit(), E2CMatchers.equalToWithinTolerance(BigDecimal.valueOf(500), 10L));
    }

    @Test
    public void upgradeSubscriptionWithExistingMembershipGivesRenewalDiscount() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 3, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addMonths(new Date(), 3), Calendar.DAY_OF_MONTH);

        Membership existingMembership = new Membership(1L, supplierId, level, ApprovalStatus.PAID, 1, purchaseDate,
                effectiveDate, expirationDate, BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE, null,
                BigDecimal.ZERO, BigDecimal.ZERO);

        when(discountReadDao.findRenewalDiscountByMonthsRemaining(3, 3)).thenReturn(BigDecimal.TEN);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, null);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(3));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(900.00));
        assertThat(builtMembership.getEarlyRenewalDiscount().doubleValue(), is(100.00));
    }

    @Test
    public void upgradeSubscriptionWithExistingMembershipGivesRenewalDiscountAndCouponDiscount() {

        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 3, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addMonths(new Date(), 3), Calendar.DAY_OF_MONTH);

        Discount dollarDiscount = new Discount(4L, "DOLLAR1", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L), "",
                Boolean.TRUE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.UPGRADE));

        when(
                discountReadDao.createDiscountQueryCriteria("SPECIAL123", null, 2, SubscriptionType.RENEW.getName(),
                        false, null, false, 1, 250, Locale.ENGLISH)).thenReturn(couponDiscountQueryCriteria);
        when(discountReadDao.findDiscounts(couponDiscountQueryCriteria)).thenReturn(Lists.newArrayList(dollarDiscount));

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("800.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, new BigDecimal("800.00"), BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        when(
                discountReadDao.createDiscountQueryCriteria("DOLLAR1", null, 3, SubscriptionType.UPGRADE.getName(),
                        false, null, false, 1, 250, Locale.ENGLISH)).thenReturn(couponDiscountQueryCriteria);
        when(discountReadDao.findDiscounts(couponDiscountQueryCriteria)).thenReturn(Lists.newArrayList(dollarDiscount));
        when(discountReadDao.findRenewalDiscountByMonthsRemaining(3, 3)).thenReturn(BigDecimal.TEN);
        when(membershipLevelReadDao.loadByValue(3L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, "DOLLAR1");

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(3));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(800.00));
        assertThat(builtMembership.getEarlyRenewalDiscount().doubleValue(), is(100.00));
    }

    @Test
    public void upgradeSubscriptionWithOngoingCouponDoesNotApplyWhenMembershipLevelChanges() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 3, new BigDecimal("1200.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

        Discount existingDiscount = new Discount(1L, "ONGOING123", DiscountAmountType.DOLLAR, BigDecimal.ZERO,
                "ONGOING", true, false, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.UPGRADE));

        MembershipDiscount existingMembershipDiscount = new MembershipDiscount(existingDiscount, BigDecimal.ZERO);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE,
                null, BigDecimal.ZERO, BigDecimal.ZERO, Lists.newArrayList(existingMembershipDiscount));
        when(membershipLevelReadDao.loadByValue(3L)).thenReturn(level);

        Membership builtMembership = membershipDiscountBuilder.build(new Membership(supplierId, level),
                existingMembership, level, null);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(3));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1200.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(1200.00));
        assertTrue(builtMembership.getMembershipDiscounts().isEmpty());
    }
}
