package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.BuyerApproval;

/**
 * Defines database change operations for the BuyerApproval domain object.
 */
public interface BuyerApprovalWriteDao {

    BuyerApproval insert(BuyerApproval approval);

    /**
     * This operation isn't strictly needed as the implementation with the current database is identical to insert().
     * However, including this method means that the mutator logic doesn't need to know that.
     */
    BuyerApproval update(BuyerApproval approval);
}
