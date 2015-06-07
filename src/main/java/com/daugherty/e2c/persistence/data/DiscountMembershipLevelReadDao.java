package com.daugherty.e2c.persistence.data;

import java.util.List;

/**
 * Defines database read operations for the Discount Membership Tier.
 */
public interface DiscountMembershipLevelReadDao {

    List<Integer> find(Long discountsId);
}
