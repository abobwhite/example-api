package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ApprovalStatusTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findByIdThatExistsReturnsEnumValue() {
        assertThat(ApprovalStatus.findById(1L), is(ApprovalStatus.UNPROFILED));
    }

    @Test
    public void findByIdThatDoesNotExistThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("-1 is not a valid ApprovalStatus Id");

        ApprovalStatus.findById(-1L);
    }

    @Test
    public void findByNameThatExistsReturnsEnumValue() {
        assertThat(ApprovalStatus.findByName("Unprofiled"), is(ApprovalStatus.UNPROFILED));
    }

    @Test
    public void findByNameThatDoesNotExistThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("bobf is not a valid ApprovalStatus Name");

        ApprovalStatus.findByName("bobf");
    }

}
