package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.PendingUser;

public interface PendingUserReadDao {
    public PendingUser loadByConfirmationToken(String confirmationToken);

    public PendingUser loadByUsername(String userName);

    public PendingUser loadPendingUserByEmailAddress(String emailAddress);

    public PendingUser loadPendingUserByPassword(String oldPassword);
}
