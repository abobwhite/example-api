package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.MembershipProductSummary;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.MembershipProductSummaryReadDao;
import com.daugherty.e2c.persistence.data.MembershipReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

@RunWith(MockitoJUnitRunner.class)
public class MembershipProductSummaryAccessorTest {
    private final MembershipProductSummary productSummary = new MembershipProductSummary(1L, 10, 2, 3, 4, 5, 6, 7, 0,
            12, 1, 13);
    private final Membership membership = new Membership(1L, new MembershipLevel(1L, 2, BigDecimal.valueOf(10), 6, 10,
            20, 20, false, 10, false, false, false, 10, false, false, false, true, false, false, false));

    private final Contact contact = new Contact("title", Gender.MALE, "first_name", "last_name", "country", "province",
            "email_address", "skype_ref_id", "msn_ref_id", "icq_ref_id", "mobile_telephone", "123.123.123.123",
            Language.ENGLISH, new Date());
    private final Company company = new Company("english_name", "chinese_name", "description", null, "1-10", "website",
            2013, "200-300", "100-150", "logo_ref_id", null);
    private final Supplier supplier = new Supplier(1L, "vXP9l0Or", contact, company, ApprovalStatus.PENDING_APPROVAL,
            1, 43L, "export_license_ref_id", null, null);

    @Mock
    private MembershipProductSummaryReadDao membershipProductSummaryReadDao;
    @Mock
    private MembershipReadDao membershipReadDao;
    @Mock
    private SupplierReadDao supplierReadDao;

