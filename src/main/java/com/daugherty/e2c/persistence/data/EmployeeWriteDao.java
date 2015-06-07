package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Employee;

public interface EmployeeWriteDao {
    Employee insert(Employee employee);

    Employee update(Employee employee);
}
