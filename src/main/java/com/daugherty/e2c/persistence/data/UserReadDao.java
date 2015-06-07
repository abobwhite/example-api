package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.User;
import com.daugherty.persistence.QueryCriteria;

public interface UserReadDao extends SortAndPaginationAware {
    public String getEmailWithPersonalNameForPartyId(Long id);

    public String getUsernameByPassword(String password);

    public String getUsernameByEmailAddress(String emailAddress);

    QueryCriteria createQueryCriteria(String usernamePrefix, String firstNamePrefix, String lastNamePrefix,
            String emailPrefix, String companyNamePrefix, String approvalStatus, String role, Boolean presence,
            Boolean locked, Boolean blocked, String sortByPropertyName, Boolean sortDescending, Integer startItem,
            Integer count);

    User find(Long userId);

    List<User> find(QueryCriteria criteria);

    List<User> findPendingUsers(QueryCriteria criteria);

    User findByPartyId(Long partyId);

}
