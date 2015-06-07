package com.daugherty.e2c.business.validator;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.MembershipReadDao;

/**
 * Validates the Message domain object.
 */
@Component("messageValidator")
public class MessageValidator implements Validator<Message> {

    @Inject
    private MembershipReadDao membershipReadDao;

    @Override
    public void validate(Message message) {
        ValidationError errors = new ValidationError();

        errors.add(message.validate());
        validateOriginatorCanMessageSuppliers(message, errors);

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    private void validateOriginatorCanMessageSuppliers(Message message, ValidationError errors) {
        Party originator = message.getOtherParty();
        if (originator != null && PartyType.SUPPLIER.equals(originator.getPartyType())) {
            Membership membership = membershipReadDao.loadBySupplierId(originator.getId());
            if (!membership.getLevel().isSupplierMessagingEnabled()) {
                errors.add(Message.OTHER_PARTY_SERIAL_PROPERTY, Validatable.MESSAGE_ORIGINATOR_NEEDS_UPGRADE);
            }
        }
    }

}
