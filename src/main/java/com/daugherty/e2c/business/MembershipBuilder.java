package com.daugherty.e2c.business;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.SubscriptionType;

@Component
public class MembershipBuilder {
    public Membership build(Membership newMembership, MembershipLevel membershipLevel, Membership existingMembership) {
        SubscriptionType subscriptionType = null;
        if (existingMembership == null) {
            subscriptionType = SubscriptionType.NEW;
        } else if (existingMembership.getLevel().getValue() < membershipLevel.getValue()) {
            subscriptionType = SubscriptionType.UPGRADE;
        } else {
            subscriptionType = SubscriptionType.RENEW;
        }
        
        newMembership.setPurchasePrice(membershipLevel.getPrice());
        newMembership.setEarlyRenewalDiscount(BigDecimal.ZERO);
        newMembership.setUpgradeCredit(BigDecimal.ZERO);
        newMembership.setPurchaseDate(today());
        newMembership.setLevel(membershipLevel);
        
        Date effectiveDate = today();
        if(SubscriptionType.RENEW.equals(subscriptionType)) {
            effectiveDate = getEffectiveDate(existingMembership);
        }
        newMembership.setEffectiveDate(effectiveDate);
        newMembership.setExpirationDate(getExpirationDate(effectiveDate, membershipLevel));

        return newMembership;

    }
    
    private Date today() {
        return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    }
    
    private Date getEffectiveDate(Membership existingMembership) {
        return existingMembership.isExpired() ? 
                DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH) : DateUtils.truncate(DateUtils.addDays(existingMembership.getExpirationDate(), 1), Calendar.DAY_OF_MONTH);
    }

    private Date getExpirationDate(Date effectiveDate, MembershipLevel membershipLevel) {
        return DateUtils.truncate(DateUtils.addMonths(effectiveDate, membershipLevel.getMonthsOfTerm()), Calendar.DAY_OF_MONTH);
    }
}
