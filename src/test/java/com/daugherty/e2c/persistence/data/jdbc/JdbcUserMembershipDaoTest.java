package com.daugherty.e2c.persistence.data.jdbc;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.UserMembership;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/2/14
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class JdbcUserMembershipDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcUserMembershipDao userMembershipDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("userMembership.sql");
    }

    @Test
    public void getAll() {
        QueryCriteria sortingAndPaginationCriteria =
                userMembershipDao.createQueryCriteria(null, null, null, 2, "effectiveDate", false, 1, 25);
        List<UserMembership> all = userMembershipDao.find(sortingAndPaginationCriteria);
        System.out.println("all = " + all.size());
    }

    @Test
    public void load() {
        UserMembership userMembership = userMembershipDao.load(88888l);
        System.out.println("userMembership = " + userMembership);
    }

}
