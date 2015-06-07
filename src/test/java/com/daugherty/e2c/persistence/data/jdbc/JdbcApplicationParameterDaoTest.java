package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.google.common.collect.Lists;

public class JdbcApplicationParameterDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcApplicationParameterDao applicationParameterDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList();
    }

    @Test
    public void loadValueByNameThatExistsReturnsValue() {
        String value = applicationParameterDao
                .loadValueForName(ApplicationParameterReadDao.RECIPIENT_APPROVAL_THRESHOLD_NAME);
        assertThat(value, is("250"));
    }

    @Test
    public void loadAllValues() {
        Map<String, String> values = applicationParameterDao.loadAllValues();
        assertThat(values.get(ApplicationParameterReadDao.RECIPIENT_APPROVAL_THRESHOLD_NAME), is("250"));
        assertThat(values.get(ApplicationParameterReadDao.NAME_ATTRIBUTE_RELEVANCE_WEIGHT), is("5"));
        assertThat(values.get(ApplicationParameterReadDao.KEYWORD_ATTRIBUTE_RELEVANCE_WEIGHT), is("5"));
        assertThat(values.get(ApplicationParameterReadDao.CATEGORY_ATTRIBUTE_RELEVANCE_WEIGHT), is("7"));
        assertThat(values.get(ApplicationParameterReadDao.DESCRIPTION_ATTRIBUTE_RELEVANCE_WEIGHT), is("4"));
        assertThat(values.get(ApplicationParameterReadDao.EXACT_TERM_MATCH_RELEVANCE_DEGREE), is("1.5"));
        assertThat(values.get(ApplicationParameterReadDao.EXACT_WORD_MATCH_RELEVANCE_DEGREE), is("2"));
        assertThat(values.get(ApplicationParameterReadDao.PARTIAL_WORD_MATCH_RELEVANCE_DEGREE), is("1"));
    }

    @Test
    public void updateValue() {

        applicationParameterDao
                .updateValueForName(ApplicationParameterReadDao.RECIPIENT_APPROVAL_THRESHOLD_NAME, "300");

        Map<String, Object> applicationParameterRowMap = jdbcTemplate
                .queryForMap("SELECT * FROM application_parameter WHERE application_parameter_name = 'Recipient Approval Threshold'");

        assertThat((String) applicationParameterRowMap.get(JdbcApplicationParameterDao.VALUE_COLUMN_NAME), is("300"));

    }
}
