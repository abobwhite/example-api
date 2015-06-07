package com.daugherty.e2c.service.controller;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.mapper.AnonymousMapper;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonAnonymous;

/**
 * REST resource for Anonymous Users.
 */
@Controller
@RequestMapping("/anonymous")
public class AnonymousService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private Mutator<Anonymous> anonymousMutator;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private AnonymousMapper anonymousMapper;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonAnonymous createAnonymous(@RequestBody JsonAnonymous jsonAnonymous, HttpServletRequest request, Locale locale) {
        Anonymous anonymous = anonymousMutator.create(anonymousMapper.toNewDomainObject(jsonAnonymous, locale));
        return new JsonAnonymous(anonymous, documentUrlFactory, locale);
    }
}
