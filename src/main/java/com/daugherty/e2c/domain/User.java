package com.daugherty.e2c.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 *
 */
public class User extends Entity {

    private static final long serialVersionUID = 17L;

    static final Function<String, SimpleGrantedAuthority> ROLE_TO_GRANTED_AUTHORITY = new Function<String, SimpleGrantedAuthority>() {
        @Override
        public SimpleGrantedAuthority apply(String role) {
            return new SimpleGrantedAuthority(role);
        }
    };

    public static final String USERNAME_SERIAL_PROPERTY = "username";
    public static final String FIRST_NAME_SERIAL_PROPERTY = "firstName";
    public static final String LAST_NAME_SERIAL_PROPERTY = "lastName";
    public static final String EMAIL_SERIAL_PROPERTY = "email";
    public static final String COMPANY_NAME_SERIAL_PROPERTY = "companyName";
    public static final String APPROVAL_STATUS_SERIAL_PROPERTY = "approvalStatus";
    public static final String ROLE_SERIAL_PROPERTY = "role";
    public static final String PRESENCE_SERIAL_PROPERTY = "presence";
    public static final String LAST_UPDATED_BY_SERIAL_PROPERTY = "lastUpdatedBy";
    public static final String LOCKED_SERIAL_PROPERTY = "locked";
    public static final String BLOCKED_SERIAL_PROPERTY = "blocked";
    public static final String LAST_UPDATED_SERIAL_PROPERTY = "lastUpdated";

    private final String username;
    private final String password;
    private final Boolean locked;
    private final Boolean blocked;
    private final Boolean enabled;
    private final Boolean isApprovedOnce;
    private Party party;
    private List<String> roles = Lists.newArrayList();
    private Date lastUpdated;
    private final int failuresSinceLastSuccess;

    public User(Long id, String username, String password, List<String> roles, int failuresSinceLastSuccess,
            boolean isLocked, boolean isBlocked, boolean isEnabled, Party party, boolean isApprovedOnce,
            Date lastUpdated) {
        super(id);
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.failuresSinceLastSuccess = failuresSinceLastSuccess;
        this.locked = isLocked;
        this.blocked = isBlocked;
        this.enabled = isEnabled;
        this.isApprovedOnce = isApprovedOnce;
        this.party = party;
        this.lastUpdated = lastUpdated;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Boolean isLocked() {
        return locked;
    }

    public Boolean isBlocked() {
        return blocked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public int getFailuresSinceLastSuccess() {
        return failuresSinceLastSuccess;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Party getParty() {
        return party;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Boolean isApprovedOnce() {
        return isApprovedOnce;
    }

    public E2CUser getE2CUser() {
        return new E2CUser(username, password, enabled, failuresSinceLastSuccess, Lists.transform(roles,
                ROLE_TO_GRANTED_AUTHORITY), party, isApprovedOnce);
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        User rhs = (User) obj;
        builder.append(username, rhs.username).append(locked, rhs.locked).append(blocked, rhs.blocked)
                .append(enabled, rhs.enabled).append(isApprovedOnce, rhs.isApprovedOnce).append(party, rhs.party);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(username).append(locked).append(blocked).append(enabled).append(isApprovedOnce).append(party);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("username", username).append("locked", locked).append("blocked", blocked)
                .append("enabled", enabled).append("isApprovedOnce", isApprovedOnce).append("party", party);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 17;
    }
}
