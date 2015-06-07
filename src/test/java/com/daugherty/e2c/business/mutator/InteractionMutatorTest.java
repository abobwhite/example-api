package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageType;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.mail.sender.BuyLeadMessageReplyMailSender;
import com.daugherty.e2c.mail.sender.ProductMessageReplyMailSender;
import com.daugherty.e2c.persistence.data.InteractionWriteDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcMessageDao;

@RunWith(MockitoJUnitRunner.class)
public class InteractionMutatorTest {

    private static final Company BUYER_COMPANY = new Company("buyer", null, null, null, null, null, null, null, null,
            null, null);
    private static final Party BUYER = new Party(586L, "jKNzKB04", null, BUYER_COMPANY, null, PartyType.BUYER, null, null);
    private static final Company SUPPLIER_COMPANY = new Company("supplier", null, null, null, null, null, null, null,
            null, null, null);
    private static final Party ENGLISH_SUPPLIER = new Party(666L, "pBVQwo0b", null, SUPPLIER_COMPANY, null, PartyType.SUPPLIER,
            null, null);

    @Mock
    private Validator<Interaction> interactionValidator;
    @Mock
    private JdbcMessageDao messageDao;
    @Mock
    private InteractionWriteDao interactionWriteDao;
    @Mock
    private ProductMessageReplyMailSender productMessageReplyMailSender;
    @Mock
    private BuyLeadMessageReplyMailSender buyLeadMessageReplyMailSender;

    @InjectMocks
    private final InteractionMutator mutator = new InteractionMutator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createInteractionUpdatesDatabaseAndSendsEmail() throws Exception {
        Interaction interaction = new Interaction(new Message(1L), "body", BUYER, ENGLISH_SUPPLIER);

        Interaction persistedInteraction = new Interaction(421L, "body", BUYER, ENGLISH_SUPPLIER, new Date(), false);
        when(interactionWriteDao.insert(interaction)).thenReturn(persistedInteraction);
        when(messageDao.load(1L)).thenReturn(new Message("subject", BUYER, persistedInteraction, MessageType.PRODUCT));
        Interaction createdInteraction = mutator.create(interaction);

        assertThat(createdInteraction, is(persistedInteraction));
        verify(interactionValidator).validate(interaction);
        verify(interactionWriteDao).insert(interaction);
        verify(productMessageReplyMailSender).send(persistedInteraction);
    }

    @Test
    public void createInvalidMessageDoesNothingButThrowValidationException() throws Exception {
        expectedException.expect(ValidationException.class);

        Interaction interaction = new Interaction(null, "body", BUYER, ENGLISH_SUPPLIER);
        doThrow(new ValidationException(new ValidationError())).when(interactionValidator).validate(interaction);

        mutator.create(interaction);

        verify(interactionValidator).validate(interaction);
        verifyZeroInteractions(interactionWriteDao, productMessageReplyMailSender);
    }

    @Test
    public void createIgnoresNonEssentialMailFailureException() throws Exception {
        Interaction interaction = new Interaction(new Message(1L), "body", BUYER, ENGLISH_SUPPLIER);

        Interaction persistedInteraction = new Interaction(421L, "body", BUYER, ENGLISH_SUPPLIER, new Date(), false);
        when(interactionWriteDao.insert(interaction)).thenReturn(persistedInteraction);
        when(messageDao.load(1L)).thenReturn(new Message("subject", BUYER, persistedInteraction, MessageType.PRODUCT));

        doThrow(new NonEssentialMailFailureException(421L, "url", new Email(), new RuntimeException("Email sucks")))
                .when(productMessageReplyMailSender).send(persistedInteraction);

        Interaction createdInteraction = mutator.create(interaction);

        assertThat(createdInteraction, is(persistedInteraction));
        verify(interactionValidator).validate(interaction);
        verify(interactionWriteDao).insert(interaction);
        verify(productMessageReplyMailSender).send(persistedInteraction);
    }

    @Test
    public void updateInteractionUpdatesDatabase() throws Exception {
        Interaction interaction = new Interaction(421L, "body", BUYER, ENGLISH_SUPPLIER, new Date(), false);

        Interaction updatedInteraction = mutator.update(interaction);

        assertThat(updatedInteraction, is(interaction));
        verifyZeroInteractions(interactionValidator, productMessageReplyMailSender);
        verify(interactionWriteDao).updateReadIndicator(interaction);
    }

}
