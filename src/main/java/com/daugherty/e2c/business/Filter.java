package com.daugherty.e2c.business;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Entity;

/**
 * Encapsulates filter criteria, including paging and sorting, for Accessor find operations.
 */
public interface Filter<E extends Entity> {

    boolean hasNoCriteria();

    String getStringCriterion(String key);

    Integer getIntegerCriterion(String key);

    Boolean getBooleanCriterion(String key);

    Long getLongCriterion(String key);

    Date getDateCriterion(String key);

    List<Long> getLongListCriterion(String key);

    String getSortBy();

    Boolean isSortDescending();

    Integer getStartItem();

    Integer getCount();

    Locale getLocale();

}
