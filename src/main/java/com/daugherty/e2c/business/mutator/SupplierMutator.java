package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.AnonymousPartyExistsException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.sender.RegistrationConfirmationMailSender;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.persistence.data.SupplierWriteDao;

/**
 * Mutator for Supplier domain objects.
 */
@Service("supplierMutator")
@Transactional
public class SupplierMutator extends BaseMutator<Supplier> {

    @Inject
    private Validator<Supplier> supplierValidator;
    @Inject
    private SupplierWriteDao supplierWriteDao;
    @Inject
    private PendingUserWriteDao pendingUserWriteDao;
    @Inject
    private RegistrationConfirmationMailSender mailSender;
    @Inject
    private PartyBusinessTypeWriteDao businessTypeWriteDao;
    @Inject
    private ApprovalStateTransitionVisitor changeDataVisitor;

    @Override
    public Supplier create(Supplier supplier) {
        Supplier persistedSupplier = null;

        try {
            supplierValidator.validate(supplier);
            persistedSupplier = supplierWriteDao.insert(supplier);
        } catch (AnonymousPartyExistsException e) {
            persistedSupplier = switchExistingAnonymousToSupplier(supplier, e.getAnonymous());
        }

        PendingUser pendingUser = supplier.getPendingUser();
        pendingUser.setPartyId(persistedSupplier.getId());
        pendingUserWriteDao.insert(pendingUser);

        mailSender.send(persistedSupplier.getContact().getEmailAddress(), pendingUser.getUsername(),
                pendingUser.getConfirmationToken(), supplier.getContact().getLanguage());

        return persistedSupplier;
    }

    @Override
    public Supplier update(Supplier supplier) {
        supplier.visit(changeDataVisitor);
        supplierValidator.validate(supplier);
        supplierWriteDao.update(supplier);
        businessTypeWriteDao.updateBusinessTypes(supplier.getSnapshotId(), supplier.getCompany().getBusinessTypes());
        return supplier;
    }

    @Override
    public void delete(Long supplierId) {
        throw new UnsupportedOperationException();
    }

    private Supplier switchExistingAnonymousToSupplier(Supplier supplier, Party anonymous) {
        anonymous.getContact().setCountry(supplier.getContact().getCountry());

        return supplierWriteDao.update(new Supplier(anonymous.getId(), anonymous.getPublicId(), anonymous.getContact(),
                anonymous.getCompany(), ApprovalStatus.UNPROFILED, anonymous.getVersion(), anonymous.getSnapshotId(),
                null, null, null));
    }

}