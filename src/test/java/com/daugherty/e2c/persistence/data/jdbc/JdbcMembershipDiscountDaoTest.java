package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.google.common.collect.Lists;

public class JdbcMembershipDiscountDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcMembershipDiscountDao jdbcMembershipDiscountDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("supplierMembership.sql");
    }

    @Test
    public void updateMembershipDiscounts() {

        MembershipDiscount membershipDiscount = new MembershipDiscount(new Discount(1L), BigDecimal.valueOf(100));

        jdbcMembershipDiscountDao.updateMembershipDiscounts(777771L, Lists.newArrayList(membershipDiscount));

        Map<String, Object> membershipAuditmembershipDiscountRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_audit_discounts WHERE membership_audit_id = ?", 777771L);
        assertThat((Long) membershipAuditmembershipDiscountRowMap.get("membership_audit_id"), is(777771L));
        assertThat((Long) membershipAuditmembershipDiscountRowMap.get("discounts_id"), is(1L));
        assertThat(((BigDecimal) membershipAuditmembershipDiscountRowMap.get("amount")).longValue(), is(100L));
        assertThat((Date) membershipAuditmembershipDiscountRowMap.get("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

    @Test
    public void loadMembershipDiscountsBySnapshot() {

        List<MembershipDiscount> membershipDiscount = jdbcMembershipDiscountDao.findBySnapshotId(888888L);

        Map<String, Object> membershipAuditmembershipDiscountRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_audit_discounts WHERE membership_audit_id = ?", 888888L);
        assertThat((Long) membershipAuditmembershipDiscountRowMap.get("membership_audit_id"), is(membershipDiscount
                .get(0).getSnapshotId()));
        assertThat((Long) membershipAuditmembershipDiscountRowMap.get("discounts_id"), is(membershipDiscount.get(0)
                .getDiscount().getId()));
        assertThat(((BigDecimal) membershipAuditmembershipDiscountRowMap.get("amount")), is(membershipDiscount.get(0)
                .getAmount()));
    }

}
