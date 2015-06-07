package com.daugherty.e2c.business.accessor;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.DiscountFilter;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.persistence.data.DiscountReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

/**
 * Implementation for Coupon Accessor
 */
@Service("discountAccessor")
public class DiscountAccessor extends BaseAccessor<Discount> {

    @Inject
    private DiscountReadDao couponReadDao;

    @Override
    public List<Discount> find(Filter<Discount> filter) {
        List<Discount> discounts = Lists.newArrayList();

        if (filter.hasNoCriteria()) {
            QueryCriteria criteria = couponReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
            discounts = couponReadDao.findAllDiscounts(criteria);
        } else {
            QueryCriteria criteria = couponReadDao.createDiscountQueryCriteria(
                    filter.getStringCriterion(DiscountFilter.DISCOUNT_CODE),
                    filter.getStringCriterion(DiscountFilter.DISCOUNT_TYPE), null, null,
                    filter.getBooleanCriterion(DiscountFilter.INCLUDE_EXPIRED), filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());

            discounts = couponReadDao.findDiscounts(criteria);

            for (Iterator<Discount> iterator = discounts.iterator(); iterator.hasNext();) {
                Discount discount = (Discount) iterator.next();

                if (filter.getIntegerCriterion(DiscountFilter.MEMBERSHIP_LEVEL) != null) {
                    Integer membershipLevel = filter.getIntegerCriterion(DiscountFilter.MEMBERSHIP_LEVEL);

                    if (!discount.getMembershipLevels().contains(membershipLevel)) {
                        iterator.remove();
                    }

                }

                if (filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE) != null) {
                    String subscriptionType = filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE);

                    if (!discount.getDiscountTypes().contains(SubscriptionType.findByName(subscriptionType))) {
                        iterator.remove();
                    }
                }
            }
        }

        return discounts;
    }

    @Override
    public Discount load(Long id, Locale locale) {
        return couponReadDao.loadById(id);
    }

}
