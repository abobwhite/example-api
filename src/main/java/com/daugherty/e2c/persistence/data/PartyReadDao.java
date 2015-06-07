package com.daugherty.e2c.persistence.data;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Party;
import com.daugherty.persistence.QueryCriteria;

public interface PartyReadDao extends SortAndPaginationAware {
    List<Party> getAll(QueryCriteria queryCriteriaCCriteroia);

    QueryCriteria createQueryCriteria(String username, String partyType, String propertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale);

    Party loadByEmailAddress(String emailAddress);

    Party loadById(Long partyId);

    boolean hasBeenApprovedOnce(Long partyId);
}
