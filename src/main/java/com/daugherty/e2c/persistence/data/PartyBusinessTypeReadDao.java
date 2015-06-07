package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.BusinessType;
import com.google.common.collect.ArrayListMultimap;

public interface PartyBusinessTypeReadDao {
    List<BusinessType> findBySnapshotId(Long snapshotId);

    ArrayListMultimap<Long, BusinessType> findBySnapshotIds(List<Long> snapshotId);
}
