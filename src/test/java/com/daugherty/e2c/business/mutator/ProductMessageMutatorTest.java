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
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.mail.sender.ProductMessageMailSender;
import com.daugherty.e2c.persistence.data.InteractionWriteDao;
import com.daugherty.e2c.persistence.data.ProductMessageWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class ProductMessageMutatorTest {

    private static final Company BUYER_COMPANY = new Company("buyer", null, null, null, null, null, null, null, null,
            null, null);
    private static final Party BUYER = new Party(586L, "jKNzKB04", null, BUYER_COMPANY, null, PartyType.BUYER, null, null);
    private static final Company SUPPLIER_COMPANY = new Company("supplier", null, null, null, null, null, null, null,
            null, null, null);
    private static final Party ENGLISH_SUPPLIER = new Party(666L, "pBVQwo0b", null, SUPPLIER_COMPANY, null, PartyType.SUPPLIER,
            null, null);

    @Mock
    private Validator<Message> messageValidator;
    @Mock
    private ProductMessageWriteDao productMessageWriteDao;
    @Mock
    private InteractionWriteDao interactionWriteDao;
    @Mock
    private ProductMessageMailSender mailSender;

    @InjectMocks
    private final ProductMessageMutator mutator = new ProductMessageMutator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createMessageFromBuyerToSupplierUpdatesDatabaseAndSendsEmail() throws Exception {
        Interaction interaction = new Interaction(new Message(null), "body", BUYER, ENGLISH_SUPPLIER);
        ProductMessage message = new ProductMessage("subject", BUYER, 242L, interaction);

        ProductMessage persistedMessage = new ProductMessage(42L, "jKNz4P4q", "subject", BUYER, false, new Date(), false);
        when(productMessageWriteDao.insert(message)).thenReturn(persistedMessage);

        Interaction persistedInteraction = new Interaction(421L, "body", BUYER, ENGLISH_SUPPLIER, new Date(), false);
        when(interactionWriteDao.insert(interaction)).thenReturn(persistedInteraction);

        ProductMessage createdMessage = mutator.create(message);

        assertThat(createdMessage, is(persistedMessage));
        assertThat(interaction.getMessage(), is(new Message(persistedMessage.getId())));
        verify(messageValidator).validate(message);
        verify(productMessageWriteDao).insert(message);
        verify(interactionWriteDao).insert(interaction);
        verify(mailSender).send(persistedMessage);
    }

    @Test
    public void createInvalidMessageDoesNothingButThrowValidationException() throws Exception {
        expectedException.expect(ValidationException.class);

        Interaction interaction = new Interaction(null, "body", BUYER, ENGLISH_SUPPLIER);
        ProductMessage message = new ProductMessage("subject", BUYER, 242L, interaction);
        doThrow(new ValidationException(new ValidationError())).when(messageValidator).validate(message);

        mutator.create(message);

        verify(messageValidator).validate(message);
        verifyZeroInteractions(productMessageWriteDao, interactionWriteDao, mailSender);
    }

    @Test
    public void createIgnoresNonEssentialMailFailureException() throws Exception {
        Interaction interaction = new Interaction(new Message(null), "body", BUYER, ENGLISH_SUPPLIER);
        ProductMessage message = new ProductMessage("subject", BUYER, 242L, interaction);

        ProductMessage persistedMessage = new ProductMessage(42L, "jKNz4P4q", "subject", BUYER, false, new Date(), false);
        when(productMessageWriteDao.insert(message)).thenReturn(persistedMessage);

        Interaction persistedInteraction = new Interaction(421L, "body", BUYER, ENGLISH_SUPPLIER, new Date(), false);
        when(interactionWriteDao.insert(interaction)).thenReturn(persistedInteraction);

        doThrow(new NonEssentialMailFailureException(421L, "url", new Email(), new RuntimeException("Email sucks")))
                .when(mailSender).send(persistedMessage);

        ProductMessage createdMessage = mutator.create(message);

        assertThat(createdMessage, is(persistedMessage));
        assertThat(interaction.getMessage(), is(new Message(persistedMessage.getId())));
        verify(messageValidator).validate(message);
        verify(productMessageWriteDao).insert(message);
        verify(interactionWriteDao).insert(interaction);
        verify(mailSender).send(persistedMessage);
    }

}
