package com.daugherty.e2c.business.mapper;

import java.security.Principal;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.service.json.JsonLatestSupplier;

@Component
public class LatestSupplierMapper extends SupplierMapper {
    
    public JsonLatestSupplier fromDomainObject(Supplier supplier, Locale locale, Principal principal){
        return new JsonLatestSupplier(supplier, documentUrlFactory, locale, principal);
    }
    
    public JsonLatestSupplier fromDomainObject(Supplier supplier, Locale locale){
        return new JsonLatestSupplier(supplier, documentUrlFactory, locale);
    }
}
