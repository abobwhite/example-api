package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.SupplierApprovalFilter;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeReadDao;
import com.daugherty.e2c.persistence.data.SupplierApprovalReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SupplierApprovalAccessorTest {

    private static final Contact CONTACT = new Contact("title", null, "first_name", "last_name", "country", "province",
            "email", "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123",
            Language.ENGLISH, new Date());
    private static final Company COMPANY = new Company("english_name", "chinese_name", "description", null, "1-10",
            "website", 2013, "200-300", "100-150", "logo_ref_id", "video_ref_id");
    private static final Supplier SUPPLIER = new Supplier(42L, "jKNz4P4q", CONTACT, COMPANY, ApprovalStatus.DRAFT, 1,
            44L, "import_license_ref_id", null, null);

    private static final SupplierApproval APPROVAL_42 = new SupplierApproval(42L, "jKNz4P4q", "title", SUPPLIER,
            SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 3, 242L, "lastUpdater", new Date());

    private static final List<SupplierApproval> DAO_APPROVALS = Lists.newArrayList(APPROVAL_42);

    private List<Supplier> suppliers;
    private ArrayListMultimap<Long, BusinessType> businessTypeBySnapshotId;

    @Mock
    private SupplierApprovalReadDao supplierApprovalReadDao;
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private PartyBusinessTypeReadDao businessTypeReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final SupplierApprovalAccessor accessor = new SupplierApprovalAccessor();

    @Before
    public void setUpFixtures() {
        suppliers = Lists.newArrayList(SUPPLIER);
        businessTypeBySnapshotId = ArrayListMultimap.create();
        businessTypeBySnapshotId.put(44L, BusinessType.AGENT);

        when(supplierReadDao.loadLatestBySupplierIds(Lists.newArrayList(42L), Locale.ENGLISH)).thenReturn(suppliers);
        when(businessTypeReadDao.findBySnapshotIds(Lists.newArrayList(44L))).thenReturn(businessTypeBySnapshotId);
    }

    @Test
    public void findWithoutFilterCriteriaCallsDaoGetAllMethod() throws Exception {
        SupplierApprovalFilter emptyFilter = new SupplierApprovalFilter(null, null, null, null, null, null, null, null,
                null, null);
        when(supplierApprovalReadDao.createSortingAndPaginationCriteria(null, Boolean.FALSE, 1, 250, Locale.ENGLISH))
                .thenReturn(queryCriteria);
        when(supplierApprovalReadDao.find(queryCriteria)).thenReturn(DAO_APPROVALS);

        List<SupplierApproval> approvals = accessor.find(emptyFilter);

        verify(supplierApprovalReadDao).find(queryCriteria);
        assertThat(approvals, is(DAO_APPROVALS));
    }

    @Test
    public void findWithFilterCriteriaCallsDaoFindMethod() throws Exception {
        when(
                supplierApprovalReadDao.createQueryCriteria("prefix", "email", "type",
                        ApprovalStatus.PENDING_APPROVAL.getName(), null, null, "title", true, 26, 50)).thenReturn(
                queryCriteria);
        when(supplierApprovalReadDao.find(queryCriteria)).thenReturn(DAO_APPROVALS);

        SupplierApprovalFilter filter = new SupplierApprovalFilter("prefix", "email", "type",
                ApprovalStatus.PENDING_APPROVAL, null, null, "title", true, 26, 50);
        List<SupplierApproval> approvals = accessor.find(filter);

        assertThat(approvals, is(DAO_APPROVALS));
    }

    @Test
    public void loadDelegatesToDao() {
        when(supplierApprovalReadDao.load(42L)).thenReturn(APPROVAL_42);

        SupplierApproval persistedApproval = accessor.load(42L, Locale.ENGLISH);

        assertThat(persistedApproval, is(APPROVAL_42));
    }

}
