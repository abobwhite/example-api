package com.daugherty.e2c.business.search;

import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MembershipLevelBucketRandomizerTest {
    @Mock
    MembershipLevelBucket productSearchResultBucket;

    private MembershipLevelBucketRandomizer randomizer = new MembershipLevelBucketRandomizer();

    @Test
    public void test() {
        Map<Integer, MembershipLevelBucket> bucketToIndexMap = new TreeMap<Integer, MembershipLevelBucket>();
        bucketToIndexMap.put(1, productSearchResultBucket);

        randomizer.randomizeResults(bucketToIndexMap);

        verify(productSearchResultBucket).randomize();
    }

}
