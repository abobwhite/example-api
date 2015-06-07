package com.daugherty.e2c.persistence.data;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Employee;
import com.daugherty.persistence.QueryCriteria;

/**
 * Defines database read operations for Employee domain objects.
 */
public interface EmployeeReadDao {

    QueryCriteria createQueryCriteria(String username, String email, String propertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale);

    List<Employee> find(QueryCriteria queryCriteria);

    Employee load(Long employeeId);
}
