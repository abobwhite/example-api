package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.MembershipDiscount;

public interface MembershipDiscountReadDao {

    List<MembershipDiscount> findBySnapshotId(Long snapshotId);

}
