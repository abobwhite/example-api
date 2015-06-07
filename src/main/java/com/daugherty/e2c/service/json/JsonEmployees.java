package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.mapper.EmployeeMapper;
import com.daugherty.e2c.domain.Employee;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of JsonEmployee objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("employees")
public class JsonEmployees extends ArrayList<JsonEmployee> {

    public JsonEmployees(List<Employee> employees, EmployeeMapper employeeMapper) {
        for (Employee employee : employees) {
            add(employeeMapper.fromDomainObject(employee, Locale.ENGLISH));
        }
    }
}
