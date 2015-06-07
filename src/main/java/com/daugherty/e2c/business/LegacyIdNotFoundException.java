package com.daugherty.e2c.business;

/**
 * Generic Runtime Exception when Not Authorized to View a Message
 */
@SuppressWarnings("serial")
public class LegacyIdNotFoundException extends RuntimeException {

    public LegacyIdNotFoundException(String message) {
        super(message);
    }
}
