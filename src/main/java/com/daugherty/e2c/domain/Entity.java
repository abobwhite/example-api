package com.daugherty.e2c.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A Domain Object that can be persisted to permanent storage.
 */
public abstract class Entity extends ValueObject {

    private static final long serialVersionUID = 1L;

    private Long id;

    public Entity() {
        super();
    }

    public Entity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    protected boolean exists() {
        return id != null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    protected final void addFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        Entity rhs = (Entity) obj;
        builder.append(id, rhs.id);
        addEntityFieldsToEqualsBuilder(builder, obj);
    }

    protected abstract void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj);

    @Override
    protected final void addFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(id);
        addEntityFieldsToHashCodeBuilder(builder);
    }

    protected abstract void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder);

    @Override
    protected final void addFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("id", id);
        addEntityFieldsToToStringBuilder(builder);
    }

    protected abstract void addEntityFieldsToToStringBuilder(ToStringBuilder builder);

}
