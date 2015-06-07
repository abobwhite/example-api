package com.daugherty.e2c.security;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.mail.sender.ForgotPasswordMailSender;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;

/**
 * Deletes (disables) the User and sends an Email to Reset Password.
 */
@Component
@Transactional
public class UserDeleter {

    static final String DISAPPROVED_BUYER = "DISAPPROVED_BUYER";
    static final String DISAPPROVED_SUPPLIER = "DISAPPROVED_SUPPLIER";

    @Inject
    private UserDetailsService userDetailsService;
    @Inject
    private UserReadDao userReadDao;
    @Inject
    private PendingUserReadDao pendingUserReadDao;
    @Inject
    private PendingUserWriteDao pendingUserWriteDao;
    @Inject
    private UserWriteDao userWriteDao;
    @Inject
    private ForgotPasswordMailSender forgotPasswordMailSender;
    @Inject
    private BuyerReadDao buyerReadDao;
    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private Hashids hashids;

    public void deleteUser(String emailAddress, Locale locale) {
        ValidationError errors = new ValidationError();

        PendingUser pendingUser = getPendingUser(emailAddress);

        if (pendingUser != null) {
            sendPendingUserForgotPasswordEmail(pendingUser, locale);
        } else {
            E2CUser user = validate(emailAddress, errors);
            if (errors.hasErrors()) {
                throw new ValidationException(errors);
            }

            sendUserForgotPasswordEmail(locale, user);
        }

    }

    private PendingUser getPendingUser(String emailAddress) {

        try {
            PendingUser pendingUser = pendingUserReadDao.loadPendingUserByEmailAddress(emailAddress);
            return pendingUser;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private void sendPendingUserForgotPasswordEmail(PendingUser user, Locale locale) {
        String confirmationToken = createConfirmationToken(user.getUsername());
        pendingUserWriteDao.disablePendingUser(user.getUsername(), confirmationToken);
        forgotPasswordMailSender.send(user.getUsername(),
                userReadDao.getEmailWithPersonalNameForPartyId(user.getPartyId()), confirmationToken, locale);
    }

    private void sendUserForgotPasswordEmail(Locale locale, E2CUser user) {
        String confirmationToken = createConfirmationToken(user.getUsername());
        userWriteDao.disableUser(user.getUsername(), confirmationToken);
        forgotPasswordMailSender.send(user.getUsername(),
                userReadDao.getEmailWithPersonalNameForPartyId(user.getParty().getId()), confirmationToken, locale);
    }

    private E2CUser validate(String emailAddress, ValidationError errors) {
        String username = null;
        try {
            username = userReadDao.getUsernameByEmailAddress(emailAddress);
        } catch (EmptyResultDataAccessException e) {
            errors.add("emailAddress", Validatable.USER_EMAIL_NOT_FOUND);
        } catch (IncorrectResultSizeDataAccessException e) {
            errors.add("emailAddress", Validatable.USER_EMAIL_NOT_FOUND);
        }

        E2CUser user = null;

        if (username != null) {
            try {
                user = (E2CUser) userDetailsService.loadUserByUsername(username);

                if (!user.isEnabled()) {
                    errors.add("username", Validatable.USER_USERNAME_NOT_ENABLED);
                }
            } catch (UsernameNotFoundException e) {
                errors.add("username", Validatable.USER_USERNAME_NOT_FOUND);
            } catch (EmptyResultDataAccessException e) {
                errors.add("username", Validatable.USER_USERNAME_NOT_FOUND);
            }
        }

        return user;
    }

    public void deleteDisapprovedBuyer(String publicBuyerId) {
        Long buyerId = hashids.decode(publicBuyerId)[0];
        Buyer buyer = buyerReadDao.loadLatest(buyerId);
        disableParty(buyer, DISAPPROVED_BUYER);
    }

    public void deleteDisapprovedSupplier(String publicSupplierId) {
        Long supplierId = hashids.decode(publicSupplierId)[0];
        Supplier supplier = supplierReadDao.loadLatest(supplierId, Locale.ENGLISH);
        disableParty(supplier, DISAPPROVED_SUPPLIER);
    }

    private void disableParty(Party party, String unusablePassword) {
        String username = userReadDao.getUsernameByEmailAddress(party.getContact().getEmailAddress());
        userWriteDao.disableUser(username, unusablePassword);
    }

    private String createConfirmationToken(String username) {
        return String.valueOf(username.hashCode() + System.currentTimeMillis());
    }

}
