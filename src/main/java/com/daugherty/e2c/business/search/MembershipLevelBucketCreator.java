package com.daugherty.e2c.business.search;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.MembershipLevelAware;
import com.google.common.collect.Maps;

/**
 * Create Products Search Results Buckets for Product Search Results
 */
@Component("membershipLevelBucketCreator")
public class MembershipLevelBucketCreator {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public <T extends MembershipLevelAware> Map<Integer, MembershipLevelBucket> createBucketMap(
            List<T> productSearchResults, int relevanceBuckets, int relevanceFloor) {

        Map<Integer, MembershipLevelBucket> indexToBucketMap = getRelevanceBuckets(productSearchResults,
                relevanceBuckets, relevanceFloor);

        indexToBucketMap.put(relevanceBuckets + 1, new MembershipLevelBucket(relevanceBuckets + 1));
        indexToBucketMap.put(relevanceBuckets + 2, new MembershipLevelBucket(relevanceBuckets + 2));
        indexToBucketMap.put(relevanceBuckets + 3, new MembershipLevelBucket(relevanceBuckets + 3));

        return indexToBucketMap;
    }

    /**
     * Creates Buckets and adds results tif the relevance is winthin buckets start and end range
     * 
     * @param productSearchResults
     */
    protected <T extends MembershipLevelAware> Map<Integer, MembershipLevelBucket> getRelevanceBuckets(
            List<T> productSearchResults, int relevanceBuckets, int relevanceFloor) {
        int ceiling = getCeiling(productSearchResults, relevanceFloor);
        int range = Math.round(((ceiling - relevanceFloor) / relevanceBuckets));

        Map<Integer, MembershipLevelBucket> indexToProductSearchResultBucket = Maps.newTreeMap();

        int startRange = relevanceFloor;
        int endRange = startRange + range;
        for (int bucketCount = 1; bucketCount <= relevanceBuckets; bucketCount++) {
            MembershipLevelBucket bucket = new MembershipLevelBucket(bucketCount, startRange, endRange);
            bucket.addProductSearchResults(productSearchResults);
            indexToProductSearchResultBucket.put(bucketCount, bucket);

            LOG.debug("Created Bucket with Index " + bucketCount + " Start Range " + startRange + " End Range "
                    + endRange + " Bucket Size " + bucket.getContents().size());

            startRange = endRange + 1;
            endRange = startRange + range;
        }
        return indexToProductSearchResultBucket;
    }

    private <T extends MembershipLevelAware> int getCeiling(List<T> productSearchResults, int relevanceFloor) {
        int max = relevanceFloor;

        for (MembershipLevelAware productSearchResult : productSearchResults) {
            if (productSearchResult.getRelevance() > max) {
                max = productSearchResult.getRelevance();
            }
        }

        return max;
    }
}
