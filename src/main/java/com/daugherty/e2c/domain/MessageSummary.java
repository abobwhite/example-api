package com.daugherty.e2c.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Summarizes Message information for a registered user.
 */
public class MessageSummary extends Entity {

    private static final long serialVersionUID = 1L;

    private Integer unread = 0;
    private Integer sentMessageCount = 0;
    private String publicId = null;

    public MessageSummary(Long id, String publicId, Integer unread, Integer sentMessageCount) {
        super(id);
        this.publicId = publicId;
        this.unread = unread;
        this.sentMessageCount = sentMessageCount;
    }

    public String getPublicId() {
        return publicId;
    }

    public Integer getUnread() {
        return unread;
    }

    public Integer getSentMessageCount() {
        return sentMessageCount;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
    }

    @Override
    protected int hashCodeMultiplier() {
        return 37;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("unread", unread).append("sentMessageCount", sentMessageCount);
    }

}
