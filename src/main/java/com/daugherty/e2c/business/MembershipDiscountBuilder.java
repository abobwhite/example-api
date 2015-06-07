package com.daugherty.e2c.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.daugherty.e2c.business.accessor.filter.DiscountFilter;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.DiscountReadDao;
import com.daugherty.persistence.QueryCriteria;

@Component
public class MembershipDiscountBuilder {
    @Inject
    protected DiscountReadDao discountReadDao;
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private SubscriptionType subscriptionType;

    public Membership build(Membership newMembership, Membership existingMembership, MembershipLevel membershipLevel,
            String couponCode) {
        if (existingMembership == null) {
            subscriptionType = SubscriptionType.NEW;
        } else if (existingMembership.getLevel().getValue() < membershipLevel.getValue()) {
            subscriptionType = SubscriptionType.UPGRADE;
        } else {
            subscriptionType = SubscriptionType.RENEW;
        }

        if (SubscriptionType.NEW.equals(subscriptionType)) {
            applyCouponDiscount(newMembership, couponCode, membershipLevel, existingMembership);
        } else {
            applyEarlyRenewalDiscount(newMembership, membershipLevel, existingMembership);

            if (SubscriptionType.UPGRADE.equals(subscriptionType)) {
                applyUpgradeCredit(newMembership, existingMembership);
            }

            if (hasExistingOngoingCouponDiscount(membershipLevel, existingMembership)) {
                applyOngoingDiscount(newMembership, membershipLevel, existingMembership);
            } else if (isEligibleForCouponDiscount(couponCode)) {
                applyCouponDiscount(newMembership, couponCode, membershipLevel, existingMembership);
            }
        }

        return newMembership;
    }

    protected void applyOngoingDiscount(Membership newMembership, MembershipLevel membershipLevel,
            Membership existingMembership) {

        BigDecimal newPurchasePrice = existingMembership.getPurchasePrice()
                .add(existingMembership.getEarlyRenewalDiscount()).add(existingMembership.getUpgradeCredit());

        BigDecimal ongoingDiscountPrice = membershipLevel.getPrice().subtract(newPurchasePrice);

        Discount discount = existingMembership.getMembershipDiscounts().get(0).getDiscount();

        newMembership.getMembershipDiscounts().add(new MembershipDiscount(discount, ongoingDiscountPrice));
        newMembership.setPurchasePrice(newPurchasePrice);
    }

    protected void applyCouponDiscount(Membership newMembership, String couponCode, MembershipLevel membershipLevel,
            Membership existingMembership) {
        if (couponCode != null) {
            Discount couponDiscount = getCoupon(couponCode, newMembership);
            if (couponDiscount != null) {
                if (isSpecialCouponAndNotNew(couponDiscount)) {
                    addSpecialCouponDiscount(couponDiscount, newMembership, membershipLevel, existingMembership);
                } else {
                    addCouponDiscount(couponDiscount, newMembership);
                }
            } else {
                ValidationError validationError = new ValidationError();
                validationError.add("code", Validatable.DISCOUNT_INVALID);
                throw new ValidationException(validationError);
            }
        }
    }

    protected void applyEarlyRenewalDiscount(Membership newMembership, MembershipLevel membershipLevel,
            Membership existingMembership) {

        if (!existingMembership.isExpired()) {
            DateTime start = new DateTime(new Date());
            DateTime end = new DateTime(existingMembership.getExpirationDate());
            int months = Months.monthsBetween(start, end).getMonths() + 1;
            try {
                BigDecimal renewalDiscountPercent = discountReadDao.findRenewalDiscountByMonthsRemaining(
                        membershipLevel.getValue(), months);

                if (renewalDiscountPercent != null) {
                    BigDecimal renewalDiscount = (renewalDiscountPercent.divide(BigDecimal.valueOf(100L)))
                            .multiply(newMembership.getPurchasePrice());

                    BigDecimal newPurchasePrice = newMembership.getPurchasePrice().subtract(renewalDiscount);

                    newMembership.setEarlyRenewalDiscount(renewalDiscount);
                    newMembership.setPurchasePrice(newPurchasePrice);
                }

            } catch (EmptyResultDataAccessException e) {
                LOG.info("No Membership Renewal Discount Found");
            }
        }
    }

    protected boolean hasExistingOngoingCouponDiscount(MembershipLevel membershipLevel, Membership existingMembership) {
        boolean ongoing = false;

        for (MembershipDiscount discount : existingMembership.getMembershipDiscounts()) {
            ongoing = discount.getDiscount().getOngoing();
        }

        return ongoing && (membershipLevel.getValue() == existingMembership.getLevel().getValue());
    }

    protected boolean isEligibleForCouponDiscount(String couponCode) {
        return couponCode != null;
    }

