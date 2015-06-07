package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.e2c.mail.sender.RegistrationApprovedMailSender;
import com.daugherty.e2c.mail.sender.RegistrationCompletionMailSender;
import com.daugherty.e2c.persistence.data.BuyerApprovalWriteDao;

/**
 * Mutator for BuyerApproval domain objects.
 */
@Service("buyerApprovalMutator")
@Transactional
public class BuyerApprovalMutator extends BaseMutator<BuyerApproval> {

    @Inject
    private BuyerApprovalWriteDao buyerApprovalWriteDao;
    @Inject
    private RegistrationCompletionMailSender registrationCompletionMailSender;
    @Inject
    private RegistrationApprovedMailSender approvalMailSender;

    @Override
    public BuyerApproval create(BuyerApproval approval) {
        BuyerApproval createdApproval = buyerApprovalWriteDao.insert(approval);
        sendCompletionEmailIfFirstVersion(approval);
        return createdApproval;
    }

    private void sendCompletionEmailIfFirstVersion(BuyerApproval approval) {
        if (approval.getVersion() == 1) {
            registrationCompletionMailSender.sendToBuyer(approval.getEmailWithPersonalName());
        }
    }

    @Override
    public BuyerApproval update(BuyerApproval approval) {
        BuyerApproval updatedApproval = buyerApprovalWriteDao.update(approval);
        sendApprovalEmailIfFirstVersionApproval(approval);
        return updatedApproval;
    }

    private void sendApprovalEmailIfFirstVersionApproval(BuyerApproval approval) {
        if (ApprovalStatus.APPROVED.equals(approval.getApprovalStatus()) && approval.getVersion() == 1) {
            approvalMailSender.sendToBuyer(approval.getEmailWithPersonalName());
        }
    }

    @Override
    public void delete(Long entityId) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
