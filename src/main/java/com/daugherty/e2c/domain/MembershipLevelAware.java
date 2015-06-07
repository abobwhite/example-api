package com.daugherty.e2c.domain;

public interface MembershipLevelAware {
    static final int DEFAULT_RELEVANCE_BUCKETS = 1;
    static final int DEFAULT_RELEVANCE_FLOOR = 0;

    public Integer getMembershipLevel();

    public Integer getRelevance();
}
