package com.daugherty.e2c.business.accessor.filter;

import com.daugherty.e2c.domain.Interaction;

/**
 * Filter criteria for Interaction retrieval operations.
 */
public class InteractionFilter extends BaseFilter<Interaction> {

    public static final String PUBLIC_MESSAGE_ID = "publicMessageId";

    public InteractionFilter(String messageId, String sortBy, Boolean sortDescending, Integer startItem, Integer count) {
        super(sortBy, sortDescending, startItem, count, null);
        addStringCriterion(PUBLIC_MESSAGE_ID, messageId);
    }

}
