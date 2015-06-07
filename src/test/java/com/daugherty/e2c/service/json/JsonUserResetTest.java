package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonUserResetTest {

    @Test
    public void argConstructor() {
        JsonUserReset user = new JsonUserReset("username", "oldPassword", "newPassword", "newPasswordConfirmation", "emailAdress");

        assertThat(user.getUsername(), is("username"));
        assertThat(user.getOldPassword(), is("oldPassword"));
        assertThat(user.getNewPassword(), is("newPassword"));
        assertThat(user.getNewPasswordConfirmation(), is("newPasswordConfirmation"));
        assertThat(user.getEmailAddress(), is("emailAdress"));
    }

    @Test
    public void setters() {
        JsonUserReset user = new JsonUserReset();
        user.setUsername("username");
        user.setNewPassword("newPassword");
        user.setNewPasswordConfirmation("newPasswordConfirmation");
        user.setOldPassword("oldPassword");
        user.setEmailAddress("emailAdress");

        assertThat(user.getUsername(), is("username"));
        assertThat(user.getOldPassword(), is("oldPassword"));
        assertThat(user.getNewPassword(), is("newPassword"));
        assertThat(user.getNewPasswordConfirmation(), is("newPasswordConfirmation"));
        assertThat(user.getEmailAddress(), is("emailAdress"));
    }

}
