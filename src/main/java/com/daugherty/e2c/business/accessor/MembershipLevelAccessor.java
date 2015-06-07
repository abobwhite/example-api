package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;

/**
 * Accessor for Membership Level domain objects.
 */
@Service("membershipLevelAccessor")
public class MembershipLevelAccessor extends BaseAccessor<MembershipLevel> {

    @Inject
    private MembershipLevelReadDao membershipLevelReadDao;

    @Override
    public List<MembershipLevel> find(Filter<MembershipLevel> filter) {
        return membershipLevelReadDao.loadAll();
    }

    @Override
    public MembershipLevel load(Long id, Locale locale) {
        return membershipLevelReadDao.loadByValue(id);
    }

}
