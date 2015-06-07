package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

public class JdbcBuyLeadDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcBuyLeadDao buyLeadDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("buyLead.sql");
    }

    @Test
    public void insertBuyLead() {
        Party party = new Party(102L);
        ProductCategory productCategory = new ProductCategory(2801L);

        BuyLead buyLead = new BuyLead(party, productCategory, new Date(), new Date());

        BuyLead createdBuyLead = buyLeadDao.insert(buyLead);
        assertThat(createdBuyLead.getId(), is(notNullValue()));

        Map<String, Object> buyLeadRowMap = jdbcTemplate.queryForMap("SELECT * FROM buy_lead WHERE buy_lead_id = ?",
                createdBuyLead.getId());
        assertThat((Long) buyLeadRowMap.get("buy_lead_id"), is(createdBuyLead.getId()));
        assertThat((Long) buyLeadRowMap.get("party_id"), is(createdBuyLead.getRequester().getId()));
        assertThat((Long) buyLeadRowMap.get("product_category_id"), is(createdBuyLead.getProductCategory().getId()));
        assertThat(buyLeadRowMap.get("effective_date"), is(notNullValue()));
        assertThat(buyLeadRowMap.get("expiration_date"), is(notNullValue()));
        assertThat((Date) buyLeadRowMap.get("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

    @Test
    public void findByPartyAndCategory() {
        BuyLead persistedBuyLead = buyLeadDao.findByPartyAndCategory(22L, 2810L);

        assertThat(persistedBuyLead.getId(), is(1L));
        assertThat(persistedBuyLead.getRequester().getId(), is(22L));
        assertThat(persistedBuyLead.getProductCategory().getId(), is(2810L));
    }

    @Test
    public void findByEmailReturnsBuyLeads() {
        QueryCriteria criteria = buyLeadDao.createBuyLeadQueryCriteria("dent@eastindiatea.com", null, null, null,
                false, "effectiveDate", false, 1, 25);
        List<BuyLead> buyLeads = buyLeadDao.findBuyLeads(criteria);

        assertThat(buyLeads, is(notNullValue()));
        assertThat(buyLeads.size(), is(2));

        assertTravelPackageBuyLead(buyLeads.get(0));
        assertResortsBuyLead(buyLeads.get(1));
    }

    @Test
    public void findByCategoryIdsReturnsBuyLeads() {
        QueryCriteria criteria = buyLeadDao.createBuyLeadQueryCriteria(null, Lists.newArrayList(2808L), null, null,
                false, "effectiveDate", false, 1, 25);
        List<BuyLead> buyLeads = buyLeadDao.findBuyLeads(criteria);

        assertThat(buyLeads, is(notNullValue()));
        assertThat(buyLeads.size(), is(1));

        assertResortsBuyLead(buyLeads.get(0));
    }

    @Test
    public void findByProviceAndIncludeExpiredReturnsBuyLeads() {
        QueryCriteria criteria = buyLeadDao.createBuyLeadQueryCriteria(null, null, "Sichuan", null, true,
                "effectiveDate", false, 1, 25);
        List<BuyLead> buyLeads = buyLeadDao.findBuyLeads(criteria);

        assertThat(buyLeads, is(notNullValue()));
        assertThat(buyLeads.size(), is(3));

        assertCruisesBuyLead(buyLeads.get(0));
        assertTravelPackageBuyLead(buyLeads.get(1));
        assertResortsBuyLead(buyLeads.get(2));

    }

    @Test
    public void findSuppliersThatHaveResondedToBuyLeads() {

        ArrayListMultimap<Long, Long> buyLeadForSuppliers = buyLeadDao.findSuppliersThatRespondedToBuyLeads(Lists
                .newArrayList(1L));

        assertThat(buyLeadForSuppliers, is(notNullValue()));
        assertThat(buyLeadForSuppliers.size(), is(1));

        assertThat(buyLeadForSuppliers.get(1L).get(0), is(29L));

    }

    private void assertTravelPackageBuyLead(BuyLead buyLead) {
        assertThat(buyLead.getId(), is(1L));
        assertThat(buyLead.getRequester().getId(), is(22L));
        assertThat(buyLead.getRequester().getContact().getEmailAddress(), is("dent@eastindiatea.com"));
        assertThat(buyLead.getProductCategory().getName(), is("Travel Packages"));
        assertThat(buyLead.isExpired(), is(false));
    }

    private void assertResortsBuyLead(BuyLead buyLead) {
        assertThat(buyLead.getId(), is(2L));
        assertThat(buyLead.getRequester().getId(), is(22L));
        assertThat(buyLead.getRequester().getContact().getEmailAddress(), is("dent@eastindiatea.com"));
        assertThat(buyLead.getProductCategory().getName(), is("Resorts"));
        assertThat(buyLead.isExpired(), is(false));
    }

    private void assertCruisesBuyLead(BuyLead buyLead) {
        assertThat(buyLead.getId(), is(3L));
        assertThat(buyLead.getRequester().getId(), is(22L));
        assertThat(buyLead.getRequester().getContact().getEmailAddress(), is("dent@eastindiatea.com"));
        assertThat(buyLead.getProductCategory().getName(), is("Cruises"));
        assertThat(buyLead.isExpired(), is(true));
    }
}
