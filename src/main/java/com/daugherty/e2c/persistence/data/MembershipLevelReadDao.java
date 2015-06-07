package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.MembershipLevel;

/**
 * Defines read operations for MembershipLevel objects.
 */
public interface MembershipLevelReadDao {

    MembershipLevel loadByValue(long value);

    List<MembershipLevel> loadAll();

}
