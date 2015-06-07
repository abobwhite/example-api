package com.daugherty.e2c.persistence.document;

/**
 * Generic Runtime Exception for Documents
 */
@SuppressWarnings("serial")
public class DocumentException extends RuntimeException {

    public DocumentException(Throwable cause) {
        super("Error while reading Document", cause);
    }

}
