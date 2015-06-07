package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.BuyerApprovalFilter;
import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.e2c.persistence.data.BuyerApprovalReadDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Accessor for SupplierApproval domain objects.
 */
@Service("buyerApprovalAccessor")
public class BuyerApprovalAccessor extends BaseAccessor<BuyerApproval> {

    @Inject
    private BuyerApprovalReadDao buyerApprovalReadDao;

    @Override
    public List<BuyerApproval> find(Filter<BuyerApproval> filter) {
        if (filter.hasNoCriteria()) {
            QueryCriteria criteria = buyerApprovalReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
            return buyerApprovalReadDao.getAll(criteria);
        } else {
            QueryCriteria criteria = buyerApprovalReadDao.createQueryCriteria(
                    filter.getStringCriterion(BuyerApprovalFilter.TITLE_PREFIX),
                    filter.getStringCriterion(BuyerApprovalFilter.EMAIL_PREFIX),
                    filter.getStringCriterion(BuyerApprovalFilter.STATUS),
                    filter.getBooleanCriterion(BuyerApprovalFilter.NEW_BUYER), filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount());
            return buyerApprovalReadDao.find(criteria);
        }
    }

    @Override
    public BuyerApproval load(Long id, Locale locale) {
        return buyerApprovalReadDao.load(id);
    }
}
