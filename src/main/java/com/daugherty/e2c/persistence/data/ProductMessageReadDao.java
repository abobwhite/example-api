package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.ProductMessage;
import com.google.common.collect.Multimap;

/**
 * Defines database read operations for Message domain objects.
 */
public interface ProductMessageReadDao extends MessageReadDao {

    ProductMessage load(Long id);
    
    Multimap<Long, Long> findProductIdListsByMessageIds(List<Long> messageIds);
}
