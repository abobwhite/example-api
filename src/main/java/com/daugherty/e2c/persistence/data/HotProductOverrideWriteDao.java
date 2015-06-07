package com.daugherty.e2c.persistence.data;

/**
 * Defines database change operations for an Admin Hot Product Override.
 */
public interface HotProductOverrideWriteDao {
    void addHotProductOverride(Long productId);

    void removeHotProductOverride(Long productId);
}
