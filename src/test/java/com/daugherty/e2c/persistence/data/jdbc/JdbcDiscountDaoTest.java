package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class JdbcDiscountDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcDiscountDao discountDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList();
    }

    @Test
    public void findAllIcludesExpiredCoupons() {
        QueryCriteria sortingAndPaginationCriteria = discountDao.createSortingAndPaginationCriteria(null, false, 1, 25,
                Locale.ENGLISH);
        List<Discount> discounts = discountDao.findAllDiscounts(sortingAndPaginationCriteria);

        assertThat(discounts, is(notNullValue()));
        assertThat(discounts.size(), is(4));
        assertDollar1(discounts.get(0));
        assertDollar2(discounts.get(1));
        assertExpired(discounts.get(2));
        assertPercentage(discounts.get(3));

        assertThat(discounts.get(0).getMembershipLevels().size(), is(3));
        assertThat(discounts.get(0).getDiscountTypes().size(), is(3));
    }

    @Test
    public void findByCodeReturnsDiscount() {
        QueryCriteria criteria = discountDao.createDiscountQueryCriteria("DOLLAR1", null, null, null, null, null,
                false, 1, 25, Locale.ENGLISH);
        List<Discount> discounts = discountDao.findDiscounts(criteria);

        assertThat(discounts, is(notNullValue()));
        assertDollar1(discounts.get(0));
    }

    @Test
    public void findByCodeDoesNotReturnExpired() {
        QueryCriteria criteria = discountDao.createDiscountQueryCriteria("EXPIRED", null, null, null, null, null,
                false, 1, 25, Locale.ENGLISH);
        List<Discount> discounts = discountDao.findDiscounts(criteria);

        assertTrue(discounts.isEmpty());
    }

    @Test
    public void findByMembershipLevelReturnsDiscounts() {
        QueryCriteria criteria = discountDao.createDiscountQueryCriteria(null, null, 2, null, null, null, false, 1, 25,
                Locale.ENGLISH);
        List<Discount> discounts = discountDao.findDiscounts(criteria);

        assertThat(discounts, is(notNullValue()));
        assertThat(discounts.size(), is(2));
        assertDollar1(discounts.get(0));
        assertPercentage(discounts.get(1));
    }

    @Test
    public void findBySubscriptionTypeReturnsDiscounts() {
        QueryCriteria criteria = discountDao.createDiscountQueryCriteria(null, null, null,
                SubscriptionType.NEW.getName(), null, null, false, 1, 25, Locale.ENGLISH);
        List<Discount> discounts = discountDao.findDiscounts(criteria);

        assertThat(discounts, is(notNullValue()));
        assertThat(discounts.size(), is(4));
        assertDollar1(discounts.get(0));
        assertDollar2(discounts.get(1));
        assertPercentage(discounts.get(2));
        assertOngoing(discounts.get(3));
    }

    @Test
    public void findRenewalDiscountByMonthsRemaining() {
        assertThat(discountDao.findRenewalDiscountByMonthsRemaining(2, 10).longValue(), is(15L));
        assertThat(discountDao.findRenewalDiscountByMonthsRemaining(3, 8).longValue(), is(10L));
        assertThat(discountDao.findRenewalDiscountByMonthsRemaining(4, 6).longValue(), is(10L));
    }

    @Test
    public void insertDiscount() {

        Discount discount = new Discount(null, "TestCode", DiscountAmountType.PERCENT, BigDecimal.valueOf(20.000),
                "TestCode", Boolean.TRUE, Boolean.TRUE, new Date(), new Date(), Sets.newHashSet(3, 4),
                Sets.newHashSet(SubscriptionType.RENEW));

        Discount createdDiscount = discountDao.insert(discount);
        assertThat(createdDiscount.getId(), is(notNullValue()));

        Map<String, Object> discountRowMap = jdbcTemplate.queryForMap("SELECT * FROM discounts WHERE discounts_id = ?",
                createdDiscount.getId());
        assertThat((Long) discountRowMap.get("discounts_id"), is(createdDiscount.getId()));
        assertThat((String) discountRowMap.get("discount_code"), is(createdDiscount.getCode()));
        assertThat((String) discountRowMap.get("discount_amount_type"), is(createdDiscount.getAmountType().getCode()));
        assertThat(((BigDecimal) discountRowMap.get("amount")).longValue(), is(createdDiscount.getAmount().longValue()));
        assertThat((Boolean) discountRowMap.get("ongoing"), is(createdDiscount.getOngoing()));
        assertThat((Boolean) discountRowMap.get("special"), is(createdDiscount.getSpecial()));
        assertThat((Date) discountRowMap.get("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

    @Test
    public void updateDiscount() {

        Discount discount = new Discount(1L, "TestCode", DiscountAmountType.PERCENT, BigDecimal.valueOf(20.000),
                "TestCode", Boolean.TRUE, Boolean.TRUE, new Date(), new Date(), Sets.newHashSet(1, 2, 3),
                Sets.newHashSet(SubscriptionType.NEW, SubscriptionType.RENEW, SubscriptionType.UPGRADE));

        Discount updatedDiscount = discountDao.update(discount);
        assertThat(updatedDiscount.getId(), is(1L));

        Map<String, Object> discountRowMap = jdbcTemplate.queryForMap("SELECT * FROM discounts WHERE discounts_id = ?",
                updatedDiscount.getId());
        assertThat((Long) discountRowMap.get("discounts_id"), is(updatedDiscount.getId()));
        assertThat((String) discountRowMap.get("discount_code"), is(updatedDiscount.getCode()));
        assertThat((String) discountRowMap.get("discount_amount_type"), is(updatedDiscount.getAmountType().getCode()));
        assertThat(((BigDecimal) discountRowMap.get("amount")).longValue(), is(updatedDiscount.getAmount().longValue()));
        assertThat((Boolean) discountRowMap.get("ongoing"), is(updatedDiscount.getOngoing()));
        assertThat((Boolean) discountRowMap.get("special"), is(updatedDiscount.getSpecial()));
        assertThat((Date) discountRowMap.get("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

    @Test
    public void findByIdReturnsDiscount() {

        Discount discount = discountDao.loadById(1L);

        assertThat(discount, is(notNullValue()));
        assertDollar1(discount);
    }

    public void assertDollar1(Discount discount) {
        assertThat(discount.getId(), is(1L));
        assertThat(discount.getCode(), is("DOLLAR1"));
        assertThat(discount.getAmountType(), is(DiscountAmountType.DOLLAR));
        assertThat(discount.getAmount().longValue(), is(100L));
        assertThat(discount.getDescription(), is("100 Dollars Off Coupon Code"));
    }

    public void assertDollar2(Discount discount) {
        assertThat(discount.getId(), is(2L));
        assertThat(discount.getCode(), is("DOLLAR2"));
        assertThat(discount.getAmountType(), is(DiscountAmountType.DOLLAR));
        assertThat(discount.getAmount().longValue(), is(200L));
        assertThat(discount.getDescription(), is("200 Dollars Off Coupon Code"));
    }

    public void assertExpired(Discount discount) {
        assertThat(discount.getId(), is(3L));
        assertThat(discount.getCode(), is("EXPIRED"));
        assertThat(discount.getAmountType(), is(DiscountAmountType.DOLLAR));
        assertThat(discount.getAmount().longValue(), is(300L));
        assertThat(discount.getDescription(), is("300 Dollars Off Expired Coupon Code"));
    }

    public void assertPercentage(Discount discount) {
        assertThat(discount.getId(), is(4L));
        assertThat(discount.getCode(), is("PERCENT"));
        assertThat(discount.getAmountType(), is(DiscountAmountType.PERCENT));
        assertThat(discount.getAmount().longValue(), is(10L));
        assertThat(discount.getDescription(), is("10 Percent Off Coupon Code"));
    }

    private void assertOngoing(Discount discount) {
        assertThat(discount.getId(), is(5L));
        assertThat(discount.getCode(), is("ONGOING"));
        assertThat(discount.getAmountType(), is(DiscountAmountType.DOLLAR));
        assertThat(discount.getAmount().longValue(), is(1000L));
        assertThat(discount.getDescription(), is("1000 Dollar Off Ongoing Coupon Code"));

    }

}
