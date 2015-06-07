package com.daugherty.e2c.business.validator;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.AnonymousPartyExistsException;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.PartyReadDao;

/**
 * Validates the Anonymous Object
 */
@Component("anonymousValidator")
public class AnonymousValidator implements Validator<Anonymous> {
    @Inject
    private PartyReadDao partyDao;

    @Override
    public void validate(Anonymous anonymous) {
        Party party = getExistingParty(anonymous);

        ValidationError errors = new ValidationError();

        if (party != null && !isAnonymous(party)) {
            errors.add(Party.EMAIL_SERIAL_PROPERTY, Validatable.ANONYMOUS_EMAIL_ADDRESS_IS_ALEADY_USED);
        }

        errors.add(anonymous.validate());

        if (errors.hasErrors()) {
            ValidationException validationException = new ValidationException(errors);
            throw validationException;
        }

        if (party != null && isAnonymous(party)) {
            throw new AnonymousPartyExistsException(party);
        }
    }

    private Party getExistingParty(Anonymous anonymous) {
        try {
            return partyDao.loadByEmailAddress(anonymous.getContact().getEmailAddress());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private boolean isAnonymous(Party party) {
        return PartyType.ANONYMOUS.equals(party.getPartyType());
    }
}
