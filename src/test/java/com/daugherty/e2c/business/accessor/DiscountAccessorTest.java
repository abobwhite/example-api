package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.DiscountFilter;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.persistence.data.DiscountReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class DiscountAccessorTest {
    private final Discount coupon = new Discount(1L, "DOLLAR", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
            "100 Dollar Off Coupon", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
            Sets.newHashSet(SubscriptionType.UPGRADE));

    private List<Discount> discounts;

    @Mock
    private DiscountReadDao discountReadDao;

    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final DiscountAccessor accessor = new DiscountAccessor();

    @Before
    public void setup() {
        discounts = Lists.newArrayList(coupon);
    }

    @Test
    public void findWithoutFilterCriteriaCallsDaoFindAllDiscountsMethod() throws Exception {
        DiscountFilter emptyFilter = new DiscountFilter(null, null, null, null, null, null, null, null, null, null);

        when(discountReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(discountReadDao.findAllDiscounts(queryCriteria)).thenReturn(discounts);

        assertThat(accessor.find(emptyFilter), is(discounts));
        verify(discountReadDao).findAllDiscounts(queryCriteria);

    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindDiscountsMethod() throws Exception {
        DiscountFilter filter = new DiscountFilter("DOLLAR", null, 2, SubscriptionType.UPGRADE, null, null, null, null,
                null, null);

        when(
                discountReadDao.createDiscountQueryCriteria("DOLLAR", null, null, null, null, null, false, 1, 250,
                        Locale.ENGLISH)).thenReturn(queryCriteria);
        when(discountReadDao.findDiscounts(queryCriteria)).thenReturn(discounts);

        assertThat(accessor.find(filter), is(discounts));
    }

    @Test
    public void loadDelegatesToAccessor() throws Exception {
        when(discountReadDao.loadById(1L)).thenReturn(coupon);

        assertThat(accessor.load(1L, Locale.ENGLISH), is(coupon));
    }
}
