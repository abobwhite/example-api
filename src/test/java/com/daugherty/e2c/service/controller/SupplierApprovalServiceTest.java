package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.ProfilePublisher;
import com.daugherty.e2c.business.accessor.filter.SupplierApprovalFilter;
import com.daugherty.e2c.business.mapper.SupplierApprovalMapper;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.security.UserDeleter;
import com.daugherty.e2c.service.json.JsonSupplier;
import com.daugherty.e2c.service.json.JsonSupplierApproval;
import com.daugherty.e2c.service.json.JsonSupplierApprovals;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SupplierApprovalServiceTest {

    private static final SupplierApproval PROFILE_APPROVAL = new SupplierApproval(1L, "vXP9l0Or", "profile title",
            new Supplier(1L), SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 3, 242L, "lastUpdater",
            new Date(1404407515988L));
    private static final SupplierApproval PRODUCT_APPROVAL = new SupplierApproval(2L, "2", "product title",
            new Supplier(1L), SupplierApproval.PRODUCT_TYPE, ApprovalStatus.PENDING_APPROVAL, 3, 242L, "lastUpdater",
            new Date(1404407515988L));
    private static final SupplierApproval CERTIFICATION_APPROVAL = new SupplierApproval(42L, "42",
            "certification title", new Supplier(42L), SupplierApproval.CERTIFICATION_TYPE,
            ApprovalStatus.PENDING_APPROVAL, 3, 242L, "lastUpdater", new Date(1404407515988L));

    @Mock
    private Accessor<SupplierApproval> accessor;
    @Mock
    private Mutator<SupplierApproval> mutator;
    @Mock
    private ApprovalStateTransitionVisitor submitForApprovalVisitor;
    @Mock
    private ApprovalStateTransitionVisitor deferApprovalVisitor;
    @Mock
    private ApprovalStateTransitionVisitor disapproveVisitor;
    @Mock
    private ApprovalStateTransitionVisitor approveVisitor;
    @Mock
    private ApprovalStateTransitionVisitor requestTranslationVisitor;
    @Mock
    private UserDeleter userDeleter;
    @Mock
    private SupplierApprovalMapper supplierApprovalMapper;
    @Mock
    private Accessor<Supplier> approvedSupplierAccessor;
    @Mock
    private Accessor<Supplier> latestSupplierAccessor;
    @Mock
    private Mutator<SupplierTranslation> supplierTranslationMutator;
    @Mock
    private Mutator<ProfileTranslation> profileTranslationMutator;
    @Mock
    private Accessor<SupplierTranslation> supplierTranslationAccessor;
    @Mock
    private ProfilePublisher profilePublisher;

    @InjectMocks
    private final SupplierApprovalService service = new SupplierApprovalService();

    @Captor
    private ArgumentCaptor<Filter<SupplierApproval>> filterCaptor;
    @Captor
    private ArgumentCaptor<SupplierApproval> approvalCaptor;
    @Captor
    private ArgumentCaptor<ProfileTranslation> profileCaptor;

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveSupplierApprovalsWithQueryStringArgumentsDelegatesToAccessor() {
        when(accessor.find(any(Filter.class))).thenReturn(
                Lists.newArrayList(PROFILE_APPROVAL, PRODUCT_APPROVAL, CERTIFICATION_APPROVAL));

        JsonSupplierApproval profile = new JsonSupplierApproval();
        profile.setId("vXP9l0Or");
        profile.setTitle("profile title");
        profile.setSupplier(new JsonSupplier("vXP9l0Or"));
        profile.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getName());
        profile.setApprovalType(SupplierApproval.PROFILE_TYPE);
        profile.setVersion(3);
        profile.setLastUpdatedAt(new Date(1404407515988L));
        profile.setLastUpdatedBy("lastUpdater");

        when(supplierApprovalMapper.fromExistingDomainObject(PROFILE_APPROVAL)).thenReturn(profile);

        JsonSupplierApproval product = new JsonSupplierApproval();
        product.setId("2");
        product.setTitle("product title");
        product.setSupplier(new JsonSupplier("vXP9l0Or"));
        product.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getName());
        product.setApprovalType(SupplierApproval.PRODUCT_TYPE);
        product.setVersion(3);
        product.setLastUpdatedAt(new Date(1404407515988L));
        product.setLastUpdatedBy("lastUpdater");

        when(supplierApprovalMapper.fromExistingDomainObject(PRODUCT_APPROVAL)).thenReturn(product);

        JsonSupplierApproval certification = new JsonSupplierApproval();
        certification.setId("42");
        certification.setTitle("certification title");
        certification.setSupplier(new JsonSupplier("vXP9l0Or"));
        certification.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getName());
        certification.setApprovalType(SupplierApproval.CERTIFICATION_TYPE);
        certification.setVersion(3);
        certification.setLastUpdatedAt(new Date(1404407515988L));
        certification.setLastUpdatedBy("lastUpdater");

        when(supplierApprovalMapper.fromExistingDomainObject(CERTIFICATION_APPROVAL)).thenReturn(certification);

        JsonSupplierApprovals jsonApprovals = service.retrieveSupplierApprovals("title", "email", "type",
                ApprovalStatus.PENDING_APPROVAL.getName(), null, null, "title", Boolean.TRUE, 26, 50);

        verify(accessor).find(filterCaptor.capture());
        Filter<SupplierApproval> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(SupplierApprovalFilter.class)));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TITLE_PREFIX), is("title"));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.EMAIL_PREFIX), is("email"));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TYPE), is("type"));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.STATUS),
                is(ApprovalStatus.PENDING_APPROVAL.getName()));
        assertThat(filter.getSortBy(), is("title"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonApprovals, is(notNullValue()));
        assertThat(jsonApprovals.size(), is(3));
        assertThatJsonApprovalMatchesExpectedValues(jsonApprovals.get(0), PROFILE_APPROVAL);
        assertThatJsonApprovalMatchesExpectedValues(jsonApprovals.get(1), PRODUCT_APPROVAL);
        assertThatJsonApprovalMatchesExpectedValues(jsonApprovals.get(2), CERTIFICATION_APPROVAL);
    }

    // TODO Products and certifications

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveSupplierApprovalsWithEmptyArgumentsDelegatesToAccessor() {
        when(accessor.find(any(Filter.class))).thenReturn(
                Lists.newArrayList(PROFILE_APPROVAL, PRODUCT_APPROVAL, CERTIFICATION_APPROVAL));

        JsonSupplierApproval profile = new JsonSupplierApproval();
        profile.setId("vXP9l0Or");
        profile.setTitle("profile title");
        profile.setSupplier(new JsonSupplier("vXP9l0Or"));
        profile.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getName());
        profile.setApprovalType(SupplierApproval.PROFILE_TYPE);
        profile.setVersion(3);
        profile.setLastUpdatedAt(new Date(1404407515988L));
        profile.setLastUpdatedBy("lastUpdater");

        when(supplierApprovalMapper.fromExistingDomainObject(PROFILE_APPROVAL)).thenReturn(profile);

        JsonSupplierApproval product = new JsonSupplierApproval();
        product.setId("2");
        product.setTitle("product title");
        product.setSupplier(new JsonSupplier("vXP9l0Or"));
        product.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getName());
        product.setApprovalType(SupplierApproval.PRODUCT_TYPE);
        product.setVersion(3);
        product.setLastUpdatedAt(new Date(1404407515988L));
        product.setLastUpdatedBy("lastUpdater");

        when(supplierApprovalMapper.fromExistingDomainObject(PRODUCT_APPROVAL)).thenReturn(product);

        JsonSupplierApproval certification = new JsonSupplierApproval();
        certification.setId("42");
        certification.setTitle("certification title");
        certification.setSupplier(new JsonSupplier("vXP9l0Or"));
        certification.setApprovalStatus(ApprovalStatus.PENDING_APPROVAL.getName());
        certification.setApprovalType(SupplierApproval.CERTIFICATION_TYPE);
        certification.setVersion(3);
        certification.setLastUpdatedAt(new Date(1404407515988L));
        certification.setLastUpdatedBy("lastUpdater");

        when(supplierApprovalMapper.fromExistingDomainObject(CERTIFICATION_APPROVAL)).thenReturn(certification);

        JsonSupplierApprovals jsonApprovals = service.retrieveSupplierApprovals("", "", "", "", null, null, "",
                Boolean.FALSE, 1, 250);

        verify(accessor).find(filterCaptor.capture());
        Filter<SupplierApproval> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(SupplierApprovalFilter.class)));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TITLE_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.EMAIL_PREFIX), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.TYPE), is(nullValue()));
        assertThat(filter.getStringCriterion(SupplierApprovalFilter.STATUS), is(nullValue()));
        assertThat(filter.getSortBy(), is(""));
        assertThat(filter.isSortDescending(), is(false));
        assertThat(filter.getStartItem(), is(1));
        assertThat(filter.getCount(), is(250));

        assertThat(jsonApprovals, is(notNullValue()));
        assertThat(jsonApprovals.size(), is(3));
        assertThatJsonApprovalMatchesExpectedValues(jsonApprovals.get(0), PROFILE_APPROVAL);
        assertThatJsonApprovalMatchesExpectedValues(jsonApprovals.get(1), PRODUCT_APPROVAL);
        assertThatJsonApprovalMatchesExpectedValues(jsonApprovals.get(2), CERTIFICATION_APPROVAL);
    }

    @Test
    public void submitForApprovalLoadsProxyApprovalAppliesSubmitForApprovalVisitorAndCreatesActualApproval()
            throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.DRAFT, 1, 242L, "fordp", new Date());
        when(accessor.load("jKNz4P4q", Locale.ENGLISH)).thenReturn(approval);

        when(submitForApprovalVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(
                ApprovalStatus.PENDING_APPROVAL);
        when(submitForApprovalVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus()))
                .thenReturn(23);

        service.submitForApproval("jKNz4P4q");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.PENDING_APPROVAL));
        assertThat(approval.getVersion(), is(23));
        verify(mutator).create(approval);

        verifyZeroInteractions(deferApprovalVisitor, disapproveVisitor, requestTranslationVisitor);
    }

    @Test
    public void deferApprovalLoadsProxyApprovalAppliesDeferApprovalVisitorAndUpdatesApproval() throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 1, 242L, "fordp", new Date());
        when(accessor.load("jKNz4P4q", Locale.ENGLISH)).thenReturn(approval);

        when(deferApprovalVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(
                ApprovalStatus.WAITING_FOR_INFORMATION);
        when(deferApprovalVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus())).thenReturn(23);

        service.deferApproval("jKNz4P4q");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.WAITING_FOR_INFORMATION));
        assertThat(approval.getVersion(), is(23));
        verify(mutator).update(approval);

        verifyZeroInteractions(submitForApprovalVisitor, disapproveVisitor, requestTranslationVisitor);
    }

    @Test
    public void disapproveLoadsProxyApprovalAppliesDisapproveVisitorAndUpdatesApproval() throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 1, 242L, "fordp", new Date());
        when(accessor.load("jKNz4P4q", Locale.ENGLISH)).thenReturn(approval);

        when(disapproveVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(
                ApprovalStatus.DISAPPROVED);
        when(disapproveVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus())).thenReturn(23);

        service.disapprove("jKNz4P4q");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.DISAPPROVED));
        assertThat(approval.getVersion(), is(23));
        verify(mutator).update(approval);
        verify(userDeleter).deleteDisapprovedSupplier("jKNz4P4q");

        verifyZeroInteractions(submitForApprovalVisitor, deferApprovalVisitor, requestTranslationVisitor);
    }

    @Test
    public void requestTranslationLoadsProxyApprovalAppliesRequestTranslationVisitorAndUpdatesApproval()
            throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 1, 242L, "fordp", new Date());
        when(accessor.load("jKNz4P4q", Locale.ENGLISH)).thenReturn(approval);

        when(requestTranslationVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(
                ApprovalStatus.PENDING_TRANSLATION);
        when(requestTranslationVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus())).thenReturn(
                23);

        service.requestTranslation("jKNz4P4q");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.PENDING_TRANSLATION));
        assertThat(approval.getVersion(), is(23));
        verify(mutator).update(approval);

        verifyZeroInteractions(submitForApprovalVisitor, deferApprovalVisitor, disapproveVisitor);
    }

    @Test
    public void approveLoadsProxyApprovalAppliesApprovalVisitorAndUpdatesApproval() throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.CERTIFICATION_TYPE, ApprovalStatus.PENDING_APPROVAL, 1, 242L, "fordp", new Date());
        when(accessor.load("jKNz4P4q", Locale.ENGLISH)).thenReturn(approval);

        when(approveVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(ApprovalStatus.APPROVED);
        when(approveVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus())).thenReturn(23);

        service.approve("jKNz4P4q");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(approval.getVersion(), is(23));
        verify(mutator).update(approval);

        verifyZeroInteractions(submitForApprovalVisitor, deferApprovalVisitor, disapproveVisitor);
    }

    @Test
    public void requestPublishTranslatesSupplierAndPublishes() throws Exception {

        Company company = new Company("Megadodo Publications", "chineseName", "tranlsatedDescription",
                Lists.newArrayList(BusinessType.AGENT), "numberfEmployees", "website", 1982, "totalAnnualSales",
                "totalImportAmount", "logoRefId", "videoRefId");

        Supplier approved = new Supplier(42L, "jKNz4P4q", null, company, ApprovalStatus.APPROVED, 22, 241L,
                "exportLicenseRefId", null, null);
        Supplier latest = new Supplier(42L, "jKNz4P4q", null, company, ApprovalStatus.PENDING_APPROVAL, 23, 242L,
                "exportLicenseRefId", null, null);

        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 23, 242L, "fordp", new Date());

        SupplierTranslation translation = new SupplierTranslation(42L, "jKNz4P4q", "title",
                ApprovalStatus.PENDING_TRANSLATION, 23, 242L, SupplierTranslation.PROFILE_TYPE, false, "lastUpdater",
                new Date());

        when(accessor.load("jKNz4P4q", Locale.ENGLISH)).thenReturn(approval);

        when(requestTranslationVisitor.getNewApprovalStatus(approval.getApprovalStatus())).thenReturn(
                ApprovalStatus.PENDING_TRANSLATION);
        when(requestTranslationVisitor.getNewVersion(approval.getVersion(), approval.getApprovalStatus())).thenReturn(
                23);
        when(approvedSupplierAccessor.load("jKNz4P4q", Locale.CHINESE)).thenReturn(approved);
        when(latestSupplierAccessor.load("jKNz4P4q", Locale.CHINESE)).thenReturn(latest);
        when(approveVisitor.getNewApprovalStatus(translation.getApprovalStatus())).thenReturn(ApprovalStatus.APPROVED);
        when(approveVisitor.getNewVersion(translation.getVersion(), translation.getApprovalStatus())).thenReturn(23);
        when(supplierTranslationAccessor.load("jKNz4P4q", Locale.ENGLISH)).thenReturn(translation);
        service.requestPublish("jKNz4P4q");

        assertThat(approval.getApprovalStatus(), is(ApprovalStatus.PENDING_TRANSLATION));
        assertThat(approval.getVersion(), is(23));
        verify(mutator).update(approval);

        verify(profileTranslationMutator).update(profileCaptor.capture());
        ProfileTranslation profileTranslation = profileCaptor.getValue();
        assertThat(profileTranslation.getCompanyDescription(), is("tranlsatedDescription"));
        assertThat(profileTranslation.getCompanyDescriptionTranslation(), is("tranlsatedDescription"));

        assertThat(translation.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(translation.getVersion(), is(23));
        verify(supplierTranslationMutator).update(translation);

        verify(profilePublisher).publish("jKNz4P4q");

        verifyZeroInteractions(submitForApprovalVisitor, deferApprovalVisitor, disapproveVisitor);
    }

    private void assertThatJsonApprovalMatchesExpectedValues(JsonSupplierApproval jsonApproval,
            SupplierApproval approval) {
        assertThat(jsonApproval.getId(), is(approval.getPublicId()));
        assertThat(jsonApproval.getTitle(), is(approval.getTitle()));
        assertThat(jsonApproval.getSupplier().getId(), is("vXP9l0Or"));
        assertThat(jsonApproval.getApprovalStatus(), is(approval.getApprovalStatus().getName()));
        assertThat(jsonApproval.getApprovalType(), is(approval.getType()));
        assertThat(jsonApproval.getLastUpdatedBy(), is(approval.getLastUpdatedBy()));
        assertThat(jsonApproval.getLastUpdatedAt().getTime(), is(approval.getLastUpdatedAt().getTime()));
    }

}
