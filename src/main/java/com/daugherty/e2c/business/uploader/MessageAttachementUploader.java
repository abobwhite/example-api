package com.daugherty.e2c.business.uploader;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageAttachment;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.MessageAttachmentReadDao;
import com.daugherty.e2c.persistence.data.MessageAttachmentWriteDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcMessageDao;
import com.daugherty.e2c.persistence.document.DocumentType;
import com.google.common.collect.Lists;

@Service
@Transactional
public class MessageAttachementUploader extends BaseDocumentUploader<Message> {

    private static final Integer MAX_ATTACHMENTS = 3;

    @Inject
    private JdbcMessageDao messageDao;
    @Inject
    private MessageAttachmentReadDao messageAttachmentReadDao;
    @Inject
    private MessageAttachmentWriteDao messageAttachmentWriteDao;

    @Override
    protected Message loadEntityById(Long messageId) {
        Message message = messageDao.load(messageId);
        message.getAttachements().addAll(messageAttachmentReadDao.loadByMessageIds(Lists.newArrayList(messageId)));
        return message;
    }

    @Override
    protected boolean isValid(Message message, ValidationError validationError) {
        if (!CollectionUtils.isEmpty(message.getAttachements())) {

            int totalAttachemntsIncludingNew = message.getAttachements().size() + 1;

            if (totalAttachemntsIncludingNew <= MAX_ATTACHMENTS) {
                return true;
            }

            validationError.add(Message.ATTACHMENTS_SERIAL_PROPERTY, Validatable.MESSAGE_ATTACHMENTS_LENGTH);
            return false;
        }

        return true;
    }

    @Override
    protected DocumentType getDocumentType() {
        return DocumentType.MESSAGE_ATTACHEMNT;
    }

    @Override
    protected void persistNewDocumentKey(Message message, String documentName, String documentKey) {
        MessageAttachment messageAttachement = new MessageAttachment(message.getId(), documentName, documentKey);
        messageAttachmentWriteDao.insertAttachment(messageAttachement);
    }
}
