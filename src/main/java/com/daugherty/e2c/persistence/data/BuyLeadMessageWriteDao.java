package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.BuyLeadMessage;

/**
 * Defines database write operations for Category Message objects.
 */
public interface BuyLeadMessageWriteDao extends MessageWriteDao {

	BuyLeadMessage insert(BuyLeadMessage message);
}
