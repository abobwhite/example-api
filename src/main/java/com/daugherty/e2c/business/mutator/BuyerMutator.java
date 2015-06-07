package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.AnonymousPartyExistsException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.mail.sender.RegistrationConfirmationMailSender;
import com.daugherty.e2c.persistence.data.BuyerWriteDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;

/**
 * Mutator for Buyer domain objects.
 */
@Service("buyerMutator")
@Transactional
public class BuyerMutator extends BaseMutator<Buyer> {

    @Inject
    private Validator<Buyer> buyerValidator;
    @Inject
    private BuyerWriteDao buyerWriteDao;
    @Inject
    private PendingUserWriteDao pendingUserWriteDao;
    @Inject
    private PartyBusinessTypeWriteDao businessTypeWriteDao;
    @Inject
    private RegistrationConfirmationMailSender mailSender;
    @Inject
    private ApprovalStateTransitionVisitor changeDataVisitor;

    @Override
    public Buyer create(Buyer buyer) {
        Buyer persistedBuyer = null;

        try {
            buyerValidator.validate(buyer);
            persistedBuyer = buyerWriteDao.insert(buyer);
        } catch (AnonymousPartyExistsException e) {
            persistedBuyer = switchExistingAnonymousToBuyer(buyer, e.getAnonymous());
        }

        PendingUser pendingUser = buyer.getPendingUser();
        pendingUser.setPartyId(persistedBuyer.getId());
        pendingUserWriteDao.insert(pendingUser);

        mailSender.send(persistedBuyer.getContact().getEmailAddress(), pendingUser.getUsername(),
                pendingUser.getConfirmationToken(), Language.CHINESE);

        return persistedBuyer;
    }

    @Override
    public Buyer update(Buyer buyer) {
        buyer.visit(changeDataVisitor);
        buyerValidator.validate(buyer);
        buyerWriteDao.update(buyer);
        businessTypeWriteDao.updateBusinessTypes(buyer.getSnapshotId(), buyer.getCompany().getBusinessTypes());
        return buyer;
    }

    @Override
    public void delete(Long entityId) {
        throw new UnsupportedOperationException();
    }

    private Buyer switchExistingAnonymousToBuyer(Buyer buyer, Party anonymous) {
        anonymous.getContact().setCountry(buyer.getContact().getCountry());

        return buyerWriteDao.update(new Buyer(anonymous.getId(), anonymous.getPublicId(), anonymous.getContact(),
                anonymous.getCompany(), ApprovalStatus.UNPROFILED, anonymous.getVersion(), anonymous.getSnapshotId(),
                null));
    }

}
