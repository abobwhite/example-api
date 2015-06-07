package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.BuyLeadMessage;

/**
 * Defines database read operations for Message domain objects.
 */
public interface BuyLeadMessageReadDao extends MessageReadDao {

	BuyLeadMessage load(Long id);
}
