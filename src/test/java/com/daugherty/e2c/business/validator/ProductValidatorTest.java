package com.daugherty.e2c.business.validator;

import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;

@RunWith(MockitoJUnitRunner.class)
public class ProductValidatorTest {

    @Mock
    private Product product;

    @InjectMocks
    private final ProductValidator validator = new ProductValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void throwsValidationExceptionWhenProductValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Product.NAME_SERIAL_PROPERTY,
                Validatable.PRODUCT_NAME_REQUIRED));

        ValidationError errors = new ValidationError();
        errors.add(Product.NAME_SERIAL_PROPERTY, Validatable.PRODUCT_NAME_REQUIRED);
        when(product.validate()).thenReturn(errors);

        validator.validate(product);
    }

}
