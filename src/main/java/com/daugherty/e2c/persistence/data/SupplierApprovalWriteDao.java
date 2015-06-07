package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.SupplierApproval;

/**
 * Defines database change operations for the SupplierApproval domain object.
 */
public interface SupplierApprovalWriteDao {

    SupplierApproval insert(SupplierApproval approval);

    /**
     * This operation isn't strictly needed as the implementation with the current database is identical to insert().
     * However, including this method means that the mutator logic doesn't need to know that.
     */
    SupplierApproval update(SupplierApproval approval);

}
