package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MessageTagTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findByIdThatExistsReturnsEnumValue() {
        assertThat(MessageTag.findById(1L), is(MessageTag.SPECIFICATIONS));
    }

    @Test
    public void findByIdThatDoesNotExistThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("-1 is not a valid MessageTag Id");

        MessageTag.findById(-1L);
    }

    @Test
    public void findByNameThatExistsReturnsEnumValue() {
        assertThat(MessageTag.findByName("Specifications"), is(MessageTag.SPECIFICATIONS));
    }

    @Test
    public void findByNameThatDoesNotExistThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("bobf is not a valid MessageTag Name");

        MessageTag.findByName("bobf");
    }

}
