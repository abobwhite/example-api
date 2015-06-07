package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.mail.sender.ProductMessageMailSender;
import com.daugherty.e2c.persistence.data.InteractionWriteDao;
import com.daugherty.e2c.persistence.data.ProductMessageWriteDao;

/**
 * Mutator for Product Message domain objects.
 */
@Service("productMessageMutator")
@Transactional
public class ProductMessageMutator extends BaseMutator<ProductMessage> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private Validator<Message> messageValidator;
    @Inject
    private ProductMessageWriteDao messageWriteDao;
    @Inject
    private InteractionWriteDao interactionWriteDao;
    @Inject
    private ProductMessageMailSender mailSender;

    @Override
    public ProductMessage create(ProductMessage message) {
        messageValidator.validate(message);
        ProductMessage createdMessage = messageWriteDao.insert(message);
        createInitialInteraction(message.getLastInteraction(), createdMessage.getId());
        sendEmail(createdMessage);
        return createdMessage;
    }

    private void createInitialInteraction(Interaction interaction, Long messageId) {
        interaction.getMessage().setId(messageId);
        interactionWriteDao.insert(interaction);
    }

    private void sendEmail(ProductMessage createdMessage) {
        try {
            mailSender.send(createdMessage);
        } catch (NonEssentialMailFailureException e) {
            LOGGER.warn("Unable to send email for Message " + createdMessage.getId(), e);
        }
    }

    @Override
    public ProductMessage update(ProductMessage message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long messageId) {
        throw new UnsupportedOperationException();
    }

}
