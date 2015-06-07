package com.daugherty.e2c.business.validator;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Supplier;

/**
 * Validates Supplier objects.
 */
@Component("supplierValidator")
public class SupplierValidator extends PartyValidator implements Validator<Supplier> {

    @Override
    public void validate(Supplier supplier) {
        super.validate(supplier);
        // TODO Add Supplier-specific validations here.
    }

}
