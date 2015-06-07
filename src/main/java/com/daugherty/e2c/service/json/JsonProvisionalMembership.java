package com.daugherty.e2c.service.json;

import com.daugherty.e2c.domain.Membership;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a Membership and Membership Level domain object to/from JSON.
 */
@JsonRootName(value = "provisionalMembership")
public class JsonProvisionalMembership extends JsonMembership {
    public JsonProvisionalMembership() {

    }

    public JsonProvisionalMembership(Membership membership, String publicPartyId) {
        super(membership, publicPartyId);
    }
}
