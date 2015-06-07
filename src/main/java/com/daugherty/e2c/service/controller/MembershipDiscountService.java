package com.daugherty.e2c.service.controller;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.MembershipFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonMembershipDiscount;

/**
 * REST resource for Membership Discounts
 */
@Controller
public class MembershipDiscountService {
    @Inject
    private MembershipFactory membershipFactory;
    @Inject
    private Accessor<Membership> membershipAccessor;
    @Inject
    private Mutator<Membership> provisionalMembershipMutator;
    
    @RequestMapping(value="/memberships/{membershipId}/discounts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public JsonMembershipDiscount createDiscount(@PathVariable Long membershipId, @RequestBody JsonMembershipDiscount jsonMembershipDiscount) {
        Membership membership = membershipAccessor.load(membershipId, Locale.ENGLISH);
        provisionalMembershipMutator.update(membershipFactory.build(membership, jsonMembershipDiscount.getDiscount().getCode()));
        jsonMembershipDiscount.setId(membership.getMembershipDiscounts().get(0).getId());
        return jsonMembershipDiscount;
    }
    
    @RequestMapping(value="/memberships/{membershipId}/discounts", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public JsonMembershipDiscount deleteDiscount(@PathVariable Long membershipId) {
        Membership membership = membershipAccessor.load(membershipId, Locale.ENGLISH);
        membership.setMembershipDiscounts(new ArrayList<MembershipDiscount>());
        provisionalMembershipMutator.update(membershipFactory.build(membership, null));
        return new JsonMembershipDiscount();
    }
}
