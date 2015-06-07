package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.MessageSummary;
import com.daugherty.e2c.persistence.data.ProductMessageReadDao;

/**
 * Accessor for MessageSummary domain objects.
 */
@Service("messageSummaryAccessor")
public class MessageSummaryAccessor extends BaseAccessor<MessageSummary> {

    @Inject
    private ProductMessageReadDao messageReadDao;

    @Override
    public List<MessageSummary> find(Filter<MessageSummary> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MessageSummary load(Long id, Locale locale) {
        Integer unreadCount = messageReadDao.loadUnreadMessagesForParty(id);
        Integer messageSentCount = messageReadDao.loadSentMessagesForParty(id);

        return new MessageSummary(id, encrypt(id), unreadCount, messageSentCount);
    }
}
