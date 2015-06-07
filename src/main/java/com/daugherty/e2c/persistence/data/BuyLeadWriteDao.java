package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.BuyLead;

/**
 * Defines database change operations for the BuyLead domain objects.
 */
public interface BuyLeadWriteDao {
    BuyLead insert(BuyLead buyer);
}
