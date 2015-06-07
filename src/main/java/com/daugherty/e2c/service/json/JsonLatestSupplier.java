package com.daugherty.e2c.service.json;

import java.security.Principal;
import java.util.Date;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Supplier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Maps;

@JsonRootName(value = "latestSupplier")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonLatestSupplier extends JsonSupplier {
    
    public JsonLatestSupplier() {
    }

    public JsonLatestSupplier(String supplierId) {
        super(supplierId);
    }

    public JsonLatestSupplier(Supplier supplier, DocumentUrlFactory documentUrlFactory, Locale locale) {
        super(supplier, documentUrlFactory, locale);
    }

    public JsonLatestSupplier(Supplier supplier, DocumentUrlFactory documentUrlFactory, Locale locale, Principal principal) {
        super(supplier, documentUrlFactory, locale, principal);
    }
    
    @Override
    protected void setLinks(Long certificationId) {
        Long now = new Date().getTime();

        links = Maps.newHashMap();
        links.put("products", "suppliers/" + getId() + "/products?latest=false&_=" + now);
        if (certificationId == null || certificationId == 0L) {
            links.put("certification", null);
        } else {
            //TODO: How does this effect preview?
            links.put("certification", "certifications/" + certificationId + "?latest=true&_=" + now);
        }
        links.put("messageSummary", "suppliers/" + getId() + "/messageSummary");
    }
}
