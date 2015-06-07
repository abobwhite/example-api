package com.daugherty.e2c.business.validator;

import static org.mockito.Mockito.mock;
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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;

@RunWith(MockitoJUnitRunner.class)
public class BuyerValidatorTest {

    @Mock
    private Buyer buyer;
    @Mock
    private Buyer otherBuyer;
    @Mock
    private Contact contact;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PendingUserReadDao pendingUserReadDao;
    @Mock
    private JdbcPartyDao partyDao;

    @InjectMocks
    private final BuyerValidator validator = new BuyerValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        when(buyer.getId()).thenReturn(1L);
        when(otherBuyer.getId()).thenReturn(2L);
    }

    @Test
    public void doesNotPerformUsernameUniquenessValidationWhenProfileStatusIsNotUnprofiled() {
        when(buyer.getApprovalStatus()).thenReturn(ApprovalStatus.DRAFT);
        when(buyer.validate()).thenReturn(new ValidationError());
        when(buyer.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");

        when(partyDao.loadByEmailAddress("emailAddress")).thenThrow(new EmptyResultDataAccessException("1", 1));

        validator.validate(buyer);

        verifyZeroInteractions(userDetailsService);
    }

    @Test
    public void throwsValidationExceptionWhenBuyerValidationFailed() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("username", Validatable.USER_USERNAME_REQUIRED));

        when(partyDao.loadByEmailAddress("emailAddress")).thenReturn(buyer);

        ValidationError errors = new ValidationError();
        errors.add("username", Validatable.USER_USERNAME_REQUIRED);
        when(buyer.validate()).thenReturn(errors);
        when(buyer.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");

        validator.validate(buyer);
    }

    @Test
    public void throwsValidationExceptionWhenUsernameUniquenessValidationFailedForExistingUser() {
        Contact contact = mock(Contact.class);

        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("username", Validatable.USER_USERNAME_NOT_UNIQUE));

        when(partyDao.loadByEmailAddress("emailAddress")).thenReturn(buyer);
        when(buyer.getApprovalStatus()).thenReturn(ApprovalStatus.UNPROFILED);
        when(buyer.getPendingUser()).thenReturn(new PendingUser("Username", null, null));
        when(buyer.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");
        when(buyer.validate()).thenReturn(new ValidationError());

        validator.validate(buyer);
    }

    @Test
    public void throwsValidationExceptionWhenUsernameUniquenessValidationFailedForPendingUser() {
        Contact contact = mock(Contact.class);

        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError("username", Validatable.USER_USERNAME_NOT_UNIQUE));

        when(partyDao.loadByEmailAddress("emailAddress")).thenReturn(buyer);
        when(buyer.getApprovalStatus()).thenReturn(ApprovalStatus.UNPROFILED);
        when(buyer.getPendingUser()).thenReturn(new PendingUser("Username", null, null));
        when(buyer.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");
        when(buyer.validate()).thenReturn(new ValidationError());

        when(userDetailsService.loadUserByUsername("Username")).thenThrow(new EmptyResultDataAccessException("1", 1));

        validator.validate(buyer);
    }

    @Test
    public void throwsValidationExceptionWhenEmailThrowIncorrectResultSize() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Party.EMAIL_SERIAL_PROPERTY,
                Validatable.PARTY_EMAIL_NOT_UNIQUE));

        when(partyDao.loadByEmailAddress("emailAddress")).thenThrow(new IncorrectResultSizeDataAccessException(1, 2));

        ValidationError errors = new ValidationError();
        when(buyer.validate()).thenReturn(errors);
        when(buyer.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");

        validator.validate(buyer);
    }

    @Test
    public void throwsValidationExceptionWhenEmailAlreadyExistsForADifferentBuyer() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Party.EMAIL_SERIAL_PROPERTY,
                Validatable.PARTY_EMAIL_NOT_UNIQUE));

        when(partyDao.loadByEmailAddress("emailAddress")).thenReturn(otherBuyer);

        ValidationError errors = new ValidationError();
        when(buyer.validate()).thenReturn(errors);
        when(buyer.getContact()).thenReturn(contact);
        when(contact.getEmailAddress()).thenReturn("emailAddress");

        validator.validate(buyer);
    }
}
