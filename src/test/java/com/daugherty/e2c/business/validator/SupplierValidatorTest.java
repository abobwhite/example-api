package com.daugherty.e2c.business.validator;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;

@RunWith(MockitoJUnitRunner.class)
public class SupplierValidatorTest {

    @Mock
    private Supplier supplier;
    @Mock
    private Supplier otherSupplier;
    @Mock
    private Contact contact;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PendingUserReadDao pendingUserReadDao;
    @Mock
    private JdbcPartyDao partyDao;

    @InjectMocks
    private final SupplierValidator validator = new SupplierValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        when(supplier.getId()).thenReturn(1L);
        when(otherSupplier.getId()).thenReturn(2L);
    }

    @Test
    public void doesNotPerformUsernameUniquenessValidationWhenProfileStatusIsNotUnprofiled() {
        when(supplier.getApprovalStatus()).thenReturn(ApprovalStatus.DRAFT);
        when(supplier.validate()).thenReturn(new ValidationError());
        when(supplier.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");
        when(partyDao.loadByEmailAddress("emailAddress")).thenThrow(new EmptyResultDataAccessException("1", 1));

        validator.validate(supplier);

        verifyZeroInteractions(userDetailsService);
    }

    @Test
    public void throwsValidationExceptionWhenSupplierValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("username", Validatable.USER_USERNAME_REQUIRED));

        ValidationError errors = new ValidationError();
        errors.add("username", Validatable.USER_USERNAME_REQUIRED);
        when(supplier.validate()).thenReturn(errors);
        when(supplier.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");
        when(partyDao.loadByEmailAddress("emailAddress")).thenReturn(supplier);

        validator.validate(supplier);
    }

    @Test
    public void throwsValidationExceptionWhenUsernameUniquenessValidationFailedForExistingUser() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("username", Validatable.USER_USERNAME_NOT_UNIQUE));

        when(supplier.getApprovalStatus()).thenReturn(ApprovalStatus.UNPROFILED);
        when(supplier.getPendingUser()).thenReturn(new PendingUser("Username", null, null));
        when(supplier.validate()).thenReturn(new ValidationError());
        when(supplier.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");
        when(partyDao.loadByEmailAddress("emailAddress")).thenReturn(supplier);

        validator.validate(supplier);
    }

    @Test
    public void throwsValidationExceptionWhenUsernameUniquenessValidationFailedForPendingUser() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("username", Validatable.USER_USERNAME_NOT_UNIQUE));

        when(supplier.getApprovalStatus()).thenReturn(ApprovalStatus.UNPROFILED);
        when(supplier.getPendingUser()).thenReturn(new PendingUser("Username", null, null));
        when(supplier.validate()).thenReturn(new ValidationError());
        when(supplier.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");
        when(partyDao.loadByEmailAddress("emailAddress")).thenReturn(supplier);
        when(userDetailsService.loadUserByUsername("Username")).thenThrow(new EmptyResultDataAccessException("1", 1));

        validator.validate(supplier);
    }

    @Test
    public void throwsValidationExceptionWhenEmailUniquenessValidationFailedForPendingUser() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Party.EMAIL_SERIAL_PROPERTY,
                Validatable.PARTY_EMAIL_NOT_UNIQUE));

        when(supplier.getApprovalStatus()).thenReturn(ApprovalStatus.UNPROFILED);
        when(supplier.getPendingUser()).thenReturn(new PendingUser("Username", null, null));
        when(supplier.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");
        when(supplier.validate()).thenReturn(new ValidationError());

        when(userDetailsService.loadUserByUsername("Username")).thenThrow(new EmptyResultDataAccessException("1", 1));
        when(pendingUserReadDao.loadByUsername("Username")).thenThrow(new EmptyResultDataAccessException("1", 1));

        when(partyDao.loadByEmailAddress("emailAddress")).thenReturn(otherSupplier);

        validator.validate(supplier);
    }
}
