package com.daugherty.e2c.business.mutator;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.persistence.data.DiscountMembershipLevelReadDao;
import com.daugherty.e2c.persistence.data.DiscountMembershipLevelWriteDao;
import com.daugherty.e2c.persistence.data.DiscountSubscriptionTypeReadDao;
import com.daugherty.e2c.persistence.data.DiscountSubscriptionTypeWriteDao;
import com.daugherty.e2c.persistence.data.DiscountWriteDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class DiscountMutatorTest {

    @Mock
    private Validator<Discount> discountValidator;
    @Mock
    private DiscountWriteDao discountWriteDao;
    @Mock
    private DiscountMembershipLevelWriteDao discountMembershipLevelWriteDao;
    @Mock
    private DiscountMembershipLevelReadDao discountMembershipLevelReadDao;
    @Mock
    private DiscountSubscriptionTypeWriteDao discountSubscriptionTypeWriteDao;
    @Mock
    private DiscountSubscriptionTypeReadDao discountSubscriptionTypeReadDao;

    @InjectMocks
    private DiscountMutator discountMutator = new DiscountMutator();

    @Test
    public void createDiscountInsertsIntoDatabase() {
        Discount discount = new Discount(1L, "DOLLAR", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
                "100 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(2),
                Sets.newHashSet(SubscriptionType.NEW));

        when(discountWriteDao.insert(discount)).thenReturn(discount);

        discountMutator.create(discount);

        verify(discountValidator).validate(discount);
        verify(discountWriteDao).insert(discount);
        verify(discountMembershipLevelWriteDao).insert(1L, Lists.newArrayList(Sets.newHashSet(2)));
        verify(discountSubscriptionTypeWriteDao).insert(1L, Lists.newArrayList(Sets.newHashSet(SubscriptionType.NEW)));
    }

    @Test
    public void updateDiscountDeletesAndUpdatesMembershipLevelsDatabase() {
        Discount discount = new Discount(1L, "DOLLAR", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
                "100 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(1, 2, 4),
                Sets.newHashSet(SubscriptionType.NEW));

        when(discountWriteDao.update(discount)).thenReturn(discount);
        when(discountMembershipLevelReadDao.find(1L)).thenReturn(Lists.newArrayList(1, 2, 3));

        discountMutator.update(discount);

        verify(discountValidator).validate(discount);
        verify(discountWriteDao).update(discount);
        verify(discountMembershipLevelWriteDao).delete(1L, Lists.newArrayList(3));
        verify(discountMembershipLevelWriteDao).insert(1L, Lists.newArrayList(4));
    }

    @Test
    public void updateDiscountDeletesAndUpdatesSubscriptionTypesDatabase() {
        Discount discount = new Discount(1L, "DOLLAR", DiscountAmountType.DOLLAR, BigDecimal.valueOf(100L),
                "100 Dollars Off", Boolean.FALSE, Boolean.FALSE, new Date(), new Date(), Sets.newHashSet(1, 2, 4),
                Sets.newHashSet(SubscriptionType.NEW, SubscriptionType.RENEW));

        when(discountWriteDao.update(discount)).thenReturn(discount);
        when(discountSubscriptionTypeReadDao.find(1L)).thenReturn(
                Lists.newArrayList(SubscriptionType.NEW, SubscriptionType.UPGRADE));

        discountMutator.update(discount);

        verify(discountValidator).validate(discount);
        verify(discountWriteDao).update(discount);
        verify(discountSubscriptionTypeWriteDao).delete(1L, Lists.newArrayList(SubscriptionType.UPGRADE));
        verify(discountSubscriptionTypeWriteDao).insert(1L, Lists.newArrayList(SubscriptionType.RENEW));
    }
}
