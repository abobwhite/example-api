package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Buyer;

public interface BuyerReadDao {

    Buyer loadLatest(Long id);

    Buyer loadApproved(Long id);
}
