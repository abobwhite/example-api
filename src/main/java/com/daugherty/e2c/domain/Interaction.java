package com.daugherty.e2c.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A specific Interaction between the originator of a Message and a recipient. Examples of this would include the
 * initial message and any replies.
 */
public class Interaction extends Entity implements Validatable {

    private static final long serialVersionUID = 1L;

    public static final String BODY_SERIAL_PROPERTY = "body";
    public static final String SENDER_SERIAL_PROPERTY = "sender";
    public static final String RECEIVER_SERIAL_PROPERTY = "receiver";

    private Message message;
    private final String body;
    private final Party sender;
    private final Party receiver;
    private final Date sentTime;
    private boolean read;

    // Constructor for new Interaction
    public Interaction(Message message, String body, Party sender, Party receiver) {
        this(null, body, sender, receiver, null, false);
        this.message = message;
    }

    // Constructor for existing Interaction
    public Interaction(Long id, String body, Party sender, Party receiver, Date sentTime, boolean read) {
        super(id);
        this.body = body;
        this.sender = sender;
        this.receiver = receiver;
        this.read = read;
        this.sentTime = sentTime;
    }

    // Constructor for just representing the time an Interaction occurred
    public Interaction(Date sentTime) {
        this(null, null, null, null, sentTime, false);
    }

    public Message getMessage() {
        return message;
    }

    public String getBody() {
        return body;
    }

    public Party getSender() {
        return sender;
    }

    public Party getReceiver() {
        return receiver;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        read = true;
    }

    @Override
    public ValidationError validate() {
        ValidationError errors = new ValidationError();
        validateBody(errors);
        validateSender(errors);
        validateReceiver(errors);
        return errors;
    }

    private void validateBody(ValidationError errors) {
        if (StringUtils.isBlank(body)) {
            errors.add(BODY_SERIAL_PROPERTY, INTERACTION_BODY_REQUIRED);
        }
        if (StringUtils.isNotBlank(body) && body.length() > 4000) {
            errors.add(BODY_SERIAL_PROPERTY, INTERACTION_BODY_LENGTH);
        }
    }

    private void validateSender(ValidationError errors) {
        if (sender == null) {
            errors.add(SENDER_SERIAL_PROPERTY, INTERACTION_SENDER_REQUIRED);
        }
    }

    private void validateReceiver(ValidationError errors) {
        if (receiver == null) {
            errors.add(RECEIVER_SERIAL_PROPERTY, INTERACTION_RECEIVER_REQUIRED);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
    }

    @Override
    protected int hashCodeMultiplier() {
        return 69;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("message", message).append("body", body).append("sender", sender).append("receiver", receiver)
                .append("sentTime", sentTime).append("read", read);
    }

}
