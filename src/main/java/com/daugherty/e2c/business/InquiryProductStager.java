package com.daugherty.e2c.business;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.SecurityUtils;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.InquiryWriteDao;

/**
 * Stages Products for sending as part of a mass Inquiry.
 */
@Service
@Transactional
public class InquiryProductStager {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private InquiryWriteDao inquiryWriteDao;
    @Inject
    private Hashids hashids;

    public void stage(String publicPartyId, List<Long> productIds) {
        for (Long productId : productIds) {
            try {
                Long partyId = hashids.decode(publicPartyId)[0];
                inquiryWriteDao.insertProductForParty(partyId, productId);
            } catch (DuplicateKeyException e) {
                LOGGER.warn(SecurityUtils.getAuthenticatedUsername() + " already has already staged product "
                        + productId);
            }
        }
    }

    public void unstage(String publicPartyId, Long productId) {
        Long partyId = hashids.decode(publicPartyId)[0];
        inquiryWriteDao.deleteProductForParty(partyId, productId);
    }

}
