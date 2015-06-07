package com.daugherty.e2c.business.mapper;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonProvisionalMembership;

@Component
public class ProvisionalMembershipMapper extends MembershipMapper {
    @Inject
    protected Hashids hashids;
    
    public JsonProvisionalMembership fromDomainObject(Membership membership){
        return new JsonProvisionalMembership(membership, hashids.encode(membership.getSupplierId()));
    }
}

