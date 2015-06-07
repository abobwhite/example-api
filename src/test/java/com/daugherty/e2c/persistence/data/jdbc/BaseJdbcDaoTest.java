package com.daugherty.e2c.persistence.data.jdbc;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/spring/jdbcDao-test-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public abstract class BaseJdbcDaoTest {

    protected static final int DATA_SQL_PARTY_COUNT = 17;
    protected static final int DATA_SQL_PARTY_AUDIT_COUNT = 17;
    protected static final int DATA_SQL_PRODUCT_COUNT = 3;
    protected static final int DATA_SQL_PRODUCT_AUDIT_COUNT = 2;
    protected static final int DATA_SQL_PA_OR_WFI_COUNT = 2;

    protected JdbcTemplate jdbcTemplate;

    @Inject
    public void setTestDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Before
    public void addTestSpecificData() {
        List<String> sqlScriptFiles = getSqlScriptFiles();
        if (sqlScriptFiles != null) {
            for (String sqlScriptFile : sqlScriptFiles) {
                JdbcTestUtils.executeSqlScript(jdbcTemplate, new EncodedResource(new ClassPathResource("data/"
                        + sqlScriptFile), "UTF-8"), false);
            }
        }
    }

    /**
     * Return a list of filenames (e.g. mydata.sql) containing INSERT statements to setup data for a specific test.
     * <p>
     * The assumption is that all such files are located in src/test/resources/data.
     */
    protected abstract List<String> getSqlScriptFiles();

    /**
     * Returns today's date with no time component.
     */
    protected static Date today() {
        return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    }

}