package com.daugherty.e2c.business.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.service.json.JsonEmployee;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeMapperTest {
    @InjectMocks
    private EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    public void transformNewTransferObject() {
        JsonEmployee jsonEmployee = new JsonEmployee();
        jsonEmployee.setUsername("bbob");
        jsonEmployee.setPassword("bbobpass");
        jsonEmployee.setFirstName("Bob");
        jsonEmployee.setLastName("White");
        jsonEmployee.setCountry("New Zealand");
        jsonEmployee.setProvince("province");
        jsonEmployee.setEmail("bob.white@quail.com");
        jsonEmployee.setSkypeRefId("skypppee");
        jsonEmployee.setMsnRefId("msn is old");
        jsonEmployee.setIcqRefId("ICQ?");
        jsonEmployee.setBusinessTelephoneNumber("8675309");
        jsonEmployee.setIpAddress("192.94.40.41");
        jsonEmployee.setRegistrationDate(new Date());

        Employee employee = employeeMapper.toNewDomainObject(jsonEmployee, Locale.ENGLISH);
        assertThat(employee.getUsername(), is(jsonEmployee.getUsername()));
        assertThat(employee.getPassword(), is(jsonEmployee.getPassword()));

        Contact contact = employee.getContact();
        assertThat(contact.getFirstName(), is(jsonEmployee.getFirstName()));
        assertThat(contact.getLastName(), is(jsonEmployee.getLastName()));
        assertThat(contact.getCountry(), is(jsonEmployee.getCountry()));
        assertThat(contact.getProvince(), is(jsonEmployee.getProvince()));
        assertThat(contact.getEmailAddress(), is(jsonEmployee.getEmail()));
        assertThat(contact.getSkypeRefId(), is(jsonEmployee.getSkypeRefId()));
        assertThat(contact.getMsnRefId(), is(jsonEmployee.getMsnRefId()));
        assertThat(contact.getIcqRefId(), is(jsonEmployee.getIcqRefId()));
        assertThat(contact.getBusinessTelephoneNumber(), is(jsonEmployee.getBusinessTelephoneNumber()));
        assertThat(contact.getIpAddress(), is(jsonEmployee.getIpAddress()));
        assertThat(contact.getRegistrationDate(), is(jsonEmployee.getRegistrationDate()));
    }
}
