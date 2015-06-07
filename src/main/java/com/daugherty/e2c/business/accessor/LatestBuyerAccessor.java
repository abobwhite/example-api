package com.daugherty.e2c.business.accessor;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.persistence.data.BuyerReadDao;

/**
 * Accessor for Buyer regardless of status
 */
@Service("latestBuyerAccessor")
public class LatestBuyerAccessor extends BuyerAccessor {

    @Inject
    private BuyerReadDao buyerReadDao;

    @Override
    protected Buyer loadBuyer(Long buyerId) {
        return buyerReadDao.loadLatest(buyerId);
    }
}
