package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.Interaction;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines database read operations for Interaction domain objects.
 */
public interface InteractionReadDao {

    QueryCriteria createQueryCriteria(Long messageId, String propertyName, Boolean sortDescending, Integer startItem,
            Integer count);

    List<Interaction> find(QueryCriteria criteria);

    Interaction load(Long id);

}
