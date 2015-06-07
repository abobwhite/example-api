package com.daugherty.e2c.persistence.document;

/**
 * Exception for Document not being found
 */
@SuppressWarnings("serial")
public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException() {
        super();
    }

    public DocumentNotFoundException(String message) {
        super(message);
    }
}
