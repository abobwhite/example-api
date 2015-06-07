package com.daugherty.e2c.business;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Entity;

/**
 * Defines operations for retrieving Entity information.
 */
public interface Accessor<E extends Entity> {

    List<E> find(Filter<E> filter);

    E load(Long id, Locale locale);
    
    E load(String publicId, Locale locale);

}
