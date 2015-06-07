package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.SupplierTranslationFilter;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.persistence.data.SupplierTranslationReadDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Accessor for SupplierTranslation domain objects.
 */
@Service("supplierTranslationAccessor")
public class SupplierTranslationAccessor extends BaseAccessor<SupplierTranslation> {

    @Inject
    private SupplierTranslationReadDao supplierTranslationReadDao;

    @Override
    public List<SupplierTranslation> find(Filter<SupplierTranslation> filter) {
        if (filter.hasNoCriteria()) {
            QueryCriteria criteria = supplierTranslationReadDao.createSortingAndPaginationCriteria(filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount(), filter.getLocale());
            return supplierTranslationReadDao.getAll(criteria);
        } else {
            QueryCriteria criteria = supplierTranslationReadDao.createQueryCriteria(
                    filter.getStringCriterion(SupplierTranslationFilter.TITLE_PREFIX),
                    filter.getStringCriterion(SupplierTranslationFilter.TYPE),
                    filter.getBooleanCriterion(SupplierTranslationFilter.TRANSLATED), filter.getSortBy(),
                    filter.isSortDescending(), filter.getStartItem(), filter.getCount());
            return supplierTranslationReadDao.find(criteria);
        }
    }

    @Override
    public SupplierTranslation load(Long id, Locale locale) {
        return supplierTranslationReadDao.load(id);
    }
}
