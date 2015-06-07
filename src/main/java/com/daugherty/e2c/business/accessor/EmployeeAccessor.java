package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.EmployeeFilter;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.persistence.data.EmployeeReadDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Implementation for Employee Accessor
 */
@Service("employeeAccessor")
public class EmployeeAccessor extends BaseAccessor<Employee> {

    @Inject
    private EmployeeReadDao employeeReadDao;

    @Override
    public List<Employee> find(Filter<Employee> filter) {
        QueryCriteria queryCriteria = employeeReadDao.createQueryCriteria(
                filter.getStringCriterion(EmployeeFilter.USERNAME), filter.getStringCriterion(EmployeeFilter.EMAIL),
                filter.getSortBy(), filter.isSortDescending(), filter.getStartItem(), filter.getCount(),
                filter.getLocale());

        return employeeReadDao.find(queryCriteria);
    }

    @Override
    public Employee load(Long id, Locale locale) {
        return employeeReadDao.load(id);
    }

}
