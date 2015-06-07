package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.MembershipFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.service.json.JsonMembershipDiscount;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class MembershipDiscountServiceTest {
    @Mock
    private Accessor<Membership> membershipAccessor;
    @Mock
    private MembershipFactory membershipFactory;
    @Mock
    private Mutator<Membership> provisionalMembershipMutator;
    @Mock
    private Mutator<MembershipDiscount> membershipDiscountMutator;
    
    @InjectMocks
    private MembershipDiscountService membershipDiscountService = new MembershipDiscountService();
    
    @Test
    public void validMembershipDiscountHasIdUponReturn() {
        Discount discount = new Discount(4L, "DOLLAR1", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L), "",
                Boolean.TRUE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.RENEW));
        JsonMembershipDiscount jsonMembershipDiscount = new JsonMembershipDiscount(discount, 1L);
        
        MembershipLevel level = new MembershipLevel(101L, 1, new BigDecimal("1000.00"), 6, 5, 2147483647, 600, true, 5,
                false, false, false, 0, false, false, false, false, false, false, false);
        
        Membership membership = new Membership(42L, level);
        List<MembershipDiscount> discounts = new ArrayList<MembershipDiscount>();
        discounts.add(new MembershipDiscount(5L, null, discount, BigDecimal.valueOf(100L)));
        membership.setMembershipDiscounts(discounts);
        membership.setId(1L);
        
        when(membershipAccessor.load(1L, Locale.ENGLISH)).thenReturn(membership);
        when(membershipFactory.build(membership, jsonMembershipDiscount.getDiscount().getCode())).thenReturn(membership);
        when(provisionalMembershipMutator.update(membership)).thenReturn(membership);
        
        JsonMembershipDiscount appliedDiscount = membershipDiscountService.createDiscount(1L, jsonMembershipDiscount);
        
        assertThat(appliedDiscount.getId(), is(5L));
    }
}
