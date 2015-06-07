package com.daugherty.e2c.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * An internal message from a Buyer (or qualified Supplier) to a Supplier inquiring about one or more Products,
 * including any replies.
 */
public class Message extends Entity implements Validatable {

    private static final long serialVersionUID = 1L;

    public static final String SUBJECT_SERIAL_PROPERTY = "subject";
    public static final String OTHER_PARTY_SERIAL_PROPERTY = "otherParty";
    public static final String LAST_INTERACTION_SENT_TIME_SERIAL_PROPERTY = "mostRecentInteractionTime";

    public static final String ATTACHMENTS_SERIAL_PROPERTY = "attachments";

    private String publicId;
    private String subject;
    private Party otherParty;
    private boolean flagged;
    private Interaction lastInteraction;
    private boolean completelyRead;
    private MessageType messageType;
    private List<MessageAttachment> attachements = Lists.newArrayList();

    // Constructor for proxy Message (used in Interaction)
    public Message(Long id) {
        super(id);
    }

    public Message(Long id, String publicId) {
        super(id);
        this.publicId = publicId;
    }

    // Constructor for new single-Product Message
    public Message(String subject, Party originator, Interaction interaction, MessageType messageType) {
        this.subject = subject;
        this.otherParty = originator;
        this.lastInteraction = interaction;
        this.messageType = messageType;
    }

    // Constructor for existing Message
    public Message(Long id, String publicId, String subject, Party otherParty, boolean flagged,
            Date lastInteractionSentTime, boolean completelyRead, MessageType messageType) {
        super(id);
        this.publicId = publicId;
        this.subject = subject;
        this.otherParty = otherParty;
        this.flagged = flagged;
        lastInteraction = new Interaction(lastInteractionSentTime);
        this.completelyRead = completelyRead;
        this.messageType = messageType;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getSubject() {
        return subject;
    }

    public Party getOtherParty() {
        return otherParty;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public Interaction getLastInteraction() {
        return lastInteraction;
    }

    public boolean isCompletelyRead() {
        return completelyRead;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setAttachements(List<MessageAttachment> attachements) {
        this.attachements = attachements;
    }

    public List<MessageAttachment> getAttachements() {
        return attachements;
    }

    @Override
    public ValidationError validate() {
        ValidationError errors = new ValidationError();
        validateSubject(errors);
        validateOtherParty(errors);
        validateInteraction(errors);
        return errors;
    }

    private void validateSubject(ValidationError errors) {
        if (StringUtils.isBlank(subject)) {
            errors.add(SUBJECT_SERIAL_PROPERTY, MESSAGE_SUBJECT_REQUIRED);
        }
        if (StringUtils.isNotBlank(subject) && subject.length() > 150) {
            errors.add(SUBJECT_SERIAL_PROPERTY, MESSAGE_SUBJECT_LENGTH);
        }
    }

    private void validateOtherParty(ValidationError errors) {
        if (otherParty == null) {
            errors.add(OTHER_PARTY_SERIAL_PROPERTY, MESSAGE_ORIGINATOR_REQUIRED);
        }
    }

    private void validateInteraction(ValidationError errors) {
        if (lastInteraction != null) {
            errors.add(lastInteraction.validate());
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        Message rhs = (Message) obj;
        builder.append(subject, rhs.subject).append(otherParty, rhs.otherParty);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 67;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(subject).append(otherParty);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("subject", subject).append("otherParty", otherParty).append("flagged", flagged)
                .append("lastInteraction", lastInteraction).append("completelyRead", completelyRead);
    }

}
