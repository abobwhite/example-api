package com.daugherty.e2c.business.mutator;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.validator.EmployeeValidator;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.domain.visitor.EmployeeUpdateVisitor;
import com.daugherty.e2c.mail.sender.NewEmployeeMailSender;
import com.daugherty.e2c.persistence.data.EmployeeReadDao;
import com.daugherty.e2c.persistence.data.EmployeeWriteDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.daugherty.e2c.security.PHPassHasher;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Mutator for Employee domain objects.
 */
@Service("employeeMutator")
@Transactional
public class EmployeeMutator extends BaseMutator<Employee> {
    public static final Function<String, SimpleGrantedAuthority> ROLE_FUNCTION = new Function<String, SimpleGrantedAuthority>() {
        @Override
        public SimpleGrantedAuthority apply(String role) {
            return new SimpleGrantedAuthority(role);
        }
    };

    @Inject
    private EmployeeValidator employeeValidator;
    @Inject
    private EmployeeReadDao employeeReadDao;
    @Inject
    private EmployeeWriteDao employeeWriteDao;
    @Inject
    private UserWriteDao userWriteDao;
    @Inject
    private NewEmployeeMailSender newEmployeeMailSender;
    @Inject
    private EmployeeUpdateVisitor employeeUpdateVisitor;

    private final PHPassHasher phpassHasher = new PHPassHasher();

    @Override
    public Employee create(Employee employee) {
        employeeValidator.validate(employee);
        Employee persistedEmployee = employeeWriteDao.insert(employee);
        List<SimpleGrantedAuthority> roles = Lists.transform(employee.getRoles(), ROLE_FUNCTION);

        userWriteDao.insert(new E2CUser(employee.getUsername(), phpassHasher.createHash(employee.getPassword()), true,
                0, roles, employee, false));

        newEmployeeMailSender.send(persistedEmployee);

        return persistedEmployee;
    }

    @Override
    public Employee update(Employee employee) {
        employeeValidator.validate(employee);
        employee.visit(employeeUpdateVisitor);

        Employee persistedEmployee = employeeWriteDao.update(employee);
        updateUserRoles(persistedEmployee);

        return employee;
    }

    @Override
    public void delete(Long entityId) {
        throw new UnsupportedOperationException();
    }

    private void updateUserRoles(Employee employee) {
        Employee persistedEmployee = employeeReadDao.load(employee.getId());

        List<String> roles = Lists.newArrayList(employee.getRoles());
        List<String> persistedRoles = persistedEmployee.getRoles();
        List<String> deletes = Lists.newArrayList();
        List<String> adds = Lists.newArrayList();

        for (String role : roles) {
            if (!persistedRoles.contains(role)) {
                adds.add(role);
            }
        }

        for (String persistedRole : persistedRoles) {
            if (!roles.contains(persistedRole)) {
                deletes.add(persistedRole);
            }
        }

        deleteRoles(persistedEmployee.getUserId(), deletes);
        addRoles(persistedEmployee.getUserId(), adds);
    }

    private void deleteRoles(Long userId, List<String> deletes) {
        for (String role : deletes) {
            userWriteDao.deleteUserRole(userId, ROLE_FUNCTION.apply(role));
        }
    }

    private void addRoles(Long userId, List<String> adds) {
        for (String role : adds) {
            userWriteDao.insertUserRole(userId, ROLE_FUNCTION.apply(role));
        }
    }
}
