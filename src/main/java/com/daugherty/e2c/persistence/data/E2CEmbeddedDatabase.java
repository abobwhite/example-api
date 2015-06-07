package com.daugherty.e2c.persistence.data;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.google.common.collect.Lists;

/**
 * Wraps a HSQL Embedded Database
 */
public class E2CEmbeddedDatabase implements DataSource {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;

    public E2CEmbeddedDatabase() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        embeddedDatabase = builder.addScript("schema-tables.sql").addScript("schema-views.sql").build();

        jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        List<String> sqlScriptFiles = Lists.newArrayList("reference-data.sql", "data.sql");

        if (sqlScriptFiles != null) {
            for (String sqlScriptFile : sqlScriptFiles) {
                JdbcTestUtils.executeSqlScript(jdbcTemplate, new EncodedResource(new ClassPathResource(sqlScriptFile),
                        "UTF-8"), false);
            }
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return embeddedDatabase.getLogWriter();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return embeddedDatabase.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return embeddedDatabase.getParentLogger();
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        embeddedDatabase.setLogWriter(printWriter);
    }

    @Override
    public void setLoginTimeout(int timeout) throws SQLException {
        embeddedDatabase.setLoginTimeout(timeout);
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        return embeddedDatabase.isWrapperFor(clazz);
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return embeddedDatabase.unwrap(arg0);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return embeddedDatabase.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return embeddedDatabase.getConnection(username, password);
    }

}
