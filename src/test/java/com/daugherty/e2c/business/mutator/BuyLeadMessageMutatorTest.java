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
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.mail.sender.BuyLeadMessageMailSender;
import com.daugherty.e2c.persistence.data.BuyLeadMessageWriteDao;
import com.daugherty.e2c.persistence.data.InteractionWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class BuyLeadMessageMutatorTest {

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
    private BuyLeadMessageWriteDao buyLeadMessageWriteDao;
    @Mock
    private InteractionWriteDao interactionWriteDao;
    @Mock
    private BuyLeadMessageMailSender mailSender;

    @InjectMocks
    private final BuyLeadMessageMutator mutator = new BuyLeadMessageMutator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createMessageFromBuyerToSupplierUpdatesDatabaseAndSendsEmail() throws Exception {
        Interaction interaction = new Interaction(new Message(null), "body", BUYER, ENGLISH_SUPPLIER);
        BuyLead buyLead = new BuyLead(1L, new Party(10L), new ProductCategory(100L), new Date(), new Date());
        BuyLeadMessage message = new BuyLeadMessage("subject", BUYER, buyLead, interaction);

        BuyLeadMessage persistedMessage = new BuyLeadMessage(42L, "jKNz4P4q", "subject", BUYER, false, new Date(), false, buyLead);
        when(buyLeadMessageWriteDao.insert(message)).thenReturn(persistedMessage);

        Interaction persistedInteraction = new Interaction(421L, "body", BUYER, ENGLISH_SUPPLIER, new Date(), false);
        when(interactionWriteDao.insert(interaction)).thenReturn(persistedInteraction);

        BuyLeadMessage createdMessage = mutator.create(message);

        assertThat(createdMessage, is(persistedMessage));
        assertThat(interaction.getMessage(), is(new Message(persistedMessage.getId())));
        verify(messageValidator).validate(message);
        verify(buyLeadMessageWriteDao).insert(message);
        verify(interactionWriteDao).insert(interaction);
        verify(mailSender).send(persistedMessage);
    }

    @Test
    public void createInvalidMessageDoesNothingButThrowValidationException() throws Exception {
        expectedException.expect(ValidationException.class);

        Interaction interaction = new Interaction(null, "body", BUYER, ENGLISH_SUPPLIER);
        BuyLead buyLead = new BuyLead(1L, new Party(10L), new ProductCategory(100L), new Date(), new Date());
        BuyLeadMessage message = new BuyLeadMessage("subject", BUYER, buyLead, interaction);
        doThrow(new ValidationException(new ValidationError())).when(messageValidator).validate(message);

        mutator.create(message);

        verify(messageValidator).validate(message);
        verifyZeroInteractions(buyLeadMessageWriteDao, interactionWriteDao);
    }
}
