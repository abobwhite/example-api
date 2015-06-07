package com.daugherty.e2c.security;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.persistence.data.UserReadDao;

/**
 * Resets a user's password after they complete the Forgot Password process.
 */
@Component("userResetPasswordUpdater")
public class UserResetPasswordUpdater extends UserUpdater {

    @Inject
    private UserReadDao userReadDao;

    @Override
    protected boolean passwordsMatch(String persistedPassword, String oldPassword) {
        return persistedPassword.equals(oldPassword);
    }

    @Override
    protected String getUsername(String username, String oldPassword) {
        return userReadDao.getUsernameByPassword(oldPassword);
    }
}
