package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import com.daugherty.e2c.domain.ValidationError;

@RunWith(MockitoJUnitRunner.class)
public class ValidationExceptionTest {
    @Mock
    private MessageSource messageSource;

    @Test
    public void messageContainsOnly1Property() {
        ValidationError error = new ValidationError();
        error.add("Error Property", "Error Message");

        when(messageSource.getMessage("Error Message", new Object[] {}, Locale.US)).thenReturn("Error 1");

        ValidationException exception = new ValidationException(error);
        exception.resolveMessageKeys(Locale.US, messageSource);

        assertThat(exception.getMessage(), is("{\"errors\":{\"Error Property\":[\"Error 1\"]}}"));
    }

    @Test
    public void messageContains2ErrorPreoperties() {
        ValidationError error = new ValidationError();
        error.add("Error Property 1", "Error Message 1");
        error.add("Error Property 2", "Error Message 2");

        when(messageSource.getMessage("Error Message 1", new Object[] {}, Locale.US)).thenReturn("Error 1");
        when(messageSource.getMessage("Error Message 2", new Object[] {}, Locale.US)).thenReturn("Error 2");

        ValidationException exception = new ValidationException(error);
        exception.resolveMessageKeys(Locale.US, messageSource);

        assertThat(exception.getMessage(),
                is("{\"errors\":{\"Error Property 1\":[\"Error 1\"],\"Error Property 2\":[\"Error 2\"]}}".toString()));
    }
}
