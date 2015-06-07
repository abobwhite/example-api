package com.daugherty.e2c.business.search;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.MembershipLevelAware;
import com.google.common.collect.Lists;

/**
 * Randomizes Results By Membership Levels
 */
@Component("membershipLevelBucketRandomizer")
public class MembershipLevelBucketRandomizer {

    public <T extends MembershipLevelAware> List<T> randomizeResults(
            Map<Integer, MembershipLevelBucket> bucketToIndexMap) {
        List<T> results = Lists.newArrayList();

        for (int x = bucketToIndexMap.keySet().size(); x > 0; x--) {
            MembershipLevelBucket bucket = bucketToIndexMap.get(x);
            bucket.randomize();
            results.addAll(bucket.getContents());

        }

        return results;
    }
}
