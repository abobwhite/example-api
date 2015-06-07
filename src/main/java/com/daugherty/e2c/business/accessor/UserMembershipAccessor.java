package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.UserMembershipFilter;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.UserMembership;
import com.daugherty.e2c.persistence.data.MembershipDiscountReadDao;
import com.daugherty.e2c.persistence.data.UserMembershipReadDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/2/14
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("userMembershipAccessor")
public class UserMembershipAccessor extends BaseAccessor<UserMembership> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private UserMembershipReadDao userMembershipReadDao;
    @Inject
    private MembershipDiscountReadDao membershipDiscountReadDao;


    @Override
    public List<UserMembership> find(Filter<UserMembership> filter) {
        QueryCriteria criteria = userMembershipReadDao.createQueryCriteria(
                filter.getStringCriterion(UserMembershipFilter.USERNAME_PREFIX),
                filter.getStringCriterion(UserMembershipFilter.EMAIL_PREFIX),
                filter.getStringCriterion(UserMembershipFilter.COMPANY_NAME_PREFIX),
                filter.getIntegerCriterion(UserMembershipFilter.MEMBERSHIP_FILTER),
                filter.getSortBy(), filter.isSortDescending(), filter.getStartItem(), filter.getCount());

        return userMembershipReadDao.find(criteria);
    }

    private void addMembershipDiscounts(Membership membership) {
        membership.setMembershipDiscounts(membershipDiscountReadDao.findBySnapshotId(membership.getSnapshotId()));
    }

    @Override
    public UserMembership load(Long id, Locale locale) {
        UserMembership membership = userMembershipReadDao.load(id);
        addMembershipDiscounts(membership);
        return membership;
    }
}
