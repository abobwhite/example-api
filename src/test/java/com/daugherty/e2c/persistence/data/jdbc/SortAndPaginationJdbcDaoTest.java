package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import com.daugherty.e2c.persistence.data.SortAndPaginationAware;
import com.daugherty.persistence.QueryCriteria;

public class SortAndPaginationJdbcDaoTest {

    private final SortAndPaginationJdbcDao dao = new TestSortAndPaginationJdbcDao();

    @Test
    public void createSortingAndPaginationCriteriaOnEnglishColumnWithoutRandomSort() throws Exception {
        QueryCriteria criteria = dao.createSortingAndPaginationCriteria("property", true, 13, 6, Locale.ENGLISH);

        assertThat(criteria.getCombinedQueryClauses(), is(" ORDER BY english DESC LIMIT 6 OFFSET 12"));
    }

    @Test
    public void createSortingAndPaginationCriteriaOnTranslatedColumnWithoutRandomSort() throws Exception {
        QueryCriteria criteria = dao.createSortingAndPaginationCriteria("property", true, 13, 6, Locale.CHINESE);

        assertThat(criteria.getCombinedQueryClauses(), is(" ORDER BY translated DESC, english DESC LIMIT 6 OFFSET 12"));
    }

    @Test
    public void createSortingAndPaginationCriteriWithRandomSort() throws Exception {
        QueryCriteria criteria = dao.createSortingAndPaginationCriteria(SortAndPaginationAware.RANDOM_SORT_PROPERTY,
                true, 13, 6, Locale.CHINESE);

        assertThat(criteria.getCombinedQueryClauses(), is(" ORDER BY RAND() LIMIT 6 OFFSET 12"));
    }

    private class TestSortAndPaginationJdbcDao extends SortAndPaginationJdbcDao {

        @Override
        protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
                Map<String, String> translatedColumnsByProperty) {
            columnsByProperty.put("property", "english");
            translatedColumnsByProperty.put("property", "translated");
        }

        @Override
        protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
            return new SqlQueryCriteria(locale, true);
        }

    }

}
