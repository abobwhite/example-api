package com.daugherty.e2c.business.search;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.MembershipLevelAware;

@Component("membershipLevelPrioritizer")
public class MembershipLevelPrioritizer {

    @Inject
    private MembershipLevelBucketCreator membershipLevelBucketCreator;
    @Inject
    private MembershipLevelBucketPromoter membershipLevelBucketPromoter;
    @Inject
    private MembershipLevelBucketRandomizer membershipLevelBucketRandomizer;

    public <T extends MembershipLevelAware> List<T> prioritize(List<T> membershipLevelAwares, int relevanceBuckets,
            int relevanceFloor) {
        Map<Integer, MembershipLevelBucket> bucketMap = membershipLevelBucketCreator.createBucketMap(
                membershipLevelAwares, relevanceBuckets, relevanceFloor);
        Map<Integer, MembershipLevelBucket> adjustedBucketMap = membershipLevelBucketPromoter
                .promoteByMembership(bucketMap);
        return membershipLevelBucketRandomizer.randomizeResults(adjustedBucketMap);
    }
}
