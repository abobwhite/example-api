package com.daugherty.e2c.business.validator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;

/**
 * Base class for validators of Party domain objects.
 */
public abstract class PartyValidator {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private UserDetailsService userDetailsService;
    @Inject
    private PendingUserReadDao pendingUserReadDao;
    @Inject
    private JdbcPartyDao partyDao;

    public void validate(Party party) {
        ValidationError errors = new ValidationError();

        if (ApprovalStatus.UNPROFILED.equals(party.getApprovalStatus())) {
            errors.add(validateUsernameIsUnique(party));
        }

        errors.add(validatEmailIsUnique(party));
        errors.add(party.validate());

        if (errors.hasErrors()) {
            ValidationException validationException = new ValidationException(errors);
            throw validationException;
        }
    }

    private ValidationError validateUsernameIsUnique(Party party) {
        ValidationError errors = new ValidationError();

        if (ApprovalStatus.UNPROFILED.equals(party.getApprovalStatus())) {
            if (isExistingUser(party) || isPendingUser(party)) {
                errors.add("username", Validatable.USER_USERNAME_NOT_UNIQUE);
            }
        }

        return errors;
    }

    private ValidationError validatEmailIsUnique(Party party) {
        ValidationError errors = new ValidationError();
        if (isExistingEmail(party)) {
            errors.add(Party.EMAIL_SERIAL_PROPERTY, Validatable.PARTY_EMAIL_NOT_UNIQUE);
        }

        return errors;
    }

    private boolean isExistingUser(Party party) {
        try {
            userDetailsService.loadUserByUsername(party.getPendingUser().getUsername());
            LOG.error("Username " + party.getPendingUser().getUsername() + " exists as a User");
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private boolean isPendingUser(Party party) {
        try {
            pendingUserReadDao.loadByUsername(party.getPendingUser().getUsername());
            LOG.error("Username " + party.getPendingUser().getUsername() + " exists as a Pending User");
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

    }

    private boolean isExistingEmail(Party party) {
        try {
            Party existingParty = partyDao.loadByEmailAddress(party.getContact().getEmailAddress());
            LOG.error("Email Address " + party.getContact().getEmailAddress() + " already exists for " + party.getId());

            return !existingParty.getId().equals(party.getId());
        } catch (EmptyResultDataAccessException e) {
            return false;
        } catch (IncorrectResultSizeDataAccessException e) {
            return true;
        }
    }
}
