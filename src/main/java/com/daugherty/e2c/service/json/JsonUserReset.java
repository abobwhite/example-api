package com.daugherty.e2c.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a User domain object to/from JSON.
 */
@JsonRootName(value = "userReset")
public class JsonUserReset {
    @JsonProperty("username")
    private String username;
    @JsonProperty("oldPassword")
    private String oldPassword;
    @JsonProperty("newPassword")
    private String newPassword;
    @JsonProperty("newPasswordConfirmation")
    private String newPasswordConfirmation;
    @JsonProperty("emailAddress")
    private String emailAddress;
    @JsonProperty("isPending")
    private Boolean isPending;
    @JsonProperty("link")
    private String link;

    public JsonUserReset() {
        super();
    }

    public JsonUserReset(String username, String oldPassword, String newPassword, String newPasswordConfirmation,
            String emailAdress) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirmation = newPasswordConfirmation;
        this.emailAddress = emailAdress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setIsPending(Boolean isPending) {
        this.isPending = isPending;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
