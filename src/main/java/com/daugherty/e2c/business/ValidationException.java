package com.daugherty.e2c.business;

import java.util.Locale;

import net.minidev.json.JSONObject;

import org.springframework.context.MessageSource;

import com.daugherty.e2c.domain.ValidationError;

/**
 * 
 * Validation Exception
 */
@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {
    private ValidationError validationError = new ValidationError();

    public ValidationException(ValidationError validationError) {
        this.validationError = validationError;
    }

    public void resolveMessageKeys(Locale locale, MessageSource messageSource) {
        if (validationError.hasErrors()) {
            ValidationError convertedError = new ValidationError();

            for (String key : validationError.getErrors().keySet()) {
                for (String message : validationError.getErrors().get(key)) {
                    convertedError.add(key, getMessageFromKey(message, locale, messageSource));
                }
            }
            validationError = convertedError;
        }

    }

    public ValidationError getValidationError() {
        return validationError;
    }

    @Override
    public String getMessage() {
        JSONObject error = new JSONObject();
        JSONObject errorMesssage = new JSONObject();
        for (String key : validationError.getErrors().keySet()) {
            errorMesssage.put(key, validationError.getErrors().get(key));
        }

        error.put("errors", errorMesssage);

        return error.toJSONString();
    }

    @Override
    public String toString() {
        return getMessage();
    }

    private String getMessageFromKey(String key, Locale locale, MessageSource messageSource) {
        return messageSource.getMessage(key, new Object[] {}, locale);
    }
}
