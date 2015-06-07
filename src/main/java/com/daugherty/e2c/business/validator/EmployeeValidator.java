package com.daugherty.e2c.business.validator;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.PartyReadDao;

/**
 * Validates the Employee Object
 */
@Component("employeeValidator")
public class EmployeeValidator implements Validator<Employee> {
    @Inject
    private PartyReadDao partyDao;

    @Override
    public void validate(Employee employee) {
        Party party = getExistingParty(employee);

        ValidationError errors = new ValidationError();

        if (party != null && !party.getId().equals(employee.getId())) {
            errors.add(Party.EMAIL_SERIAL_PROPERTY, Validatable.EMPLOYEE_EMAIL_ADDRESS_IS_ALEADY_USED);
        }

        errors.add(employee.validate());

        if (errors.hasErrors()) {
            ValidationException validationException = new ValidationException(errors);
            throw validationException;
        }

    }

    private Party getExistingParty(Employee employee) {
        try {
            return partyDao.loadByEmailAddress(employee.getContact().getEmailAddress());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
