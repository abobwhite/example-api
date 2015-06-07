package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.DiscountFilter;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.service.json.JsonDiscount;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceTest {

    @Mock
    private Accessor<Discount> discountAccessor;
    @Mock
    private Mutator<Discount> discountMutator;

    @InjectMocks
    private DiscountService discountService = new DiscountService();

    private Authentication originalAuthentication;

    @Before
    public void rememberAuthentication() {
        originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
    }

    @After
    public void restoreAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
    }

    @Test
    public void findDiscountDelegatesToAccessor() {

        Discount discount = new Discount(1L, "DOLLAR", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
                "100 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.NEW));

        when(discountAccessor.load(1L, Locale.ENGLISH)).thenReturn(discount);

        JsonDiscount existingDiscount = discountService.findDiscount(1L);

        assertThat(existingDiscount.getId(), is(1L));
    }

    @Test
    public void findDiscountsDelegatesToAccessor() {

        Discount discount = new Discount(1L, "DOLLAR", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
                "100 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.NEW));

        when(discountAccessor.find(Mockito.any(DiscountFilter.class))).thenReturn(Lists.newArrayList(discount));
        List<JsonDiscount> existingDiscounts = discountService.findDiscounts("DOLLAR", "Dollar", 1, "New", true, null,
                false, 1, 250, Locale.ENGLISH);

        assertThat(existingDiscounts.get(0).getId(), is(1L));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void createDiscountDelegatesToMutator() {

        JsonDiscount jsonDiscount = new JsonDiscount(null, "code", "description", "Dollar", BigDecimal.TEN, null, 
                Boolean.TRUE, Boolean.FALSE, new DateTime(2014, 2, 1, 0, 0).toDateMidnight().toDate(), new DateTime(
                        2014, 2, 1, 0, 0).toDateMidnight().toDate(), Lists.newArrayList(1),
                Lists.newArrayList(SubscriptionType.NEW));

        Discount persistedDiscount = new Discount(1L, jsonDiscount.getCode(),
                DiscountAmountType.findByDescription(jsonDiscount.getType()), jsonDiscount.getDiscountAmount(),
                jsonDiscount.getDescription(), jsonDiscount.getOngoing(), jsonDiscount.getSpecial(),
                jsonDiscount.getEffectiveDate(), jsonDiscount.getExpirationDate(), Sets.newHashSet(jsonDiscount
                        .getMembershipLevels()), Sets.newHashSet(jsonDiscount
                        .toSubscriptionTypeDomainObjects(jsonDiscount.getSubscriptionTypes())));
        when(discountMutator.create(Mockito.any(Discount.class))).thenReturn(persistedDiscount);

        JsonDiscount createdDiscount = discountService.createDiscount(jsonDiscount);

        assertThat(createdDiscount.getId(), is(1L));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void updateDiscountDelegatesToMutator() {

        JsonDiscount jsonDiscount = new JsonDiscount(1L, "code", "description", "Dollar", BigDecimal.TEN, null,
                Boolean.TRUE, Boolean.FALSE, new DateTime(2014, 2, 1, 0, 0).toDateMidnight().toDate(), new DateTime(
                        2014, 2, 1, 0, 0).toDateMidnight().toDate(), Lists.newArrayList(1),
                Lists.newArrayList(SubscriptionType.NEW));

        Discount persistedDiscount = new Discount(1L, jsonDiscount.getCode(),
                DiscountAmountType.findByDescription(jsonDiscount.getType()), jsonDiscount.getDiscountAmount(),
                jsonDiscount.getDescription(), jsonDiscount.getOngoing(), jsonDiscount.getSpecial(),
                jsonDiscount.getEffectiveDate(), jsonDiscount.getExpirationDate(), Sets.newHashSet(jsonDiscount
                        .getMembershipLevels()), Sets.newHashSet(jsonDiscount
                        .toSubscriptionTypeDomainObjects(jsonDiscount.getSubscriptionTypes())));
        when(discountMutator.update(Mockito.any(Discount.class))).thenReturn(persistedDiscount);

        JsonDiscount updatedDiscount = discountService.updateDiscount(1L, jsonDiscount);

        assertThat(updatedDiscount.getId(), is(1L));
    }
}
