package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.SubscriptionType;

/**
 * Defines database write operations for the Discount Subscription Type.
 */
public interface DiscountSubscriptionTypeWriteDao {
    SubscriptionType insert(Long discountsId, SubscriptionType subscriptionType);

    List<SubscriptionType> insert(Long discountsId, List<SubscriptionType> subscriptionTypes);

    void delete(Long discountsId, List<SubscriptionType> subscriptionTypes);
}
