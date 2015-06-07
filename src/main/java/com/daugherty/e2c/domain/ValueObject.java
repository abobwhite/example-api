package com.daugherty.e2c.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A Domain Object that is not persisted to permanent storage, except as part of Entity objects.
 */
public abstract class ValueObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        EqualsBuilder builder = new EqualsBuilder();
        addFieldsToEqualsBuilder(builder, obj);
        return builder.isEquals();
    }

    protected abstract void addFieldsToEqualsBuilder(EqualsBuilder builder, Object obj);

    @Override
    public final int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(23, hashCodeMultiplier());
        addFieldsToHashCodeBuilder(builder);
        return builder.toHashCode();
    }

    /**
     * Override with a non-zero odd number that is unique across the set of Domain Objects.
     */
    protected abstract int hashCodeMultiplier();

    protected abstract void addFieldsToHashCodeBuilder(HashCodeBuilder builder);

    @Override
    public final String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
        addFieldsToToStringBuilder(builder);
        return builder.toString();
    }

    protected abstract void addFieldsToToStringBuilder(ToStringBuilder builder);

}
