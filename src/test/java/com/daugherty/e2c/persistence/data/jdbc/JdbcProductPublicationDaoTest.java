package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.google.common.collect.Lists;

public class JdbcProductPublicationDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcProductPublicationDao dao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList();
    }

    @Test
    public void insertPublishedInsertsRecordIntoAssociationTable() {
        Long productId = 41L;
        boolean published = true;

        dao.insertPublished(productId, published);

        Map<String, Object> publishedStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM published_product_status WHERE product_id = ? AND published_product_id NOT IN (61, 62, 71, 72)",
                        productId);
        assertThat((Long) publishedStatusRowMap.get("product_id"), is(productId));
        assertThat((Boolean) publishedStatusRowMap.get("is_published"), is(published));
        assertThat((Date) publishedStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

    @Test
    public void insertHotInsertsRecordIntoAssociationTable() {
        Long productId = 41L;
        boolean hot = false;

        dao.insertHot(productId, hot);

        Map<String, Object> hotStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM hot_product_status WHERE product_id = ? AND hot_product_id NOT IN (51, 52, 61, 62)",
                productId);
        assertThat((Long) hotStatusRowMap.get("product_id"), is(productId));
        assertThat((Boolean) hotStatusRowMap.get("hot_product"), is(hot));
        assertThat((Date) hotStatusRowMap.get("event_datetime"), E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

}
