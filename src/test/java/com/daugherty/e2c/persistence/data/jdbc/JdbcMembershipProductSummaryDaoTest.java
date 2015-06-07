package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.MembershipProductSummary;
import com.google.common.collect.Lists;

public class JdbcMembershipProductSummaryDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcMembershipProductSummaryDao dao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList();
    }

    @Test
    public void loadByMembershipIdReturnsProductSummary() throws Exception {
        MembershipProductSummary productSummary = dao.loadByMembershipId(2166667L);

        assertThat(productSummary, is(notNullValue()));
        assertThatProductSummaryForMembership2166667MatchesExpectedValues(productSummary);
    }

    @Test
    public void loadByProductIdReturnsProductSummary() throws Exception {
        MembershipProductSummary productSummary = dao.loadByProductId(41L);

        assertThat(productSummary, is(notNullValue()));
        assertThatProductSummaryForMembership2166667MatchesExpectedValues(productSummary);
    }

    private void assertThatProductSummaryForMembership2166667MatchesExpectedValues(
            MembershipProductSummary productSummary) {
        assertThat(productSummary.getId(), is(2166667L));
        assertThat(productSummary.getTotal(), is(1));
        assertThat(productSummary.getPublished(), is(1));
        assertThat(productSummary.getPendingApproval(), is(0));
        assertThat(productSummary.getPendingTranslation(), is(0));
        assertThat(productSummary.getWaitingForInformation(), is(0));
        assertThat(productSummary.getUnpublished(), is(0));
        assertThat(productSummary.getHotProducts(), is(1));
        assertThat(productSummary.getProductsRemaining(), is(0));
    }

}