    @InjectMocks
    private final MembershipProductSummaryAccessor productSummaryAccessor = new MembershipProductSummaryAccessor();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        supplier.getMemberships().add(membership);
    }

    @Test
    public void loadMembershipProductSummaryPopulatesCountsWithAMembership() {
        when(membershipProductSummaryReadDao.loadByMembershipId(1L)).thenReturn(productSummary);
        when(membershipReadDao.loadTotalProductsByMembershipId(1L)).thenReturn(20);
        when(membershipReadDao.loadByMembershipId(1L)).thenReturn(membership);
        when(supplierReadDao.loadLatest(1L, Locale.ENGLISH)).thenReturn(supplier);

        MembershipProductSummary productSummary = productSummaryAccessor.load(1L, Locale.ENGLISH);

        assertThat(productSummary, is(notNullValue()));
        assertThat(productSummary.getId(), is(1L));
        assertThat(productSummary.getTotal(), is(10));
        assertThat(productSummary.getPublished(), is(2));
        assertThat(productSummary.getPendingApproval(), is(3));
        assertThat(productSummary.getPendingTranslation(), is(4));
        assertThat(productSummary.getWaitingForInformation(), is(5));
        assertThat(productSummary.getUnpublished(), is(6));
        assertThat(productSummary.getHotProducts(), is(7));
        assertThat(productSummary.getProductsRemaining(), is(20));
        assertThat(productSummary.getApproved(), is(12));
        assertThat(productSummary.getDisapproved(), is(1));
        assertThat(productSummary.getDraft(), is(13));
    }

    @Test
    public void loadMembershipProductSummaryReturnsEmptyWhenSummaryNotFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(membershipProductSummaryReadDao).loadByMembershipId(1L);

        when(membershipReadDao.loadTotalProductsByMembershipId(1L)).thenReturn(20);
        when(membershipReadDao.loadByMembershipId(1L)).thenReturn(membership);
        when(supplierReadDao.loadLatest(1L, Locale.ENGLISH)).thenReturn(supplier);

        MembershipProductSummary productSummary = productSummaryAccessor.load(1L, Locale.ENGLISH);

        assertThat(productSummary, is(notNullValue()));
        assertThat(productSummary.getId(), is(1L));
        assertThat(productSummary.getTotal(), is(0));
        assertThat(productSummary.getPublished(), is(0));
        assertThat(productSummary.getPendingApproval(), is(0));
        assertThat(productSummary.getPendingTranslation(), is(0));
        assertThat(productSummary.getWaitingForInformation(), is(0));
        assertThat(productSummary.getUnpublished(), is(0));
        assertThat(productSummary.getHotProducts(), is(0));
        assertThat(productSummary.getProductsRemaining(), is(20));
        assertThat(productSummary.getApproved(), is(0));
        assertThat(productSummary.getDisapproved(), is(0));
        assertThat(productSummary.getDraft(), is(0));
    }

    @Test
    public void loadMembershipProductSetSummaryRemainingProducts() {
        when(membershipProductSummaryReadDao.loadByMembershipId(1L)).thenReturn(productSummary);
        when(membershipReadDao.loadTotalProductsByMembershipId(1L)).thenReturn(20);
        when(membershipReadDao.loadByMembershipId(1L)).thenReturn(membership);
        when(supplierReadDao.loadLatest(1L, Locale.ENGLISH)).thenReturn(supplier);

        MembershipProductSummary productSummary = productSummaryAccessor.load(1L, Locale.ENGLISH);

        assertThat(productSummary, is(notNullValue()));
        assertThat(productSummary.getId(), is(1L));
        assertThat(productSummary.getTotal(), is(10));
        assertThat(productSummary.getPublished(), is(2));
        assertThat(productSummary.getPendingApproval(), is(3));
        assertThat(productSummary.getPendingTranslation(), is(4));
        assertThat(productSummary.getWaitingForInformation(), is(5));
        assertThat(productSummary.getUnpublished(), is(6));
        assertThat(productSummary.getHotProducts(), is(7));
        assertThat(productSummary.getProductsRemaining(), is(20));
        assertThat(productSummary.getApproved(), is(12));
        assertThat(productSummary.getDisapproved(), is(1));
        assertThat(productSummary.getDraft(), is(13));
    }

    @Test
    public void loadMembershipProductSetSummaryRemainingProductsToPublish() {
        when(membershipProductSummaryReadDao.loadByMembershipId(1L)).thenReturn(productSummary);
        when(membershipReadDao.loadTotalProductsByMembershipId(1L)).thenReturn(20);
        when(membershipReadDao.loadByMembershipId(1L)).thenReturn(membership);
        when(supplierReadDao.loadLatest(1L, Locale.ENGLISH)).thenReturn(supplier);

        MembershipProductSummary productSummary = productSummaryAccessor.load(1L, Locale.ENGLISH);

        assertThat(productSummary, is(notNullValue()));
        assertThat(productSummary.getId(), is(1L));
        assertThat(productSummary.getTotal(), is(10));
        assertThat(productSummary.getPublished(), is(2));
        assertThat(productSummary.getPendingApproval(), is(3));
        assertThat(productSummary.getPendingTranslation(), is(4));
        assertThat(productSummary.getWaitingForInformation(), is(5));
        assertThat(productSummary.getUnpublished(), is(6));
        assertThat(productSummary.getHotProducts(), is(7));
        assertThat(productSummary.getProductsRemaining(), is(20));
        assertThat(productSummary.getApproved(), is(12));
        assertThat(productSummary.getDraft(), is(13));
        assertThat(productSummary.getDisapproved(), is(1));
        assertThat(productSummary.getPublishRemaining(), is(8));
    }

    @Test
    public void loadMembershipProductSetSummaryRemainingHotProductsToPromote() {
        when(membershipProductSummaryReadDao.loadByMembershipId(1L)).thenReturn(productSummary);
        when(membershipReadDao.loadTotalProductsByMembershipId(1L)).thenReturn(20);
        when(membershipReadDao.loadByMembershipId(1L)).thenReturn(membership);
        when(supplierReadDao.loadLatest(1L, Locale.ENGLISH)).thenReturn(supplier);

        MembershipProductSummary productSummary = productSummaryAccessor.load(1L, Locale.ENGLISH);

        assertThat(productSummary, is(notNullValue()));
        assertThat(productSummary.getId(), is(1L));
        assertThat(productSummary.getTotal(), is(10));
        assertThat(productSummary.getPublished(), is(2));
        assertThat(productSummary.getPendingApproval(), is(3));
        assertThat(productSummary.getPendingTranslation(), is(4));
        assertThat(productSummary.getWaitingForInformation(), is(5));
        assertThat(productSummary.getUnpublished(), is(6));
        assertThat(productSummary.getHotProducts(), is(7));
        assertThat(productSummary.getProductsRemaining(), is(20));
        assertThat(productSummary.getApproved(), is(12));
        assertThat(productSummary.getDraft(), is(13));
        assertThat(productSummary.getDisapproved(), is(1));
        assertThat(productSummary.getPublishRemaining(), is(8));
        assertThat(productSummary.getHotProductsRemaining(), is(3));
    }
}
