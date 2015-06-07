package com.daugherty.e2c.service.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.daugherty.e2c.business.LegacyIdNotFoundException;
import com.daugherty.e2c.business.MessageNotAuthorizedException;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.document.DocumentException;
import com.daugherty.e2c.persistence.document.DocumentNotFoundException;

/**
 * Handles runtime exceptions for all controllers.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private MessageSource messageSource;

    @ExceptionHandler(value = { EmptyResultDataAccessException.class, DocumentNotFoundException.class,
            LegacyIdNotFoundException.class })
    public ResponseEntity<String> handleEmptyResultsetDataAccessException(RuntimeException e, HttpServletRequest request) {
        LOG.error("Resource missing at " + request.getRequestURI(), e);
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        LOG.error("API error occurred for " + request.getRequestURI(), e);
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(ValidationException e, HttpServletRequest request,
            HttpServletResponse response, Locale locale) throws IOException {
        LOG.error("Validation error occurred for " + request.getRequestURI() + ": " + e.getMessage());
        e.resolveMessageKeys(locale, messageSource);
        response.setContentType("text/plain");
        response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());

        PrintWriter out = response.getWriter();
        out.write(e.getMessage());
        out.flush();
        out.close();
    }

    @ExceptionHandler(UploaderException.class)
    public void handleUploaderException(UploaderException e, HttpServletRequest request, HttpServletResponse response,
            Locale locale) throws IOException {
        LOG.error("Document Upload error occurred for " + request.getRequestURI(), e);

        ValidationError validationError = new ValidationError();
        validationError.add("image", Validatable.INVALID_IMAGE);

        handleValidationException(new ValidationException(validationError), request, response, locale);
    }

    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<String> handleDocumentException(DocumentException e, HttpServletRequest request, Locale locale) {
        LOG.error("Unable to read Document for " + request.getRequestURI(), e);
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MessageNotAuthorizedException.class)
    public ResponseEntity<String> handleDocumentException(MessageNotAuthorizedException e, HttpServletRequest request,
            Locale locale) {
        LOG.error("Unable to read Message for " + request.getRequestURI(), e);
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
