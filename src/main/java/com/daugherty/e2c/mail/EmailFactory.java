package com.daugherty.e2c.mail;

/**
 * Creates an Email.
 */
public class EmailFactory {

    private String fromEmailAddress;
    private String subject;
    private String bodyTemplate;

    EmailFactory() {
        // Needed by CGLIB (for profiling and tracing)
    }

    /**
     * @param fromEmailAddress
     *            - address email will be from
     * @param subject
     *            - subject line of the email
     * @param bodyTemplate
     *            - template for static text with %s as place holder for dynamic text
     */
    public EmailFactory(String fromEmailAddress, String subject, String bodyTemplate) {
        this.fromEmailAddress = fromEmailAddress;
        this.subject = subject;
        this.bodyTemplate = bodyTemplate;
    }

    /**
     * Creates an Email from a known sender.
     * 
     * @param toEmailAddress
     *            - address email will be sent
     * @param bodyTemplateArgs
     *            - array of args that will be put into template
     */
    public Email createEmail(String toEmailAddress, Object[] bodyTemplateArgs) {
        return createEmail(fromEmailAddress, toEmailAddress, bodyTemplateArgs);
    }

    /**
     * Creates an Email from a specified sender.
     * 
     * @param fromEmailAddress
     *            - address email will be from
     * @param toEmailAddress
     *            - address email will be sent
     * @param bodyTemplateArgs
     *            - array of args that will be put into template
     */
    public Email createEmail(String fromEmailAddress, String toEmailAddress, Object[] bodyTemplateArgs) {
        Email email = new Email();
        email.setFrom(fromEmailAddress);
        email.setSubject(subject);
        email.setTo(toEmailAddress);
        email.setBody(String.format(bodyTemplate, bodyTemplateArgs));
        email.setContent("text/html; charset=utf-8");
        return email;
    }

}
