package com.daugherty.e2c.business.accessor.filter;

import java.util.Locale;

import com.daugherty.e2c.domain.ProductCategory;

/**
 * Filter criteria for ProductCategory retrieval operations.
 */
public class ProductCategoryFilter extends BaseFilter<ProductCategory> {

    public static final String LEVEL2_COUNT = "level2Count";
    public static final String CATEGORY_ID = "categoryId";

    public ProductCategoryFilter(Integer level2Count, Long categoryId, Locale locale) {
        super(ProductCategory.NAME_SERIAL_PROPERTY, false, 1, 5000, locale);
        addIntegerCriterion(LEVEL2_COUNT, level2Count);
        addLongCriterion(CATEGORY_ID, categoryId);
    }

}
