package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.google.common.collect.Lists;

public class JdbcHotProductOverrideDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcHotProductOverrideDao jdbcHotProductOverrideDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("product.sql");
    }

    @Test
    public void addHotProductOverride() {
        jdbcHotProductOverrideDao.addHotProductOverride(41L);

        Map<String, Object> hotProductOverrideMap = jdbcTemplate.queryForMap(
                "SELECT * FROM hot_product_override WHERE product_id = ?", 41L);
        assertThat((Long) hotProductOverrideMap.get("product_id"), is(41L));
    }

    @Test
    public void removeHotProductOverride() {
        expectedException.expect(EmptyResultDataAccessException.class);

        jdbcHotProductOverrideDao.removeHotProductOverride(25L);

        jdbcTemplate.queryForMap("SELECT * FROM hot_product_override WHERE product_id = ?", 25L);
    }

}
