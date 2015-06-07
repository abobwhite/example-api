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

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.PayPalLinkBuilder;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.MembershipFilter;
import com.daugherty.e2c.business.mapper.MembershipMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonMembership;
import com.daugherty.e2c.service.json.JsonMemberships;
import com.google.common.collect.Lists;

/**
 * REST resource for Memberships.
 */
@Controller
@RequestMapping("/memberships")
public class MembershipService {
    @Inject
    private Accessor<Membership> membershipAccessor;
    @Inject
    private Mutator<Membership> membershipMutator;
    @Inject
    private PayPalLinkBuilder payPalLinkBuilder;
    @Inject
    private MembershipMapper membershipMapper;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonMembership retrieveMembership(@PathVariable Long id, @RequestParam(required = false) Integer membershipLevel, Locale locale) {
        Membership membership = membershipAccessor.load(id, Locale.ENGLISH);

        return membership == null ? null : buildJsonMemberShip(locale, membership);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonMemberships retrieveMemberships(@RequestParam(value = "ids[]", required = false) Long[] membershipIds,
            @RequestParam(value = "partyId", required = false) String publicPartyId, @RequestParam(
                    value = BaseFilter.SORT_BY, required = false, defaultValue = "id") String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDescending, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count) {

        MembershipFilter membershipFilter = new MembershipFilter(
                membershipIds != null ? Lists.newArrayList(membershipIds) : null, publicPartyId, sortBy,
                sortDescending, startItem, count);

        List<Membership> memberships = membershipAccessor.find(membershipFilter);

        return new JsonMemberships(memberships, membershipMapper);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonMembership updateMembership(@PathVariable Long id, @RequestBody JsonMembership jsonMembership, Locale locale) {
        Membership membership = membershipMapper.toExistingDomainObject(id, jsonMembership);

        return buildJsonMemberShip(locale, membershipMutator.update(membership));
    }

    private JsonMembership buildJsonMemberShip(Locale locale, Membership membership) {
        JsonMembership jsonMembership = membershipMapper.fromDomainObject(membership);
        if (ApprovalStatus.PROVISIONAL.equals(membership.getApprovalStatus())) {
            jsonMembership.setPaypalLink(payPalLinkBuilder.build(jsonMembership, locale));
        }
        
        return jsonMembership;
    }
}
