package com.daugherty.e2c.business;

import com.daugherty.e2c.domain.Entity;

/**
 * Defines operations for validation.
 */
public interface Validator<E extends Entity> {

    void validate(E entity);
}
