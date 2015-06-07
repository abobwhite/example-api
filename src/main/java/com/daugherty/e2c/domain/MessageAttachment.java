package com.daugherty.e2c.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents an attachment for a Message.
 */
public class MessageAttachment extends Entity {

    private static final long serialVersionUID = 1L;

    private final String attachmentName;
    private final String attachmentLink;
    private final Long messageId;

    public MessageAttachment(String attachmentName, String attachmentLink) {
        this(null, null, attachmentName, attachmentLink);
    }

    public MessageAttachment(Long messageId, String attachmentName, String attachmentLink) {
        this(null, messageId, attachmentName, attachmentLink);
    }

    public MessageAttachment(Long id, Long messageId, String attachmentName, String attachmentLink) {
        super(id);
        this.messageId = messageId;
        this.attachmentName = attachmentName;
        this.attachmentLink = attachmentLink;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public String getAttachmentLink() {
        return attachmentLink;
    }

    public Long getMessageId() {
        return messageId;
    }

    @Override
    protected int hashCodeMultiplier() {
        return 19;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        MessageAttachment rhs = (MessageAttachment) obj;
        builder.append(getId(), rhs.getId()).append(attachmentName, rhs.attachmentName)
                .append(attachmentLink, rhs.attachmentLink).append(messageId, rhs.messageId);

    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(getId()).append(attachmentName).append(attachmentLink).append(messageId);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("id", getId()).append("attachmentName", attachmentName).append("attachmentLink", attachmentLink)
                .append("messageId", messageId);
    }
}
