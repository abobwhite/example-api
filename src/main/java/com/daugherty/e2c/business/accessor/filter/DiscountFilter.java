package com.daugherty.e2c.business.accessor.filter;

import java.util.Locale;

import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.SubscriptionType;

/**
 * Filter criteria for coupon retrieval operations.
 */
public class DiscountFilter extends BaseFilter<Discount> {

    public static final String DISCOUNT_CODE = "discountCode";
    public static final String DISCOUNT_TYPE = "discountType";
    public static final String SUBSCRIPTION_TYPE = "subscriptionType";
    public static final String MEMBERSHIP_LEVEL = "membershipLevel";
    public static final String INCLUDE_EXPIRED = "includeExpired";

    public DiscountFilter(String discountCode, String discountType, Integer membershipLevel,
            SubscriptionType subscriptionType, Boolean includeExpired, String sortBy, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale) {
        super(sortBy, sortDescending, startItem, count, locale);
        addStringCriterion(DISCOUNT_CODE, discountCode);
        addStringCriterion(DISCOUNT_TYPE,
                discountType == null ? null : DiscountAmountType.findByDescription(discountType).getCode());
        addStringCriterion(SUBSCRIPTION_TYPE, subscriptionType == null ? null : subscriptionType.getName());
        addIntegerCriterion(MEMBERSHIP_LEVEL, membershipLevel);
        addBooleanCriterion(INCLUDE_EXPIRED, includeExpired);
    }
}
