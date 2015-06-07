package com.daugherty.e2c.persistence.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Discount;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines database read operations for the Coupon domain object.
 */
public interface DiscountReadDao extends SortAndPaginationAware {
    QueryCriteria createDiscountQueryCriteria(String discountCode, String discountType, Integer membershipLevel,
            String subscriptionType, Boolean includeExpired, String propertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale);

    List<Discount> findDiscounts(QueryCriteria criteria);

    List<Discount> findAllDiscounts(QueryCriteria criteria);

    BigDecimal findRenewalDiscountByMonthsRemaining(Integer membershipTier, Integer month);

    Discount loadById(Long discountId);
}
