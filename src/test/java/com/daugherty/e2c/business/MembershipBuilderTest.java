package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.persistence.data.DiscountReadDao;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;
import com.daugherty.persistence.QueryCriteria;

@RunWith(MockitoJUnitRunner.class)
public class MembershipBuilderTest {
    @Mock
    private MembershipLevelReadDao membershipLevelReadDao;
    @Mock
    private DiscountReadDao discountReadDao;
    @Mock
    private QueryCriteria discountQueryCriteria;

    @InjectMocks
    private MembershipBuilder membershipBuilder = new MembershipBuilder();
    
    @Test
    public void createMembershipForNewSubscription() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 1, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, false, false, false, false);

        when(membershipLevelReadDao.loadByValue(1L)).thenReturn(level);

        Membership builtMembership = membershipBuilder.build(new Membership(supplierId, level), level, null);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(1));
        assertThat(builtMembership.getEffectiveDate(), is(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)));
        assertThat(builtMembership.getExpirationDate(), is(DateUtils.truncate(DateUtils.addMonths(new Date(), level.getMonthsOfTerm()), Calendar.DAY_OF_MONTH)));
        assertThat(builtMembership.getPurchasePrice(), is(new BigDecimal("1000.00")));
    }

    @Test
    public void createMembershipForRenewSubscriptionWhileHasActiveMembership() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addMonths(new Date(), 3), Calendar.DAY_OF_MONTH);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipBuilder.build(new Membership(supplierId, level), level, existingMembership);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getEffectiveDate(), is(DateUtils.truncate(DateUtils.addDays(expirationDate, 1), Calendar.DAY_OF_MONTH)));
        assertThat(builtMembership.getExpirationDate(), is(DateUtils.truncate(DateUtils.addMonths(DateUtils.addDays(expirationDate, 1), 6), Calendar.DAY_OF_MONTH)));
    }

    @Test
    public void createMembershipForRenewSubscriptionWhileHasExpiredMembership() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);
        
        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);
        when(membershipLevelReadDao.loadByValue(2L)).thenReturn(level);

        Membership builtMembership = membershipBuilder.build(new Membership(supplierId, level), level, existingMembership);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(2));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getEffectiveDate(), is(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)));
        assertThat(builtMembership.getExpirationDate(), is(DateUtils.truncate(DateUtils.addMonths(new Date(), 6), Calendar.DAY_OF_MONTH)));
    } 

    @Test
    public void createMembershipForUpgradeSubscriptionWhileHasActiveMembership() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 3, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);
        
        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addMonths(new Date(), 3), Calendar.DAY_OF_MONTH);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);

        when(membershipLevelReadDao.loadByValue(3L)).thenReturn(level);

        Membership builtMembership = membershipBuilder.build(new Membership(supplierId, level), level, existingMembership);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(3));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getEffectiveDate(), is(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)));
        assertThat(builtMembership.getExpirationDate(), is(DateUtils.truncate(DateUtils.addMonths(new Date(), 6), Calendar.DAY_OF_MONTH)));    
   }

    @Test
    public void createMembershipForUpgradeSubscriptionWhileHasExpiredMembership() {
        Long supplierId = 42L;

        MembershipLevel level = new MembershipLevel(101L, 3, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, true, false, false, false);

        Date purchaseDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date effectiveDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -3), Calendar.DAY_OF_MONTH);
        Date expirationDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

        MembershipLevel existingMembershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5,
                2147483647, 600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership existingMembership = new Membership(1L, supplierId, existingMembershipLevel, ApprovalStatus.PAID, 1,
                purchaseDate, effectiveDate, expirationDate, BigDecimal.ZERO,
                BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO);

        when(membershipLevelReadDao.loadByValue(3L)).thenReturn(level);

        Membership builtMembership = membershipBuilder.build(new Membership(supplierId, level), level, existingMembership);

        assertThat(builtMembership.getSupplierId(), is(supplierId));
        assertThat(builtMembership.getLevel().getValue(), is(3));
        assertThat(builtMembership.getLevel().getPrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getPurchasePrice().doubleValue(), is(1000.00));
        assertThat(builtMembership.getEffectiveDate(), is(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH)));
        assertThat(builtMembership.getExpirationDate(), is(DateUtils.truncate(DateUtils.addMonths(new Date(), 6), Calendar.DAY_OF_MONTH)));
    }
}
