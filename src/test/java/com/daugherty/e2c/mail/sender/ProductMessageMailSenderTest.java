package com.daugherty.e2c.mail.sender;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
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

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.PaymentType;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.persistence.data.MembershipReadDao;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProductMessageMailSenderTest {
    private static final Product PRODUCT = new Product(242L, new ProductInformation(null, "Product 242", null, null,
            null, null, null, null, null, null, null), null, null, null, null, null);

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Email email;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private SupplierReadDao supplierReadDao;
    @Mock
    private ProductReadDao productReadDao;
    @Mock
    private JdbcPartyDao partyDao;
    @Mock
    private EmailFactory englishMessageNotificationEmail;
    @Mock
    private EmailFactory spanishMessageNotificationEmail;
    @Mock
    private EmailFactory advancedSupplierMessageEmail;
    @Mock
    private MembershipReadDao membershipReadDao;

    @InjectMocks
    private final ProductMessageMailSender mailSender = new ProductMessageMailSender();

    @Captor
    private ArgumentCaptor<Object[]> bodyArgumentsCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpCommonMocks() {
        when(emailUrlBuilder.buildGlobalSiteUrl()).thenReturn("globalSiteUrl");
        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenReturn("englishEmailUrl");
        when(emailUrlBuilder.buildMailUrl(Language.SPANISH)).thenReturn("spanishEmailUrl");

    }

    @Test
    public void sendEmailToEnglishSupplierWithoutAdvancedMailCapabilityPostsWithAppropriateTemplate() throws Exception {
        Buyer buyer = new Buyer(586L, "jKNzKB04", new Contact(null, null, null, null, "buyer@somewhere.com", null,
                null, null), null, null, null, null, null);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);
        Membership membership = buildMembership(777L, 666L, new DateTime().plusDays(1).toDate());

        ProductMessage message = new ProductMessage("subject", buyer, PRODUCT.getId(), new Interaction(new Message(42L, "jKNz4P4q"), "body",
                buyer, supplier));
        message.setId(42L);
        message.setPublicId("jKNz4P4q");

        when(partyDao.loadById(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(productReadDao.loadPublishedByProductIds(message.getProductIds(), Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(PRODUCT));
        when(membershipReadDao.loadBySupplierId(666L)).thenReturn(membership);
        when(englishMessageNotificationEmail.createEmail(eq(supplier.getEmailWithPersonalName()), any(Object[].class)))
                .thenReturn(email);

        mailSender.send(message);

        verify(englishMessageNotificationEmail).createEmail(eq(supplier.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(3));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        String expectedProductListItems = "<li>" + PRODUCT.getInformation().getName() + "</li>";
        assertThat(bodyArguments[1].toString(), is(expectedProductListItems));
        assertThat(bodyArguments[2].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendEmailToSpanishSupplierWithoutAdvancedMailCapabilityPostsWithAppropriateTemplate() throws Exception {
        Buyer buyer = new Buyer(586L, "jKNzKB04", new Contact(null, null, null, null, "buyer@somewhere.com", null,
                null, null), null, null, null, null, null);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.SPANISH);
        Membership membership = buildMembership(777L, 666L, new DateTime().plusDays(1).toDate());

        ProductMessage message = new ProductMessage("subject", buyer, PRODUCT.getId(), new Interaction(new Message(42L, "jKNz4P4q"), "body",
                buyer, supplier));
        message.setId(42L);
        message.setPublicId("jKNz4P4q");

        when(partyDao.loadById(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(productReadDao.loadPublishedByProductIds(message.getProductIds(), Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(PRODUCT));
        when(membershipReadDao.loadBySupplierId(666L)).thenReturn(membership);

        when(spanishMessageNotificationEmail.createEmail(eq(supplier.getEmailWithPersonalName()), any(Object[].class)))
                .thenReturn(email);

        mailSender.send(message);

        verify(spanishMessageNotificationEmail).createEmail(eq(supplier.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(3));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        String expectedProductListItems = "<li>" + PRODUCT.getInformation().getName() + "</li>";
        assertThat(bodyArguments[1].toString(), is(expectedProductListItems));
        assertThat(bodyArguments[2].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("spanishEmailUrl", email, Email.class);
    }

    @Test
    public void sendEmailToSupplierWithAdvancedMailCapabilityButIsExpiredPostsWithAppropriateTemplate()
            throws Exception {
        Buyer buyer = new Buyer(586L, "jKNzKB04", new Contact(null, null, null, null, "buyer@somewhere.com", null,
                null, null), null, null, null, null, null);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);
        Membership membership = buildMembership(777L, 666L, new DateTime().minusDays(1).toDate());

        ProductMessage message = new ProductMessage("subject", buyer, PRODUCT.getId(), new Interaction(new Message(42L, "jKNz4P4q"), "body",
                buyer, supplier));
        message.setId(42L);
        message.setPublicId("jKNz4P4q");

        when(partyDao.loadById(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(productReadDao.loadPublishedByProductIds(message.getProductIds(), Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(PRODUCT));
        when(membershipReadDao.loadBySupplierId(666L)).thenReturn(membership);
        when(englishMessageNotificationEmail.createEmail(eq(supplier.getEmailWithPersonalName()), any(Object[].class)))
                .thenReturn(email);

        mailSender.send(message);

        verify(englishMessageNotificationEmail).createEmail(eq(supplier.getEmailWithPersonalName()),
                bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(3));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        String expectedProductListItems = "<li>" + PRODUCT.getInformation().getName() + "</li>";
        assertThat(bodyArguments[1].toString(), is(expectedProductListItems));
        assertThat(bodyArguments[2].toString(), is("globalSiteUrl/dashboard/messages/view/jKNz4P4q"));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendEmailFromBuyerToEnglishSupplierWithAdvancedMailCapabilityPostsWithAppropriateTemplate()
            throws Exception {
        Buyer buyer = new Buyer(586L, "jKNzKB04", new Contact(null, null, null, null, "buyer@somewhere.com", null,
                null, null), null, null, null, null, null);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);
        Membership membership = buildMembership(777L, 666L, new DateTime().plusDays(1).toDate());

        ProductMessage message = new ProductMessage("subject", buyer, PRODUCT.getId(), new Interaction(null, "body",
                buyer, supplier));
        message.setId(42L);
        message.addMessageTags(Lists.newArrayList(MessageTag.COMPANY_OVERVIEW, MessageTag.DELIVERY_TIME));

        when(partyDao.loadById(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(productReadDao.loadPublishedByProductIds(message.getProductIds(), Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(PRODUCT));
        when(membershipReadDao.loadBySupplierId(666L)).thenReturn(membership);

        when(
                advancedSupplierMessageEmail.createEmail(eq(buyer.getEmailWithPersonalName()),
                        eq(supplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(message);

        verify(advancedSupplierMessageEmail).createEmail(eq(buyer.getEmailWithPersonalName()),
                eq(supplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(4));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        String expectedProductListItems = "<li>" + PRODUCT.getInformation().getName() + "</li>";
        assertThat(bodyArguments[1].toString(), is(expectedProductListItems));
        String expectedMessageTagListItems = "<li>" + MessageTag.COMPANY_OVERVIEW.getName() + "</li><li>"
                + MessageTag.DELIVERY_TIME.getName() + "</li>";
        assertThat(bodyArguments[2].toString(), is(expectedMessageTagListItems));
        assertThat(bodyArguments[3].toString(), is(message.getLastInteraction().getBody()));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendEmailFromBuyerToSpanishSupplierWithAdvancedMailCapabilityPostsWithAppropriateTemplate()
            throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.SPANISH);
        Buyer buyer = new Buyer(586L, "jKNzKB04", new Contact(null, null, null, null, "buyer@somewhere.com", null,
                null, null), null, null, null, null, null);
        Membership membership = buildMembership(777L, 666L, new DateTime().plusDays(1).toDate());

        ProductMessage message = new ProductMessage("subject", buyer, PRODUCT.getId(), new Interaction(null, "body",
                buyer, supplier));
        message.setId(42L);

        when(partyDao.loadById(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(productReadDao.loadPublishedByProductIds(message.getProductIds(), Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(PRODUCT));
        when(membershipReadDao.loadBySupplierId(666L)).thenReturn(membership);

        when(
                advancedSupplierMessageEmail.createEmail(eq(buyer.getEmailWithPersonalName()),
                        eq(supplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(message);

        verify(advancedSupplierMessageEmail).createEmail(eq(buyer.getEmailWithPersonalName()),
                eq(supplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(4));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        String expectedProductListItems = "<li>" + PRODUCT.getInformation().getName() + "</li>";
        assertThat(bodyArguments[1].toString(), is(expectedProductListItems));
        assertThat(bodyArguments[2].toString(), is(""));
        assertThat(bodyArguments[3].toString(), is(message.getLastInteraction().getBody()));

        verify(restTemplate).postForObject("spanishEmailUrl", email, Email.class);
    }

    @Test
    public void sendEmailFromSupplierToEnglishSupplierWithAdvancedMailCapabilityPostsWithAppropriateTemplate()
            throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);
        Supplier sendingSupplier = buildSupplier(667L, "Jp0bnB0j", "sending-supplier@somewhere-else.com", false,
                Language.ENGLISH);
        Membership membership = buildMembership(777L, 666L, new DateTime().plusDays(1).toDate());

        ProductMessage message = new ProductMessage("subject", sendingSupplier, PRODUCT.getId(), new Interaction(null,
                "body", sendingSupplier, supplier));
        message.setId(42L);
        message.addMessageTags(Lists.newArrayList(MessageTag.COMPANY_OVERVIEW, MessageTag.DELIVERY_TIME));

        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(supplierReadDao.loadApproved(667L, Locale.ENGLISH)).thenReturn(sendingSupplier);
        when(productReadDao.loadPublishedByProductIds(message.getProductIds(), Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(PRODUCT));
        when(membershipReadDao.loadBySupplierId(666L)).thenReturn(membership);
        when(
                advancedSupplierMessageEmail.createEmail(eq(sendingSupplier.getEmailWithPersonalName()),
                        eq(supplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(message);

        verify(advancedSupplierMessageEmail).createEmail(eq(sendingSupplier.getEmailWithPersonalName()),
                eq(supplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(4));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        String expectedProductListItems = "<li>" + PRODUCT.getInformation().getName() + "</li>";
        assertThat(bodyArguments[1].toString(), is(expectedProductListItems));
        String expectedMessageTagListItems = "<li>" + MessageTag.COMPANY_OVERVIEW.getName() + "</li><li>"
                + MessageTag.DELIVERY_TIME.getName() + "</li>";
        assertThat(bodyArguments[2].toString(), is(expectedMessageTagListItems));
        assertThat(bodyArguments[3].toString(), is(message.getLastInteraction().getBody()));

        verify(restTemplate).postForObject("englishEmailUrl", email, Email.class);
    }

    @Test
    public void sendEmailFromSupplierToSpanishSupplierWithAdvancedMailCapabilityPostsWithAppropriateTemplate()
            throws Exception {
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.SPANISH);
        Supplier sendingSupplier = buildSupplier(667L, "Jp0bnB0j", "sending-supplier@somewhere-else.com", false,
                Language.ENGLISH);
        Membership membership = buildMembership(777L, 666L, new DateTime().plusDays(1).toDate());

        ProductMessage message = new ProductMessage("subject", sendingSupplier, PRODUCT.getId(), new Interaction(null,
                "body", sendingSupplier, supplier));
        message.setId(42L);

        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(supplierReadDao.loadApproved(667L, Locale.ENGLISH)).thenReturn(sendingSupplier);
        when(productReadDao.loadPublishedByProductIds(message.getProductIds(), Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(PRODUCT));
        when(membershipReadDao.loadBySupplierId(666L)).thenReturn(membership);
        when(
                advancedSupplierMessageEmail.createEmail(eq(sendingSupplier.getEmailWithPersonalName()),
                        eq(supplier.getEmailWithPersonalName()), any(Object[].class))).thenReturn(email);

        mailSender.send(message);

        verify(advancedSupplierMessageEmail).createEmail(eq(sendingSupplier.getEmailWithPersonalName()),
                eq(supplier.getEmailWithPersonalName()), bodyArgumentsCaptor.capture());
        Object[] bodyArguments = bodyArgumentsCaptor.getValue();
        assertThat(bodyArguments.length, is(4));
        assertThat(bodyArguments[0].toString(), is(supplier.getContact().getFirstName()));
        String expectedProductListItems = "<li>" + PRODUCT.getInformation().getName() + "</li>";
        assertThat(bodyArguments[1].toString(), is(expectedProductListItems));
        assertThat(bodyArguments[2].toString(), is(""));
        assertThat(bodyArguments[3].toString(), is(message.getLastInteraction().getBody()));

        verify(restTemplate).postForObject("spanishEmailUrl", email, Email.class);
    }

    @Test
    public void sendThrowsCheckedExceptionWhenRESTPostFails() throws Exception {
        Buyer buyer = new Buyer(586L, "jKNzKB04", new Contact(null, null, null, null, "buyer@somewhere.com", null,
                null, null), null, null, null, null, null);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", false, Language.ENGLISH);
        Membership membership = buildMembership(777L, 666L, new DateTime().plusDays(1).toDate());

        when(email.getTo()).thenReturn("toEmailAddress");
        expectedException.expect(NonEssentialMailFailureException.class);
        expectedException.expectMessage("Could not send email to toEmailAddress");
        expectedException.expectMessage("via service englishEmailUrl");
        expectedException.expectMessage("for Interaction 242");

        Interaction interaction = new Interaction(new Message(42L), "body", buyer, supplier);
        interaction.setId(242L);
        ProductMessage message = new ProductMessage("subject", buyer, PRODUCT.getId(), interaction);
        message.setId(42L);

        when(partyDao.loadById(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);
        when(productReadDao.loadPublishedByProductIds(message.getProductIds(), Locale.ENGLISH)).thenReturn(
                Lists.newArrayList(PRODUCT));
        when(membershipReadDao.loadBySupplierId(666L)).thenReturn(membership);

        when(englishMessageNotificationEmail.createEmail(eq(supplier.getEmailWithPersonalName()), any(Object[].class)))
                .thenReturn(email);

        when(restTemplate.postForObject("englishEmailUrl", email, Email.class)).thenThrow(
                new RestClientException("Email error!"));

        mailSender.send(message);
    }

    @Test
    public void sendThrowsCheckedExceptionOnAnyOtherRuntimeException() throws Exception {
        Buyer buyer = new Buyer(586L, "jKNzKB04", new Contact(null, null, null, null, "buyer@somewhere.com", null,
                null, null), null, null, null, null, null);
        Supplier supplier = buildSupplier(666L, "pBVQwo0b", "supplier@somewhere.com", true, Language.ENGLISH);

        when(partyDao.loadById(586L)).thenReturn(buyer);
        when(supplierReadDao.loadApproved(666L, Locale.ENGLISH)).thenReturn(supplier);

        expectedException.expect(NonEssentialMailFailureException.class);
        expectedException.expectMessage("Could not send email for Interaction 242");

        Interaction interaction = new Interaction(new Message(42L), "body", buyer, supplier);
        interaction.setId(242L);
        ProductMessage message = new ProductMessage("subject", buyer, PRODUCT.getId(), interaction);
        message.setId(42L);

        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenThrow(new RuntimeException("Something broke!"));

        mailSender.send(message);
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

    private Membership buildMembership(Long id, Long supplierId, Date expirationDate) {
        MembershipLevel level = new MembershipLevel(null, 4, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, true, false, false, false);
        return new Membership(id, supplierId, level, ApprovalStatus.APPROVED, 1, new Date(), new DateTime()
                .minusMonths(3).toDate(), expirationDate, BigDecimal.ZERO, BigDecimal.ZERO, 1L, PaymentType.NONE, null,
                BigDecimal.ZERO, BigDecimal.ZERO);
    }

}
