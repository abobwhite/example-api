package com.daugherty.e2c.persistence.data;

public interface MessageWriteDao {
    void insertMessageFlag(Long messageId, Long partyId);

    void deleteMessageFlag(Long messageId, Long partyId);

    int deleteMessagesByPartyId(Long partyId);
}
