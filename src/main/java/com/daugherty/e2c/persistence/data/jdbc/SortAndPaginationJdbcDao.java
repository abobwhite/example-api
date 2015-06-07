package com.daugherty.e2c.persistence.data.jdbc;

import java.util.Locale;
import java.util.Map;

import com.daugherty.e2c.persistence.data.SortAndPaginationAware;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Maps;

/**
 * Base class for Spring JDBC-based DAOs that provide sorting and pagination capabilities.
 */
public abstract class SortAndPaginationJdbcDao extends JdbcDao implements SortAndPaginationAware {

    private final Map<String, String> columnsByProperty = Maps.newHashMap();
    private final Map<String, String> translatedColumnsByProperty = Maps.newHashMap();

    public SortAndPaginationJdbcDao() {
        mapDomainObjectPropertiesToColumnNames(columnsByProperty, translatedColumnsByProperty);
    }

    protected abstract void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty);

    @Override
    public QueryCriteria createSortingAndPaginationCriteria(String sortPropertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale) {
        return createSqlQueryCriteria(sortPropertyName, sortDescending, startItem, count, locale);
    }

    protected SqlQueryCriteria createSqlQueryCriteria(String sortPropertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale) {
        SqlQueryCriteria criteria = createSqlQueryCriteria(locale);
        if (SortAndPaginationAware.RANDOM_SORT_PROPERTY.equals(sortPropertyName)) {
            return criteria.appendRandomSort().paginate(startItem, count);
        } else {
            criteria = sortByTranslatedValueIfAvailableThenByEnglishValue(sortPropertyName, sortDescending, startItem,
                    count, locale, criteria);
            return criteria.paginate(startItem, count);
        }
    }

    private SqlQueryCriteria sortByTranslatedValueIfAvailableThenByEnglishValue(String sortPropertyName,
            Boolean sortDescending, Integer startItem, Integer count, Locale locale, SqlQueryCriteria criteria) {
        if (!Locale.ENGLISH.equals(locale)) {
            criteria.appendSort(translatedColumnsByProperty.get(sortPropertyName), sortDescending);
        }
        return criteria.appendSort(columnsByProperty.get(sortPropertyName), sortDescending);
    }

    protected abstract SqlQueryCriteria createSqlQueryCriteria(Locale locale);

}
