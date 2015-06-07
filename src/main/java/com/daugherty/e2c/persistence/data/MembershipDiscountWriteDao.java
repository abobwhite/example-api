package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.MembershipDiscount;

/**
 * Defines database write operations for the Membership Discount domain object.
 */
public interface MembershipDiscountWriteDao {
    List<MembershipDiscount> updateMembershipDiscounts(Long snapshotId, List<MembershipDiscount> membershipDiscounts);
}
