package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GenderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findByReadableNameThatExistsReturnsEnumValue() {
        assertThat(Gender.findByReadableName("Male"), is(Gender.MALE));
    }

    @Test
    public void findByReadableNameThatDoesNotExistThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("bobf is not a valid Gender ReadableName");

        Gender.findByReadableName("bobf");
    }

}
