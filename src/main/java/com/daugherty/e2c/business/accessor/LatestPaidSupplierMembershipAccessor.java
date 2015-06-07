package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.persistence.data.MembershipDiscountReadDao;
import com.daugherty.e2c.persistence.data.MembershipReadDao;

/**
 * Accessor for Latest Paid Supplier Membership By Supplier Id domain objects.
 */
@Service("latestPaidSupplierMembershipAccessor")
public class LatestPaidSupplierMembershipAccessor extends BaseAccessor<Membership> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private MembershipReadDao membershipReadDao;
    @Inject
    private MembershipDiscountReadDao membershipDiscountReadDao;

    @Override
    public List<Membership> find(Filter<Membership> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Membership load(Long id, Locale locale) {
        try {
            Membership membership = membershipReadDao.loadBySupplierId(id);
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
