package com.daugherty.e2c.persistence.data.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.CollectionUtils;

import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Maps;

/**
 * SQL-based implementation of the QueryCriteria interface. Encapsulates knowledge of database column names within same
 * package as the DAOs.
 */
public class SqlQueryCriteria implements QueryCriteria {

    private final StringBuilder joinClauses;
    private final StringBuilder filterClause;
    private final Map<String, Object> parameterMap;
    private final StringBuilder sortClause;
    private String paginationClause;
    private final boolean filterWillStartWithWhereKeyword;

    public SqlQueryCriteria(Locale locale, boolean filterWillStartWithWhereKeyword) {
        joinClauses = new StringBuilder();
        filterClause = new StringBuilder();
        parameterMap = Maps.newHashMap();
        if (locale != null) {
            parameterMap.put(LANGUAGE_PARAMETER_NAME, locale.getLanguage());
        }
        sortClause = new StringBuilder();
        paginationClause = "";
        this.filterWillStartWithWhereKeyword = filterWillStartWithWhereKeyword;
    }

    SqlQueryCriteria appendInnerJoinWithPaginatedRandomSort(String nestedViewName, String joinColumnName,
            String outerViewName, String conditionColumnName, String parameterName, String operator,
            Boolean parameterValue, Integer startItem, Integer count) {
        joinClauses.append(" INNER JOIN (SELECT ").append(joinColumnName).append(" FROM ").append(nestedViewName)
                .append(" WHERE ").append(conditionColumnName).append(operator).append(":").append(parameterName)
                .append(" ORDER BY RAND()").append(buildPaginationClause(startItem, count)).append(") rand_inner ON ")
                .append(outerViewName).append(".").append(joinColumnName).append(" = rand_inner.")
                .append(joinColumnName);
        parameterMap.put(parameterName, parameterValue);
        return this;
    }

    SqlQueryCriteria appendEqualsSubClause(String columnName, String parameterName, String value) {
        if (StringUtils.isNotBlank(value)) {
            appendSubClause(columnName, parameterName, " = ", value);
        }
        return this;
    }

    SqlQueryCriteria appendEqualsSubClause(String columnName, String parameterName, Long value) {
        if (value != null) {
            appendSubClause(columnName, parameterName, " = ", value);
        }
        return this;
    }

    SqlQueryCriteria appendEqualsSubClause(String columnName, String parameterName, Integer value) {
        if (value != null) {
            appendSubClause(columnName, parameterName, " = ", value);
        }
        return this;
    }

    SqlQueryCriteria appendEqualsSubClause(String columnName, String parameterName, Boolean value) {
        if (value != null) {
            appendSubClause(columnName, parameterName, " = ", value);
        }
        return this;
    }

    SqlQueryCriteria appendGreaterThanSubClause(String columnName, String parameterName, Long value) {
        if (value != null) {
            appendSubClause(columnName, parameterName, " > ", value);
        }
        return this;
    }

    SqlQueryCriteria appendGreaterThanSubClause(String columnName, String parameterName, Integer value) {
        if (value != null) {
            appendSubClause(columnName, parameterName, " > ", value);
        }
        return this;
    }

    SqlQueryCriteria appendGreaterThanOrEqualSubClause(String columnName, String parameterName, Date value) {
        if (value != null) {
            appendSubClause(columnName, parameterName, " >= ", value);
        }
        return this;
    }

    public SqlQueryCriteria appendInClause(String columnName, String parameterName, List<Long> values) {
        if (!CollectionUtils.isEmpty(values)) {
            filterClause.append(getFilterClauseKeyword()).append(columnName).append(" IN ").append("(:")
                    .append(parameterName).append(")");
            parameterMap.put(parameterName, values);
        }

        return this;
    }

    private void appendSubClause(String columnName, String parameterName, String operator, Object parameterValue) {
        filterClause.append(getFilterClauseKeyword()).append(columnName).append(operator).append(":")
                .append(parameterName);
        parameterMap.put(parameterName, parameterValue);
    }

