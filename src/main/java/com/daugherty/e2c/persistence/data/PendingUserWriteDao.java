package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.PendingUser;

public interface PendingUserWriteDao {

    public PendingUser insert(PendingUser pendingUser);

    public void delete(Long id);

    public void deleteByPartyId(Long partyId);

    void disablePendingUser(String username, String password);

    void changePassword(String username, String password);
}
