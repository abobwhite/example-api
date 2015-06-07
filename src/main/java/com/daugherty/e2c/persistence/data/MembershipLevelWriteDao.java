package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.MembershipLevel;

/**
 * Defines write operations for MembershipLevel objects.
 */
public interface MembershipLevelWriteDao {

    void expireMembershipLevel(Integer value);

    MembershipLevel create(MembershipLevel entity);

}
