package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.UserMembership;
import com.daugherty.persistence.QueryCriteria;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/2/14
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserMembershipReadDao {
    UserMembership load(Long id);

    List<UserMembership> find(QueryCriteria criteria);

    QueryCriteria createQueryCriteria(String usernamePrefix, String emailPrefix,
                                      String companyNamePrefix, Integer membershipLevel, String sortByPropertyName,
                                      Boolean sortDescending, Integer startItem, Integer count);
}
