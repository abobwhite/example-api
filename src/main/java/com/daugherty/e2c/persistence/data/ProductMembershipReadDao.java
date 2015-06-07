package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Membership;

/**
 * Finds the associated membership for a product
 */
public interface ProductMembershipReadDao {
    public Membership findMembershipByProductId(Long productId);
}
