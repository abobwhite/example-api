package com.daugherty.e2c.service.security;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.persistence.data.UserWriteDao;

/**
 * Enables/disables User access
 */
@Component
@Transactional
public class UserGatekeeper {

    @Inject
    private UserWriteDao userWriteDao;


    public void unlockUser(String username) {
        userWriteDao.resetFailureCount(username);
    }

    public void blockUser(String username) {
        userWriteDao.blockUser(username);
    }

    public void unblockUser(String username) {
        userWriteDao.unblockUser(username);
    }
}
