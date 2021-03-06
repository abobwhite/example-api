package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.persistence.data.MembershipDiscountReadDao;
import com.daugherty.e2c.persistence.data.MembershipReadDao;

@RunWith(MockitoJUnitRunner.class)
public class MembershipAccessorTest {
    @Mock
    private MembershipReadDao membershipReadDao;
    @Mock
    private MembershipDiscountReadDao membershipDiscountReadDao;

    @InjectMocks
    private final MembershipAccessor membershipAccessor = new MembershipAccessor();

    @Test
    public void loadFindsMembershipByMembershipId() {
        long supplierId = 42L;
        long membershipId = 43L;
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, null, 6, 5, 2147483647, 600, true, 0, false,
                false, false, 0, false, false, false, false, false, false, false);
        Membership membership = new Membership(supplierId, membershipLevel);
        membership.setId(membershipId);

        when(membershipReadDao.loadByMembershipId(membershipId)).thenReturn(membership);

        Membership persistedMembership = membershipAccessor.load(membershipId, Locale.ENGLISH);

        assertThat(persistedMembership, is(membership));
        assertThat(persistedMembership.getLevel(), is(membershipLevel));
    }

}
