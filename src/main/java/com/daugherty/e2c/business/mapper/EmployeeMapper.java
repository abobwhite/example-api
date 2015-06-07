package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.service.json.JsonEmployee;

@Component
public class EmployeeMapper extends PartyMapper {

    public Employee toNewDomainObject(JsonEmployee json, Locale locale) {
        return new Employee(json.getUsername(), json.getPassword(), json.getRoles(), toContactDomainObject(json, null,
                null, locale));
    }

    public Employee toExistingDomainObject(String publicId, JsonEmployee json, Locale locale) {
        Long partyId = hashids.decode(publicId)[0];
        return new Employee(partyId, publicId, toContactDomainObject(json, null, null, locale),
                ApprovalStatus.findByName(json.getApprovalStatus()), json.getUsername(), json.getLocked(),
                json.getBlocked(), json.getUserId(), json.getRoles(), json.getVersion(), null);
    }

    public JsonEmployee fromDomainObject(Employee employee, Locale locale) {
        return new JsonEmployee(employee, documentUrlFactory, locale);
    }
}
