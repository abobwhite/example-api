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
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonMembershipLevel;
import com.daugherty.e2c.service.json.JsonMembershipLevels;

@Controller
@RequestMapping("/membershipLevels")
public class MembershipLevelService {

    @Inject
    private Accessor<MembershipLevel> membershipLevelAccessor;
    @Inject
    private Mutator<MembershipLevel> membershipLevelMutator;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public JsonMembershipLevel retrieveMembershipLevel(@PathVariable Long id) {
        MembershipLevel membershipLevel = membershipLevelAccessor.load(id, Locale.ENGLISH);
        return new JsonMembershipLevel(membershipLevel);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public JsonMembershipLevels retrieveMembershipLevels() {
        List<MembershipLevel> membershipLevels = membershipLevelAccessor.find(null);
        return new JsonMembershipLevels(membershipLevels);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonMembershipLevel createMembershipLevel(@RequestBody JsonMembershipLevel jsonMembershipLevel) {
        MembershipLevel membershipLevel = membershipLevelMutator.create(jsonMembershipLevel.toNewDomainObject());
        return new JsonMembershipLevel(membershipLevel);
    }
}
