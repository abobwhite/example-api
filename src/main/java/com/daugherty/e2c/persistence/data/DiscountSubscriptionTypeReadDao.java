package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.SubscriptionType;

/**
 * Defines database read operations for the Discount Subscription Type.
 */
public interface DiscountSubscriptionTypeReadDao {

    List<SubscriptionType> find(Long discountsId);
}
