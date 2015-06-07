package com.daugherty.e2c.business.validator;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ValidationError;

/**
 * Validates the Product domain object.
 */
@Component("productValidator")
public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {
        ValidationError errors = new ValidationError();
        errors.add(product.validate());
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

}
