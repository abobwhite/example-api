package com.daugherty.e2c.business.validator;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.ValidationError;

/**
 * Validates the Inquiry domain object.
 */
@Component("discountValidator")
public class DiscountValidator implements Validator<Discount> {

    @Override
    public void validate(Discount discount) {
        ValidationError errors = new ValidationError();
        errors.add(discount.validate());
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }
}