    private String getFilterClauseKeyword() {
        return filterWillStartWithWhereKeyword && filterClause.length() == 0 ? " WHERE " : " AND ";
    }

    SqlQueryCriteria appendLikeSubClause(String columnName, String parameterName, String value) {
        if (StringUtils.isNotBlank(value)) {
            appendSubClause(columnName, parameterName, " LIKE ", "%" + value + "%");
        }
        return this;
    }

    SqlQueryCriteria appendSubQuerySubClause(String outerQueryColumnPrefix, String columnName,
            String subQueryTableName, String subQueryColumnName, String parameterName, Long value) {
        if (value != null) {
            filterClause.append(getFilterClauseKeyword()).append(outerQueryColumn(outerQueryColumnPrefix, columnName))
                    .append(" IN (SELECT ").append(columnName).append(" FROM ").append(subQueryTableName)
                    .append(" WHERE ").append(subQueryColumnName).append(" = ").append(":").append(parameterName)
                    .append(")");
            parameterMap.put(parameterName, value);
        }
        return this;
    }

    private String outerQueryColumn(String outerQueryColumnPrefix, String columnName) {
        if (StringUtils.isNotBlank(outerQueryColumnPrefix)) {
            return outerQueryColumnPrefix + columnName;
        } else {
            return columnName;
        }
    }

    SqlQueryCriteria appendSubQuerySubClause(String outerQueryColumnPrefix, String columnName,
            String subQueryTableName, String subQueryColumnName, String parameterName, List<Long> values) {
        if (values != null && !values.isEmpty()) {
            filterClause.append(getFilterClauseKeyword()).append(outerQueryColumn(outerQueryColumnPrefix, columnName))
                    .append(" IN (SELECT ").append(columnName).append(" FROM ").append(subQueryTableName)
                    .append(" WHERE ").append(subQueryColumnName).append(" IN (").append(":").append(parameterName)
                    .append("))");
            parameterMap.put(parameterName, values);
        }
        return this;
    }

    SqlQueryCriteria append(String value) {
        if (StringUtils.isNotBlank(value)) {
            filterClause.append(getFilterClauseKeyword()).append(value);
        }
        return this;
    }

    SqlQueryCriteria appendRandomSort() {
        sortClause.append(getSortClauseKeyword()).append("RAND()");
        return this;
    }

    SqlQueryCriteria appendSort(String columnName, Boolean sortDescending) {
        if (StringUtils.isNotBlank(columnName)) {
            String sortDirection = Boolean.TRUE.equals(sortDescending) ? " DESC" : " ASC";
            sortClause.append(getSortClauseKeyword()).append(columnName).append(sortDirection);
        }
        return this;
    }

    private String getSortClauseKeyword() {
        return sortClause.length() == 0 ? " ORDER BY " : ", ";
    }

    SqlQueryCriteria paginate(Integer startItem, Integer count) {
        if (isPositive(startItem) && isPositive(count)) {
            paginationClause = buildPaginationClause(startItem, count);
        }
        return this;
    }

    private String buildPaginationClause(Integer startItem, Integer count) {
        return " LIMIT " + count + " OFFSET " + (startItem - 1);
    }

    private boolean isPositive(Integer number) {
        return number != null && number > 0;
    }

    @Override
    public String getCombinedQueryClauses() {
        return joinClauses.toString() + filterClause.toString() + sortClause.toString() + paginationClause;
    }

    @Override
    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    @Override
    public String getLanguage() {
        return parameterMap.containsKey(LANGUAGE_PARAMETER_NAME) ? parameterMap.get(LANGUAGE_PARAMETER_NAME).toString()
                : null;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
        builder.append("joinClauses", joinClauses).append("filterClause", filterClause)
                .append("parameterMap", parameterMap).append("sortClause", sortClause)
                .append("paginationClause", paginationClause);
        return builder.toString();
    }

}
