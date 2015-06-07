package com.daugherty.e2c.business.accessor.filter;

import com.daugherty.e2c.domain.Message;

/**
 * Filter criteria for Message retrieval operations.
 */
public class MessageFilter extends BaseFilter<Message> {

    public static final String RECEIVER_ID = "receiverId";
    public static final String FLAGGED = "flagged";
    public static final String SENDER_ID = "senderId";

    public MessageFilter(String receiverId, Boolean flagged, String senderId, String sortBy, Boolean sortDescending,
            Integer startItem, Integer count) {
        super(sortBy, sortDescending, startItem, count, null);
        addStringCriterion(RECEIVER_ID, receiverId);
        addBooleanCriterion(FLAGGED, flagged);
        addStringCriterion(SENDER_ID, senderId);
    }

}
