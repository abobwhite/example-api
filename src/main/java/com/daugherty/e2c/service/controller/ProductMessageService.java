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

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.ProductMessageAccessor;
import com.daugherty.e2c.business.mapper.ProductMessageMapper;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonProductMessage;

/**
 * REST resource for Messages.
 */
@Controller
@RequestMapping("/productMessages")
public class ProductMessageService {

    @Inject
    private ProductMessageAccessor messageAccessor;
    @Inject
    private Mutator<ProductMessage> productMessageMutator;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private ProductMessageMapper productMessageMapper;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.ADMIN, Role.BUYER_MODERATOR })
    @ResponseBody
    public JsonProductMessage createMessage(@RequestBody JsonProductMessage jsonMessage, Locale locale) {
        ProductMessage message = productMessageMutator.create(productMessageMapper.toNewDomainObject(jsonMessage, locale));
        return new JsonProductMessage(message, documentUrlFactory, locale);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonProductMessage retrieveMessage(@PathVariable String id, Principal principal, Locale locale) {
        return new JsonProductMessage(messageAccessor.load(id, Locale.ENGLISH), documentUrlFactory, locale);
    }
}
