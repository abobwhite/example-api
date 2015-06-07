package com.daugherty.e2c.security;

import org.springframework.stereotype.Component;

/**
 * Changes the user's password while they are authenticated.
 */
@Component("userChangePasswordUpdater")
public class UserChangePasswordUpdater extends UserUpdater {

    private PHPassHasher phpassHasher = new PHPassHasher();

    @Override
    protected boolean passwordsMatch(String persistedPassword, String oldPassword) {
        return phpassHasher.isMatch(oldPassword, persistedPassword);
    }

    @Override
    protected String getUsername(String username, String oldPassword) {
        return username;
    }

    void setPhpassHasherForTest(PHPassHasher hasher) {
        phpassHasher = hasher;
    }
}
