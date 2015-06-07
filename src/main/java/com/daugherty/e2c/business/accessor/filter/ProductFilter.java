package com.daugherty.e2c.business.accessor.filter;

import java.util.Locale;
import java.util.Map;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.google.common.collect.Maps;

/**
 * Filter criteria for published Product retrieval operations.
 */
public class ProductFilter extends BaseFilter<Product> {

    public static final String COUNTRY = "country";
    public static final String CATEGORY = "category";
    public static final String HOT = "hot";
    public static final String BUSINESS_TYPE = "businessType";
    public static final String MEMBERSHIP_LEVEL = "membershipLevel";
    public static final String PUBLIC_SUPPLIER_ID = "publicSupplierId";
    public static final String PRODUCT_NAME = "name";
    public static final String STATUS = "status";
    public static final String PUBLISHED = "published";
    public static final String PRODUCT_IDS = "productIds";

    private final Map<String, ProductCategory> categoryCriteria = Maps.newHashMap();

    public ProductFilter(String country, ProductCategory category, Boolean hot, Long businessTypeId,
            Integer membershipLevel, String productsIds, String publicSupplierId, String productName,
            ApprovalStatus approvalStatus, Boolean published, String sortBy, Boolean sortDescending, Integer startItem,
            Integer count, Locale locale) {
        super(sortBy, sortDescending, startItem, count, locale);
        addStringCriterion(COUNTRY, country);
        addProductCategoryCriterion(CATEGORY, category);
        addBooleanCriterion(HOT, hot);
        addLongCriterion(BUSINESS_TYPE, businessTypeId);
        addIntegerCriterion(MEMBERSHIP_LEVEL, membershipLevel);
        addStringCriterion(PRODUCT_IDS, productsIds);
        addStringCriterion(PUBLIC_SUPPLIER_ID, publicSupplierId);
        addStringCriterion(PRODUCT_NAME, productName);
        addStringCriterion(STATUS, approvalStatus);
        addBooleanCriterion(PUBLISHED, published);
    }

    private void addProductCategoryCriterion(String key, ProductCategory value) {
        if (value != null) {
            categoryCriteria.put(key, value);
        }
    }

    public ProductCategory getProductCategoryCriterion(String key) {
        return categoryCriteria.get(key);
    }

    @Override
    public boolean hasNoCriteria() {
        return super.hasNoCriteria() && categoryCriteria.isEmpty();
    }

}
