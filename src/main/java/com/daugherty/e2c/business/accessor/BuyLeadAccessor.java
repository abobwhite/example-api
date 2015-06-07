package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.BuyLeadFilter;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.persistence.data.BuyLeadReadDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

/**
 * Implementation for Buy Lead Accessor
 */
@Service("buyLeadAccessor")
public class BuyLeadAccessor extends BaseAccessor<BuyLead> {
    static final Function<Party, Long> PARTY_SNAPSHOT_ID_FUNCTION = new Function<Party, Long>() {
        @Override
        public Long apply(Party party) {
            return party.getSnapshotId();
        }
    };

    static final Function<BuyLead, Party> BUY_LEAD_PARTY_FUNCTION = new Function<BuyLead, Party>() {
        @Override
        public Party apply(BuyLead buyLead) {
            return buyLead.getRequester();
        }
    };

    static final Function<BuyLead, Long> BUY_LEAD_ID_FUNCTION = new Function<BuyLead, Long>() {
        @Override
        public Long apply(BuyLead buyLead) {
            return buyLead.getId();
        }
    };

    @Inject
    private BuyLeadReadDao buyLeadReadDao;
    @Inject
    private PartyBusinessTypeReadDao businessTypeReadDao;

    @Override
    public List<BuyLead> find(Filter<BuyLead> filter) {
        QueryCriteria criteria = null;

        if (filter.hasNoCriteria()) {
            criteria = buyLeadReadDao.createSortingAndPaginationCriteria(filter.getSortBy(), filter.isSortDescending(),
                    filter.getStartItem(), filter.getCount(), filter.getLocale());
        } else {
            List<Long> categoryIds = buildListOfCategoryIdsIncludingAllDescendants(((BuyLeadFilter) filter)
                    .getProductCategoryCriterion(BuyLeadFilter.PRODUCT_CATEGORY));

            criteria = buyLeadReadDao.createBuyLeadQueryCriteria(filter.getStringCriterion(BuyLeadFilter.EMAIL_PREFIX),
                    categoryIds, filter.getStringCriterion(BuyLeadFilter.PROVINCE),
                    filter.getDateCriterion(BuyLeadFilter.EFFECTIVE_SINCE),
                    filter.getBooleanCriterion(BuyLeadFilter.INCLUDE_EXPIRED), filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount());

        }

        List<BuyLead> buyLeads = buyLeadReadDao.findBuyLeads(criteria);

        addPartyBusinessTypes(buyLeads);
        addRespondingSuppliers(buyLeads);

        return buyLeads;
    }

    private void addPartyBusinessTypes(List<BuyLead> buyLeads) {
        List<Party> parties = Lists.transform(buyLeads, BUY_LEAD_PARTY_FUNCTION);
        ArrayListMultimap<Long, BusinessType> businessTypeToSnapshot = businessTypeReadDao.findBySnapshotIds(Lists
                .transform(parties, PARTY_SNAPSHOT_ID_FUNCTION));

        for (Party party : parties) {
            party.getCompany().setBusinessTypes(businessTypeToSnapshot.get(party.getSnapshotId()));
        }
    }

    private void addRespondingSuppliers(List<BuyLead> buyLeads) {
        List<Long> buyLeadIds = Lists.transform(buyLeads, BUY_LEAD_ID_FUNCTION);
        ArrayListMultimap<Long, Long> respondingSupplierBuyLead = buyLeadReadDao
                .findSuppliersThatRespondedToBuyLeads(buyLeadIds);

        for (BuyLead buyLead : buyLeads) {
            buyLead.setRespondingSuppliers(respondingSupplierBuyLead.get(buyLead.getId()));
        }
    }

    @Override
    public BuyLead load(Long id, Locale locale) {
        throw new UnsupportedOperationException();
    }

    private List<Long> buildListOfCategoryIdsIncludingAllDescendants(ProductCategory category) {
        if (category == null) {
            return null;
        }
        List<Long> categoryIds = Lists.newArrayList();
        addCategoryIdToList(category, categoryIds);
        return categoryIds;
    }

    private void addCategoryIdToList(ProductCategory category, List<Long> categoryIds) {
        categoryIds.add(category.getId());
        for (ProductCategory child : category.getChildren()) {
            addCategoryIdToList(child, categoryIds);
        }
    }
}
