package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.service.json.JsonProductImage;

@Component
public class ProductImageMapper {
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    
    public ProductImage toDomainObject(JsonProductImage json, Locale locale) {
        return new ProductImage(documentUrlFactory.removeDocumentUrl(json.getImageUrl(), locale), json.isPrimary());
    }
}
