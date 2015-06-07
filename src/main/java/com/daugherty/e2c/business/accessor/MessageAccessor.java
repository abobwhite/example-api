package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.MessageCollectionHydrator;
import com.daugherty.e2c.business.accessor.filter.MessageFilter;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.persistence.data.jdbc.JdbcMessageDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

/**
 * Accessor for Message domain objects.
 */
@Service("messageAccessor")
public class MessageAccessor extends BaseAccessor<Message> {
    @Inject
    private JdbcMessageDao messageDao;
    @Inject
    private MessageCollectionHydrator messageCollectionHydrator;

    @Override
    public List<Message> find(Filter<Message> filter) {
        List<Message> messages = findMessages(filter);

        messageCollectionHydrator.hydrateAll(messages);

        return messages;
    }

    private List<Message> findMessages(Filter<Message> filter) {
        String publicReceiverId = filter.getStringCriterion(MessageFilter.RECEIVER_ID);
        String publicSenderId = filter.getStringCriterion(MessageFilter.SENDER_ID);
        Long receiverId = null;
        Long senderId = null;

        if (publicReceiverId != null) {
            receiverId = decrypt(publicReceiverId);
        }

        if (publicSenderId != null) {
            senderId = decrypt(publicSenderId);
        }

        QueryCriteria criteria = messageDao.createQueryCriteria(receiverId,
                filter.getBooleanCriterion(MessageFilter.FLAGGED), senderId, filter.getSortBy(),
                filter.isSortDescending(), filter.getStartItem(), filter.getCount());

        if ((receiverId != null && receiverId.equals(senderId))) {
            return messageDao.findForParty(criteria);
        } else {
            return messageDao.find(criteria);
        }
    }

    @Override
    public Message load(Long id, Locale locale) {
        Message message = messageDao.load(id);

        messageCollectionHydrator.hydrate(message);

        return message;
    }

    public Message load(String id, Locale locale, String retrieveAsPartyId) {
        Message message = messageDao.load(decrypt(id), decrypt(retrieveAsPartyId));

        messageCollectionHydrator.hydrateAll(Lists.newArrayList(message));

        return message;
    }

}
