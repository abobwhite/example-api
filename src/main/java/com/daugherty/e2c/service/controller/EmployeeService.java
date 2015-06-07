package com.daugherty.e2c.service.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.EmployeeAccessor;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.EmployeeFilter;
import com.daugherty.e2c.business.mapper.EmployeeMapper;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonEmployee;
import com.daugherty.e2c.service.json.JsonEmployees;

/**
 * REST resource for Anonymous Users.
 */
@Controller
@RequestMapping("/employees")
public class EmployeeService {

    @Inject
    private EmployeeAccessor employeeAccessor;
    @Inject
    private EmployeeMapper employeeMapper;
    @Inject
    private Mutator<Employee> employeeMutator;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonEmployee findEmployee(@PathVariable String id, Locale locale) {
        return employeeMapper.fromDomainObject(employeeAccessor.load(id, locale), locale);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonEmployees retrieveEmployees(
            @RequestParam(value = EmployeeFilter.USERNAME, required = false) String username,
            @RequestParam(value = EmployeeFilter.EMAIL, required = false) String emailAddress,
            @RequestParam(value = BaseFilter.SORT_BY, required = false, defaultValue = EmployeeFilter.USERNAME) String sortBy,
            @RequestParam(value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "200") Integer count, Locale locale) {

        EmployeeFilter filter = new EmployeeFilter(username, emailAddress, sortBy, sortDesc, startItem, count, locale);
        List<Employee> employees = employeeAccessor.find(filter);

        return new JsonEmployees(employees, employeeMapper);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(Role.ADMIN)
    @ResponseBody
    public JsonEmployee createEmployee(@RequestBody JsonEmployee jsonEmployee, Locale locale) {

        return employeeMapper.fromDomainObject(
                employeeMutator.create(employeeMapper.toNewDomainObject(jsonEmployee, locale)), locale);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(Role.ADMIN)
    @ResponseBody
    public JsonEmployee updateEmployee(@PathVariable String id, @RequestBody JsonEmployee jsonEmployee, Locale locale) {

        return employeeMapper.fromDomainObject(
                employeeMutator.update(employeeMapper.toExistingDomainObject(id, jsonEmployee, locale)), locale);
    }
}
