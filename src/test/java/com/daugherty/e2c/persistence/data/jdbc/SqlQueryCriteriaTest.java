package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.google.common.collect.Lists;

public class SqlQueryCriteriaTest {

    @Test
    public void combinedQueryClausesIsEmptyIfNothingWasAppended() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);
        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void parameterMapIsEmptyIfNothingWasAppended() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);
        assertThat(criteria.getParameterMap(), is(notNullValue()));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void getParameterMapReturnsEmptyMapWhenConstructedWithoutLocale() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);
        assertThat(criteria.getParameterMap(), is(notNullValue()));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void getParameterMapIncludesLangaugeParameterWithAbbreviationOfLocaleUsedInConstructor() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(Locale.CHINESE, false);
        assertThat(criteria.getParameterMap().get(SqlQueryCriteria.LANGUAGE_PARAMETER_NAME).toString(),
                is(Locale.CHINESE.getLanguage()));
    }

    @Test
    public void getLanguageReturnsNullWhenConstructedWithoutLocale() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);
        assertThat(criteria.getLanguage(), is(nullValue()));
    }

    @Test
    public void getLanguageReturnsLanguageOfLocaleUsedInConstructor() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(Locale.CHINESE, false);
        assertThat(criteria.getLanguage(), is(Locale.CHINESE.getLanguage()));
    }

    @Test
    public void appendInnerJoinWithPaginatedRandomSortGeneratesInnerJoinWithBooleanParameterInNestedQuery()
            throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendInnerJoinWithPaginatedRandomSort("nestedViewName", "joinColumnName", "outerViewName",
                "conditionColumn", "parameter", " = ", true, 1, 25);

        assertThat(
                criteria.getCombinedQueryClauses(),
                is(" INNER JOIN (SELECT joinColumnName FROM nestedViewName WHERE conditionColumn = :parameter ORDER BY RAND() LIMIT 25 OFFSET 0) rand_inner ON outerViewName.joinColumnName = rand_inner.joinColumnName"));
        assertThat(criteria.getParameterMap().get("parameter").toString(), is("true"));
    }

    @Test
    public void appendEqualsSubClauseWithNullStringValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", (String) null);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendEqualsSubClauseWithBlankStringValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", "  ");

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendEqualsSubClauseWithStringValueAddsToExistingWhereClauseAsAnd() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", "value");

        assertThat(criteria.getCombinedQueryClauses(), is(" AND column = :parameter"));
    }

    @Test
    public void appendEqualsSubClauseWithFirstStringValueAddsToNewWhereClauseAsWhere() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, true);

        criteria.appendEqualsSubClause("column", "parameter", "value");

        assertThat(criteria.getCombinedQueryClauses(), is(" WHERE column = :parameter"));
    }

    @Test
    public void appendEqualsSubClauseWithSubsequentStringValueAddsToNewWhereClauseAsAnd() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, true);

        criteria.appendEqualsSubClause("column1", "parameter1", "value1");
        criteria.appendEqualsSubClause("column2", "parameter2", "value2");

        assertThat(criteria.getCombinedQueryClauses(), is(" WHERE column1 = :parameter1 AND column2 = :parameter2"));
    }

    @Test
    public void appendEqualsSubClauseWithStringValueAddsToParameterMap() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", "value");

        assertThat(criteria.getParameterMap().get("parameter").toString(), is("value"));
    }

    @Test
    public void appendEqualsSubClauseWithNullLongValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", (Long) null);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendEqualsSubClauseWithLongValueAddsToParameterMap() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", 42L);

        assertThat(criteria.getParameterMap().get("parameter").toString(), is("42"));
    }

    @Test
    public void appendEqualsSubClauseWithNullIntegerValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", (Integer) null);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendEqualsSubClauseWithIntegerValueAddsToParameterMap() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", 42);

        assertThat(criteria.getParameterMap().get("parameter").toString(), is("42"));
    }

    @Test
    public void appendEqualsSubClauseWithNullBooleanValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", (Boolean) null);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendEqualsSubClauseWithBooleanValueAddsToParameterMap() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendEqualsSubClause("column", "parameter", Boolean.TRUE);

        assertThat(criteria.getParameterMap().get("parameter").toString(), is("true"));
    }

    @Test
    public void appendLikeSubClauseWithNullStringValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendLikeSubClause("column", "parameter", null);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendLikeSubClauseWithBlankStringValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendLikeSubClause("column", "parameter", "  ");

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendLikeSubClauseWithStringValueAddsToExistingWhereClauseAsAnd() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendLikeSubClause("column", "parameter", "value");

        assertThat(criteria.getCombinedQueryClauses(), is(" AND column LIKE :parameter"));
    }

    @Test
    public void appendLikeSubClauseWithFirstStringValueAddsToNewWhereClauseAsWhere() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, true);

        criteria.appendLikeSubClause("column", "parameter", "value");

        assertThat(criteria.getCombinedQueryClauses(), is(" WHERE column LIKE :parameter"));
    }

    @Test
    public void appendLikeSubClauseWithSubsequentStringValueAddsToNewWhereClauseAsAnd() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, true);

        criteria.appendLikeSubClause("column1", "parameter1", "value1");
        criteria.appendLikeSubClause("column2", "parameter2", "value2");

        assertThat(criteria.getCombinedQueryClauses(),
                is(" WHERE column1 LIKE :parameter1 AND column2 LIKE :parameter2"));
    }

    @Test
    public void appendLikeSubClauseWithStringValueAddsToParameterMap() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendLikeSubClause("column", "parameter", "value");

        assertThat(criteria.getParameterMap().get("parameter").toString(), is("%value%"));
    }

    @Test
    public void appendSubQuerySubClauseWithNullLongValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter", (Long) null);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendSubQuerySubClauseWithLongValueAddsToExistingWhereClauseAsAnd() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter", 42L);

        assertThat(criteria.getCombinedQueryClauses(),
                is(" AND column IN (SELECT column FROM subQueryTable WHERE subQueryColumn = :parameter)"));
    }

    @Test
    public void appendSubQuerySubClauseWithFirstLongValueAddsToNewWhereClauseAsWhere() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, true);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter", 42L);

        assertThat(criteria.getCombinedQueryClauses(),
                is(" WHERE column IN (SELECT column FROM subQueryTable WHERE subQueryColumn = :parameter)"));
    }

    @Test
    public void appendSubQuerySubClauseWithSubsequentLongValueAddsToNewWhereClauseAsAnd() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, true);

        criteria.appendSubQuerySubClause(null, "column1", "subQueryTable1", "subQueryColumn1", "parameter1", 42L);
        criteria.appendSubQuerySubClause(null, "column2", "subQueryTable2", "subQueryColumn2", "parameter2", 586L);

        assertThat(
                criteria.getCombinedQueryClauses(),
                is(" WHERE column1 IN (SELECT column1 FROM subQueryTable1 WHERE subQueryColumn1 = :parameter1) AND column2 IN (SELECT column2 FROM subQueryTable2 WHERE subQueryColumn2 = :parameter2)"));
    }

    @Test
    public void appendSubQuerySubClauseWithLongValueAndOuterColumnPrefixAddsThatPrefixToOuterQueryClauseOnly()
            throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause("prefix.", "column", "subQueryTable", "subQueryColumn", "parameter", 42L);

        assertThat(criteria.getCombinedQueryClauses(),
                is(" AND prefix.column IN (SELECT column FROM subQueryTable WHERE subQueryColumn = :parameter)"));
    }

    @Test
    public void appendSubQuerySubClauseWithLongValueAddsToParameterMap() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter", 42L);

        assertThat(criteria.getParameterMap().get("parameter").toString(), is("42"));
    }

    @Test
    public void appendSubQuerySubClauseWithNullLongListValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter",
                (List<Long>) null);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendSubQuerySubClauseWithLongListValueAddsToExistingWhereClauseAsAnd() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter",
                Lists.newArrayList(1L, 2L));

        assertThat(criteria.getCombinedQueryClauses(),
                is(" AND column IN (SELECT column FROM subQueryTable WHERE subQueryColumn IN (:parameter))"));
    }

    @Test
    public void appendSubQuerySubClauseWithEmptyLongListValueDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter",
                Lists.<Long> newArrayList());

        assertThat(criteria.getCombinedQueryClauses(), is(""));
        assertThat(criteria.getParameterMap().isEmpty(), is(true));
    }

    @Test
    public void appendSubQuerySubClauseWithFirstLongListValueAddsToNewWhereClauseAsWhere() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, true);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter",
                Lists.newArrayList(1L, 2L));

        assertThat(criteria.getCombinedQueryClauses(),
                is(" WHERE column IN (SELECT column FROM subQueryTable WHERE subQueryColumn IN (:parameter))"));
    }

    @Test
    public void appendSubQuerySubClauseWithSubsequentLongListValueAddsToNewWhereClauseAsAnd() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, true);

        criteria.appendSubQuerySubClause(null, "column1", "subQueryTable1", "subQueryColumn1", "parameter1",
                Lists.newArrayList(1L, 2L));
        criteria.appendSubQuerySubClause(null, "column2", "subQueryTable2", "subQueryColumn2", "parameter2",
                Lists.newArrayList(3L, 4L));

        assertThat(
                criteria.getCombinedQueryClauses(),
                is(" WHERE column1 IN (SELECT column1 FROM subQueryTable1 WHERE subQueryColumn1 IN (:parameter1)) AND column2 IN (SELECT column2 FROM subQueryTable2 WHERE subQueryColumn2 IN (:parameter2))"));
    }

    @Test
    public void appendSubQuerySubClauseWithLongListValueAndOuterColumnPrefixAddsThatPrefixToOuterQueryClauseOnly()
            throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause("prefix.", "column", "subQueryTable", "subQueryColumn", "parameter",
                Lists.newArrayList(1L, 2L));

        assertThat(criteria.getCombinedQueryClauses(),
                is(" AND prefix.column IN (SELECT column FROM subQueryTable WHERE subQueryColumn IN (:parameter))"));
    }

    @Test
    public void appendSubQuerySubClauseWithLongListValueAddsToParameterMap() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSubQuerySubClause(null, "column", "subQueryTable", "subQueryColumn", "parameter",
                Lists.newArrayList(1L, 2L));

        assertThat(criteria.getParameterMap().get("parameter").toString(), is("[1, 2]"));
    }

    @Test
    public void appendRandomSortGeneratesOrderByClauseWhenMainViewParametersDoNotExist() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendRandomSort();

        assertThat(criteria.getCombinedQueryClauses(), is(" ORDER BY RAND()"));
    }

    @Test
    public void appendSortWithColumnNameAndAscendingSortGeneratesOrderByClause() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSort("column", false);

        assertThat(criteria.getCombinedQueryClauses(), is(" ORDER BY column ASC"));
    }

    @Test
    public void appendSortWithColumnNameAndDescendingSortGeneratesOrderByClause() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSort("column", true);

        assertThat(criteria.getCombinedQueryClauses(), is(" ORDER BY column DESC"));
    }

    @Test
    public void appendSortWithColumnNameAndNullSortGeneratesOrderByClauseForAscendingSort() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSort("column", null);

        assertThat(criteria.getCombinedQueryClauses(), is(" ORDER BY column ASC"));
    }

    @Test
    public void appendSortWithNullColumnNameDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSort(null, true);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void appendSortWithBlankColumnNameDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSort("  ", true);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void paginateWithPositiveIntegersGeneratesLimitAndOffsetClauses() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.paginate(1, 25);

        assertThat(criteria.getCombinedQueryClauses(), is(" LIMIT 25 OFFSET 0"));
    }

    @Test
    public void paginateWithZeroOffsetDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.paginate(0, 25);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void paginateWithNegativeOffsetDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.paginate(-1, 25);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void paginateWithNullOffsetDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.paginate(null, 25);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void paginateWithZeroCountDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.paginate(1, 0);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void paginateWithNegativeCountDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.paginate(1, -1);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void paginateWithNullCountDoesNothing() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.paginate(1, null);

        assertThat(criteria.getCombinedQueryClauses(), is(""));
    }

    @Test
    public void appendSortAndPaginateCombine() throws Exception {
        SqlQueryCriteria criteria = new SqlQueryCriteria(null, false);

        criteria.appendSort("column", true);
        criteria.paginate(1, 25);

        assertThat(criteria.getCombinedQueryClauses(), is(" ORDER BY column DESC LIMIT 25 OFFSET 0"));
    }

}
