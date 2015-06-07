package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.Membership;
import com.google.common.collect.Lists;

public class JdbcProductMembershipDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcProductMembershipDao jdbcProductMembershipDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("product.sql");
    }

    @Test
    public void getMembershipByProductId() {
        Long productId = 22L;

        Membership membership = jdbcProductMembershipDao.findMembershipByProductId(productId);

        assertThat(membership.getId(), is(99911666L));
    }

    @Test
    public void createProductMembership() {
        Long productId = 24L;
        Long membershipId = 99911666L;

        jdbcProductMembershipDao.create(productId, membershipId);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_membership WHERE product_id = ?", productId);
        assertThat((Long) partyAuditStatusRowMap.get("product_id"), is(productId));
        assertThat((Long) partyAuditStatusRowMap.get("membership_id"), is(membershipId));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

}
