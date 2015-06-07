package com.daugherty.e2c.business.mutator;

import java.util.Date;

import javax.inject.Inject;

import org.joda.time.DateMidnight;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.persistence.data.MembershipDiscountWriteDao;
import com.daugherty.e2c.persistence.data.MembershipWriteDao;

/**
 * Mutator for Membership domain objects.
 */
@SuppressWarnings("deprecation")
@Service("supplierApprovalMembershipMutator")
@Transactional
public class SupplierApprovalMembershipMutator extends BaseMutator<Membership> {

    @Inject
    private MembershipWriteDao membershipWriteDao;
    @Inject
    private MembershipDiscountWriteDao membershipDiscountWriteDao;

    @Override
    public Membership create(Membership entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Membership update(Membership entity) {

        if (entity != null && Integer.valueOf(1).equals(entity.getLevel().getValue())) {

            Date expirationDate = new DateMidnight().plusMonths(entity.getLevel().getMonthsOfTerm()).plusDays(3)
                    .toDate();

            entity.setExpirationDate(expirationDate);

            membershipWriteDao.update(entity);
            entity.setMembershipDiscounts(membershipDiscountWriteDao.updateMembershipDiscounts(entity.getSnapshotId(),
                    entity.getMembershipDiscounts()));
        }

        return entity;
    }

    @Override
    public void delete(Long membershipId) {
        throw new UnsupportedOperationException();
    }
}