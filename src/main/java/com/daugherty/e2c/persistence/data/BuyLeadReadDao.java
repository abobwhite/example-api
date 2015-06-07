package com.daugherty.e2c.persistence.data;

import java.util.Date;
import java.util.List;

import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.ArrayListMultimap;

public interface BuyLeadReadDao extends SortAndPaginationAware {
    BuyLead findByPartyAndCategory(Long partyId, Long productCategoryId);

    QueryCriteria createBuyLeadQueryCriteria(String emailAddress, List<Long> categoryIds, String province,
            Date effectiveSince, Boolean includeExpired, String propertyName, Boolean sortDescending,
            Integer startItem, Integer count);

    List<BuyLead> findBuyLeads(QueryCriteria criteria);

    ArrayListMultimap<Long, Long> findSuppliersThatRespondedToBuyLeads(List<Long> buyLeadIds);
}
