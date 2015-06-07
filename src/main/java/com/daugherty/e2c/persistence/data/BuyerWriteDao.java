package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Buyer;

public interface BuyerWriteDao {
    Buyer insert(Buyer buyer);

    Buyer update(Buyer buyer);

    Buyer recordEvent(Buyer buyer);

    Buyer switchToBuyer(Buyer buyer);
}
