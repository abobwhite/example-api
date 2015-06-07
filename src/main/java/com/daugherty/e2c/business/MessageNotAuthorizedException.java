package com.daugherty.e2c.business;

/**
 * Generic Runtime Exception when Not Authorized to View a Message
 */
@SuppressWarnings("serial")
public class MessageNotAuthorizedException extends RuntimeException {

    public MessageNotAuthorizedException() {
        super("You are not Authorized to View this Message");
    }
}
