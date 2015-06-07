package com.daugherty.e2c.security;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.mail.sender.ChangePasswordMailSender;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.persistence.data.UserReadDao;

/**
 * Enables the User and changes old password to new password.
 */
@Transactional
@Component("pendingUserUpdater")
public class PendingUserUpdater {

    @Inject
    private PendingUserReadDao pendingUserReadDao;
    @Inject
    private PendingUserWriteDao pendingUserWriteDao;
    @Inject
    private UserReadDao userReadDao;
    @Inject
    private ChangePasswordMailSender changePasswordMailSender;

    public void updatePassword(String username, String oldPassword, String newPassword, String newPasswordConfirmation,
            Locale locale) {
        ValidationError errors = new ValidationError();

        PendingUser pendingUser = pendingUserReadDao.loadPendingUserByPassword(oldPassword);

        if (!newPassword.equals(newPasswordConfirmation)) {
            errors.add("password", Validatable.USER_PASSWORD_NOT_MATCH);
        }

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        pendingUserWriteDao.changePassword(pendingUser.getUsername(), newPassword);
        changePasswordMailSender.send(userReadDao.getEmailWithPersonalNameForPartyId(pendingUser.getPartyId()),
                pendingUser.getUsername(), locale);
    }
}
