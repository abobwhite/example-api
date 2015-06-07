package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PendingUserTest {

    @Test
    public void generateConfirmationTokenOnConstructionWithValidUserName() throws Exception {
        PendingUser pendingUser = new PendingUser(
                "123456789012345678901234567890123456789012345678901234567890123456789012345", null, null);
        assertThat(pendingUser.getConfirmationToken(), is("-2048175914")); // TODO Add timestamp?
    }

    @Test
    public void doNotGenerateConfirmationTokenOnConstructionWithNullUserName() throws Exception {
        PendingUser pendingUser = new PendingUser(null, null, null);
        assertThat(pendingUser.getConfirmationToken(), is(nullValue()));
    }

    @Test
    public void validateUsernameRequiredHasErrorWhenNull() {
        PendingUser pendingUser = new PendingUser(null, null, null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_REQUIRED));
    }

    @Test
    public void validateUsernameRequiredHasErrorWhenEmpty() {
        PendingUser pendingUser = new PendingUser("", null, null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_REQUIRED));
    }

    @Test
    public void validateUsernameRequiredHasNoErrorWhenNotEmpty() {
        PendingUser pendingUser = new PendingUser("apdent", null, null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_REQUIRED));
    }

    @Test
    public void validateUsernameLengthHasErrorWhenLessThan6() {
        PendingUser pendingUser = new PendingUser("user", null, null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_LENGTH));
    }

    @Test
    public void validateUsernameLengthHasErrorWhenGreaterThan75() {
        PendingUser pendingUser = new PendingUser(
                "123456789012345678901234567890123456789012345678901234567890123456789012345_Extra", null, null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_LENGTH));
    }

    @Test
    public void validateUsernameHasNoErrorWhen6() {
        PendingUser pendingUser = new PendingUser("userss", null, null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_LENGTH));
    }

    @Test
    public void validateUsernameHasNoErrorWhen75() {
        PendingUser pendingUser = new PendingUser(
                "123456789012345678901234567890123456789012345678901234567890123456789012345", null, null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_LENGTH));
    }

    @Test
    public void validateUsernameHasNoErrorWhenBetween6And75() {
        PendingUser pendingUser = new PendingUser("asdfghjkfdf", null, null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_LENGTH));
    }

    @Test
    public void validateUsernameHasNoErrorWhenIncludesSpecialCharacters() {
        PendingUser pendingUser = new PendingUser("asdfghjk%^$^&fdf", null, null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_HAS_SPACES));
    }

    @Test
    public void validateUsernameHasErrorWhenIncludesEmbeddedSpaces() {
        PendingUser pendingUser = new PendingUser("asdf ghjk%^ $^&fdf", null, null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.USERNAME_SERIAL_PROPERTY,
                Validatable.USER_USERNAME_HAS_SPACES));
    }

    @Test
    public void validateUsernameStripsOffLeadingAndTrailingSpacesAndHasNoErrors() {
        PendingUser pendingUser = new PendingUser(" apdent ", null, null);

        ValidationError errors = pendingUser.validate();

        assertThat(pendingUser.getUsername(), is("apdent"));
        assertFalse(errors.getErrors().containsKey(PendingUser.USERNAME_SERIAL_PROPERTY));
    }

    @Test
    public void validatePasswordRequiredHasErrorWhenNull() {
        PendingUser pendingUser = new PendingUser(null, null, null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_REQUIRED));
    }

    @Test
    public void validatePasswordRequiredHasErrorWhenEmpty() {
        PendingUser pendingUser = new PendingUser(null, "", null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_REQUIRED));
    }

    @Test
    public void validatePasswordRequiredHasNoErrorWhenNotEmpty() {
        PendingUser pendingUser = new PendingUser(null, "password", null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_REQUIRED));
    }

    @Test
    public void validatePasswordLengthHasErrorWhenLessThan6() {
        PendingUser pendingUser = new PendingUser(null, "user", null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_LENGTH));
    }

    @Test
    public void validatePasswordLengthHasErrorWhenGreaterThan25() {
        PendingUser pendingUser = new PendingUser(null, "1234567890123456789012345_Extra", null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_LENGTH));
    }

    @Test
    public void validatePasswordHasNoErrorWhen6() {
        PendingUser pendingUser = new PendingUser(null, "userss", null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_LENGTH));
    }

    @Test
    public void validatePasswordHasNoErrorWhen25() {
        PendingUser pendingUser = new PendingUser(null, "1234567890123456789012345", null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_LENGTH));
    }

    @Test
    public void validatePasswordHasNoErrorWhenBetween6And25() {
        PendingUser pendingUser = new PendingUser(null, "asdfghjkfdf", null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_LENGTH));
    }

    @Test
    public void validatePasswordHasNoErrorWhenIncludesSpecialCharacters() {
        PendingUser pendingUser = new PendingUser(null, "asdfghj^&%*#$^&kfdf", null);

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_HAS_SPACES));
    }

    @Test
    public void validatePasswordHasErrorWhenIncludesEmbeddedSpaces() {
        PendingUser pendingUser = new PendingUser(null, "asdf ghj^&%*# $^&kfdf", null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.PASSWORD_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_HAS_SPACES));
    }

    @Test
    public void validatePasswordStripsOffLeadingAndTrailingSpacesAndHasNoErrors() {
        PendingUser pendingUser = new PendingUser(null, " towels ", null);

        ValidationError errors = pendingUser.validate();

        assertThat(pendingUser.getPassword(), is("towels"));
        assertFalse(errors.getErrors().containsKey(PendingUser.PASSWORD_SERIAL_PROPERTY));
    }

    @Test
    public void validatePasswordConfirmationHasNoErrorWhenMatchingPassword() {
        PendingUser pendingUser = new PendingUser(null, "password", "password");

        ValidationError errors = pendingUser.validate();

        assertFalse(errors.getErrors().containsEntry(PendingUser.PASSWORD_CONFIRMATION_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_NOT_MATCH));
    }

    @Test
    public void validatePasswordConfirmationHasErrorWhenNotMatchingPassword() {
        PendingUser pendingUser = new PendingUser(null, "password", "something else");

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.PASSWORD_CONFIRMATION_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_NOT_MATCH));
    }

    @Test
    public void validatePasswordConfirmationHasErrorWhenBlank() {
        PendingUser pendingUser = new PendingUser(null, "password", "  ");

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.PASSWORD_CONFIRMATION_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_NOT_MATCH));
    }

    @Test
    public void validatePasswordConfirmationHasErrorWhenNull() {
        PendingUser pendingUser = new PendingUser(null, "password", null);

        ValidationError errors = pendingUser.validate();

        assertTrue(errors.getErrors().containsEntry(PendingUser.PASSWORD_CONFIRMATION_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_NOT_MATCH));
    }

    @Test
    public void validatePasswordConfirmationStripsOffLeadingAndTrailingSpacesAndHasNoErrors() {
        PendingUser pendingUser = new PendingUser(null, "password", " password ");

        ValidationError errors = pendingUser.validate();

        assertThat(pendingUser.getPasswordConfirmation(), is("password"));
        assertFalse(errors.getErrors().containsEntry(PendingUser.PASSWORD_CONFIRMATION_SERIAL_PROPERTY,
                Validatable.USER_PASSWORD_NOT_MATCH));
    }

}
