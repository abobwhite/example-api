package com.daugherty.e2c.business.validator;

import static org.mockito.Mockito.when;

import java.util.Locale;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.MembershipReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MembershipValidatorTest {

    @Mock
    private Membership membership;
    @Mock
    private MembershipReadDao membershipReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final MembershipValidator validator = new MembershipValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void throwValidationExceptionWhenMembershipValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Membership.EFFECTIVE_DATE_SERIAL_PROPERTY,
                Validatable.EFFECTIVE_DATE_REQUIRED));

        ValidationError validationError = new ValidationError();
        validationError.add(Membership.EFFECTIVE_DATE_SERIAL_PROPERTY, Validatable.EFFECTIVE_DATE_REQUIRED);

        when(membership.validate()).thenReturn(validationError);

        validator.validate(membership);
    }

    @Test
    public void throwValidationExceptionWhenMembershipEffectiveDateValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Membership.EFFECTIVE_DATE_SERIAL_PROPERTY,
                Validatable.EFFECTIVE_DATE_INVALID_RANGE));

        Membership existingMembership = new Membership(42L, null, new MembershipLevel(1), ApprovalStatus.PAID, null,
                null, new DateTime().minusDays(7).toDate(), new DateTime().plusDays(7).toDate(), null, null, null,
                null, null, null, null);

        ValidationError validationError = new ValidationError();

        when(membership.getApprovalStatus()).thenReturn(ApprovalStatus.PROVISIONAL);
        when(membership.getLevel()).thenReturn(new MembershipLevel(1));
        when(membership.validate()).thenReturn(validationError);
        when(membership.getSupplierId()).thenReturn(42L);
        when(membership.getEffectiveDate()).thenReturn(new DateTime().toDate());
        when(membership.getExpirationDate()).thenReturn(new DateTime().plusDays(1).toDate());
        when(membershipReadDao.createQueryCriteria(null, 42L, "effectiveDate", false, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(membershipReadDao.getMemberships(queryCriteria)).thenReturn(Lists.newArrayList(existingMembership));
        when(membershipReadDao.loadBySupplierId(42L)).thenReturn(existingMembership);

        validator.validate(membership);
    }

    @Test
    public void throwValidationExceptionWhenMembershipExpirationDateValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Membership.EXPIRATION_DATE_SERIAL_PROPERTY,
                Validatable.EXPIRATION_DATE_INVALID_RANGE));

        Membership existingMembership = new Membership(42L, null, new MembershipLevel(1), null, null, null,
                new DateTime().minusDays(7).toDate(), new DateTime().plusDays(7).toDate(), null, null, null, null,
                null, null, null);

        ValidationError validationError = new ValidationError();

        when(membership.getApprovalStatus()).thenReturn(ApprovalStatus.PROVISIONAL);
        when(membership.getLevel()).thenReturn(new MembershipLevel(1));
        when(membership.validate()).thenReturn(validationError);
        when(membership.getSupplierId()).thenReturn(42L);
        when(membership.getEffectiveDate()).thenReturn(new DateTime().toDate());
        when(membership.getExpirationDate()).thenReturn(new DateTime().plusDays(1).toDate());
        when(membershipReadDao.createQueryCriteria(null, 42L, "effectiveDate", false, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(membershipReadDao.getMemberships(queryCriteria)).thenReturn(Lists.newArrayList(existingMembership));
        when(membershipReadDao.loadBySupplierId(42L)).thenReturn(existingMembership);

        validator.validate(membership);
    }
}
