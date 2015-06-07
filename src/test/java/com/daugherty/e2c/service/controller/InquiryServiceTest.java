package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.InquiryProductStager;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.InquiryFilter;
import com.daugherty.e2c.business.mapper.InquiryMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonInquiries;
import com.daugherty.e2c.service.json.JsonInquiry;
import com.daugherty.e2c.service.json.JsonParty;
import com.daugherty.e2c.service.json.JsonProductIds;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class InquiryServiceTest {

    private static final long USER_PARTY_ID = 42L;

    @Mock
    private Accessor<Inquiry> inquiryAccessor;
    @Mock
    private Mutator<Inquiry> inquiryMutator;
    @Mock
    private InquiryProductStager productStager;
    @Mock
    private DocumentUrlFactory documentUrlFactory;
    @Mock
    private InquiryMapper inquiryMapper;

    @InjectMocks
    private final InquiryService service = new InquiryService();

    private Authentication originalAuthentication;

    @Captor
    private ArgumentCaptor<Filter<Inquiry>> filterCaptor;

    @Before
    public void rememberAuthentication() {
        originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
    }

    @After
    public void restoreAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveInquiryBasketDelegatesToAccessor() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(USER_PARTY_ID));

        Inquiry inquiry = new Inquiry(USER_PARTY_ID, Lists.newArrayList(1L, 2L, 3L));
        when(inquiryAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(inquiry));

        JsonInquiry basket = service.retrieveInquiryBasket(Locale.ENGLISH);

        verify(inquiryAccessor).find(filterCaptor.capture());
        Filter<Inquiry> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(InquiryFilter.class)));
        assertThat(filter.getLongCriterion(InquiryFilter.SENDER_ID), is(USER_PARTY_ID));
        assertThat(filter.getBooleanCriterion(InquiryFilter.UNSUBMITTED), is(true));
        assertThat(filter.getBooleanCriterion(InquiryFilter.DISAPPROVED), is(nullValue()));
        assertThat(filter.getSortBy(), is(nullValue()));
        assertThat(filter.isSortDescending(), is(false));
        assertThat(filter.getStartItem(), is(1));
        assertThat(filter.getCount(), is(1));

        assertThat(basket, is(notNullValue()));
        assertThat(basket.getId(), is(42L));
    }

    @Test
    public void stageProductsDelegatesToInquiryProductStager() throws Exception {
        JsonProductIds jsonProductIds = new JsonProductIds();
        jsonProductIds.add(42L);

        service.stageProducts(jsonProductIds, "pBVQJNb4");

        verify(productStager).stage("pBVQJNb4", jsonProductIds);
    }

    @Test
    public void unstageProductDelegatesToInquiryProductStager() throws Exception {
        service.unstageProduct(42L, "pBVQJNb4");

        verify(productStager).unstage("pBVQJNb4", 42L);
    }

    @Test
    public void submitInquiryBasketDelegatesToMutator() throws Exception {
        JsonInquiry requestInquiry = new JsonInquiry();
        JsonParty originator = new JsonParty();
        originator.setId("jKNz4P4q");
        originator.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        originator.setPartyType(PartyType.ANONYMOUS.toString());
        requestInquiry.setOriginator(originator);
        requestInquiry.setProductIds(Lists.newArrayList(1L, 2L, 3L));
        requestInquiry.setSubject("subject");
        requestInquiry.setBody("body");
        requestInquiry.setMessageTags(Lists.newArrayList(MessageTag.SPECIFICATIONS.getName(),
                MessageTag.DELIVERY_TIME.getName()));

        Inquiry persistedInquiry = new Inquiry(242L, Lists.<Long> newArrayList());
        when(inquiryMapper.toNewDomainObject(requestInquiry, Locale.ENGLISH)).thenReturn(persistedInquiry);
        when(inquiryMutator.create(inquiryMapper.toNewDomainObject(requestInquiry, Locale.ENGLISH))).thenReturn(persistedInquiry);

        JsonInquiry responseInquiry = service.submitInquiryBasket(requestInquiry, Locale.ENGLISH);

        assertThat(responseInquiry.getId(), is(persistedInquiry.getId()));
        assertThat(responseInquiry.getProductIds().isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveInquiriesNeedingApprovalDelegatesToAccessor() throws Exception {
        Company buyerCompany = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(11L, "g3Vl8VbWZ", null, buyerCompany, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);

        Company supplierCompany = new Company("supplier", null, null, null, null, null, null, null, null, null, null);
        Party supplier = new Party(12L, "OgPjrPqE", null, supplierCompany, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);

        Inquiry inquiry1 = new Inquiry(1L, buyer, Lists.newArrayList(6L, 7L), "subject1", "body1",
                Lists.newArrayList(MessageTag.DELIVERY_TIME));
        Inquiry inquiry2 = new Inquiry(2L, supplier, Lists.newArrayList(8L, 9L), "subject2", "body2",
                Lists.newArrayList(MessageTag.SPECIFICATIONS));
        when(inquiryAccessor.find(any(Filter.class))).thenReturn(Lists.newArrayList(inquiry1, inquiry2));

        JsonInquiries jsonInquiries = service.retrieveInquiriesNeedingApproval(42L, Boolean.TRUE, null, "whatever",
                Boolean.TRUE, 26, 50, Locale.ENGLISH);

        verify(inquiryAccessor).find(filterCaptor.capture());
        Filter<Inquiry> filter = filterCaptor.getValue();
        assertThat(filter, is(instanceOf(InquiryFilter.class)));
        assertThat(filter.getLongCriterion(InquiryFilter.SENDER_ID), is(42L));
        assertThat(filter.getBooleanCriterion(InquiryFilter.UNSUBMITTED), is(false));
        assertThat(filter.getBooleanCriterion(InquiryFilter.DISAPPROVED), is(true));
        assertThat(filter.getSortBy(), is("whatever"));
        assertThat(filter.isSortDescending(), is(true));
        assertThat(filter.getStartItem(), is(26));
        assertThat(filter.getCount(), is(50));

        assertThat(jsonInquiries, is(notNullValue()));
        assertThat(jsonInquiries.size(), is(2));
        assertThat(jsonInquiries.get(0).getId(), is(inquiry1.getId()));
        assertThat(jsonInquiries.get(1).getId(), is(inquiry2.getId()));
    }

    @Test
    public void retrievePendingInquieryByIdDelegatesToAccessor() throws Exception {
        Company buyerCompany = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(11L, "g3Vl8VbWZ", null, buyerCompany, ApprovalStatus.APPROVED, PartyType.BUYER, null, null);

        Inquiry inquiry = new Inquiry(42L, buyer, Lists.newArrayList(6L, 7L), "subject", "body",
                Lists.newArrayList(MessageTag.DELIVERY_TIME));
        when(inquiryAccessor.load(42L, Locale.ENGLISH)).thenReturn(inquiry);

        JsonInquiry jsonInquiry = service.retrieveInquiry(42L);

        assertThat(jsonInquiry.getBody(), is("body"));
        assertThat(jsonInquiry.getSubject(), is("subject"));
        assertThat(jsonInquiry.getOriginator().getEnglishCompanyName(), is("buyer"));
    }

    @Test
    public void approvePendingInquiryLoadsAndUpdatesInquiry() throws Exception {
        Company buyerCompany = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(11L, "g3Vl8VbWZ", null, buyerCompany, null, PartyType.BUYER, null, null);

        Inquiry inquiry = new Inquiry(42L, buyer, Lists.newArrayList(6L, 7L), "subject", "body",
                Lists.newArrayList(MessageTag.DELIVERY_TIME));
        when(inquiryAccessor.load(42L, Locale.ENGLISH)).thenReturn(inquiry);

        service.approvePendingInquiry(42L);

        verify(inquiryMutator).update(inquiry);
    }

    @Test
    public void disapprovePendingInquiryDeletesInquiry() throws Exception {
        service.disapprovePendingInquiry(42L);

        verify(inquiryMutator).delete(42L);
    }

    private RememberMeAuthenticationToken buildAuthenticationToken(Long partyId) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.SUPPLIER, Role.BUYER);
        E2CUser user = new E2CUser("username", "password", true, 0, authorities, new Party(partyId), true);

        return new RememberMeAuthenticationToken("e2c", user, authorities);
    }

}
