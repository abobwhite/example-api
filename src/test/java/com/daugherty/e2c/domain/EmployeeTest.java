package com.daugherty.e2c.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;

public class EmployeeTest {

    @Test
    public void isValidWhenAllRequiredFieldsArePresent() {
        Contact contact = new Contact(null, null, "First", "Name", null, null, "emailAddres@email.com", null, null,
                null, "+12347439", null, null, null);

        Employee employeeParty = new Employee("username", "password", Lists.newArrayList("role"), contact);

        ValidationError validationError = employeeParty.validate();

        assertTrue(validationError.getErrors().isEmpty());
    }

}
