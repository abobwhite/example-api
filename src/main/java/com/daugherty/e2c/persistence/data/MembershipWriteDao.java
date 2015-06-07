package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Membership;

/**
 * Defines database write operations for Membership objects.
 */
public interface MembershipWriteDao {

    Membership insert(Membership membership);

    Membership update(Membership membership);

    int deleteMembershipsByPartyId(Long partyId);

}
