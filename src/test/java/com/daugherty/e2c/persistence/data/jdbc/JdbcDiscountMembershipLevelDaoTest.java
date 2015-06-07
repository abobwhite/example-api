package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.google.common.collect.Lists;

public class JdbcDiscountMembershipLevelDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcDiscountMembershipLevelDao jdbcDiscountMembershipLevelDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("discounts.sql");
    }

    @Test
    public void insertDiscountMembershipLevel() {

        Long discountsId = 100L;
        Integer level = 1;

        jdbcDiscountMembershipLevelDao.insert(discountsId, level);

        Map<String, Object> discountMembershipLevelRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM discounts_membership_tier WHERE discounts_id = ? AND membership_tier_id = ?",
                discountsId, level);
        assertThat((Long) discountMembershipLevelRowMap.get("discounts_membership_tier_id"), is(notNullValue()));
        assertThat((Long) discountMembershipLevelRowMap.get("discounts_id"), is(discountsId));
        assertThat((Long) discountMembershipLevelRowMap.get("membership_tier_id"), is(level.longValue()));

    }

    @Test
    public void insertDiscountMembershipLevels() {

        Long discountsId = 100L;
        List<Integer> levels = Lists.newArrayList(1, 2, 3);

        jdbcDiscountMembershipLevelDao.insert(discountsId, levels);

        List<Map<String, Object>> discountMembershipLevelRowMaps = jdbcTemplate.queryForList(
                "SELECT * FROM discounts_membership_tier WHERE discounts_id = ? AND membership_tier_id IN (?, ?, ?)",
                discountsId, 1, 2, 3);

        assertThat(discountMembershipLevelRowMaps.size(), is(3));
        assertThat((Long) discountMembershipLevelRowMaps.get(0).get("discounts_id"), is(discountsId));
        assertThat((Long) discountMembershipLevelRowMaps.get(0).get("membership_tier_id"), is(1L));
        assertThat((Long) discountMembershipLevelRowMaps.get(1).get("discounts_id"), is(discountsId));
        assertThat((Long) discountMembershipLevelRowMaps.get(1).get("membership_tier_id"), is(2L));
        assertThat((Long) discountMembershipLevelRowMaps.get(2).get("discounts_id"), is(discountsId));
        assertThat((Long) discountMembershipLevelRowMaps.get(2).get("membership_tier_id"), is(3L));

    }

    @Test
    public void deleteDiscountMembershipLevels() {

        Long discountsId = 1L;
        List<Integer> levels = Lists.newArrayList(1, 2);

        jdbcDiscountMembershipLevelDao.delete(discountsId, levels);

        List<Map<String, Object>> discountMembershipLevelRowMaps = jdbcTemplate.queryForList(
                "SELECT * FROM discounts_membership_tier WHERE discounts_id = ?", discountsId);

        assertThat(discountMembershipLevelRowMaps.size(), is(1));
        assertThat((Long) discountMembershipLevelRowMaps.get(0).get("discounts_id"), is(discountsId));
        assertThat((Long) discountMembershipLevelRowMaps.get(0).get("membership_tier_id"), is(3L));

    }

    @Test
    public void findDiscountMembershipLevelsByDiscountId() {

        Long discountsId = 1L;

        List<Integer> levels = jdbcDiscountMembershipLevelDao.find(discountsId);

        assertThat(levels.size(), is(3));
        assertTrue(levels.contains(1));
        assertTrue(levels.contains(2));
        assertTrue(levels.contains(3));

    }
}
