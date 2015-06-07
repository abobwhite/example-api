package com.daugherty.e2c.business.search;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.daugherty.e2c.domain.MembershipLevelAware;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * A bucket of product search results by relevance range
 */
public class MembershipLevelBucket<T extends MembershipLevelAware> {
    private int index;
    private int startRange;
    private int endRange;
    private List<T> contents = Lists.newArrayList();

    public MembershipLevelBucket(int index) {
        this.index = index;
    }

    public MembershipLevelBucket(int index, int startRange, int endRange) {
        this.index = index;
        this.startRange = startRange;
        this.endRange = endRange;
    }

    public void addProductSearchResults(List<T> productSearchResults) {
        for (ListIterator<T> iterator = productSearchResults.listIterator(); iterator.hasNext();) {
            T productSearchResult = (T) iterator.next();

            if (isInRange(productSearchResult)) {
                contents.add(productSearchResult);
                iterator.remove();
            }
        }
    }

    public void promoteTo(MembershipLevelBucket higherBucket) {
        final int membershipLevel = higherBucket.getIndex() - index + 1;

        Collection<T> resultsToPromote = Collections2.filter(contents, new Predicate<MembershipLevelAware>() {

            @Override
            public boolean apply(MembershipLevelAware result) {
                return result.getMembershipLevel().equals(membershipLevel);
            }
        });

        higherBucket.getContents().addAll(resultsToPromote);
        contents.removeAll(resultsToPromote);
    }

    public void randomize() {
        Collections.shuffle(contents);
    }

    public final int getIndex() {
        return index;
    }

    public final int getStartRange() {
        return startRange;
    }

    public final int getEndRange() {
        return endRange;
    }

    public List<T> getContents() {
        return contents;
    }

    private boolean isInRange(MembershipLevelAware productSearchResult) {
        return startRange <= productSearchResult.getRelevance() && productSearchResult.getRelevance() <= endRange;
    }
}
