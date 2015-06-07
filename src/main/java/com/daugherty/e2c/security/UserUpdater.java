package com.daugherty.e2c.security;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.mail.sender.ChangePasswordMailSender;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;

/**
 * Enables the User and changes old password to new password.
 */
@Transactional
public abstract class UserUpdater {

    @Inject
    private UserReadDao userReadDao;
    @Inject
    private UserWriteDao userWriteDao;
    @Inject
    private UserDetailsService userDetailsService;
    @Inject
    private ChangePasswordMailSender changePasswordMailSender;

    public void updatePassword(String username, String oldPassword, String newPassword, String newPasswordConfirmation,
            Locale locale) {
        ValidationError errors = new ValidationError();

        String foundUsername = getUsername(username, oldPassword);

        E2CUser user = getUser(foundUsername, oldPassword, newPassword, newPasswordConfirmation, errors);

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        userWriteDao.changePassword(user.getUsername(), user.getPassword(), newPassword);
        changePasswordMailSender.send(userReadDao.getEmailWithPersonalNameForPartyId(user.getParty().getId()),
                foundUsername, locale);
    }

    private E2CUser getUser(String username, String oldPassword, String newPassword, String newPasswordConfirmation,
            ValidationError errors) {
        E2CUser user = null;
        try {
            user = (E2CUser) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            errors.add("username", Validatable.USER_USERNAME_NOT_FOUND);
        }

        if (user != null && !passwordsMatch(user.getPassword(), oldPassword)) {
            errors.add("oldPassword", Validatable.USER_OLD_PASSWORD_NOT_MATCH);
        }

        if (!newPassword.equals(newPasswordConfirmation)) {
            errors.add("password", Validatable.USER_PASSWORD_NOT_MATCH);
        }

        return user;
    }

    protected abstract String getUsername(String username, String oldPassword);

    protected abstract boolean passwordsMatch(String persistedPassword, String oldPassword);

}
