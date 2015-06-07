package com.daugherty.e2c.business.validator;

import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.business.AnonymousPartyExistsException;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.persistence.data.PartyReadDao;

@RunWith(MockitoJUnitRunner.class)
public class AnonymousValidatorTest {
    @Mock
    private PartyReadDao partyDao;

    @InjectMocks
    private AnonymousValidator validator = new AnonymousValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void hasEmailAlreadyUsedValidatoinErrorWhenExistingAndNotAnonymous() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Party.EMAIL_SERIAL_PROPERTY,
                Validatable.ANONYMOUS_EMAIL_ADDRESS_IS_ALEADY_USED));

        Contact contact = new Contact(null, null, "First", "Name", "Country", null, "Existing Email Address", null,
                null, null, null, null, null, null);
        Company company = new Company("English Company Name", null, null, null, null, "website@website.com", null,
                null, null, null, null);

        Buyer buyer = new Buyer(null, contact, company);
        Anonymous anonymous = new Anonymous(contact, company);

        when(partyDao.loadByEmailAddress("Existing Email Address")).thenReturn(buyer);

        validator.validate(anonymous);
    }

    @Test
    public void hasAnonymousPartyExistsExceptionWhenExistingAndAnonymous() {
        expectedException.expect(AnonymousPartyExistsException.class);

        Contact contact = new Contact(null, null, "First", "Name", "Country", null, "email@email.com", null, null,
                null, "+12345678", null, null, null);
        Company company = new Company("English Company Name", null, null, null, null, "website@website.com", null,
                null, null, null, null);
        Anonymous anonymous = new Anonymous(contact, company);

        when(partyDao.loadByEmailAddress("email@email.com")).thenReturn(anonymous);

        validator.validate(anonymous);
    }
}
