package com.daugherty.e2c.service.json;

import java.security.Principal;
import java.util.Date;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Maps;

@JsonRootName(value = "latestProduct")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonLatestProduct extends JsonProduct {

    public JsonLatestProduct() {
        super();
    }

    public JsonLatestProduct(Product product, DocumentUrlFactory documentUrlFactory, Locale locale, Principal principal) {
        super(product, documentUrlFactory, locale, principal);
    }

    @Override
    public final void setLinks(Long certificationId) {

        Long now = new Date().getTime();

        links = Maps.newHashMap();
        if (certificationId == null || certificationId == 0L) {
            links.put("certification", null);
        } else {
            links.put("certification", "certifications/" + certificationId + "?latest=false&_=" + now);
        }
    }
}
