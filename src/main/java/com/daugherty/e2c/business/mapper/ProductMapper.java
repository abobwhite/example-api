package com.daugherty.e2c.business.mapper;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonProduct;
import com.daugherty.e2c.service.json.JsonProductImage;
import com.google.common.collect.Lists;

@Component
public class ProductMapper {
    @Inject
    private Hashids hashids;
    @Inject
    private ProductImageMapper productImageMapper;

    public Product toNewDomainObject(JsonProduct json) {
        return new Product(buildProductInformation(json), buildCategories(json.getCategories()));
    }

    public Product toExistingDomainObject(Long id, JsonProduct json, Locale locale) {
        Product product = new Product(id, buildProductInformation(json), buildProductMetadata(json), null,
                ApprovalStatus.findByName(json.getApprovalStatus()), json.getVersion(), null);
        product.setImages(buildImages(json.getImages(), locale));
        return product;
    }

    private ProductInformation buildProductInformation(JsonProduct json) {
        Long supplierId = json.getSupplier() == null ? null : hashids.decode(json.getSupplier().getId())[0];
        ProductInformation productInfo = new ProductInformation(supplierId, json.getName(), json.getDescription(),
                json.getPaymentTerms(), json.getMinimumOrder(), json.getFreightOnBoardPort(),
                json.getFreightOnBoardPrice(), json.getCountry(), json.getLeadTime(), json.getModel(),
                json.getSpecifications());

        productInfo.getSupplier().setPublicId(json.getSupplier() == null ? null : json.getSupplier().getId());
        return productInfo;
    }

    private ProductMetadata buildProductMetadata(JsonProduct json) {
        ProductMetadata metadata = new ProductMetadata(json.getChineseName(), json.getEnglishName(),
                json.getMetaTags(), json.getKeywords(), json.isPublished(), json.isHotProduct(),
                json.isHotProductOverride(), json.getPublicationDate());
        metadata.setCategories(buildCategories(json.getCategories()));
        return metadata;
    }

    private List<ProductImage> buildImages(List<JsonProductImage> images, Locale locale) {
        List<ProductImage> entityImages = Lists.newArrayList();
        for (JsonProductImage jsonImage : images) {
            entityImages.add(productImageMapper.toDomainObject(jsonImage, locale));
        }
        return entityImages;
    }

    private List<ProductCategory> buildCategories(List<Long> categories) {
        List<ProductCategory> entityCategories = Lists.newArrayList();
        for (Long categoryId : categories) {
            entityCategories.add(new ProductCategory(categoryId));
        }
        return entityCategories;
    }
}
