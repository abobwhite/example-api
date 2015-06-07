package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BusinessTypeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findByIdThatExistsReturnsEnumValue() {
        assertThat(BusinessType.findById(1L), is(BusinessType.AGENT));
    }

    @Test
    public void findByIdThatDoesNotExistThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("-1 is not a valid BusinessType Id");

        BusinessType.findById(-1L);
    }

    @Test
    public void findByNameThatExistsReturnsEnumValue() {
        assertThat(BusinessType.findByName("Agent"), is(BusinessType.AGENT));
    }

    @Test
    public void findByNameThatDoesNotExistThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("bobf is not a valid BusinessType Name");

        BusinessType.findByName("bobf");
    }

}