    private void addSpecialCouponDiscount(Discount discount, Membership newMembership, MembershipLevel membershipLevel,
            Membership existingMembership) {

        BigDecimal newPurchasePrice = existingMembership.getPurchasePrice()
                .add(existingMembership.getEarlyRenewalDiscount()).add(existingMembership.getUpgradeCredit());

        BigDecimal specialDiscountPrice = membershipLevel.getPrice().subtract(newPurchasePrice);
        BigDecimal additionalDiscount = BigDecimal.ZERO;

        if (discount.getAmount().doubleValue() > 0) {
            if (DiscountAmountType.DOLLAR.equals(discount.getAmountType())) {
                additionalDiscount = discount.getAmount();
            } else {
                BigDecimal percentOff = discount.getAmount();
                additionalDiscount = (percentOff.divide(BigDecimal.valueOf(100L))).multiply(newPurchasePrice);
            }
            newPurchasePrice = newPurchasePrice.subtract(additionalDiscount);
        }

        newMembership.getMembershipDiscounts().add(
                new MembershipDiscount(discount, specialDiscountPrice.add(additionalDiscount)));
        newMembership.setPurchasePrice(newPurchasePrice);
    }

    // TODO: Remove usage of DateMidnight
    private void applyUpgradeCredit(Membership membership, Membership existingMembership) {

        if (!existingMembership.isExpired()) {
            DateMidnight today = new DateMidnight();
            DateMidnight existingStartDate = new DateMidnight(existingMembership.getEffectiveDate());
            DateMidnight existingEndDate = new DateMidnight(existingMembership.getExpirationDate());

            Days days = Days.daysBetween(existingStartDate, existingEndDate);
            Days remainingDays = Days.daysBetween(today, existingEndDate);

            BigDecimal amountPerDay = existingMembership.getPaymentAmount().divide(
                    BigDecimal.valueOf(days.getDays() - 1), 2, RoundingMode.HALF_DOWN);

            BigDecimal upgradeCredit = amountPerDay.multiply(BigDecimal.valueOf(remainingDays.getDays())).doubleValue() < existingMembership
                    .getPaymentAmount().doubleValue() ? amountPerDay.multiply(BigDecimal.valueOf(remainingDays
                    .getDays())) : existingMembership.getPaymentAmount();

            BigDecimal newPurchasePrice = membership.getPurchasePrice().subtract(upgradeCredit);

            membership.setUpgradeCredit(upgradeCredit);
            membership.setPurchasePrice(newPurchasePrice);

        }
    }

    private boolean isSpecialCouponAndNotNew(Discount couponDiscount) {
        return isSpecialCoupon(couponDiscount) && !SubscriptionType.NEW.equals(subscriptionType);
    }

    private Boolean isSpecialCoupon(Discount couponDiscount) {
        return couponDiscount.getSpecial();
    }

    private void addCouponDiscount(Discount discount, Membership membership) {
        BigDecimal discountPrice = BigDecimal.ZERO;
        if (DiscountAmountType.DOLLAR.equals(discount.getAmountType())) {
            discountPrice = discount.getAmount();
        } else {
            BigDecimal percentOff = discount.getAmount();
            discountPrice = (percentOff.divide(BigDecimal.valueOf(100L))).multiply(membership.getPurchasePrice());
        }

        BigDecimal newPurchasePrice = membership.getPurchasePrice().subtract(discountPrice);

        if (newPurchasePrice.doubleValue() < 0) {
            LOG.error("Coupon cannot make purchase price less than or equal to zero. Cannot apply coupon.");
            ValidationError validationError = new ValidationError();
            validationError.add("code", Validatable.DISCOUNT_INVALID);
            throw new ValidationException(validationError);
        }

        membership.getMembershipDiscounts().add(new MembershipDiscount(discount, discountPrice));
        membership.setPurchasePrice(newPurchasePrice);
    }

    private Discount getCoupon(String couponDiscountCode, Membership newMembership) {
        DiscountFilter filter = new DiscountFilter(couponDiscountCode, null, newMembership.getLevel().getValue(),
                subscriptionType, false, null, null, null, null, null);
        QueryCriteria criteria = discountReadDao.createDiscountQueryCriteria(
                filter.getStringCriterion(DiscountFilter.DISCOUNT_CODE),
                filter.getStringCriterion(DiscountFilter.DISCOUNT_TYPE),
                filter.getIntegerCriterion(DiscountFilter.MEMBERSHIP_LEVEL),
                filter.getStringCriterion(DiscountFilter.SUBSCRIPTION_TYPE),
                filter.getBooleanCriterion(DiscountFilter.INCLUDE_EXPIRED), filter.getSortBy(),
                filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());

        List<Discount> discounts = discountReadDao.findDiscounts(criteria);

        if (!discounts.isEmpty() && discounts.size() == 1) {
            return discounts.get(0);
        }

        return null;
    }
}
