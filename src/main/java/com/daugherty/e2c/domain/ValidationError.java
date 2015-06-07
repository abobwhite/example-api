package com.daugherty.e2c.domain;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Represents a Validation Error Object
 */
public class ValidationError {
    private Multimap<String, String> errors = ArrayListMultimap.create();

    public ValidationError() {
    }

    public Multimap<String, String> getErrors() {
        return errors;
    }

    /**
     * @return - Validation Error contains errors
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * @param property
     *            - represents the domain object property in error
     * @param message
     *            - represents the error message key in resource bundle
     */
    public void add(String property, String message) {
        errors.put(property, message);
    }

    /**
     * @param validationError
     *            - represents a Validation Error you want combined into this Validation Error
     */
    public void add(ValidationError validationError) {
        if (validationError.hasErrors()) {
            for (String key : validationError.getErrors().keySet()) {
                for (String message : validationError.getErrors().get(key)) {
                    add(key, message);
                }
            }
        }
    }
}
