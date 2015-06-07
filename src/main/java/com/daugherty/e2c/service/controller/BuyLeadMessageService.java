package com.daugherty.e2c.service.controller;

import java.security.Principal;
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
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.mapper.BuyLeadMessageMapper;
import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonBuyLeadMessage;

/**
 * REST resource for Messages.
 */
@Controller
@RequestMapping("/buyLeadMessages")
public class BuyLeadMessageService {

    @Inject
    private Accessor<BuyLeadMessage> buyLeadMessageAccessor;
    @Inject
    private Mutator<BuyLeadMessage> buyLeadMessageMutator;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private BuyLeadMessageMapper buyLeadMessageMapper;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.ADMIN, Role.BUYER_MODERATOR })
    @ResponseBody
    public JsonBuyLeadMessage createMessage(@RequestBody JsonBuyLeadMessage jsonMessage, Locale locale) {
        BuyLeadMessage message = buyLeadMessageMutator.create(buyLeadMessageMapper.toNewDomainObject(jsonMessage, locale));
        return new JsonBuyLeadMessage(message, documentUrlFactory, locale);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonBuyLeadMessage retrieveMessage(@PathVariable String id, Principal principal, Locale locale) {

        return new JsonBuyLeadMessage(buyLeadMessageAccessor.load(id, Locale.ENGLISH), documentUrlFactory, locale);

    }
}
