package com.daugherty.e2c.persistence.data;

public interface ProductMembershipWriteDao {
    public void create(Long productId, Long membershipId);
}
