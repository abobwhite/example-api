package com.daugherty.e2c.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * Represents an individual (Buyer or Supplier) that has not yet completed the site registration process.
 */
public class PendingUser extends Entity implements Validatable {

    private static final long serialVersionUID = 1L;

    public static final String USERNAME_SERIAL_PROPERTY = "username";
    public static final String PASSWORD_SERIAL_PROPERTY = "password";
    public static final String PASSWORD_CONFIRMATION_SERIAL_PROPERTY = "passwordConfirmation";

    private String username;
    private String password;
    private String passwordConfirmation;
    private Long partyId;
    private String confirmationToken;

    /**
     * Constructor for new PendingUser instances.
     */
    public PendingUser(String username, String password, String passwordConfirmation) {
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        if (username != null) {
            confirmationToken = String.valueOf(username.hashCode());
        }
    }

    /**
     * Constructor for existing PendingUser instances.
     */
    public PendingUser(Long id, String username, String password, Long partyId, String confirmationToken) {
        super(id);
        this.username = username;
        this.password = password;
        this.partyId = partyId;
        this.confirmationToken = confirmationToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    @Override
    public ValidationError validate() {
        ValidationError error = new ValidationError();
        validateUsername(error);
        validatePassword(error);
        validatePasswordConfirmation(error);
        return error;
    }

    public void validateUsername(ValidationError errors) {
        if (StringUtils.isEmpty(username)) {
            errors.add(USERNAME_SERIAL_PROPERTY, USER_USERNAME_REQUIRED);
        } else {
            username = username.trim();
            if (username.length() < 6 || username.length() > 75) {
                errors.add(USERNAME_SERIAL_PROPERTY, USER_USERNAME_LENGTH);
            }
            if (username.contains(" ")) {
                errors.add(USERNAME_SERIAL_PROPERTY, USER_USERNAME_HAS_SPACES);
            }
        }
    }

    public void validatePassword(ValidationError errors) {
        if (StringUtils.isEmpty(password)) {
            errors.add(PASSWORD_SERIAL_PROPERTY, USER_PASSWORD_REQUIRED);
        } else {
            password = password.trim();
            if (password.length() < 6 || password.length() > 25) {
                errors.add(PASSWORD_SERIAL_PROPERTY, USER_PASSWORD_LENGTH);
            }
            if (password.contains(" ")) {
                errors.add(PASSWORD_SERIAL_PROPERTY, USER_PASSWORD_HAS_SPACES);
            }
        }
    }

    private void validatePasswordConfirmation(ValidationError errors) {
        if (StringUtils.isNotEmpty(passwordConfirmation)) {
            passwordConfirmation = passwordConfirmation.trim();
        }
        if (!Objects.equal(password, passwordConfirmation)) {
            errors.add(PASSWORD_CONFIRMATION_SERIAL_PROPERTY, USER_PASSWORD_NOT_MATCH);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        PendingUser rhs = (PendingUser) obj;
        builder.append(username, rhs.username).append(partyId, rhs.partyId)
                .append(confirmationToken, rhs.confirmationToken);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(username).append(partyId).append(confirmationToken);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("username", username).append("partyId", partyId).append("confirmationToken", confirmationToken);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 1005;
    }

}
