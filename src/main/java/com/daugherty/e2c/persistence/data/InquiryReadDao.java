package com.daugherty.e2c.persistence.data;

import java.util.List;

import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Multimap;

/**
 * Defines database read operations for Inquiry domain objects.
 */
public interface InquiryReadDao {

    Inquiry loadBasket(Long partyId);

    Multimap<Party, Long> findSuppliersForProductsInBasket(Long partyId);

    Inquiry loadPendingInquiry(Long inquiryId);

    Multimap<Party, Long> findSuppliersForProductsInPendingInquiry(Long id);

    QueryCriteria createQueryCriteria(Long senderId, Boolean disapproved, String orginatorCompany, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count);

    List<Inquiry> findPendingInquiries(QueryCriteria queryCriteria);

    Multimap<Long, Long> findProductsForPendingInquiries(List<Long> inquiryIds);

    Multimap<Long, MessageTag> findMessageTagsForPendingInquiries(List<Long> inquiryIds);

}
