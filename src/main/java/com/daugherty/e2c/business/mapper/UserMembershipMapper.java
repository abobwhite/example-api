package com.daugherty.e2c.business.mapper;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.UserMembership;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonUserMembership;

@Component
public class UserMembershipMapper {
    @Inject
    private Hashids hashids;
    
    public JsonUserMembership fromDomainObject(UserMembership userMembership){
        String publicSupplierId = hashids.encode(userMembership.getSupplierId());
        return new JsonUserMembership(userMembership, publicSupplierId);
    }
}
