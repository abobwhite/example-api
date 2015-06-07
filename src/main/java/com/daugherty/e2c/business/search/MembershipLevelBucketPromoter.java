package com.daugherty.e2c.business.search;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Promote results to different buckets based on membership level
 */
@Component("membershipLevelResultBucketPromoter")
public class MembershipLevelBucketPromoter {
    public Map<Integer, MembershipLevelBucket> promoteByMembership(Map<Integer, MembershipLevelBucket> bucketToIndexMap) {

        for (int x = bucketToIndexMap.keySet().size(); x > 0; x--) {
            MembershipLevelBucket bucket = bucketToIndexMap.get(x);

            if (!bucket.getContents().isEmpty()) {
                for (int i = 3; i > 0; i--) {
                    MembershipLevelBucket higherBucket = bucketToIndexMap.get(bucket.getIndex() + i);
                    bucket.promoteTo(higherBucket);
                }
            }

        }
        return bucketToIndexMap;
    }
}
