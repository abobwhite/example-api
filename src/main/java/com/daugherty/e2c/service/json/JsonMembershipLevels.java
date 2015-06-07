package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.List;

import com.daugherty.e2c.domain.MembershipLevel;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of membership Levels domain objects to/from JSON..
 */
@SuppressWarnings("serial")
@JsonRootName("membershipLevels")
public class JsonMembershipLevels extends ArrayList<JsonMembershipLevel> {

    public JsonMembershipLevels(List<MembershipLevel> membershipLevels) {
        for (MembershipLevel membershipLevel : membershipLevels) {
            add(new JsonMembershipLevel(membershipLevel));
        }
    }
}
