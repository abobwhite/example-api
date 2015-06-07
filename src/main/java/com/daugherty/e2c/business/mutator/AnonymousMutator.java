package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.business.AnonymousPartyExistsException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.persistence.data.AnonymousWriteDao;
import com.daugherty.e2c.persistence.data.PartyBusinessTypeWriteDao;

/**
 * Mutator for Anonymous domain objects.
 */
@Service("anonymousMutator")
@Transactional
public class AnonymousMutator extends BaseMutator<Anonymous> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private Validator<Anonymous> anonymousValidator;
    @Inject
    private AnonymousWriteDao anonymousWriteDao;
    @Inject
    private PartyBusinessTypeWriteDao businessTypeWriteDao;

    @Override
    public Anonymous create(Anonymous anonymous) {
        try {
            anonymousValidator.validate(anonymous);
            Anonymous createdAnonymous = anonymousWriteDao.insert(anonymous);

            if (!CollectionUtils.isEmpty(anonymous.getCompany().getBusinessTypes())) {
                businessTypeWriteDao.updateBusinessTypes(createdAnonymous.getSnapshotId(), anonymous.getCompany()
                        .getBusinessTypes());
            }

            return createdAnonymous;
        } catch (AnonymousPartyExistsException e) {
            LOG.debug("Anonymous party already Exists for " + anonymous.getContact().getEmailAddress());
            Party existing = e.getAnonymous();

            return anonymousWriteDao.update(new Anonymous(existing.getId(), anonymous.getPublicId(), anonymous
                    .getContact(), anonymous.getCompany(), ApprovalStatus.UNPROFILED, existing.getVersion(), existing
                    .getSnapshotId()));
        }

    }

    @Override
    public Anonymous update(Anonymous anonymous) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long entityId) {
        throw new UnsupportedOperationException();
    }

}
