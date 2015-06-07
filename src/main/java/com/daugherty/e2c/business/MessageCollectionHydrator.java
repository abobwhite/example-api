package com.daugherty.e2c.business;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageAttachment;
import com.daugherty.e2c.persistence.data.MessageAttachmentReadDao;
import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Set attachments on messages.
 */
@Component("messageCollectionHydrator")
public class MessageCollectionHydrator {

    static final Function<Message, Long> MESSAGE_MESSAGE_ID_FUNCTION = new Function<Message, Long>() {
        @Override
        public Long apply(Message message) {
            return message.getId();
        }
    };

    @Inject
    private MessageAttachmentReadDao messageAttachmentReadDao;

    public Message hydrate(Message message) {
        if (message != null) {
            hydrateAll(Lists.newArrayList(message));
        }

        return message;
    }

    public List<? extends Message> hydrateAll(List<? extends Message> messages) {
        List<Long> messageIds = Lists.transform(messages, MESSAGE_MESSAGE_ID_FUNCTION);

        Multimap<Long, MessageAttachment> attachmentsByMessageId = getMessageAttachementsMap(messageIds);

        for (Message message : messages) {
            message.getAttachements().addAll(attachmentsByMessageId.get(message.getId()));

        }

        return messages;
    }

    private Multimap<Long, MessageAttachment> getMessageAttachementsMap(List<Long> messageIds) {
        List<MessageAttachment> messageAttachments = messageAttachmentReadDao.loadByMessageIds(messageIds);

        Multimap<Long, MessageAttachment> messageIdToMessageAttachment = ArrayListMultimap.create();

        for (MessageAttachment messageAttachement : messageAttachments) {
            messageIdToMessageAttachment.put(messageAttachement.getMessageId(), messageAttachement);
        }

        return messageIdToMessageAttachment;
    }
}
