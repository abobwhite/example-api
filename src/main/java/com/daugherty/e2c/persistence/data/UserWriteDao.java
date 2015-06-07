package com.daugherty.e2c.persistence.data;

import org.springframework.security.core.GrantedAuthority;

import com.daugherty.e2c.domain.E2CUser;

/**
 * Defines database change operations for the User domain object.
 */
public interface UserWriteDao {

    void insert(E2CUser user);

    void insertUserRole(Long userId, GrantedAuthority authority);

    void deleteUserRole(Long userId, GrantedAuthority authority);

    void incrementFailureCount(String username);

    void resetFailureCount(String username);

    void disableUser(String username, String password);

    void changePassword(String username, String oldPassword, String newPassword);

    void blockUser(String username);

    void unblockUser(String username);

    int deleteUser(Long userId);
}
