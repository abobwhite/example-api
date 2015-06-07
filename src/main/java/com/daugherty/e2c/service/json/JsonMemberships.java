package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.List;

import com.daugherty.e2c.business.mapper.MembershipMapper;
import com.daugherty.e2c.domain.Membership;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of Json Membership objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("memberships")
public class JsonMemberships extends ArrayList<JsonMembership> {
    public JsonMemberships(List<Membership> memberships, MembershipMapper mapper) {
        for (Membership membership : memberships) {
            add(mapper.fromDomainObject(membership));
        }
    }
}
