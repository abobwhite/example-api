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
public class NewMembershipMailSenderTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private MembershipLevel membershipLevel;
    @Mock
    private Supplier supplier;
    @Mock
    private Contact contact;
    @Mock
    private EmailFactory englishMembershipLevelOneWelcomeEmail;
    @Mock
    private EmailFactory spanishMembershipLevelOneWelcomeEmail;
    @Mock
    private EmailFactory englishMembershipLevelTwoWelcomeEmail;
    @Mock
    private EmailFactory spanishMembershipLevelTwoWelcomeEmail;
    @Mock
    private EmailFactory englishMembershipLevelThreeWelcomeEmail;
    @Mock
    private EmailFactory spanishMembershipLevelThreeWelcomeEmail;
    @Mock
    private EmailFactory englishMembershipLevelFourWelcomeEmail;
    @Mock
    private EmailFactory spanishMembershipLevelFourWelcomeEmail;
    @Mock
    private Email email;

    @InjectMocks
    private NewMembershipMailSender newMembershipMailSender = new NewMembershipMailSender();

    @Before
    public void setUp() {
        when(membershipLevel.getProductCount()).thenReturn(15);
        when(membershipLevel.getHotProductCount()).thenReturn(10);
        when(supplier.getContact()).thenReturn(contact);
        when(supplier.getEmailWithPersonalName()).thenReturn("EmailWithPersonalName");
        when(contact.getFirstName()).thenReturn("FirstName");
        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenReturn("englishUrl");
        when(emailUrlBuilder.buildMailUrl(Language.SPANISH)).thenReturn("spanishUrl");
    }

    @Test
    public void levelOneEnglishEmail() {
        when(membershipLevel.getValue()).thenReturn(1);
        when(contact.getLanguage()).thenReturn(Language.ENGLISH);
        when(
                englishMembershipLevelOneWelcomeEmail.createEmail("EmailWithPersonalName", new Object[] { "FirstName",
                        15 })).thenReturn(email);

        newMembershipMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("englishUrl", email, Email.class);
    }

    @Test
    public void levelOneSpanishEmail() {
        when(membershipLevel.getValue()).thenReturn(1);
        when(contact.getLanguage()).thenReturn(Language.SPANISH);
        when(
                spanishMembershipLevelOneWelcomeEmail.createEmail("EmailWithPersonalName", new Object[] { "FirstName",
                        15 })).thenReturn(email);

        newMembershipMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("spanishUrl", email, Email.class);
    }

    @Test
    public void levelTwoEnglishEmail() {
        when(membershipLevel.getValue()).thenReturn(2);
        when(contact.getLanguage()).thenReturn(Language.ENGLISH);
        when(
                englishMembershipLevelTwoWelcomeEmail.createEmail("EmailWithPersonalName", new Object[] { "FirstName",
                        15, 10 })).thenReturn(email);

        newMembershipMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("englishUrl", email, Email.class);
    }

    @Test
    public void levelTwoSpanishEmail() {
        when(membershipLevel.getValue()).thenReturn(2);
        when(contact.getLanguage()).thenReturn(Language.SPANISH);
        when(
                spanishMembershipLevelTwoWelcomeEmail.createEmail("EmailWithPersonalName", new Object[] { "FirstName",
                        15, 10 })).thenReturn(email);

        newMembershipMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("spanishUrl", email, Email.class);
    }

    @Test
    public void levelThreeEnglishEmail() {
        when(membershipLevel.getValue()).thenReturn(3);
        when(contact.getLanguage()).thenReturn(Language.ENGLISH);
        when(
                englishMembershipLevelThreeWelcomeEmail.createEmail("EmailWithPersonalName", new Object[] {
                        "FirstName", 15, 10 })).thenReturn(email);

        newMembershipMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("englishUrl", email, Email.class);
    }

    @Test
    public void levelThreeSpanishEmail() {
        when(membershipLevel.getValue()).thenReturn(3);
        when(contact.getLanguage()).thenReturn(Language.SPANISH);
        when(
                spanishMembershipLevelThreeWelcomeEmail.createEmail("EmailWithPersonalName", new Object[] {
                        "FirstName", 15, 10 })).thenReturn(email);

        newMembershipMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("spanishUrl", email, Email.class);
    }

    @Test
    public void levelFourEnglishEmail() {
        when(membershipLevel.getValue()).thenReturn(4);
        when(contact.getLanguage()).thenReturn(Language.ENGLISH);
        when(
                englishMembershipLevelFourWelcomeEmail.createEmail("EmailWithPersonalName", new Object[] { "FirstName",
                        15, 10 })).thenReturn(email);

        newMembershipMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("englishUrl", email, Email.class);
    }

    @Test
    public void levelFourSpanishEmail() {
        when(membershipLevel.getValue()).thenReturn(4);
        when(contact.getLanguage()).thenReturn(Language.SPANISH);
        when(
                spanishMembershipLevelFourWelcomeEmail.createEmail("EmailWithPersonalName", new Object[] { "FirstName",
                        15, 10 })).thenReturn(email);

        newMembershipMailSender.sendEmail(supplier, membershipLevel);

        verify(restTemplate).postForObject("spanishUrl", email, Email.class);
    }
}
