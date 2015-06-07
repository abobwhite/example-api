package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.SupplierTranslationFilter;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.persistence.data.SupplierTranslationReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SupplierTranslationAccessorTest {

    private static final SupplierTranslation TRANSLATION_23 = new SupplierTranslation(23L, "Jp0bwPjG", "title",
            ApprovalStatus.PENDING_TRANSLATION, 4, 232L, SupplierTranslation.PROFILE_TYPE, false, "lastUpdater", new Date());
    private static final List<SupplierTranslation> DAO_TRANSLATIONS = Lists.newArrayList(TRANSLATION_23);

    @Mock
    private SupplierTranslationReadDao supplierTranslationReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final SupplierTranslationAccessor accessor = new SupplierTranslationAccessor();

    @Test
    public void findWithoutFilterCriteriaCallsDaoGetAllMethod() throws Exception {
        SupplierTranslationFilter emptyFilter = new SupplierTranslationFilter(null, null, null, null, null, null, null);
        when(supplierTranslationReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(supplierTranslationReadDao.getAll(queryCriteria)).thenReturn(DAO_TRANSLATIONS);

        List<SupplierTranslation> approvals = accessor.find(emptyFilter);

        verify(supplierTranslationReadDao).getAll(queryCriteria);
        assertThat(approvals, is(DAO_TRANSLATIONS));
    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        when(
                supplierTranslationReadDao.createQueryCriteria("prefix", "type",
                        null, "title", true, 26, 50)).thenReturn(queryCriteria);
        when(supplierTranslationReadDao.find(queryCriteria)).thenReturn(DAO_TRANSLATIONS);

        SupplierTranslationFilter filter = new SupplierTranslationFilter("prefix", "type",
                null, "title", true, 26, 50);
        List<SupplierTranslation> approvals = accessor.find(filter);

        assertThat(approvals, is(DAO_TRANSLATIONS));
    }

    @Test
    public void loadDelegatesToDao() {
        when(supplierTranslationReadDao.load(23L)).thenReturn(TRANSLATION_23);

        SupplierTranslation persistedApproval = accessor.load(23L, Locale.ENGLISH);

        assertThat(persistedApproval, is(TRANSLATION_23));
    }
}
