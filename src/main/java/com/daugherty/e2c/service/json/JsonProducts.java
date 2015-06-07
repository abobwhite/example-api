package com.daugherty.e2c.service.json;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Product;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of Products domain objects to/from JSON..
 */
@SuppressWarnings("serial")
@JsonRootName("products")
public class JsonProducts extends ArrayList<JsonProduct> {

    public JsonProducts(List<Product> products, DocumentUrlFactory documentUrlFactory, Locale locale,
            Principal principal) {
        for (Product product : products) {
            add(new JsonProduct(product, documentUrlFactory, locale, principal));
        }
    }
}
