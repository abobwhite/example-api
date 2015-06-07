package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.EmployeeFilter;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.persistence.data.EmployeeReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeAccessorTest {

    private Contact contact = new Contact(null, null, "First", "Name", null, null, "email@email.com", null, null, null,
            null, null, null, null);
    private Employee employee = new Employee("username", "password", Lists.newArrayList("role"), contact);

    @Mock
    private EmployeeReadDao employeeReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final EmployeeAccessor accessor = new EmployeeAccessor();

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        when(employeeReadDao.createQueryCriteria("user", "email", "username", true, 26, 50, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        List<Employee> daoEmployees = Lists.newArrayList(employee);

        when(employeeReadDao.find(queryCriteria)).thenReturn(daoEmployees);

        EmployeeFilter filter = new EmployeeFilter("user", "email", "username", true, 26, 50, Locale.ENGLISH);
        List<Employee> employees = accessor.find(filter);

        assertThat(employees, is(daoEmployees));
    }

    @Test
    public void loadCallsDaoLoadMethod() throws Exception {
        when(employeeReadDao.load(421L)).thenReturn(employee);

        Employee persistedEmployee = accessor.load(421L, Locale.ENGLISH);

        assertThat(persistedEmployee, is(persistedEmployee));
    }

}
