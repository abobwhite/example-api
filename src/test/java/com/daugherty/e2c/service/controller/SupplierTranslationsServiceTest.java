package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

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
import com.daugherty.e2c.business.accessor.filter.SupplierTranslationFilter;
import com.daugherty.e2c.business.mapper.ProfileTranslationMapper;
import com.daugherty.e2c.business.mapper.SupplierTranslationMapper;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.service.json.JsonProfileTranslation;
import com.daugherty.e2c.service.json.JsonSupplierTranslation;
import com.daugherty.e2c.service.json.JsonSupplierTranslations;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class SupplierTranslationsServiceTest {
    @Mock
    private Accessor<SupplierTranslation> supplierTranslationAccessor;
    @Mock
    private Accessor<ProfileTranslation> profileTranslationAccessor;
    @Mock
    private Accessor<Supplier> latestSupplierAccessor;
    @Mock
    private Mutator<SupplierTranslation> supplierTranslationMutator;
    @Mock
    private Mutator<ProfileTranslation> profileTranslationMutator;
    @Mock
    private ApprovalStateTransitionVisitor approveVisitor;
    @Mock
    private ApprovalStateTransitionVisitor moderateVisitor;
    @Mock
    private ProfilePublisher profilePublisher;
    @Mock
    private Accessor<Membership> membershipAccessor;
    @Mock
    private Mutator<Membership> supplierApprovalMembershipMutator;
    @Mock
    private SupplierTranslationMapper supplierTranslationMapper;
    @Mock
    private ProfileTranslationMapper profileTranslationMapper;

    @InjectMocks
    private final SupplierTranslationsService service = new SupplierTranslationsService();

    @Captor
    private ArgumentCaptor<Filter<SupplierTranslation>> filterCaptor;
    @Captor
    private ArgumentCaptor<ProfileTranslation> profileTranslationCaptor;

    @Test
    public void approveTransalationDelegatesToProfilePublisher() {

        MembershipLevel membershipLevel = new MembershipLevel(101L, 2, new BigDecimal("1000.00"), 6, 5, 2147483647,
                600, true, 5, false, false, false, 0, false, false, false, true, false, false, false);

        Membership membership = new Membership(42L, 1L, membershipLevel, ApprovalStatus.PAID, 1, new Date(),
                new Date(), new Date(), BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE, null, BigDecimal.ZERO,
                BigDecimal.ZERO, new ArrayList<MembershipDiscount>());

        Supplier supplier = new Supplier(123L, "Jp0brJ0j", null, null, null, 0, 1L, null, null, null);
        supplier.getMemberships().add(membership);

        SupplierTranslation supplierTranslation = new SupplierTranslation(1L, "vXP9l0Or", "title",
                ApprovalStatus.APPROVED, 4, 232L, SupplierTranslation.PROFILE_TYPE, false, "lastUpdater", new Date());

        when(supplierTranslationAccessor.load("vXP9l0Or", Locale.ENGLISH)).thenReturn(supplierTranslation);
        when(latestSupplierAccessor.load("vXP9l0Or", Locale.ENGLISH)).thenReturn(supplier);
        when(membershipAccessor.load(42L, Locale.ENGLISH)).thenReturn(membership);

        service.approveTranslation("vXP9l0Or");

        verify(supplierTranslationMutator).update(supplierTranslation);
        verify(supplierApprovalMembershipMutator).update(membership);
        verify(profilePublisher).publish("vXP9l0Or");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveSupplierTranslationsWithQueryStringArgumentsDelegatesToAccessor() {
        String titlePrefix = "prefix";
        String translationType = SupplierTranslation.PROFILE_TYPE;

        SupplierTranslation translation = new SupplierTranslation(23L, "Jp0bwPjG", titlePrefix + "suffix",
                ApprovalStatus.PENDING_TRANSLATION, 1, 1L, translationType, false, "lastUpdater", new Date());

        when(supplierTranslationAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(translation));

        JsonSupplierTranslation json = new JsonSupplierTranslation();
        json.setId(translation.getPublicId());
        json.setApprovalStatus(translation.getApprovalStatus().getName());
        json.setTitle(translation.getTitle());
        json.setTranslationType(translation.getType());
        json.setTranslated(translation.isTranslated() ? "Yes" : "No");
        json.setLastUpdatedBy(translation.getLastUpdatedBy());
        json.setLastUpdatedAt(translation.getLastUpdatedAt());

        when(supplierTranslationMapper.fromExistingDomainObject(translation)).thenReturn(json);

        JsonSupplierTranslations jsonTranslations = service.retrieveSupplierTranslations(titlePrefix, translationType,
                ApprovalStatus.PENDING_TRANSLATION.getName(), "title", true, 0, 20);

        verify(supplierTranslationAccessor).find(filterCaptor.capture());
        Filter<SupplierTranslation> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(SupplierTranslationFilter.class)));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TITLE_PREFIX), is(titlePrefix));
        assertThat(filter.getStringCriterion(SupplierTranslationFilter.TYPE), is(translationType));
        assertThat(filter.getBooleanCriterion(SupplierTranslationFilter.TRANSLATED), is(translation.isTranslated()));
        assertThat(filter.getSortBy(), is("title"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(0));
        assertThat(filter.getCount(), is(20));

        assertThat(jsonTranslations, is(notNullValue()));
        assertThat(jsonTranslations.size(), is(1));
        assertThat(jsonTranslations.get(0).getId(), is(translation.getPublicId()));
        assertThat(jsonTranslations.get(0).getTitle(), is(translation.getTitle()));
        assertThat(jsonTranslations.get(0).getApprovalStatus(), is(translation.getApprovalStatus().getName()));
        assertThat(jsonTranslations.get(0).getTranslationType(), is(translationType));
        assertThat(jsonTranslations.get(0).getLastUpdatedBy(), is(translation.getLastUpdatedBy()));
        assertThat(jsonTranslations.get(0).getLastUpdatedAt(), is(translation.getLastUpdatedAt()));
    }

    @Test
    public void saveProfileTranslationDelegatesToMutator() {
        JsonProfileTranslation jsonTranslation = new JsonProfileTranslation();
        jsonTranslation.setCompanyDescription("description");
        jsonTranslation.setCompanyDescriptionTranslation("translated description");

        Supplier supplier = new Supplier(123L, "Jp0brJ0j", null, null, null, 0, 1L, null, null, null);

        when(latestSupplierAccessor.load(supplier.getPublicId(), Locale.CHINESE)).thenReturn(supplier);

        ProfileTranslation asSavedTranslation = new ProfileTranslation(supplier.getId(), supplier.getPublicId(),
                supplier.getSnapshotId(), "original description", "as saved translation");

        ProfileTranslation mappedTranslation = new ProfileTranslation(supplier.getId(), supplier.getPublicId(),
                supplier.getSnapshotId(), "description", "translated description");

        when(profileTranslationMapper.toExistingDomainObject(supplier, jsonTranslation)).thenReturn(mappedTranslation);

        when(profileTranslationMutator.update(any(ProfileTranslation.class))).thenReturn(asSavedTranslation);

        JsonProfileTranslation mappedJson = new JsonProfileTranslation();
        mappedJson.setId("Jp0brJ0j");
        mappedJson.setCompanyDescription("original description");
        mappedJson.setCompanyDescriptionTranslation("as saved translation");
        Map<String, String> links = Maps.newHashMap();
        links.put("supplier", "suppliers/Jp0brJ0j?latest=true");
        mappedJson.setLinks(links);

        when(profileTranslationMapper.fromExistingDomainObject(mappedTranslation, links)).thenReturn(mappedJson);

        JsonProfileTranslation savedJsonTranslation = service.saveProfileTranslation(jsonTranslation,
                supplier.getPublicId());

        assertThat(savedJsonTranslation.getCompanyDescription(), is(asSavedTranslation.getCompanyDescription()));
        assertThat(savedJsonTranslation.getCompanyDescriptionTranslation(),
                is(asSavedTranslation.getCompanyDescriptionTranslation()));

        verify(profileTranslationMutator).update(profileTranslationCaptor.capture());
        ProfileTranslation toBeSavedTranslation = profileTranslationCaptor.getValue();
        assertThat(toBeSavedTranslation.getId(), is(supplier.getId()));
        assertThat(toBeSavedTranslation.getSnapshotId(), is(supplier.getSnapshotId()));
        assertThat(toBeSavedTranslation.getCompanyDescription(), is(jsonTranslation.getCompanyDescription()));
        assertThat(toBeSavedTranslation.getCompanyDescriptionTranslation(),
                is(jsonTranslation.getCompanyDescriptionTranslation()));
    }

    @Test
    public void approveTranslationVisitsTranslationAndDelegatesToMutator() {
        String supplierId = "vXP9l0Or";
        SupplierTranslation translation = mock(SupplierTranslation.class);
        when(supplierTranslationAccessor.load(supplierId, Locale.ENGLISH)).thenReturn(translation);

        Supplier supplier = new Supplier(1L, "vXP9l0Or", null, null, null, 0, 1L, null, null, null);

        when(latestSupplierAccessor.load(supplier.getPublicId(), Locale.ENGLISH)).thenReturn(supplier);

        service.approveTranslation(supplierId);

        verify(translation).visit(approveVisitor);
        verify(supplierTranslationMutator).update(translation);
    }

    @Test
    public void sendToModeratorVisitsTranslationAndDelegatesToMutator() {
        String supplierId = "vXP9l0Or";
        SupplierTranslation translation = mock(SupplierTranslation.class);
        when(supplierTranslationAccessor.load(supplierId, Locale.ENGLISH)).thenReturn(translation);

        service.sendToModerator(supplierId);

        verify(translation).visit(moderateVisitor);
        verify(supplierTranslationMutator).update(translation);
    }

    @Test
    public void retrieveProfileTranslationDelegatesToAccessor() {
        String translationPublicId = "vXP9l0Or";
        ProfileTranslation translation = new ProfileTranslation(null, null, null, null, null);
        when(profileTranslationAccessor.load(translationPublicId, Locale.ENGLISH)).thenReturn(translation);

        service.retrieveProfileTranslation(translationPublicId);

        verify(profileTranslationAccessor).load(translationPublicId, Locale.ENGLISH);
    }
}
