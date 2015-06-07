package com.daugherty.e2c.service.controller;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/2/14
 * Time: 7:56 AM
 * To change this template use File | Settings | File Templates.
 */

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.UserMembershipFilter;
import com.daugherty.e2c.business.mapper.UserMembershipMapper;
import com.daugherty.e2c.domain.UserMembership;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonUserMembership;
import com.daugherty.e2c.service.json.JsonUserMemberships;

/**
 * REST resource for Memberships.
 */
@Controller
public class UserMembershipService {
    @Inject
    private Accessor<UserMembership> userMembershipAccessor;
    @Inject
    private UserMembershipMapper userMembershipMapper;


    @RequestMapping(value = "userMemberships", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY})
    @ResponseBody
    public JsonUserMemberships retrieveUserMembership(Locale locale,
            @RequestParam(value = UserMembershipFilter.USERNAME_PREFIX, required = false) String username,
            @RequestParam(value = UserMembershipFilter.EMAIL_PREFIX, required = false) String email,
            @RequestParam(value = UserMembershipFilter.COMPANY_NAME_PREFIX, required = false) String companyName,
            @RequestParam(value = UserMembershipFilter.MEMBERSHIP_FILTER, required = false) Integer membershipLevel,
            @RequestParam(value = BaseFilter.SORT_BY, required = false) String sortBy,
            @RequestParam(value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDesc,
            @RequestParam(value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem,
            @RequestParam(value = BaseFilter.COUNT, defaultValue = "1000") Integer count
    ) {
        UserMembershipFilter filter = new UserMembershipFilter(username, email, companyName,
                membershipLevel, sortBy, sortDesc, startItem, count);

        List<UserMembership> memberships = userMembershipAccessor.find(filter);
        JsonUserMemberships jsonUserMemberships = new JsonUserMemberships();
        if (memberships != null && !memberships.isEmpty()) {
            for (UserMembership membership : memberships) {
                jsonUserMemberships.add(userMembershipMapper.fromDomainObject(membership));
            }
        }
        return jsonUserMemberships;
    }

    @RequestMapping(value = "userMemberships/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY})
    @ResponseBody
    public JsonUserMembership retrieveUserMembershipById(@PathVariable Long id, Locale locale) {
        UserMembership membership = userMembershipAccessor.load(id, locale);
        return userMembershipMapper.fromDomainObject(membership);
    }


}
