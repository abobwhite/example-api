package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.daugherty.e2c.persistence.data.InquiryReadDao;
import com.daugherty.e2c.persistence.data.InquiryWriteDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Mutator for Inquiry domain objects.
 */
@Service("inquiryMutator")
@Transactional
public class InquiryMutator extends BaseMutator<Inquiry> {

    @Inject
    private Validator<Inquiry> inquiryValidator;
    @Inject
    private ApplicationParameterReadDao applicationParameterReadDao;
    @Inject
    private InquiryReadDao inquiryReadDao;
    @Inject
    private InquiryWriteDao inquiryWriteDao;
    @Inject
    private JdbcPartyDao partyDao;
    @Inject
    private Mutator<ProductMessage> productMessageMutator;
    @Inject
    private TaskExecutor messagingTaskExecutor;

    @Override
    public Inquiry create(Inquiry inquiry) {
        inquiryValidator.validate(inquiry);
        Long originatorId = inquiry.getOriginator().getId();

        Multimap<Party, Long> productsBySupplier = inquiryReadDao.findSuppliersForProductsInBasket(originatorId);
        if (numberOfReceiversRequiresAdminApproval(productsBySupplier) || isUnverified(originatorId)) {
            recordInquiryForAdminApproval(inquiry);
        } else {
            sendMessageToEachSupplier(inquiry, productsBySupplier);
        }

        inquiryWriteDao.clearBasket(originatorId);
        return createEmptyBasket(originatorId);
    }

    private boolean numberOfReceiversRequiresAdminApproval(Multimap<Party, Long> productsBySupplier) {
        return productsBySupplier.keySet().size() >= getRecipientApprovalThreshold();
    }

    private boolean isUnverified(Long originatorId) {
        try {
            Party party = partyDao.loadById(originatorId);
            return party.isAnonymous() || (party.isBuyer() && !partyDao.hasBeenApprovedOnce(originatorId));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Integer getRecipientApprovalThreshold() {
        String untypedValue = applicationParameterReadDao
                .loadValueForName(ApplicationParameterReadDao.RECIPIENT_APPROVAL_THRESHOLD_NAME);
        return Integer.valueOf(untypedValue);
    }

    private void recordInquiryForAdminApproval(Inquiry inquiry) {
        inquiryWriteDao.insertPendingInquiry(inquiry);
    }

    private void sendMessageToEachSupplier(Inquiry inquiry, Multimap<Party, Long> productsBySupplier) {
        for (Party supplier : productsBySupplier.keySet()) {
            final ProductMessage message = inquiry.createMessage(supplier, productsBySupplier.get(supplier));
            messagingTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    productMessageMutator.create(message);
                }
            });
        }
    }

    private Inquiry createEmptyBasket(Long originatorId) {
        return new Inquiry(originatorId, Lists.<Long> newArrayList());
    }

    @Override
    public Inquiry update(Inquiry inquiry) {
        approvePendingInquiry(inquiry);
        return createEmptyBasket(null);
    }

    private void approvePendingInquiry(Inquiry inquiry) {
        Multimap<Party, Long> productsBySupplier = inquiryReadDao.findSuppliersForProductsInPendingInquiry(inquiry
                .getId());
        sendMessageToEachSupplier(inquiry, productsBySupplier);
        inquiryWriteDao.deletePendingInquiry(inquiry.getId());
    }

    @Override
    public void delete(Long inquiryId) {
        inquiryWriteDao.disapprovePendingInquiry(inquiryId);
    }

}
