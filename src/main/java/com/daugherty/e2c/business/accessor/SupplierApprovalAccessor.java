package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.SupplierApprovalFilter;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.SupplierApprovalReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Accessor for SupplierApproval domain objects.
 */
@Service("supplierApprovalAccessor")
public class SupplierApprovalAccessor extends BaseAccessor<SupplierApproval> {

    static final Function<SupplierApproval, Long> SUPPLIER_APPROVAL_SUPPLIER_ID_FUNCTION = new Function<SupplierApproval, Long>() {
        @Override
        public Long apply(SupplierApproval supplierApproval) {
            return supplierApproval.getSupplier().getId();
        }
    };

    static final Function<Supplier, Long> SUPPLIER_SNAPSHOT_ID_FUNCTION = new Function<Supplier, Long>() {
        @Override
        public Long apply(Supplier supplier) {
            return supplier.getSnapshotId();
        }
    };

    @Inject
    private SupplierApprovalReadDao supplierApprovalReadDao;
    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private PartyBusinessTypeReadDao businessTypeReadDao;

    @Override
    public List<SupplierApproval> find(Filter<SupplierApproval> filter) {
        List<SupplierApproval> supplierApprovals = Lists.newArrayList();

        if (filter.hasNoCriteria()) {
            QueryCriteria criteria = supplierApprovalReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
            supplierApprovals = supplierApprovalReadDao.find(criteria);
        } else {
            QueryCriteria criteria = supplierApprovalReadDao.createQueryCriteria(
                    filter.getStringCriterion(SupplierApprovalFilter.TITLE_PREFIX),
                    filter.getStringCriterion(SupplierApprovalFilter.EMAIL_PREFIX),
                    filter.getStringCriterion(SupplierApprovalFilter.TYPE),
                    filter.getStringCriterion(SupplierApprovalFilter.STATUS),
                    filter.getBooleanCriterion(SupplierApprovalFilter.NEW_SUPPLIER),
                    filter.getBooleanCriterion(SupplierApprovalFilter.PAID_SUPPLIER), filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount());
            supplierApprovals = supplierApprovalReadDao.find(criteria);
        }

        return hydrateAll(supplierApprovals, filter.getLocale());
    }

    @Override
    public SupplierApproval load(Long id, Locale locale) {
        SupplierApproval supplierApproval = supplierApprovalReadDao.load(id);
        hydrateAll(Lists.newArrayList(supplierApproval), locale);
        return supplierApproval;
    }

    private List<SupplierApproval> hydrateAll(List<SupplierApproval> supplierApprovals, Locale locale) {
        List<Long> supplierIds = Lists.transform(supplierApprovals, SUPPLIER_APPROVAL_SUPPLIER_ID_FUNCTION);

        Map<Long, Supplier> supplierById = getSupplierMap(supplierIds, locale);

        for (SupplierApproval supplierApproval : supplierApprovals) {
            supplierApproval.setSupplier(supplierById.get(supplierApproval.getSupplier().getId()));
        }

        return supplierApprovals;
    }

    private Map<Long, Supplier> getSupplierMap(List<Long> supplierIds, Locale locale) {
        List<Supplier> suppliers = supplierReadDao.loadLatestBySupplierIds(supplierIds, locale);
        ArrayListMultimap<Long, BusinessType> businessTypeToSnapshot = businessTypeReadDao.findBySnapshotIds(Lists
                .transform(suppliers, SUPPLIER_SNAPSHOT_ID_FUNCTION));

        Map<Long, Supplier> supplierIdToSupplier = Maps.newHashMap();

        for (Supplier supplier : suppliers) {
            supplier.getCompany().setBusinessTypes(businessTypeToSnapshot.get(supplier.getSnapshotId()));
            supplierIdToSupplier.put(supplier.getId(), supplier);
        }
        return supplierIdToSupplier;
    }

}
