package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;

/**
 * Accessor for Buyer domain objects.
 */
@Service("buyerAccessor")
public abstract class BuyerAccessor extends BaseAccessor<Buyer> {

    @Inject
    private PartyBusinessTypeReadDao businessTypeReadDao;

    @Override
    public List<Buyer> find(Filter<Buyer> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Buyer load(Long id, Locale locale) {
        Buyer buyer = loadBuyer(id);
        buyer.getCompany().setBusinessTypes(businessTypeReadDao.findBySnapshotId(buyer.getSnapshotId()));
        return buyer;
    }

    protected abstract Buyer loadBuyer(Long buyerId);
}
