package com.daugherty.e2c.business.accessor.filter;

import java.util.Date;
import java.util.Map;

import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.ProductCategory;
import com.google.common.collect.Maps;

public class BuyLeadFilter extends BaseFilter<BuyLead> {
	
    public static final String EMAIL_PREFIX = "emailAddress";
    public static final String PRODUCT_CATEGORY = "productCategory";
    public static final String PROVINCE = "province";
    public static final String EFFECTIVE_SINCE = "effectiveSince";
    public static final String INCLUDE_EXPIRED = "includeExpired";
    
    private final Map<String, ProductCategory> categoryCriteria = Maps.newHashMap();
    
    public BuyLeadFilter(String emailPrefix, ProductCategory productCategory, String province, Date effectiveSince, Boolean includeExpired,
            String sortBy, Boolean sortDesc, Integer startItem, Integer count) {
        super(sortBy, sortDesc, startItem, count, null);
        
        addStringCriterion(EMAIL_PREFIX, emailPrefix);
        addProductCategoryCriterion(PRODUCT_CATEGORY, productCategory);
        addStringCriterion(PROVINCE, province);
        addDateCriterion(EFFECTIVE_SINCE, effectiveSince);
        addBooleanCriterion(INCLUDE_EXPIRED, includeExpired);
        
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
