package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines database read operations for the BuyerApproval domain object.
 */
public interface BuyerApprovalReadDao extends SortAndPaginationAware {

    List<BuyerApproval> getAll(QueryCriteria sortingAndPaginationCriteria);

    QueryCriteria createQueryCriteria(String titlePrefix, String emailPrefix, String approvalStatus, Boolean newBuyer,
            String propertyName, Boolean sortDescending, Integer startItem, Integer count);

    List<BuyerApproval> find(QueryCriteria criteria);

    BuyerApproval load(Long id);
}
