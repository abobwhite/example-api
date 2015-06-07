package com.daugherty.e2c.business;

import com.daugherty.e2c.domain.Entity;

/**
 * Defines operations for changing Entity information.
 */
public interface Mutator<E extends Entity> {

    E create(E entity);

    E update(E entity);

    void delete(Long entityId);

    void delete(String entityId);

}
