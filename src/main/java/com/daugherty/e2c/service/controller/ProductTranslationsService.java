package com.daugherty.e2c.service.controller;

import java.util.Locale;
import java.util.Map;

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
import com.daugherty.e2c.business.ProductPublisher;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductTranslation;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonProductTranslation;
import com.google.common.collect.Maps;

/**
 * REST resource for Product Translations.
 */
@Controller
@RequestMapping(value = "/productTranslations")
public class ProductTranslationsService {
    @Inject
    private Accessor<ProductTranslation> productTranslationAccessor;
    @Inject
    private Accessor<Product> latestProductAccessor;
    @Inject
    private Mutator<ProductTranslation> productTranslationMutator;
    @Inject
    private Accessor<SupplierTranslation> supplierTranslationAccessor;
    @Inject
    private ApprovalStateTransitionVisitor approveVisitor;
    @Inject
    private ApprovalStateTransitionVisitor moderateVisitor;
    @Inject
    private Mutator<SupplierTranslation> supplierTranslationMutator;
    @Inject
    private ProductPublisher productPublisher;
    
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public JsonProductTranslation retrieveProductTranslation(@PathVariable Long productId) {
        ProductTranslation productTranslation = productTranslationAccessor.load(productId, Locale.ENGLISH);
        return new JsonProductTranslation(productTranslation, getLinks(productId.toString()));
    }
    
    @RequestMapping(value = "/{productId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public JsonProductTranslation saveProductTranslation(@RequestBody JsonProductTranslation jsonTranslation,
            @PathVariable Long productId) {
        ProductTranslation translation = jsonTranslation.toDomainObject(latestProductAccessor.load(productId,
                Locale.CHINESE));
        ProductTranslation updatedTranslation = productTranslationMutator.update(translation);
        return new JsonProductTranslation(updatedTranslation, getLinks(productId.toString()));
    }
    
    @RequestMapping(value = "/{id}/approved", method = RequestMethod.POST)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public void approveTranslation(@PathVariable Long id) {
        SupplierTranslation supplierTranslation = supplierTranslationAccessor.load(id, Locale.ENGLISH);
        supplierTranslation.visit(approveVisitor);
        supplierTranslationMutator.update(supplierTranslation);

        productPublisher.publish(id);
    }

    @RequestMapping(value = "/{id}/sendToModerator", method = RequestMethod.POST)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public void sendToModerator(@PathVariable Long id) {
        SupplierTranslation supplierTranslation = supplierTranslationAccessor.load(id, Locale.ENGLISH);
        supplierTranslation.visit(moderateVisitor);
        supplierTranslationMutator.update(supplierTranslation);
    }
    
    private Map<String, String> getLinks(String id) {
        Map<String, String> links = Maps.newHashMap();
        links.put("product", "products/" + id + "?latest=true");
        return links;
    }
}
