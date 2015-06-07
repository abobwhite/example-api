package com.daugherty.e2c.mail;

/**
 * Represents a failure to send an email that is not an integral part of the business task (e.g. Message notifications).
 */
public class NonEssentialMailFailureException extends Exception {

    private static final long serialVersionUID = 1L;

    public NonEssentialMailFailureException(Long interactionId, String url, Email email, Throwable e) {
        super(buildMessage(interactionId, url, email), e);
    }

    public NonEssentialMailFailureException(Long interactionId, RuntimeException e) {
        super(buildMessage(interactionId), e);
    }

    private static String buildMessage(Long interactionId) {
        return "Could not send email for Interaction " + interactionId;
    }

    private static String buildMessage(Long interactionId, String url, Email email) {
        return "Could not send email to " + email.getTo() + " via service " + url + " for Interaction " + interactionId;
    }

}
