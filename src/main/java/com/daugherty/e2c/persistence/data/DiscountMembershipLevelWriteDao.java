package com.daugherty.e2c.persistence.data;

import java.util.List;

/**
 * Defines database write operations for the Discount Membership Tier.
 */
public interface DiscountMembershipLevelWriteDao {
    Integer insert(Long discountsId, Integer level);

    List<Integer> insert(Long discountsId, List<Integer> levels);

    void delete(Long discountsId, List<Integer> levels);
}
