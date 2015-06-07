package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.SubscriptionType;
import com.google.common.collect.Lists;

public class JdbcDiscountSubscriptionTypeDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcDiscountSubscriptionTypeDao discountSubscriptionTypeDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("discounts.sql");
    }

    @Test
    public void insertDiscountSubscriptionType() {

        Long discountsId = 100L;
        SubscriptionType subscriptionType = SubscriptionType.NEW;

        discountSubscriptionTypeDao.insert(discountsId, subscriptionType);

        Map<String, Object> discountSubscriptionTypeRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM discounts_subscription_type WHERE discounts_id = ? AND subscription_type_id = ?",
                discountsId, SubscriptionType.NEW.getId());
        assertThat((Long) discountSubscriptionTypeRowMap.get("discounts_subscription_type_id"), is(notNullValue()));
        assertThat((Long) discountSubscriptionTypeRowMap.get("discounts_id"), is(discountsId));
        assertThat((Long) discountSubscriptionTypeRowMap.get("subscription_type_id"), is(SubscriptionType.NEW.getId()));

    }

    @Test
    public void insertDiscountSubscriptionTypes() {

        Long discountsId = 100L;
        List<SubscriptionType> subscriptionTypes = Lists.newArrayList(SubscriptionType.NEW, SubscriptionType.RENEW);

        discountSubscriptionTypeDao.insert(discountsId, subscriptionTypes);

        List<Map<String, Object>> discountSubscriptionTypeRowMaps = jdbcTemplate.queryForList(
                "SELECT * FROM discounts_subscription_type WHERE discounts_id = ? AND subscription_type_id IN (?, ?)",
                discountsId, SubscriptionType.NEW.getId(), SubscriptionType.RENEW.getId());

        assertThat(discountSubscriptionTypeRowMaps.size(), is(2));
        assertThat((Long) discountSubscriptionTypeRowMaps.get(0).get("discounts_id"), is(discountsId));
        assertThat((Long) discountSubscriptionTypeRowMaps.get(0).get("subscription_type_id"),
                is(SubscriptionType.NEW.getId()));
        assertThat((Long) discountSubscriptionTypeRowMaps.get(1).get("discounts_id"), is(discountsId));
        assertThat((Long) discountSubscriptionTypeRowMaps.get(1).get("subscription_type_id"),
                is(SubscriptionType.RENEW.getId()));

    }

    @Test
    public void deleteDiscountSubscriptionTypes() {

        Long discountsId = 1L;
        List<SubscriptionType> subscriptionTypes = Lists.newArrayList(SubscriptionType.NEW, SubscriptionType.RENEW);

        discountSubscriptionTypeDao.delete(discountsId, subscriptionTypes);

        List<Map<String, Object>> discountSubscriptionTypeRowMaps = jdbcTemplate.queryForList(
                "SELECT * FROM discounts_subscription_type WHERE discounts_id = ?", discountsId);

        assertThat(discountSubscriptionTypeRowMaps.size(), is(1));
        assertThat((Long) discountSubscriptionTypeRowMaps.get(0).get("discounts_id"), is(discountsId));
        assertThat((Long) discountSubscriptionTypeRowMaps.get(0).get("subscription_type_id"),
                is(SubscriptionType.UPGRADE.getId()));

    }

    @Test
    public void findDiscountSubscriptionTypesByDiscountId() {

        Long discountsId = 1L;

        List<SubscriptionType> subscriptionTypes = discountSubscriptionTypeDao.find(discountsId);

        assertThat(subscriptionTypes.size(), is(3));
        assertTrue(subscriptionTypes.contains(SubscriptionType.NEW));
        assertTrue(subscriptionTypes.contains(SubscriptionType.UPGRADE));
        assertTrue(subscriptionTypes.contains(SubscriptionType.RENEW));

    }

}
