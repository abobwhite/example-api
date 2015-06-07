package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.MembershipFilter;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.persistence.data.MembershipDiscountReadDao;
import com.daugherty.e2c.persistence.data.MembershipReadDao;

/**
 * Accessor for Membership domain objects.
 */
@Service("membershipAccessor")
public class MembershipAccessor extends BaseAccessor<Membership> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private MembershipReadDao membershipReadDao;
    @Inject
    private MembershipDiscountReadDao membershipDiscountReadDao;

    @Override
    public List<Membership> find(Filter<Membership> filter) {

        return membershipReadDao.getMemberships(membershipReadDao.createQueryCriteria(
                filter.getLongListCriterion(MembershipFilter.MEMBERSHIP_IDS),
                getPartyId(filter.getStringCriterion(MembershipFilter.PUBLIC_PARTY_ID)), filter.getSortBy(),
                filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale()));
    }

    @Override
    public Membership load(Long id, Locale locale) {
        try {
            Membership membership = membershipReadDao.loadByMembershipId(id);
            addMembershipDiscounts(membership);
            return membership;
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("No Membership found, this is probably a new Supplier");
        }

        return null;
    }

    private void addMembershipDiscounts(Membership membership) {
        membership.setMembershipDiscounts(membershipDiscountReadDao.findBySnapshotId(membership.getSnapshotId()));
    }

}
