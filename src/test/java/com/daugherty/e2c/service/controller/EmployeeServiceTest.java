package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.EmployeeAccessor;
import com.daugherty.e2c.business.accessor.filter.EmployeeFilter;
import com.daugherty.e2c.business.mapper.EmployeeMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.service.json.JsonEmployee;
import com.daugherty.e2c.service.json.JsonEmployees;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeAccessor employeeAccessor;
    @Mock
    private Mutator<Employee> employeeMutator;
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private final EmployeeService service = new EmployeeService();

    @Captor
    private ArgumentCaptor<Filter<Employee>> filterCaptor;

    @Test
    public void retrieveUsersWithEmptyArgumentsDelegatesToAccessor() {
        JsonEmployee jsonEmployee = new JsonEmployee();
        jsonEmployee.setUsername("username");
        jsonEmployee.setPassword("password");
        jsonEmployee.setFirstName("first");
        jsonEmployee.setLastName("last");
        jsonEmployee.setEmail("email");

        Employee persistedEmployee = new Employee(42L, "jKNz4P4q", new Contact(jsonEmployee.getFirstName(),
                jsonEmployee.getLastName(), null, null, jsonEmployee.getEmail(), null, null, new Date()),
                ApprovalStatus.APPROVED, "username", false, true, null, Lists.newArrayList("role"), null, null);

        when(employeeAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(persistedEmployee));
        when(employeeMapper.fromDomainObject(persistedEmployee, Locale.ENGLISH)).thenReturn(jsonEmployee);

        JsonEmployees jsonEmployees = service.retrieveEmployees("user", "emailAddress", "username", Boolean.FALSE, 1,
                250, Locale.ENGLISH);

        verify(employeeAccessor).find(filterCaptor.capture());
        Filter<Employee> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(EmployeeFilter.class)));
        assertThat(filter.getStringCriterion(EmployeeFilter.USERNAME), is("user"));
        assertThat(filter.getStringCriterion(EmployeeFilter.EMAIL), is("emailAddress"));
        assertThat(filter.getSortBy(), is("username"));
        assertThat(filter.isSortDescending(), is(false));
        assertThat(filter.getStartItem(), is(1));
        assertThat(filter.getCount(), is(250));

        assertThat(jsonEmployees, is(notNullValue()));
        assertThat(jsonEmployees.size(), is(1));
        assertThat(jsonEmployees.get(0).getFirstName(), is("first"));
        assertThat(jsonEmployees.get(0).getLastName(), is("last"));
    }

    @Test
    public void findEmployeeDelegatesToAccessor() {
        JsonEmployee jsonEmployee = new JsonEmployee();
        jsonEmployee.setUsername("username");
        jsonEmployee.setPassword("password");
        jsonEmployee.setFirstName("first");
        jsonEmployee.setLastName("last");
        jsonEmployee.setEmail("email");

        Employee persistedEmployee = new Employee(42L, "jKNz4P4q", new Contact(jsonEmployee.getFirstName(),
                jsonEmployee.getLastName(), null, null, jsonEmployee.getEmail(), null, null, new Date()),
                ApprovalStatus.APPROVED, "username", false, true, null, Lists.newArrayList("role"), null, null);

        when(employeeAccessor.load("jKNz4P4q", Locale.ENGLISH)).thenReturn(persistedEmployee);
        when(employeeMapper.fromDomainObject(persistedEmployee, Locale.ENGLISH)).thenReturn(jsonEmployee);

        JsonEmployee existingEmployee = service.findEmployee("jKNz4P4q", Locale.ENGLISH);

        assertThat(existingEmployee.getFirstName(), is("first"));
        assertThat(existingEmployee.getLastName(), is("last"));
    }

    @Test
    public void createEmployeeDelegatesToEmployeeMutator() {
        JsonEmployee requestEmployee = new JsonEmployee();
        requestEmployee.setUsername("username");
        requestEmployee.setPassword("password");
        requestEmployee.setFirstName("first");
        requestEmployee.setLastName("last");
        requestEmployee.setEmail("email");

        Employee persistedEmployee = new Employee(42L, "jKNz4P4q", new Contact(requestEmployee.getFirstName(),
                requestEmployee.getLastName(), null, null, requestEmployee.getEmail(), null, null, new Date()),
                ApprovalStatus.APPROVED, "username", false, true, null, Lists.newArrayList("role"), null, null);

        when(employeeMapper.toNewDomainObject(requestEmployee, Locale.ENGLISH)).thenReturn(persistedEmployee);
        when(employeeMutator.create(persistedEmployee)).thenReturn(persistedEmployee);
        when(employeeMapper.fromDomainObject(persistedEmployee, Locale.ENGLISH)).thenReturn(requestEmployee);

        JsonEmployee responseEmployee = service.createEmployee(requestEmployee, Locale.ENGLISH);

        assertThat(responseEmployee.getFirstName(), is("first"));
        assertThat(responseEmployee.getLastName(), is("last"));
        assertThat(responseEmployee.getEmail(), is("email"));
    }

    @Test
    public void updateEmployeeDelegatesToEmployeeMutator() {
        JsonEmployee requestEmployee = new JsonEmployee();
        requestEmployee.setId("publicId");
        requestEmployee.setUsername("username");
        requestEmployee.setPassword("password");
        requestEmployee.setFirstName("first");
        requestEmployee.setLastName("last");
        requestEmployee.setEmail("email");

        Employee persistedEmployee = new Employee(42L, "jKNz4P4q", new Contact(requestEmployee.getFirstName(),
                requestEmployee.getLastName(), null, null, requestEmployee.getEmail(), null, null, new Date()),
                ApprovalStatus.APPROVED, "username", false, true, null, Lists.newArrayList("role"), null, null);

        when(employeeMapper.toExistingDomainObject("publicId", requestEmployee, Locale.ENGLISH)).thenReturn(
                persistedEmployee);
        when(employeeMutator.update(persistedEmployee)).thenReturn(persistedEmployee);
        when(employeeMapper.fromDomainObject(persistedEmployee, Locale.ENGLISH)).thenReturn(requestEmployee);

        JsonEmployee responseEmployee = service.updateEmployee("publicId", requestEmployee, Locale.ENGLISH);

        assertThat(responseEmployee.getFirstName(), is("first"));
        assertThat(responseEmployee.getLastName(), is("last"));
        assertThat(responseEmployee.getEmail(), is("email"));
    }

}
