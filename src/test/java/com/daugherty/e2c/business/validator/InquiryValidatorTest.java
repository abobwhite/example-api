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
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;

@RunWith(MockitoJUnitRunner.class)
public class InquiryValidatorTest {

    @Mock
    private Inquiry inquiry;

    @InjectMocks
    private final InquiryValidator validator = new InquiryValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void throwsValidationExceptionWhenInquiryValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Inquiry.BODY_SERIAL_PROPERTY,
                Validatable.INTERACTION_BODY_REQUIRED));

        ValidationError errors = new ValidationError();
        errors.add(Inquiry.BODY_SERIAL_PROPERTY, Validatable.INTERACTION_BODY_REQUIRED);
        when(inquiry.validate()).thenReturn(errors);

        validator.validate(inquiry);
    }

}
