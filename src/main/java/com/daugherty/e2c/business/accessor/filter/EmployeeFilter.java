package com.daugherty.e2c.business.accessor.filter;

import java.util.Locale;

import com.daugherty.e2c.domain.Employee;

/**
 * Filter criteria for employee retrieval operations.
 */
public class EmployeeFilter extends BaseFilter<Employee> {

    public static final String USERNAME = "username";
    public static final String EMAIL = "emailAddress";

    public EmployeeFilter(String username, String emailAddress, String sortBy, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale) {
        super(sortBy, sortDescending, startItem, count, locale);
        addStringCriterion(USERNAME, username);
        addStringCriterion(EMAIL, emailAddress);
    }
}
