package com.daugherty.e2c.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Represents a user of the Export to China system.
 * <p>
 * We currently do not care about expired accounts or passwords. However, an account can be disabled and locked.
 */
public class E2CUser extends User {

    private static final long serialVersionUID = -1403176036839202762L;

    private static final int FAILURES_SINCE_LAST_SUCCESS_LIMIT = 5;

    private Party party;
    private boolean isApprovedOnce;
    private final int failuresSinceLastSuccess;

    public E2CUser(String username, String password, boolean enabled, int failuresSinceLastSuccess,
            Collection<? extends GrantedAuthority> authorities, Party party, boolean isApprovedOnce) {
        super(username, password, enabled, true, true, isAccountUnlocked(failuresSinceLastSuccess), authorities);
        this.failuresSinceLastSuccess = failuresSinceLastSuccess;
        this.isApprovedOnce = isApprovedOnce;
        this.party = party;
    }

    private static boolean isAccountUnlocked(int failuresSinceLastSuccess) {
        return failuresSinceLastSuccess < FAILURES_SINCE_LAST_SUCCESS_LIMIT;
    }

    public Party getParty() {
        return party;
    }

    public boolean isApprovedOnce() {
        return isApprovedOnce;
    }

    public int getFailuresSinceLastSuccess() {
        return failuresSinceLastSuccess;
    }
}
