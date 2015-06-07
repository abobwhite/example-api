package com.daugherty.e2c.business.validator;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Buyer;

/**
 * Validates the Buyer Object
 */
@Component("buyerValidator")
public class BuyerValidator extends PartyValidator implements Validator<Buyer> {

    @Override
    public void validate(Buyer buyer) {
        super.validate(buyer);
        // TODO Add Buyer-specific validations here.
    }

}
