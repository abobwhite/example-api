package com.daugherty.e2c.business.mutator;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.persistence.data.MembershipLevelWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class MembershipLevelMutatorTest {

    @Mock
    private MembershipLevelWriteDao membershipLevelWriteDao;

    @InjectMocks
    private MembershipLevelMutator membershipLevelMutator = new MembershipLevelMutator();

    @Test
    public void createExpiresAndCreates() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.valueOf(5.86), 6, 5, 2147483647, 600,
                true, 0, false, false, false, 0, false, false, false, false, false, false, false);

        membershipLevelMutator.create(membershipLevel);

        verify(membershipLevelWriteDao).expireMembershipLevel(1);
        verify(membershipLevelWriteDao).create(membershipLevel);
    }
}
