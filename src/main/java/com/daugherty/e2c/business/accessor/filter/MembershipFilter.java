package com.daugherty.e2c.business.accessor.filter;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.domain.Membership;

/**
 * Filter criteria for Membership retrieval operations.
 */
public class MembershipFilter extends BaseFilter<Membership> {
    public static final String MEMBERSHIP_IDS = "membership_ids";
    public static final String PUBLIC_PARTY_ID = "public_party_id";

    public MembershipFilter(List<Long> membershipIds, String publicPartyId, String sortBy, Boolean sortDescending, Integer startItem,
            Integer count) {
        super(sortBy, sortDescending, startItem, count, Locale.ENGLISH);
        addLongListCriterion(MEMBERSHIP_IDS, membershipIds);
        addStringCriterion(PUBLIC_PARTY_ID, publicPartyId);
    }

}
