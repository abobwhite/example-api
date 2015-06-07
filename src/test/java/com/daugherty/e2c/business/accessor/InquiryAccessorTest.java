package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.accessor.filter.InquiryFilter;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.persistence.data.InquiryReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class InquiryAccessorTest {

    @Mock
    private InquiryReadDao inquiryReadDao;
    @Mock
    private QueryCriteria queryCriteria;

    @InjectMocks
    private final InquiryAccessor accessor = new InquiryAccessor();

    @Test
    public void findWithUnsubmittedFilterCriterionCallsDaoLoadInquiryBasketMethod() throws Exception {
        Inquiry daoInquiry = new Inquiry(42L, Lists.newArrayList(1L, 2L, 3L));
        when(inquiryReadDao.loadBasket(42L)).thenReturn(daoInquiry);

        InquiryFilter filter = new InquiryFilter(42L, true, false, null, "whatever", true, 26, 50);
        List<Inquiry> inquiries = accessor.find(filter);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(1));
        assertThat(inquiries.get(0), is(daoInquiry));
    }

    @Test
    public void findWithSubmittedFilterCriterionCallsDaoFindPendingInquiriesMethod() throws Exception {
        long inquiryId = 42L;
        when(inquiryReadDao.createQueryCriteria(inquiryId, true, null, "whatever", true, 26, 50)).thenReturn(
                queryCriteria);

        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(11L, "g3Vl8VbW", null, company, null, PartyType.BUYER, null, null);

        List<Inquiry> daoInquiries = Lists.newArrayList(new Inquiry(inquiryId, buyer, "subject", "body", new Date()));
        when(inquiryReadDao.findPendingInquiries(queryCriteria)).thenReturn(daoInquiries);

        Multimap<Long, Long> productIdsByInquiryId = ArrayListMultimap.create();
        productIdsByInquiryId.put(inquiryId, 242L);
        productIdsByInquiryId.put(inquiryId, 808L);
        when(inquiryReadDao.findProductsForPendingInquiries(Lists.newArrayList(inquiryId))).thenReturn(
                productIdsByInquiryId);

        Multimap<Long, MessageTag> messageTagsByInquiryId = ArrayListMultimap.create();
        messageTagsByInquiryId.put(inquiryId, MessageTag.COMPANY_OVERVIEW);
        messageTagsByInquiryId.put(inquiryId, MessageTag.DELIVERY_TIME);
        when(inquiryReadDao.findMessageTagsForPendingInquiries(Lists.newArrayList(inquiryId))).thenReturn(
                messageTagsByInquiryId);

        InquiryFilter filter = new InquiryFilter(inquiryId, false, true, null, "whatever", true, 26, 50);
        List<Inquiry> inquiries = accessor.find(filter);

        assertThat(inquiries, is(daoInquiries));
        assertThat(inquiries.get(0).getProductIds().size(), is(2));
        assertThat(inquiries.get(0).getProductIds().get(0), is(242L));
        assertThat(inquiries.get(0).getProductIds().get(1), is(808L));
        assertThat(inquiries.get(0).getMessageTags().size(), is(2));
        assertThat(inquiries.get(0).getMessageTags().get(0), is(MessageTag.COMPANY_OVERVIEW));
        assertThat(inquiries.get(0).getMessageTags().get(1), is(MessageTag.DELIVERY_TIME));
    }

    @Test
    public void loadCallsDaoLoadPendingInquiryMethod() throws Exception {
        long inquiryId = 42L;

        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(11L, "g3Vl8VbW", null, company, null, PartyType.BUYER, null, null);

        Inquiry daoInquiry = new Inquiry(inquiryId, buyer, "subject", "body", new Date());
        when(inquiryReadDao.loadPendingInquiry(inquiryId)).thenReturn(daoInquiry);

        Multimap<Long, Long> productIdsByInquiryId = ArrayListMultimap.create();
        productIdsByInquiryId.put(inquiryId, 242L);
        productIdsByInquiryId.put(inquiryId, 808L);
        when(inquiryReadDao.findProductsForPendingInquiries(Lists.newArrayList(inquiryId))).thenReturn(
                productIdsByInquiryId);

        Multimap<Long, MessageTag> messageTagsByInquiryId = ArrayListMultimap.create();
        messageTagsByInquiryId.put(inquiryId, MessageTag.COMPANY_OVERVIEW);
        messageTagsByInquiryId.put(inquiryId, MessageTag.DELIVERY_TIME);
        when(inquiryReadDao.findMessageTagsForPendingInquiries(Lists.newArrayList(inquiryId))).thenReturn(
                messageTagsByInquiryId);

        Inquiry inquiry = accessor.load(inquiryId, Locale.ENGLISH);

        assertThat(inquiry, is(daoInquiry));
        assertThat(inquiry.getProductIds().size(), is(2));
        assertThat(inquiry.getProductIds().get(0), is(242L));
        assertThat(inquiry.getProductIds().get(1), is(808L));
        assertThat(inquiry.getMessageTags().size(), is(2));
        assertThat(inquiry.getMessageTags().get(0), is(MessageTag.COMPANY_OVERVIEW));
        assertThat(inquiry.getMessageTags().get(1), is(MessageTag.DELIVERY_TIME));
    }

}
