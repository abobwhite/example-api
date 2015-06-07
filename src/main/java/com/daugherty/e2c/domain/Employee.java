package com.daugherty.e2c.domain;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * A Employee party of the system
 */
public class Employee extends Party {

    private static final long serialVersionUID = 1L;
    private String password;
    private Long userId;
    private Boolean locked = false;
    private Boolean blocked = false;
    private List<String> roles = Lists.newArrayList();

    /**
     * Constructor for new Employee instances.
     */
    public Employee(String username, String password, List<String> roles, Contact contact) {
        this(null, null, contact, ApprovalStatus.APPROVED, username, false, true, null, roles, 1, null);
        this.password = password;
    }

    /**
     * Constructor for existing Employee instances.
     */
    public Employee(Long id, String publicId, Contact contact, ApprovalStatus approvalStatus, String username,
            Boolean locked, Boolean blocked, Long userId, List<String> roles, Integer version, Long snapshotId) {
        super(id, publicId, contact, null, approvalStatus, PartyType.INTERNAL, version, snapshotId);
        this.username = username;
        this.locked = locked;
        this.blocked = blocked;
        this.userId = userId;
        this.roles = roles;
    }

    @Override
    public ValidationError validate() {
        ValidationError error = new ValidationError();

        validateFirstName(error);
        validateLastName(error);
        validateEmail(error);

        return error;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Boolean getLocked() {
        return locked;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        super.addEntityFieldsToEqualsBuilder(builder, obj);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        super.addEntityFieldsToHashCodeBuilder(builder);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        super.addEntityFieldsToToStringBuilder(builder);
    }
}
