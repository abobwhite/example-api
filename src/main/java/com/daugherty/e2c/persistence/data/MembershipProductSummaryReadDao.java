package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.MembershipProductSummary;

/**
 * Defines database change operations for the Membership's Product Summary.
 */
public interface MembershipProductSummaryReadDao {

    MembershipProductSummary loadByMembershipId(Long membershipId);

    MembershipProductSummary loadByProductId(Long productId);

}
