package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageType;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.mail.sender.BuyLeadMessageReplyMailSender;
import com.daugherty.e2c.mail.sender.ProductMessageReplyMailSender;
import com.daugherty.e2c.persistence.data.InteractionWriteDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcMessageDao;

/**
 * Mutator for Interaction domain objects.
 */
@Service("interactionMutator")
@Transactional
public class InteractionMutator extends BaseMutator<Interaction> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private Validator<Interaction> interactionValidator;
    @Inject
    private JdbcMessageDao messageDao;
    @Inject
    private InteractionWriteDao interactionWriteDao;
    @Inject
    private ProductMessageReplyMailSender productMessageReplyMailSender;
    @Inject
    private BuyLeadMessageReplyMailSender buyLeadMessageReplyMailSender;

    @Override
    public Interaction create(Interaction interaction) {
        interactionValidator.validate(interaction);
        Interaction createdInteraction = interactionWriteDao.insert(interaction);
        sendEmail(interaction.getMessage().getId(), createdInteraction);
        return createdInteraction;
    }

    private void sendEmail(Long messageId, Interaction createdInteraction) {
        try {
            Message message = messageDao.load(messageId);

            if (MessageType.PRODUCT.equals(message.getMessageType())) {
                productMessageReplyMailSender.send(createdInteraction);
            } else {
                buyLeadMessageReplyMailSender.send(createdInteraction);
            }

        } catch (NonEssentialMailFailureException e) {
            LOGGER.warn("Unable to send email for Message reply " + createdInteraction.getId(), e);
        }
    }

    @Override
    public Interaction update(Interaction interaction) {
        interactionWriteDao.updateReadIndicator(interaction);
        return interaction;
    }

    @Override
    public void delete(Long interactionId) {
        throw new UnsupportedOperationException();
    }

}
