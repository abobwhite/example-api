package com.daugherty.e2c.business.accessor.filter;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Entity;
import com.google.common.collect.Maps;

/**
 * Base class for Filter implementations.
 */
public abstract class BaseFilter<E extends Entity> implements Filter<E> {

    public static final String SORT_BY = "sort_by";
    public static final String SORT_DESC = "sort_desc";
    public static final String START_ITEM = "start_item";
    public static final String COUNT = "count";

    private final Map<String, String> stringCriteria = Maps.newHashMap();
    private final Map<String, Integer> integerCriteria = Maps.newHashMap();
    private final Map<String, Long> longCriteria = Maps.newHashMap();
    private final Map<String, Boolean> booleanCriteria = Maps.newHashMap();
    private final Map<String, Date> dateCriteria = Maps.newHashMap();
    private final Map<String, List<Long>> longListCriteria = Maps.newHashMap();
    private final String sortBy;
    private Boolean sortDescending = Boolean.FALSE;
    private Integer startItem = 1;
    private Integer count = 250;
    private Locale locale = Locale.ENGLISH;

    public BaseFilter(String sortBy, Boolean sortDescending, Integer startItem, Integer count, Locale locale) {
        this.sortBy = sortBy;
        if (sortDescending != null) {
            this.sortDescending = sortDescending;
        }
        if (startItem != null) {
            this.startItem = startItem;
        }
        if (count != null) {
            this.count = count;
        }
        if (locale != null) {
            this.locale = locale;
        }
    }

    protected void addStringCriterion(String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            stringCriteria.put(key, value);
        }
    }

    protected void addStringCriterion(String key, ApprovalStatus value) {
        if (value != null) {
            addStringCriterion(key, value.getName());
        }
    }

    protected void addBooleanCriterion(String key, Boolean value) {
        if (value != null) {
            booleanCriteria.put(key, value);
        }
    }

    protected void addIntegerCriterion(String key, Integer value) {
        if (value != null) {
            integerCriteria.put(key, value);
        }
    }

    protected void addLongCriterion(String key, Long value) {
        if (value != null) {
            longCriteria.put(key, value);
        }
    }

    protected void addDateCriterion(String key, Date value) {
        if (value != null) {
            dateCriteria.put(key, value);
        }
    }

    protected void addLongListCriterion(String key, List<Long> values) {
        if (values != null) {
            longListCriteria.put(key, values);
        }
    }

    @Override
    public boolean hasNoCriteria() {
        return stringCriteria.isEmpty() && integerCriteria.isEmpty() && booleanCriteria.isEmpty()
                && longCriteria.isEmpty() && dateCriteria.isEmpty() && longListCriteria.isEmpty();
    }

    @Override
    public String getStringCriterion(String key) {
        return stringCriteria.get(key);
    }

    @Override
    public Integer getIntegerCriterion(String key) {
        return integerCriteria.get(key);
    }

    @Override
    public Boolean getBooleanCriterion(String key) {
        return booleanCriteria.get(key);
    }

    @Override
    public Long getLongCriterion(String key) {
        return longCriteria.get(key);
    }

    @Override
    public Date getDateCriterion(String key) {
        return dateCriteria.get(key);
    }

    @Override
    public List<Long> getLongListCriterion(String key) {
        return longListCriteria.get(key);
    }

    @Override
    public String getSortBy() {
        return sortBy;
    }

    @Override
    public Boolean isSortDescending() {
        return sortDescending;
    }

    @Override
    public Integer getStartItem() {
        return startItem;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

}
