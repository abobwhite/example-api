package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.MessageAttachment;
import com.daugherty.e2c.persistence.data.MessageAttachmentReadDao;
import com.daugherty.e2c.persistence.data.MessageAttachmentWriteDao;
import com.daugherty.e2c.persistence.document.DocumentDao;

/**
 * Mutator for Message Attachment domain objects.
 */
@Service("messageAttachmentMutator")
@Transactional
public class MessageAttachmentMutator extends BaseMutator<MessageAttachment> {

    @Inject
    private MessageAttachmentReadDao messageAttachmentReadDao;
    @Inject
    private MessageAttachmentWriteDao messageAttachmentWriteDao;
    @Inject
    private DocumentDao documentDao;

    @Override
    public MessageAttachment create(MessageAttachment entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MessageAttachment update(MessageAttachment entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long messageAttachmentId) {
        MessageAttachment messageAttachement = messageAttachmentReadDao.load(messageAttachmentId);

        documentDao.delete(messageAttachement.getAttachmentLink());

        messageAttachmentWriteDao.deleteAttachement(messageAttachmentId);
    }

}
