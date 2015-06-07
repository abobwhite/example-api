package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines database read operations for the SupplierApproval domain object.
 */
public interface SupplierApprovalReadDao extends SortAndPaginationAware {

    QueryCriteria createQueryCriteria(String titlePrefix, String emailPrefix, String approvalType,
            String approvalStatus, Boolean newSupplier, Boolean paidSupplier, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count);

    List<SupplierApproval> find(QueryCriteria criteria);

    SupplierApproval load(Long id);

}
