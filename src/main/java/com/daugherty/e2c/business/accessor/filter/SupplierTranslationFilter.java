package com.daugherty.e2c.business.accessor.filter;

import com.daugherty.e2c.domain.SupplierTranslation;

/**
 *
 */
public class SupplierTranslationFilter extends BaseFilter<SupplierTranslation> {

    public static final String TITLE_PREFIX = "title_prefix";
    public static final String TYPE = "type";
    public static final String TRANSLATED = "translated";

    public SupplierTranslationFilter(String titlePrefix, String translationType, Boolean translated,
            String sortBy, Boolean sortDesc, Integer startItem, Integer count) {
        super(sortBy, sortDesc, startItem, count, null);
        addStringCriterion(TITLE_PREFIX, titlePrefix);
        addStringCriterion(TYPE, translationType);
        addBooleanCriterion(TRANSLATED, translated);
    }
}
