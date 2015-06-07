package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.accessor.filter.InquiryFilter;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.persistence.data.InquiryReadDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Accessor for Inquiry domain objects.
 */
@Service("inquiryAccessor")
public class InquiryAccessor extends BaseAccessor<Inquiry> {

    private static final Function<Inquiry, Long> INQUIRY_ID_FUNCTION = new Function<Inquiry, Long>() {
        @Override
        public Long apply(Inquiry inquiry) {
            return inquiry.getId();
        }
    };

    @Inject
    private InquiryReadDao inquiryReadDao;

    @Override
    public List<Inquiry> find(Filter<Inquiry> filter) {
        if (Boolean.TRUE.equals(filter.getBooleanCriterion(InquiryFilter.UNSUBMITTED))) {
            return loadInquiryBasket(filter);
        } else {
            return findPendingInquiries(filter);
        }
    }

    private List<Inquiry> loadInquiryBasket(Filter<Inquiry> filter) {
        return Lists.newArrayList(inquiryReadDao.loadBasket(filter.getLongCriterion(InquiryFilter.SENDER_ID)));
    }

    private List<Inquiry> findPendingInquiries(Filter<Inquiry> filter) {
        List<Inquiry> inquiries = findPendingInquiriesInDatabase(filter);
        hydratePendingInquiries(inquiries);
        return inquiries;
    }

    private List<Inquiry> findPendingInquiriesInDatabase(Filter<Inquiry> filter) {
        QueryCriteria criteria = inquiryReadDao.createQueryCriteria(filter.getLongCriterion(InquiryFilter.SENDER_ID),
                filter.getBooleanCriterion(InquiryFilter.DISAPPROVED),
                filter.getStringCriterion(InquiryFilter.ORGINATOR_COMPANY), filter.getSortBy(),
                filter.isSortDescending(), filter.getStartItem(), filter.getCount());
        return inquiryReadDao.findPendingInquiries(criteria);
    }

    private void hydratePendingInquiries(List<Inquiry> inquiries) {
        List<Long> inquiryIds = Lists.transform(inquiries, INQUIRY_ID_FUNCTION);
        Multimap<Long, Long> productIdsByInquiry = inquiryReadDao.findProductsForPendingInquiries(inquiryIds);
        Multimap<Long, MessageTag> messageTagsByInquiry = inquiryReadDao.findMessageTagsForPendingInquiries(inquiryIds);

        for (Inquiry inquiry : inquiries) {
            inquiry.addProductIds(productIdsByInquiry.get(inquiry.getId()));
            inquiry.addMessageTags(messageTagsByInquiry.get(inquiry.getId()));
        }
    }

    @Override
    public Inquiry load(Long id, Locale locale) {
        Inquiry inquiry = inquiryReadDao.loadPendingInquiry(id);
        hydratePendingInquiries(Lists.newArrayList(inquiry));
        return inquiry;
    }

}
