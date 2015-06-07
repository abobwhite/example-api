package com.daugherty.e2c.service.controller;

import java.security.Principal;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.security.Role;

/**
 * REST resource for authenticated sessions.
 */
@Controller
@RequestMapping("/sessions")
public class SessionService {

    static final String RESTRICTED_VALUE = "(restricted)";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(Role.Spring.IS_AUTHENTICATED_FULLY)
    @ResponseBody
    public UserDetails retrieveAuthenticatedUser(Principal authenticatedUser) {
        logger.debug("Delegating user retrieval to " + userDetailsService.getClass().getSimpleName());
        UserDetails user = userDetailsService.loadUserByUsername(authenticatedUser.getName());
        return sanitizeUser(user);
    }

    private E2CUser sanitizeUser(UserDetails user) {
        E2CUser sanitizedUser = new E2CUser(user.getUsername(), RESTRICTED_VALUE, user.isEnabled(), 0,
                user.getAuthorities(), ((E2CUser) user).getParty(), ((E2CUser) user).isApprovedOnce());

        return sanitizedUser;
    }
}
