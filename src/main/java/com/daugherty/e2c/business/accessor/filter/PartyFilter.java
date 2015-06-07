package com.daugherty.e2c.business.accessor.filter;

import java.util.Locale;

import com.daugherty.e2c.domain.Party;

/**
 * Filter criteria for published Product retrieval operations.
 */
public class PartyFilter extends BaseFilter<Party> {

    public static final String USERNAME_PREFIX = "username";
    public static final String PARTY_TYPE = "partyType";

    public PartyFilter(String username, String partyType, String sortBy, Boolean sortDescending, Integer startItem,
            Integer count, Locale locale) {
        super(sortBy, sortDescending, startItem, count, locale);
        addStringCriterion(USERNAME_PREFIX, username);
        addStringCriterion(PARTY_TYPE, partyType);
    }
}
