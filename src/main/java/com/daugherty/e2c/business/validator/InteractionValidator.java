package com.daugherty.e2c.business.validator;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.ValidationError;

/**
 * Validates the Interaction domain object.
 */
@Component("interactionValidator")
public class InteractionValidator implements Validator<Interaction> {

    @Override
    public void validate(Interaction interaction) {
        ValidationError errors = new ValidationError();
        errors.add(interaction.validate());
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

}
