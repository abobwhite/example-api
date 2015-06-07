package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.Message;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines database read operations for Message domain objects.
 */
public interface MessageReadDao {

    QueryCriteria createQueryCriteria(Long receiverId, Boolean flagged, Long senderId, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count);

    List<Message> find(QueryCriteria criteria);

    List<Message> findForParty(QueryCriteria criteria);

    Integer loadUnreadMessagesForParty(Long id);

    Integer loadSentMessagesForParty(Long id);

    Message load(Long id);

    Message load(Long id, Long retrieveAsPartyId);
}
