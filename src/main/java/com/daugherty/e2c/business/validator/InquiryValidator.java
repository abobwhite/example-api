package com.daugherty.e2c.business.validator;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.ValidationError;

/**
 * Validates the Inquiry domain object.
 */
@Component("inquiryValidator")
public class InquiryValidator implements Validator<Inquiry> {

    @Override
    public void validate(Inquiry inquiry) {
        ValidationError errors = new ValidationError();
        errors.add(inquiry.validate());
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

}
