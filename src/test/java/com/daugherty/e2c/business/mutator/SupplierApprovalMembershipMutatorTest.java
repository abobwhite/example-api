package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.joda.time.DateMidnight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.persistence.data.MembershipDiscountWriteDao;
import com.daugherty.e2c.persistence.data.MembershipWriteDao;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("deprecation")
public class SupplierApprovalMembershipMutatorTest {
    @Mock
    private MembershipWriteDao membershipWriteDao;
    @Mock
    private MembershipDiscountWriteDao membershipDiscountWriteDao;

    @InjectMocks
    private SupplierApprovalMembershipMutator supplierApprovalMembershipMutator = new SupplierApprovalMembershipMutator();

    @Test
    public void membershipIsNotUpdatedWhenLevelIsGreaterThan1() {
        DateMidnight purchaseDate = new DateMidnight().minusMonths(3);
        DateMidnight effectiveDate = new DateMidnight().minusMonths(3);
        DateMidnight expirationDate = new DateMidnight().plusMonths(1);

        MembershipLevel membershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647,
                600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership membership = new Membership(1L, 42L, membershipLevel, ApprovalStatus.PAID, 1, purchaseDate.toDate(),
                effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO, BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<MembershipDiscount>());

        supplierApprovalMembershipMutator.update(membership);

        verifyZeroInteractions(membershipWriteDao, membershipDiscountWriteDao);
    }

    @Test
    public void membershipExpirationDateIsUpdatedWhenLevelIs1() {
        DateMidnight purchaseDate = new DateMidnight().minusMonths(3);
        DateMidnight effectiveDate = new DateMidnight().minusMonths(3);
        DateMidnight expirationDate = new DateMidnight().plusMonths(1);

        MembershipLevel membershipLevel = new MembershipLevel(101L, 1, new BigDecimal("1000.00"), 6, 5, 2147483647,
                600, true, 5, false, false, false, 0, false, false, false, false, false, false, false);

        Membership membership = new Membership(1L, 42L, membershipLevel, ApprovalStatus.PAID, 1, purchaseDate.toDate(),
                effectiveDate.toDate(), expirationDate.toDate(), BigDecimal.ZERO, BigDecimal.ZERO, 1L,
                PaymentType.NONE, null, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<MembershipDiscount>());

        supplierApprovalMembershipMutator.update(membership);

        assertThat(new DateMidnight().plusMonths(6).plusDays(3).toDate(), is(membership.getExpirationDate()));

        verify(membershipWriteDao).update(membership);
        verify(membershipDiscountWriteDao).updateMembershipDiscounts(membership.getSnapshotId(),
                membership.getMembershipDiscounts());
    }
}
