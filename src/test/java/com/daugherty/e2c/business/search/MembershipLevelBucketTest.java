package com.daugherty.e2c.business.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.daugherty.e2c.domain.MembershipLevelAware;
import com.daugherty.e2c.domain.ProductSearchResult;
import com.google.common.collect.Lists;

public class MembershipLevelBucketTest {
    MembershipLevelAware startRange = new ProductSearchResult(0);
    MembershipLevelAware inRange = new ProductSearchResult(10);
    MembershipLevelAware endRange = new ProductSearchResult(13);
    MembershipLevelAware outOfRange = new ProductSearchResult(14);
    List<MembershipLevelAware> results = Lists.newArrayList(startRange, inRange, endRange, outOfRange);

    private MembershipLevelBucket bucket = new MembershipLevelBucket(1, 0, 13);

    @Test
    public void addProductsSearchReultsWhenInRange() {
        bucket.addProductSearchResults(results);

        assertTrue(bucket.getContents().contains(startRange));
        assertTrue(bucket.getContents().contains(inRange));
        assertTrue(bucket.getContents().contains(endRange));
        assertFalse(bucket.getContents().contains(outOfRange));
    }
}
