package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.mail.sender.BuyLeadMessageMailSender;
import com.daugherty.e2c.persistence.data.BuyLeadMessageWriteDao;
import com.daugherty.e2c.persistence.data.InteractionWriteDao;

/**
 * Mutator for Buy Lead Message domain objects.
 */
@Service("buyLeadMessageMutator")
@Transactional
public class BuyLeadMessageMutator extends BaseMutator<BuyLeadMessage> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private Validator<Message> messageValidator;
    @Inject
    private BuyLeadMessageWriteDao buyLeadMessageWriteDao;
    @Inject
    private InteractionWriteDao interactionWriteDao;
    @Inject
    private BuyLeadMessageMailSender mailSender;

    @Override
    public BuyLeadMessage create(BuyLeadMessage message) {
        messageValidator.validate(message);
        BuyLeadMessage createdMessage = buyLeadMessageWriteDao.insert(message);
        createInitialInteraction(message.getLastInteraction(), createdMessage.getId());
        sendEmail(createdMessage);
        return createdMessage;
    }

    private void createInitialInteraction(Interaction interaction, Long messageId) {
        interaction.getMessage().setId(messageId);
        interactionWriteDao.insert(interaction);
    }

    private void sendEmail(BuyLeadMessage createdMessage) {
        try {
            mailSender.send(createdMessage);
        } catch (NonEssentialMailFailureException e) {
            LOGGER.warn("Unable to send email for Message " + createdMessage.getId(), e);
        }
    }

    @Override
    public BuyLeadMessage update(BuyLeadMessage message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long messageId) {
        throw new UnsupportedOperationException();
    }

}
