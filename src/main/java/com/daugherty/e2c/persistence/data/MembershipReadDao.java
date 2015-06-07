package com.daugherty.e2c.persistence.data;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines read operations for Membership objects.
 */
public interface MembershipReadDao {
    Membership loadByMembershipId(Long id);

    Membership loadBySupplierId(Long supplierId);

    Integer loadTotalProductsByMembershipId(Long membershipId);

    Membership loadProvisionalBySupplierId(Long id);

    List<Membership> getMemberships(QueryCriteria sortingAndPaginationCriteria);

    QueryCriteria createQueryCriteria(List<Long> membershipIds, Long partyId, String propertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale);
}
