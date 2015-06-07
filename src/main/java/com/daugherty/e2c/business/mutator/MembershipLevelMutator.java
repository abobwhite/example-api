package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.persistence.data.MembershipLevelWriteDao;

/**
 * Mutator for Membership Level domain objects.
 */
@Service("membershipLevelMutator")
@Transactional
public class MembershipLevelMutator extends BaseMutator<MembershipLevel> {

    @Inject
    private MembershipLevelWriteDao membershipLevelWriteDao;

    @Override
    public MembershipLevel create(MembershipLevel entity) {
        membershipLevelWriteDao.expireMembershipLevel(entity.getValue());
        return membershipLevelWriteDao.create(entity);
    }

    @Override
    public MembershipLevel update(MembershipLevel entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long entityId) {
        throw new UnsupportedOperationException();
    }

}
