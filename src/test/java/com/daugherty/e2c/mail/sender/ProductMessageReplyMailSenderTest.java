package com.daugherty.e2c.mail.sender;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import org.jasypt.util.numeric.IntegerNumberEncryptor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.persistence.data.AnonymousReadDao;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

@RunWith(MockitoJUnitRunner.class)
public class ProductMessageReplyMailSenderTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Email email;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private AnonymousReadDao anonymousReadDao;
    @Mock
    private BuyerReadDao buyerReadDao;
    @Mock
    private EmailFactory supplierToBuyerReplyNotificationEmail;
    @Mock
    private EmailFactory supplierToEnglishSupplierReplyNotificationEmail;
    @Mock
    private EmailFactory supplierToSpanishSupplierReplyNotificationEmail;
    @Mock
    private EmailFactory buyerToEnglishSupplierReplyNotificationEmail;
    @Mock
    private EmailFactory buyerToSpanishSupplierReplyNotificationEmail;
    @Mock
    private EmailFactory advancedSupplierReplyEmail;
    @Mock
    private IntegerNumberEncryptor entityIdNumberEncryptor;

    @InjectMocks
    private final ProductMessageReplyMailSender mailSender = new ProductMessageReplyMailSender();

    @Captor
    private ArgumentCaptor<Object[]> bodyArgumentsCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpCommonMocks() {
        when(emailUrlBuilder.buildBuyerSiteUrl()).thenReturn("buyerSiteUrl");
        when(emailUrlBuilder.buildGlobalSiteUrl()).thenReturn("globalSiteUrl");

        when(emailUrlBuilder.buildMailUrl(Language.CHINESE)).thenReturn("chineseEmailUrl");
        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenReturn("englishEmailUrl");
        when(emailUrlBuilder.buildMailUrl(Language.SPANISH)).thenReturn("spanishEmailUrl");

        when(entityIdNumberEncryptor.encrypt(any(BigInteger.class))).thenReturn(BigInteger.ONE);
    }

    @Test
    public void sendReplyFromSupplierWithoutAdvancedMailCapabilityToBuyer() throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);

        Buyer buyer = buildBuyer("buyer@somewhere.com");

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, buyer);

        when(buyerReadDao.loadLatest(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(
                supplierToBuyerReplyNotificationEmail.createEmail(eq(buyer.getEmailWithPersonalName()),
                        any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(supplierToBuyerReplyNotificationEmail).createEmail(eq(buyer.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is("buyerSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("chineseEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromSupplierWithoutAdvancedMailCapabilityToEnglishSupplierWithoutAdvancedMailCapability()
            throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        Supplier messagingSupplier = buildSupplier(667L, "Jp0bnB0j", "messaging-supplier@somewhere-else.com", false,
                Language.ENGLISH);
        when(supplierReadDao.loadApproved(667L, Locale.ENGLISH)).thenReturn(messagingSupplier);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, messagingSupplier);

        when(
                supplierToEnglishSupplierReplyNotificationEmail.createEmail(
                        eq(messagingSupplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(supplierToEnglishSupplierReplyNotificationEmail).createEmail(
                eq(messagingSupplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(2));
        assertThat(bodyArguments[0].toString(), is(messagingSupplier.getContact().getFirstName()));
        assertThat(bodyArguments[1].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromSupplierWithoutAdvancedMailCapabilityToSpanishSupplierWithoutAdvancedMailCapability()
            throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        Supplier messagingSupplier = buildSupplier(667L, "Jp0bnB0j", "messaging-supplier@somewhere-else.com", false,
                Language.SPANISH);
        when(supplierReadDao.loadApproved(667L, Locale.ENGLISH)).thenReturn(messagingSupplier);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, messagingSupplier);

        when(
                supplierToSpanishSupplierReplyNotificationEmail.createEmail(
                        eq(messagingSupplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(supplierToSpanishSupplierReplyNotificationEmail).createEmail(
                eq(messagingSupplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(2));
        assertThat(bodyArguments[0].toString(), is(messagingSupplier.getContact().getFirstName()));
        assertThat(bodyArguments[1].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("spanishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromSupplierWithAdvancedMailCapabilityToBuyer() throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);
        Buyer buyer = buildBuyer("buyer@somewhere.com");
        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, buyer);

        when(buyerReadDao.loadLatest(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(
                supplierToBuyerReplyNotificationEmail.createEmail(eq(buyer.getEmailWithPersonalName()),
                        any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(supplierToBuyerReplyNotificationEmail).createEmail(eq(buyer.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());

        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is("buyerSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("chineseEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromSupplierWithAdvancedMailCapabilityToEnglishSupplierWithoutAdvancedMailCapability()
            throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        Supplier messagingSupplier = buildSupplier(667L, "Jp0bnB0j", "messaging-supplier@somewhere-else.com", false,
                Language.ENGLISH);
        when(supplierReadDao.loadApproved(667L, Locale.ENGLISH)).thenReturn(messagingSupplier);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, messagingSupplier);

        when(
                supplierToEnglishSupplierReplyNotificationEmail.createEmail(
                        eq(messagingSupplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(supplierToEnglishSupplierReplyNotificationEmail).createEmail(
                eq(messagingSupplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(2));
        assertThat(bodyArguments[0].toString(), is(messagingSupplier.getContact().getFirstName()));
        assertThat(bodyArguments[1].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromSupplierWithAdvancedMailCapabilityToSpanishSupplierWithoutAdvancedMailCapability()
            throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        Supplier messagingSupplier = buildSupplier(667L, "Jp0bnB0j", "messaging-supplier@somewhere-else.com", false,
                Language.SPANISH);
        when(supplierReadDao.loadApproved(667L, Locale.ENGLISH)).thenReturn(messagingSupplier);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, messagingSupplier);

        when(
                supplierToSpanishSupplierReplyNotificationEmail.createEmail(
                        eq(messagingSupplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(supplierToSpanishSupplierReplyNotificationEmail).createEmail(
                eq(messagingSupplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(2));
        assertThat(bodyArguments[0].toString(), is(messagingSupplier.getContact().getFirstName()));
        assertThat(bodyArguments[1].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("spanishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromBuyerToEnglishSupplierWithoutAdvancedMailCapability() throws Exception {
        Buyer buyer = buildBuyer("buyer@somewhere.com");
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);
        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", buyer, supplier);

        when(buyerReadDao.loadLatest(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(
                buyerToEnglishSupplierReplyNotificationEmail.createEmail(eq(supplier.getEmailWithPersonalName()),
                        any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(buyerToEnglishSupplierReplyNotificationEmail).createEmail(eq(supplier.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(2));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        assertThat(bodyArguments[1].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromBuyerToSpanishSupplierWithoutAdvancedMailCapability() throws Exception {
        Buyer buyer = buildBuyer("buyer@somewhere.com");
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.SPANISH);
        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", buyer, supplier);

        when(buyerReadDao.loadLatest(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(
                buyerToSpanishSupplierReplyNotificationEmail.createEmail(eq(supplier.getEmailWithPersonalName()),
                        any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(buyerToSpanishSupplierReplyNotificationEmail).createEmail(eq(supplier.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(2));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        assertThat(bodyArguments[1].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("spanishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromBuyerToEnglishSupplierWithAdvancedMailCapability() throws Exception {
        Buyer buyer = buildBuyer("buyer@somewhere.com");
        when(buyerReadDao.loadLatest(586L)).thenReturn(buyer);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", buyer, supplier);

        when(
                advancedSupplierReplyEmail.createEmail(eq(buyer.getEmailWithPersonalName()),
                        eq(supplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(advancedSupplierReplyEmail).createEmail(eq(buyer.getEmailWithPersonalName()),
                eq(supplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is(interaction.getBody()));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromBuyerToSpanishSupplierWithAdvancedMailCapability() throws Exception {
        Buyer buyer = buildBuyer("buyer@somewhere.com");
        when(buyerReadDao.loadLatest(586L)).thenReturn(buyer);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.SPANISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", buyer, supplier);

        when(
                advancedSupplierReplyEmail.createEmail(eq(buyer.getEmailWithPersonalName()),
                        eq(supplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(advancedSupplierReplyEmail).createEmail(eq(buyer.getEmailWithPersonalName()),
                eq(supplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is(interaction.getBody()));

        verify(restTemplate).postForObject("spanishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromSupplierWithoutAdvancedMailCapabilityToEnglishSupplierWithAdvancedMailCapability()
            throws Exception {
        Supplier messagingSupplier = buildSupplier(667L, "Jp0bnB0j", "messaging-supplier@somewhere-else.com", false,
                Language.ENGLISH);
        when(supplierReadDao.loadApproved(667L, Locale.ENGLISH)).thenReturn(messagingSupplier);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", messagingSupplier, supplier);

        when(
                advancedSupplierReplyEmail.createEmail(eq(messagingSupplier.getEmailWithPersonalName()),
                        eq(supplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(advancedSupplierReplyEmail).createEmail(eq(messagingSupplier.getEmailWithPersonalName()),
                eq(supplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is(interaction.getBody()));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromSupplierWithoutAdvancedMailCapabilityToSpanishSupplierWithAdvancedMailCapability()
            throws Exception {
        Supplier messagingSupplier = buildSupplier(667L, "Jp0bnB0j", "messaging-supplier@somewhere-else.com", false,
                Language.ENGLISH);
        when(supplierReadDao.loadApproved(667L, Locale.ENGLISH)).thenReturn(messagingSupplier);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.SPANISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", messagingSupplier, supplier);

        when(
                advancedSupplierReplyEmail.createEmail(eq(messagingSupplier.getEmailWithPersonalName()),
                        eq(supplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(advancedSupplierReplyEmail).createEmail(eq(messagingSupplier.getEmailWithPersonalName()),
                eq(supplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is(interaction.getBody()));

        verify(restTemplate).postForObject("spanishEmailUrl", email, Email.class);
    }

    @Test
    public void sendThrowsCheckedExceptionWhenRESTPostFails() throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        Buyer buyer = buildBuyer("buyer@somewhere.com");
        when(buyerReadDao.loadLatest(586L)).thenReturn(buyer);

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, buyer);
        interaction.setId(242L);
        when(email.getTo()).thenReturn("toEmailAddress");
        expectedException.expect(NonEssentialMailFailureException.class);
        expectedException.expectMessage("Could not send email to toEmailAddress");
        expectedException.expectMessage("via service chineseEmailUrl");
        expectedException.expectMessage("for Interaction 242");

        when(
                supplierToBuyerReplyNotificationEmail.createEmail(eq(buyer.getEmailWithPersonalName()),
                        any(Object[].class))).thenReturn(email);

        when(restTemplate.postForObject("chineseEmailUrl", email, Email.class)).thenThrow(
                new RestClientException("Email error!"));

        mailSender.send(interaction);
    }

    @Test
    public void sendThrowsCheckedExceptionOnAnyOtherRuntimeException() throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        Buyer buyer = buildBuyer("buyer@somewhere.com");

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, buyer);
        interaction.setId(242L);
        expectedException.expect(NonEssentialMailFailureException.class);
        expectedException.expectMessage("Could not send email for Interaction 242");

        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenThrow(new RuntimeException("Something broke!"));

        mailSender.send(interaction);
    }

    @Test
    public void sendReplyFromSupplierWithoutAdvancedMailCapabilityToAnonymous() throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);

        Anonymous anonymous = buildAnonymous("anonymous@somewhere.com");

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, anonymous);

        when(anonymousReadDao.load(585L)).thenReturn(anonymous);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(
                supplierToBuyerReplyNotificationEmail.createEmail(eq(anonymous.getEmailWithPersonalName()),
                        any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(supplierToBuyerReplyNotificationEmail).createEmail(eq(anonymous.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is("buyerSiteUrl/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("chineseEmailUrl", email, Email.class);
    }

    @Test
    public void sendReplyFromSupplierWithAdvancedMailCapabilityToAnonymous() throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);

        Anonymous anonymous = buildAnonymous("anonymous@somewhere.com");

        Interaction interaction = new Interaction(new Message(42L, "jKNz4P4q"), "body", supplier, anonymous);

        when(anonymousReadDao.load(585L)).thenReturn(anonymous);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(
                supplierToBuyerReplyNotificationEmail.createEmail(eq(anonymous.getEmailWithPersonalName()),
                        any(Object[].class))).thenReturn(email);

        mailSender.send(interaction);

        verify(supplierToBuyerReplyNotificationEmail).createEmail(eq(anonymous.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(1));
        assertThat(bodyArguments[0].toString(), is("buyerSiteUrl/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("chineseEmailUrl", email, Email.class);
    }

    private Anonymous buildAnonymous(String emailAddress) {
        return new Anonymous(585L, "aePYnnVb", new Contact(null, null, null, null, emailAddress, null,
                Language.CHINESE, null), null, null, null, null);
    }

    private Buyer buildBuyer(String emailAddress) {
        return new Buyer(586L, "jKNzKB04", new Contact(null, null, null, null, emailAddress, null, Language.CHINESE,
                null), null, null, null, null, null);
    }

    private Supplier buildSupplier(Long id, String publicId, String emailAddress,
            boolean advancedWebAndEmailCapabilityEnabled, Language language) {

        Membership membership = new Membership(1L, new MembershipLevel(1L, 2, BigDecimal.valueOf(10), 6, 10, 20, 20,
                false, 10, false, false, false, 10, false, advancedWebAndEmailCapabilityEnabled, false, true,
                false, false, false));

        Supplier supplier = new Supplier(id, publicId, new Contact("first", null, null, null, emailAddress, null,
                language, null), null, null, null, null, null, null, null);

        supplier.getMemberships().add(membership);

        return supplier;
    }

}
