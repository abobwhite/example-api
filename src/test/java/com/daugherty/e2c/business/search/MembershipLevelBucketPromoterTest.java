package com.daugherty.e2c.business.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.daugherty.e2c.domain.ProductSearchResult;
import com.google.common.collect.Maps;

public class MembershipLevelBucketPromoterTest {
    MembershipLevelBucket bucket1 = new MembershipLevelBucket(1);
    MembershipLevelBucket bucket2 = new MembershipLevelBucket(2);
    MembershipLevelBucket bucket3 = new MembershipLevelBucket(3);
    MembershipLevelBucket bucket4 = new MembershipLevelBucket(4);
    private Map<Integer, MembershipLevelBucket> bucketByIndex = Maps.newTreeMap();

    private MembershipLevelBucketPromoter promoter = new MembershipLevelBucketPromoter();

    @Before
    public void setup() {
        bucketByIndex.put(1, bucket1);
        bucketByIndex.put(2, bucket2);
        bucketByIndex.put(3, bucket3);
        bucketByIndex.put(4, bucket4);
    }

    @Test
    public void membershipLevelOneIsNotPromoted() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Olive Oil", "", "", 0, 1);
        bucket1.getContents().add(productSearchResult);

        promoter.promoteByMembership(bucketByIndex);

        assertTrue(bucket1.getContents().contains(productSearchResult));
        assertFalse(bucket2.getContents().contains(productSearchResult));
        assertFalse(bucket3.getContents().contains(productSearchResult));
        assertFalse(bucket4.getContents().contains(productSearchResult));
    }

    @Test
    public void membershipLevelTwoIsPromotedOneBucket() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Olive Oil", "", "", 0, 2);
        bucket1.getContents().add(productSearchResult);

        promoter.promoteByMembership(bucketByIndex);

        assertFalse(bucket1.getContents().contains(productSearchResult));
        assertTrue(bucket2.getContents().contains(productSearchResult));
        assertFalse(bucket3.getContents().contains(productSearchResult));
        assertFalse(bucket4.getContents().contains(productSearchResult));
    }

    @Test
    public void membershipLevelThreeIsPromotedTwoBuckets() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Olive Oil", "", "", 0, 3);
        bucket1.getContents().add(productSearchResult);

        promoter.promoteByMembership(bucketByIndex);

        assertFalse(bucket1.getContents().contains(productSearchResult));
        assertFalse(bucket2.getContents().contains(productSearchResult));
        assertTrue(bucket3.getContents().contains(productSearchResult));
        assertFalse(bucket4.getContents().contains(productSearchResult));
    }

    @Test
    public void membershipLevelFourIsPromotedThreeBuckets() {
        ProductSearchResult productSearchResult = new ProductSearchResult(1L, 2L, "Olive Oil", "", "", 0, 4);
        bucket1.getContents().add(productSearchResult);

        promoter.promoteByMembership(bucketByIndex);

        assertFalse(bucket1.getContents().contains(productSearchResult));
        assertFalse(bucket2.getContents().contains(productSearchResult));
        assertFalse(bucket3.getContents().contains(productSearchResult));
        assertTrue(bucket4.getContents().contains(productSearchResult));
    }
}
