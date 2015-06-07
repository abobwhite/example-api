package com.daugherty.e2c.service.controller;

import java.security.Principal;
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
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonApprovedProduct;

/**
 * REST resource for Products.
 */
@Controller
public class ApprovedProductService {
    @Inject
    private Accessor<Product> approvedProductAccessor;
    @Inject
    private DocumentUrlFactory documentUrlFactory;

    @RequestMapping(value = "/approvedProducts/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonApprovedProduct retrieveProduct(@PathVariable Long id, @RequestParam(required = false,
            defaultValue = "false") Locale locale, Principal principal) {
        Product product = approvedProductAccessor.load(id, locale);
        return new JsonApprovedProduct(product, documentUrlFactory, locale, principal);
    }

}
