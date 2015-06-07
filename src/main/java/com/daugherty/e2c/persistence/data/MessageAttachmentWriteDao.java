package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.MessageAttachment;

public interface MessageAttachmentWriteDao {
    void insertAttachment(MessageAttachment messageAttachement);

    int deleteAttachement(Long messageAttachmentId);
}
