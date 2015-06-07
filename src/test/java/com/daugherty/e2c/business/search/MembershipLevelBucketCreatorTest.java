package com.daugherty.e2c.business.search;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.daugherty.e2c.domain.MembershipLevelAware;
import com.daugherty.e2c.domain.ProductSearchResult;

public class MembershipLevelBucketCreatorTest {
    MembershipLevelAware rel23 = new ProductSearchResult(23);
    MembershipLevelAware rel14 = new ProductSearchResult(14);
    MembershipLevelAware rel47 = new ProductSearchResult(47);
    MembershipLevelAware rel99 = new ProductSearchResult(99);
    MembershipLevelAware rel3 = new ProductSearchResult(3);
    MembershipLevelAware rel16 = new ProductSearchResult(16);
    MembershipLevelAware rel75 = new ProductSearchResult(75);
    List<MembershipLevelAware> productSearchResults;

    private MembershipLevelBucketCreator creator = new MembershipLevelBucketCreator();

    @Before
    public void setup() {
        productSearchResults = new ArrayList<MembershipLevelAware>();
        productSearchResults.add(rel14);
        productSearchResults.add(rel23);
        productSearchResults.add(rel99);
        productSearchResults.add(rel16);
        productSearchResults.add(rel3);
        productSearchResults.add(rel47);
        productSearchResults.add(rel75);
    }

    @Test
    public void resultsAreAddedToCorrectBuckets() {
        Map<Integer, MembershipLevelBucket> indexToBucketMap = creator.createBucketMap(productSearchResults, 7, 0);

        MembershipLevelBucket bucket1 = indexToBucketMap.get(1);
        MembershipLevelBucket bucket2 = indexToBucketMap.get(2);
        MembershipLevelBucket bucket3 = indexToBucketMap.get(3);
        MembershipLevelBucket bucket4 = indexToBucketMap.get(4);
        MembershipLevelBucket bucket5 = indexToBucketMap.get(5);
        MembershipLevelBucket bucket6 = indexToBucketMap.get(6);
        MembershipLevelBucket bucket7 = indexToBucketMap.get(7);
        MembershipLevelBucket bucket8 = indexToBucketMap.get(8);
        MembershipLevelBucket bucket9 = indexToBucketMap.get(9);
        MembershipLevelBucket bucket10 = indexToBucketMap.get(10);

        assertThat(bucket1.getStartRange(), is(0));
        assertThat(bucket1.getEndRange(), is(14));
        assertThat(bucket2.getStartRange(), is(15));
        assertThat(bucket2.getEndRange(), is(29));
        assertThat(bucket3.getStartRange(), is(30));
        assertThat(bucket3.getEndRange(), is(44));
        assertThat(bucket4.getStartRange(), is(45));
        assertThat(bucket4.getEndRange(), is(59));
        assertThat(bucket5.getStartRange(), is(60));
        assertThat(bucket5.getEndRange(), is(74));
        assertThat(bucket6.getStartRange(), is(75));
        assertThat(bucket6.getEndRange(), is(89));
        assertThat(bucket7.getStartRange(), is(90));
        assertThat(bucket7.getEndRange(), is(104));

        assertTrue(bucket1.getContents().contains(rel3));
        assertTrue(bucket1.getContents().contains(rel14));
        assertTrue(bucket2.getContents().contains(rel16));
        assertTrue(bucket2.getContents().contains(rel23));
        assertTrue(bucket3.getContents().isEmpty());
        assertTrue(bucket4.getContents().contains(rel47));
        assertTrue(bucket5.getContents().isEmpty());
        assertTrue(bucket6.getContents().contains(rel75));
        assertTrue(bucket7.getContents().contains(rel99));
        assertTrue(bucket8.getContents().isEmpty());
        assertTrue(bucket9.getContents().isEmpty());
        assertTrue(bucket10.getContents().isEmpty());
    }

    @Test
    public void resultsAreNotAddedToAnyBucketWhenLessThanRelevanceFloor() {
        ProductSearchResult belowRelevanceFloor = new ProductSearchResult();
        belowRelevanceFloor.setRelevance(0 - 1);

        productSearchResults.add(belowRelevanceFloor);

        Map<Integer, MembershipLevelBucket> indexToBucketMap = creator.createBucketMap(productSearchResults, 7, 0);

        MembershipLevelBucket bucket1 = indexToBucketMap.get(1);
        MembershipLevelBucket bucket2 = indexToBucketMap.get(2);
        MembershipLevelBucket bucket3 = indexToBucketMap.get(3);
        MembershipLevelBucket bucket4 = indexToBucketMap.get(4);
        MembershipLevelBucket bucket5 = indexToBucketMap.get(5);
        MembershipLevelBucket bucket6 = indexToBucketMap.get(6);
        MembershipLevelBucket bucket7 = indexToBucketMap.get(7);

        assertFalse(bucket1.getContents().contains(belowRelevanceFloor));
        assertFalse(bucket2.getContents().contains(belowRelevanceFloor));
        assertFalse(bucket3.getContents().contains(belowRelevanceFloor));
        assertFalse(bucket4.getContents().contains(belowRelevanceFloor));
        assertFalse(bucket5.getContents().contains(belowRelevanceFloor));
        assertFalse(bucket6.getContents().contains(belowRelevanceFloor));
        assertFalse(bucket7.getContents().contains(belowRelevanceFloor));

    }

    @Test
    public void resultsAreAddedToCorrectBucketsWhenEqualBucketEndRange() {
        Map<Integer, MembershipLevelBucket> indexToBucketMap = creator.createBucketMap(productSearchResults, 7, 0);

        MembershipLevelBucket bucket1 = indexToBucketMap.get(1);

        assertThat(bucket1.getStartRange(), is(0));
        assertThat(bucket1.getEndRange(), is(14));

        assertTrue(bucket1.getContents().contains(rel14));

    }

    @Test
    public void resultsAreAddedToCorrectBucketsWhenEqualBucketStartRange() {
        Map<Integer, MembershipLevelBucket> indexToBucketMap = creator.createBucketMap(productSearchResults, 7, 0);

        MembershipLevelBucket bucket6 = indexToBucketMap.get(6);

        assertTrue(bucket6.getContents().contains(rel75));

    }
}
