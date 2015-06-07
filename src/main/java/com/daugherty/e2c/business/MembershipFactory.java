package com.daugherty.e2c.business;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;

/**
 * Factory for creating instances of memberships;
 */
@Component
public class MembershipFactory {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    protected Accessor<Membership> latestPaidSupplierMembershipAccessor;
    @Inject
    protected MembershipLevelReadDao membershipLevelReadDao;
    @Inject
    private MembershipDiscountBuilder membershipDiscountBuilder;
    @Inject
    private MembershipBuilder membershipBuilder;

    public Membership build(Membership membership, String couponCode) {
        Membership existingMembership = getExistingMembership(membership.getSupplierId());

        MembershipLevel newMembershipLevel = membershipLevelReadDao.loadByValue(membership.getLevel().getValue());
        membership.setPurchasePrice(membership.getLevel().getPrice());
        membership = membershipDiscountBuilder.build(membership, existingMembership, newMembershipLevel, couponCode);
        
        return membership;
    }
    
    public Membership build(Membership membership) {
        Membership existingMembership = getExistingMembership(membership.getSupplierId());

        MembershipLevel newMembershipLevel = membershipLevelReadDao.loadByValue(membership.getLevel().getValue());

        membership = membershipBuilder.build(membership, newMembershipLevel, existingMembership);
        membership = membershipDiscountBuilder.build(membership, existingMembership, newMembershipLevel, null);
        
        return membership;
    }

    private Membership getExistingMembership(Long supplierId) {
        try {
            return latestPaidSupplierMembershipAccessor.load(supplierId, Locale.ENGLISH);
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("No Membership found, this is probably a new Supplier");
        }

        return null;
    }
}
