package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Inquiry;

/**
 * Defines database change operations for Inquiry domain objects.
 */
public interface InquiryWriteDao {

    void insertProductForParty(Long partyId, Long productId);

    void deleteProductForParty(Long partyId, Long productId);

    void clearBasket(Long partyId);

    Inquiry insertPendingInquiry(Inquiry inquiry);

    void deletePendingInquiry(Long id);

    void disapprovePendingInquiry(Long id);

    int deletePendingInquiriesByPartyId(Long partyId);

}
