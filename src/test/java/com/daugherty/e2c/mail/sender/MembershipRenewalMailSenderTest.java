package com.daugherty.e2c.mail.sender;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;

@RunWith(MockitoJUnitRunner.class)
public class MembershipRenewalMailSenderTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private EmailFactory englishMembershipLevelOneRenewalEmail;
    @Mock
    private EmailFactory spanishMembershipLevelOneRenewalEmail;
    @Mock
    private EmailFactory englishMembershipLevelTwoRenewalEmail;
    @Mock
    private EmailFactory spanishMembershipLevelTwoRenewalEmail;
    @Mock
    private EmailFactory englishMembershipLevelThreeRenewalEmail;
    @Mock
    private EmailFactory spanishMembershipLevelThreeRenewalEmail;
    @Mock
    private EmailFactory englishMembershipLevelFourRenewalEmail;
    @Mock
    private EmailFactory spanishMembershipLevelFourRenewalEmail;
    @Mock
    private MembershipLevel membershipLevel;

    @InjectMocks
    private MembershipRenewalMailSender membershipRenewalMailSender = new MembershipRenewalMailSender();

    @Before
    public void setup() {
        when(membershipLevel.getProductCount()).thenReturn(15);
        when(membershipLevel.getHotProductCount()).thenReturn(10);

        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenReturn("englishPostUrl");
        when(emailUrlBuilder.buildMailUrl(Language.SPANISH)).thenReturn("spanishPostUrl");
    }

    @Test
    public void sendEnglishLevelOneRenewalEmail() {
        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.ENGLISH, null), null, null, null, null, null, null, null);

        when(membershipLevel.getValue()).thenReturn(1);

        Email email = new Email();
        when(
                englishMembershipLevelOneRenewalEmail.createEmail(
                        "supplierFirst supplierLast <supplier@somewhere.com>", new Object[] { "supplierFirst", 15 }))
                .thenReturn(email);

        membershipRenewalMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("englishPostUrl", email, Email.class);
    }

    @Test
    public void sendSpanishLevelOneRenewalEmail() {
        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.SPANISH, null), null, null, null, null, null, null, null);

        when(membershipLevel.getValue()).thenReturn(1);

        Email email = new Email();
        when(
                spanishMembershipLevelOneRenewalEmail.createEmail(
                        "supplierFirst supplierLast <supplier@somewhere.com>", new Object[] { "supplierFirst", 15 }))
                .thenReturn(email);

        membershipRenewalMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("spanishPostUrl", email, Email.class);
    }

    @Test
    public void sendEnglishLevelTwoRenewalEmail() {
        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.ENGLISH, null), null, null, null, null, null, null, null);

        when(membershipLevel.getValue()).thenReturn(2);

        Email email = new Email();
        when(
                englishMembershipLevelTwoRenewalEmail
                        .createEmail("supplierFirst supplierLast <supplier@somewhere.com>", new Object[] {
                                "supplierFirst", 15, 10 })).thenReturn(email);

        membershipRenewalMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("englishPostUrl", email, Email.class);
    }

    @Test
    public void sendSpanishLevelTwoRenewalEmail() {
        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.SPANISH, null), null, null, null, null, null, null, null);

        when(membershipLevel.getValue()).thenReturn(2);

        Email email = new Email();
        when(
                spanishMembershipLevelTwoRenewalEmail
                        .createEmail("supplierFirst supplierLast <supplier@somewhere.com>", new Object[] {
                                "supplierFirst", 15, 10 })).thenReturn(email);

        membershipRenewalMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("spanishPostUrl", email, Email.class);
    }

    @Test
    public void sendEnglishLevelThreeRenewalEmail() {
        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.ENGLISH, null), null, null, null, null, null, null, null);

        when(membershipLevel.getValue()).thenReturn(3);

        Email email = new Email();
        when(
                englishMembershipLevelThreeRenewalEmail
                        .createEmail("supplierFirst supplierLast <supplier@somewhere.com>", new Object[] {
                                "supplierFirst", 15, 10 })).thenReturn(email);

        membershipRenewalMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("englishPostUrl", email, Email.class);
    }

    @Test
    public void sendSpanishLevelThreeRenewalEmail() {
        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.SPANISH, null), null, null, null, null, null, null, null);

        when(membershipLevel.getValue()).thenReturn(3);

        Email email = new Email();
        when(
                spanishMembershipLevelThreeRenewalEmail
                        .createEmail("supplierFirst supplierLast <supplier@somewhere.com>", new Object[] {
                                "supplierFirst", 15, 10 })).thenReturn(email);

        membershipRenewalMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("spanishPostUrl", email, Email.class);
    }

    @Test
    public void sendEnglishLevelFourRenewalEmail() {
        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.ENGLISH, null), null, null, null, null, null, null, null);

        when(membershipLevel.getValue()).thenReturn(4);

        Email email = new Email();
        when(
                englishMembershipLevelFourRenewalEmail
                        .createEmail("supplierFirst supplierLast <supplier@somewhere.com>", new Object[] {
                                "supplierFirst", 15, 10 })).thenReturn(email);

        membershipRenewalMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("englishPostUrl", email, Email.class);
    }

    @Test
    public void sendSpanishLevelFourRenewalEmail() {
        Supplier supplier = new Supplier(2L, "GpP8xPem", new Contact("supplierFirst", "supplierLast", null, null,
                "supplier@somewhere.com", null, Language.SPANISH, null), null, null, null, null, null, null, null);

        when(membershipLevel.getValue()).thenReturn(4);

        Email email = new Email();
        when(
                spanishMembershipLevelFourRenewalEmail
                        .createEmail("supplierFirst supplierLast <supplier@somewhere.com>", new Object[] {
                                "supplierFirst", 15, 10 })).thenReturn(email);

        membershipRenewalMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("spanishPostUrl", email, Email.class);
    }
}
