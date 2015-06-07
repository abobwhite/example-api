package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.MessageAttachment;

/**
 * Defines database read operations for the Message Attachemnt domain object.
 */
public interface MessageAttachmentReadDao {
    List<MessageAttachment> loadByMessageIds(List<Long> messageIds);

    MessageAttachment load(Long messageAttachmentId);
}
