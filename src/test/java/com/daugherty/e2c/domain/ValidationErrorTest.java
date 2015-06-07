package com.daugherty.e2c.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidationErrorTest {
    private ValidationError validationError = new ValidationError();

    @Test
    public void hasErrorsIsTrue() {
        validationError.add("errorProperty", "errorMessage");

        assertTrue(validationError.hasErrors());
    }

    @Test
    public void hasErrorsIsFalse() {
        assertFalse(validationError.hasErrors());
    }

    @Test
    public void addWithStringProperties() {
        validationError.add("errorProperty", "errorMessage");

        assertTrue(validationError.hasErrors());
        assertTrue(validationError.getErrors().containsEntry("errorProperty", "errorMessage"));
    }

    @Test
    public void addWithValidationError() {
        ValidationError anotherValidationError = new ValidationError();
        anotherValidationError.add("errorProperty", "errorMessage");

        validationError.add(anotherValidationError);

        assertTrue(validationError.hasErrors());
        assertTrue(validationError.getErrors().containsEntry("errorProperty", "errorMessage"));
    }

}
