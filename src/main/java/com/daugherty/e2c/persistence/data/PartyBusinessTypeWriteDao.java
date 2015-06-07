package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.BusinessType;

public interface PartyBusinessTypeWriteDao {
    List<BusinessType> updateBusinessTypes(Long snapshotId, List<BusinessType> businessTypes);
}
