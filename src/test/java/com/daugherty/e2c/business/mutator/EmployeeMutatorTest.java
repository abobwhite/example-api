package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.validator.EmployeeValidator;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.domain.visitor.EmployeeUpdateVisitor;
import com.daugherty.e2c.mail.sender.NewEmployeeMailSender;
import com.daugherty.e2c.persistence.data.EmployeeReadDao;
import com.daugherty.e2c.persistence.data.EmployeeWriteDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeMutatorTest {

    @Mock
    private EmployeeValidator employeeValidator;
    @Mock
    private EmployeeReadDao employeeReadDao;
    @Mock
    private EmployeeWriteDao employeeWriteDao;
    @Mock
    private UserWriteDao userWriteDao;
    @Mock
    private NewEmployeeMailSender newEmployeeMailSender;
    @Mock
    private EmployeeUpdateVisitor employeeUpdateVisitor;

    @InjectMocks
    private EmployeeMutator mutator = new EmployeeMutator();

    @Captor
    private ArgumentCaptor<E2CUser> userCaptor;

    @Before
    public void setUp() {
        Contact contact = new Contact(null, null, "First", "Name", null, null, "email@email.com", null, null, null,
                null, null, null, null);

        Employee employee = new Employee(1L, "publicId", contact, ApprovalStatus.APPROVED, "username", null, null, 2L,
                Lists.newArrayList("role"), 1, null);

        when(employeeReadDao.load(1L)).thenReturn(employee);
    }

    @Test
    public void insertEmployeeCallsDaoInsert() {
        Contact contact = new Contact(null, null, "First", "Name", null, null, "email@email.com", null, null, null,
                null, null, null, null);
        Employee employee = new Employee("username", "password", Lists.newArrayList("role"), contact);

        mutator.create(employee);

        verify(employeeValidator).validate(employee);
        verify(employeeWriteDao).insert(employee);
        verify(userWriteDao).insert(userCaptor.capture());
        verify(newEmployeeMailSender).send(Mockito.any(Employee.class));

        E2CUser user = userCaptor.getValue();
        assertThat(user.getUsername(), is("username"));
    }

    @Test
    public void updateEmployeeCallsDaoUpdate() {
        Contact contact = new Contact(null, null, "First", "Name", null, null, "email@email.com", null, null, null,
                null, null, null, null);
        Employee employee = new Employee(1L, "publicId", contact, ApprovalStatus.APPROVED, "username", null, null, 2L,
                Lists.newArrayList("updatedRole"), 1, null);

        when(employeeWriteDao.update(employee)).thenReturn(employee);

        mutator.update(employee);

        verify(employeeValidator).validate(employee);
        verify(employeeWriteDao).update(employee);
        verify(userWriteDao).deleteUserRole(2L, EmployeeMutator.ROLE_FUNCTION.apply("role"));
        verify(userWriteDao).insertUserRole(2L, EmployeeMutator.ROLE_FUNCTION.apply("updatedRole"));
    }

}
