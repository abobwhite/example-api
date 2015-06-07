package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Discount;

/**
 * Defines database change operations for Discount domain objects.
 */
public interface DiscountWriteDao {
    Discount insert(Discount discount);

    Discount update(Discount discount);
}
