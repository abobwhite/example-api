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
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.persistence.data.PartyReadDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeValidatorTest {
    @Mock
    private PartyReadDao partyDao;

    @InjectMocks
    private EmployeeValidator validator = new EmployeeValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void hasEmailAlreadyUsedValidatoinErrorWhenExistingAndNotAnonymous() {
        expectedException.expect(ValidationException.class);
        expectedException.expect(E2CMatchers.fieldHasValidationError(Party.EMAIL_SERIAL_PROPERTY,
                Validatable.EMPLOYEE_EMAIL_ADDRESS_IS_ALEADY_USED));

        Contact contact = new Contact(null, null, "First", "Name", "Country", null, "Existing Email Address", null,
                null, null, null, null, null, null);

        Buyer buyer = new Buyer(null, contact, null);
        buyer.setId(1L);
        Employee employee = new Employee("username", "password", Lists.newArrayList("role"), contact);
        employee.setId(2L);

        when(partyDao.loadByEmailAddress("Existing Email Address")).thenReturn(buyer);

        validator.validate(employee);
    }

}
