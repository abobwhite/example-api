package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.daugherty.e2c.persistence.data.InquiryReadDao;
import com.daugherty.e2c.persistence.data.InquiryWriteDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class InquiryMutatorTest {

    private static final Company BUYER_COMPANY = new Company("buyer", null, null, null, null, null, null, null, null,
            null, null);
    private static final Company ANONYMOUS_COMPANY = new Company("anonymous", null, null, null, null, null, null, null,
            null, null, null);
    private static final Company ENGLISH_SUPPLIER_COMPANY = new Company("english supplier", null, null, null, null,
            null, null, null, null, null, null);
    private static final Company SPANISH_SUPPLIER_COMPANY = new Company("spanish supplier", null, null, null, null,
            null, null, null, null, null, null);
    private static final Party BUYER = new Party(586L, "jKNzKB04", null, BUYER_COMPANY, null, PartyType.BUYER, null, null);
    private static final Party ANONYMOUS = new Party(587L, "en0vqQND", null, ANONYMOUS_COMPANY, null, PartyType.ANONYMOUS, null,
            null);
    private static final Party ENGLISH_SUPPLIER = new Party(666L, "pBVQwo0b", null, ENGLISH_SUPPLIER_COMPANY, null,
            PartyType.SUPPLIER, null, null);
    private static final Party SPANISH_SUPPLIER = new Party(667L, "Jp0bnB0j", null, SPANISH_SUPPLIER_COMPANY, null,
            PartyType.SUPPLIER, null, null);

    @Mock
    private Validator<Inquiry> inquiryValidator;
    @Mock
    private ApplicationParameterReadDao applicationParameterReadDao;
    @Mock
    private InquiryReadDao inquiryReadDao;
    @Mock
    private InquiryWriteDao inquiryWriteDao;
    @Mock
    private JdbcPartyDao partyDao;
    @Mock
    private Mutator<ProductMessage> productMessageMutator;
    @Mock
    private TaskExecutor messagingTaskExecutor;

    @InjectMocks
    private final InquiryMutator mutator = new InquiryMutator();

    @Captor
    private ArgumentCaptor<ProductMessage> productMessageCaptor;
    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createInquiryForLessThanRecipientApprovalThresholdCreatesMessagesAndClearsInquiryBasket() {
        List<Long> productIds = Lists.newArrayList(1L, 2L, 3L);
        Inquiry inquiry = new Inquiry(null, BUYER, productIds, "subject", "body",
                Lists.newArrayList(MessageTag.FOB_PRICE));

        Multimap<Party, Long> productsByRecipient = ArrayListMultimap.create();
        productsByRecipient.putAll(ENGLISH_SUPPLIER, Lists.newArrayList(1L, 3L));
        productsByRecipient.putAll(SPANISH_SUPPLIER, Lists.newArrayList(2L));
        when(inquiryReadDao.findSuppliersForProductsInBasket(BUYER.getId())).thenReturn(productsByRecipient);

        when(
                applicationParameterReadDao
                        .loadValueForName(ApplicationParameterReadDao.RECIPIENT_APPROVAL_THRESHOLD_NAME)).thenReturn(
                "3");

        when(partyDao.loadById(BUYER.getId())).thenThrow(new EmptyResultDataAccessException(1));

        Inquiry createdInquiry = mutator.create(inquiry);

        assertThatInquiryIsClear(createdInquiry, BUYER.getId());
        verify(inquiryValidator).validate(inquiry);
        verify(inquiryWriteDao, never()).insertPendingInquiry(inquiry);
        verify(inquiryWriteDao).clearBasket(BUYER.getId());

        verify(messagingTaskExecutor, times(2)).execute(runnableCaptor.capture());
        List<Runnable> messageTasks = runnableCaptor.getAllValues();
        for (Runnable messageTask : messageTasks) {
            messageTask.run();
        }
        verify(productMessageMutator, times(2)).create(productMessageCaptor.capture());
        List<ProductMessage> messages = productMessageCaptor.getAllValues();
        assertThat(messages.size(), is(2));

        ProductMessage englishSupplierMessage = findByReceiver(messages, ENGLISH_SUPPLIER);
        assertThat(englishSupplierMessage.getProductIds().size(), is(2));
        assertThat(englishSupplierMessage.getProductIds().get(0), is(1L));
        assertThat(englishSupplierMessage.getProductIds().get(1), is(3L));
        assertThat(englishSupplierMessage.getOtherParty(), is(BUYER));
        assertThat(englishSupplierMessage.getSubject(), is(inquiry.getSubject()));
        assertThat(englishSupplierMessage.getMessageTags(), is(inquiry.getMessageTags()));
        assertThat(englishSupplierMessage.getLastInteraction().getBody(), is(inquiry.getBody()));
        assertThat(englishSupplierMessage.getLastInteraction().getSender(), is(inquiry.getOriginator()));
        assertThat(englishSupplierMessage.getLastInteraction().getReceiver(), is(ENGLISH_SUPPLIER));

        ProductMessage spanishSupplierMessage = findByReceiver(messages, SPANISH_SUPPLIER);
        assertThat(spanishSupplierMessage.getProductIds().size(), is(1));
        assertThat(spanishSupplierMessage.getProductIds().get(0), is(2L));
        assertThat(spanishSupplierMessage.getOtherParty(), is(BUYER));
        assertThat(spanishSupplierMessage.getSubject(), is(inquiry.getSubject()));
        assertThat(spanishSupplierMessage.getMessageTags(), is(inquiry.getMessageTags()));
        assertThat(spanishSupplierMessage.getLastInteraction().getBody(), is(inquiry.getBody()));
        assertThat(spanishSupplierMessage.getLastInteraction().getSender(), is(inquiry.getOriginator()));
        assertThat(spanishSupplierMessage.getLastInteraction().getReceiver(), is(SPANISH_SUPPLIER));
    }

    @Test
    public void createInquiryAtOrBeyondRecipientApprovalThresholdSavesPendingMessagesAndClearsInquiryBasket() {
        List<Long> productIds = Lists.newArrayList(1L, 2L, 3L);
        Inquiry inquiry = new Inquiry(null, BUYER, productIds, "subject", "body",
                Lists.newArrayList(MessageTag.FOB_PRICE));

        Multimap<Party, Long> productsByRecipient = ArrayListMultimap.create();
        productsByRecipient.putAll(ENGLISH_SUPPLIER, Lists.newArrayList(1L, 3L));
        productsByRecipient.putAll(SPANISH_SUPPLIER, Lists.newArrayList(2L));
        when(inquiryReadDao.findSuppliersForProductsInBasket(BUYER.getId())).thenReturn(productsByRecipient);

        when(
                applicationParameterReadDao
                        .loadValueForName(ApplicationParameterReadDao.RECIPIENT_APPROVAL_THRESHOLD_NAME)).thenReturn(
                "2");

        Inquiry createdInquiry = mutator.create(inquiry);

        assertThatInquiryIsClear(createdInquiry, BUYER.getId());
        verify(inquiryValidator).validate(inquiry);
        verify(inquiryWriteDao).insertPendingInquiry(inquiry);
        verify(inquiryWriteDao).clearBasket(BUYER.getId());
        verifyZeroInteractions(productMessageMutator, messagingTaskExecutor);
    }

    @Test
    public void createInquiryForAnonymousPartySavesPendingMessagesAndClearsInquiryBasket() {
        List<Long> productIds = Lists.newArrayList(1L, 2L, 3L);
        Inquiry inquiry = new Inquiry(null, ANONYMOUS, productIds, "subject", "body",
                Lists.newArrayList(MessageTag.FOB_PRICE));

        Multimap<Party, Long> productsByRecipient = ArrayListMultimap.create();
        productsByRecipient.putAll(ENGLISH_SUPPLIER, Lists.newArrayList(1L, 3L));
        productsByRecipient.putAll(SPANISH_SUPPLIER, Lists.newArrayList(2L));
        when(inquiryReadDao.findSuppliersForProductsInBasket(ANONYMOUS.getId())).thenReturn(productsByRecipient);

        when(
                applicationParameterReadDao
                        .loadValueForName(ApplicationParameterReadDao.RECIPIENT_APPROVAL_THRESHOLD_NAME)).thenReturn(
                "3");

        Contact contact = new Contact(null, null, "First", "Name", null, null, "emailAddres@email.com", null, null,
                null, null, null, null, null);
        Company company = new Company("English Company Name", null, null, null, null, "website@website.com", null,
                null, null, null, null);

        Anonymous anonymousParty = new Anonymous(contact, company);

        when(partyDao.loadById(ANONYMOUS.getId())).thenReturn(anonymousParty);

        Inquiry createdInquiry = mutator.create(inquiry);

        assertThatInquiryIsClear(createdInquiry, ANONYMOUS.getId());
        verify(inquiryValidator).validate(inquiry);
        verify(inquiryWriteDao).insertPendingInquiry(inquiry);
        verify(inquiryWriteDao).clearBasket(ANONYMOUS.getId());
        verifyZeroInteractions(productMessageMutator, messagingTaskExecutor);
    }

    @Test
    public void createInquiryForUnverifiedBuyerPartySavesPendingMessagesAndClearsInquiryBasket() {
        List<Long> productIds = Lists.newArrayList(1L, 2L, 3L);
        Inquiry inquiry = new Inquiry(null, BUYER, productIds, "subject", "body",
                Lists.newArrayList(MessageTag.FOB_PRICE));

        Multimap<Party, Long> productsByRecipient = ArrayListMultimap.create();
        productsByRecipient.putAll(ENGLISH_SUPPLIER, Lists.newArrayList(1L, 3L));
        productsByRecipient.putAll(SPANISH_SUPPLIER, Lists.newArrayList(2L));
        when(inquiryReadDao.findSuppliersForProductsInBasket(BUYER.getId())).thenReturn(productsByRecipient);

        when(
                applicationParameterReadDao
                        .loadValueForName(ApplicationParameterReadDao.RECIPIENT_APPROVAL_THRESHOLD_NAME)).thenReturn(
                "3");

        when(partyDao.loadById(BUYER.getId())).thenReturn(BUYER);
        when(partyDao.hasBeenApprovedOnce(BUYER.getId())).thenReturn(false);

        Inquiry createdInquiry = mutator.create(inquiry);

        assertThatInquiryIsClear(createdInquiry, BUYER.getId());
        verify(inquiryValidator).validate(inquiry);
        verify(inquiryWriteDao).insertPendingInquiry(inquiry);
        verify(inquiryWriteDao).clearBasket(BUYER.getId());
        verifyZeroInteractions(productMessageMutator, messagingTaskExecutor);
    }

    @Test
    public void createInvalidInquiryDoesNothingButThrowValidationException() {
        expectedException.expect(ValidationException.class);

        Inquiry inquiry = new Inquiry(null, BUYER, Lists.newArrayList(1L, 2L, 3L), "subject", "body",
                Lists.newArrayList(MessageTag.FOB_PRICE));
        doThrow(new ValidationException(new ValidationError())).when(inquiryValidator).validate(inquiry);

        mutator.create(inquiry);

        verify(inquiryValidator).validate(inquiry);
        verifyZeroInteractions(inquiryReadDao, inquiryWriteDao, productMessageMutator, messagingTaskExecutor);
    }

    @Test
    public void updateApprovesInquirySoCreatesMessagesAndRemovesPendingInquiryData() throws Exception {
        List<Long> productIds = Lists.newArrayList(1L, 2L, 3L);
        Inquiry inquiry = new Inquiry(42L, BUYER, productIds, "subject", "body",
                Lists.newArrayList(MessageTag.FOB_PRICE));

        Multimap<Party, Long> productsByRecipient = ArrayListMultimap.create();
        productsByRecipient.putAll(ENGLISH_SUPPLIER, Lists.newArrayList(1L, 3L));
        productsByRecipient.putAll(SPANISH_SUPPLIER, Lists.newArrayList(2L));
        when(inquiryReadDao.findSuppliersForProductsInPendingInquiry(inquiry.getId())).thenReturn(productsByRecipient);

        Inquiry updatedInquiry = mutator.update(inquiry);

        assertThatInquiryIsClear(updatedInquiry, null);
        verify(inquiryWriteDao).deletePendingInquiry(inquiry.getId());
        verifyZeroInteractions(inquiryValidator);

        verify(messagingTaskExecutor, times(2)).execute(runnableCaptor.capture());
        List<Runnable> messageTasks = runnableCaptor.getAllValues();
        for (Runnable messageTask : messageTasks) {
            messageTask.run();
        }
        verify(productMessageMutator, times(2)).create(productMessageCaptor.capture());
        List<ProductMessage> messages = productMessageCaptor.getAllValues();
        assertThat(messages.size(), is(2));

        ProductMessage englishSupplierMessage = findByReceiver(messages, ENGLISH_SUPPLIER);
        assertThat(englishSupplierMessage.getProductIds().size(), is(2));
        assertThat(englishSupplierMessage.getProductIds().get(0), is(1L));
        assertThat(englishSupplierMessage.getProductIds().get(1), is(3L));
        assertThat(englishSupplierMessage.getOtherParty(), is(BUYER));
        assertThat(englishSupplierMessage.getSubject(), is(inquiry.getSubject()));
        assertThat(englishSupplierMessage.getMessageTags(), is(inquiry.getMessageTags()));
        assertThat(englishSupplierMessage.getLastInteraction().getBody(), is(inquiry.getBody()));
        assertThat(englishSupplierMessage.getLastInteraction().getSender(), is(inquiry.getOriginator()));
        assertThat(englishSupplierMessage.getLastInteraction().getReceiver(), is(ENGLISH_SUPPLIER));

        ProductMessage spanishSupplierMessage = findByReceiver(messages, SPANISH_SUPPLIER);
        assertThat(spanishSupplierMessage.getProductIds().size(), is(1));
        assertThat(spanishSupplierMessage.getProductIds().get(0), is(2L));
        assertThat(spanishSupplierMessage.getOtherParty(), is(BUYER));
        assertThat(spanishSupplierMessage.getSubject(), is(inquiry.getSubject()));
        assertThat(spanishSupplierMessage.getMessageTags(), is(inquiry.getMessageTags()));
        assertThat(spanishSupplierMessage.getLastInteraction().getBody(), is(inquiry.getBody()));
        assertThat(spanishSupplierMessage.getLastInteraction().getSender(), is(inquiry.getOriginator()));
        assertThat(spanishSupplierMessage.getLastInteraction().getReceiver(), is(SPANISH_SUPPLIER));
    }

    @Test
    public void deleteDisapprovesInquirySoUpdatesPendingInquiryInDatabase() throws Exception {
        mutator.delete(42L);

        verify(inquiryWriteDao).disapprovePendingInquiry(42L);
    }

    private void assertThatInquiryIsClear(Inquiry createdInquiry, Long id) {
        assertThat(createdInquiry, is(notNullValue()));
        assertThat(createdInquiry.getId(), is(id));
        assertThat(createdInquiry.getProductIds().isEmpty(), is(true));
        assertThat(createdInquiry.getSubject(), is(nullValue()));
        assertThat(createdInquiry.getBody(), is(nullValue()));
        assertThat(createdInquiry.getMessageTags().isEmpty(), is(true));
    }

    private ProductMessage findByReceiver(List<ProductMessage> messages, final Party receiver) {
        return Iterables.find(messages, new Predicate<ProductMessage>() {
            @Override
            public boolean apply(ProductMessage input) {
                return input.getLastInteraction().getReceiver().equals(receiver);
            }
        });
    }

}
