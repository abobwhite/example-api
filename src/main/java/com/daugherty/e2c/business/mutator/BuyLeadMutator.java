package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.mail.sender.BuyLeadAnonymousPartyMessageMailSender;
import com.daugherty.e2c.persistence.data.BuyLeadWriteDao;

/**
 * Mutator for Buy Lead domain objects.
 */
@Service("buyLeadMutator")
@Transactional
public class BuyLeadMutator extends BaseMutator<BuyLead> {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private Validator<BuyLead> buyLeadValidator;
    @Inject
    private BuyLeadWriteDao buyLeadWriteDao;
    @Inject
    private BuyLeadAnonymousPartyMessageMailSender mailSender;

    @Override
    public BuyLead create(BuyLead buyLead) {
        buyLeadValidator.validate(buyLead);

        BuyLead createdBuyLead = buyLeadWriteDao.insert(buyLead);

        if (PartyType.ANONYMOUS.equals(buyLead.getRequester().getPartyType())) {
            try {
                mailSender.send(buyLead);
            } catch (NonEssentialMailFailureException e) {
                LOGGER.warn("Unable to send email for Buy Lead " + createdBuyLead.getId(), e);
            }
        }

        return createdBuyLead;
    }

    @Override
    public BuyLead update(BuyLead buyLead) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }

}
