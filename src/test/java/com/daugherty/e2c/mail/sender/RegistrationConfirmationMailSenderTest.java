package com.daugherty.e2c.mail.sender;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPendingUserDao;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationConfirmationMailSenderTest {

    @Mock
    private EmailFactory buyerRegistrationConfirmationEmail;
    @Mock
    private EmailFactory englishSupplierRegistrationConfirmationEmail;
    @Mock
    private EmailFactory spanishSupplierRegistrationConfirmationEmail;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Email email;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private JdbcPartyDao partyDao;
    @Mock
    private JdbcPendingUserDao pendingUserDao;

    @InjectMocks
    private final RegistrationConfirmationMailSender registrationConfirmationMailSender = new RegistrationConfirmationMailSender();

    @Test
    public void resendEmailToBuyerCreatesTokenURLAndPosts() {
        String toAddress = "testToAddress";
        String username = "testUsername";
        String password = "testPassword";
        String confirmationToken = "testConfirmationToken";
        String confirmationTokenURL = "emailConfirmationUrl/sessions/first/" + confirmationToken;
        Contact contact = new Contact(null, null, null, null, null, null, toAddress, null, null, null, null, null,
                Language.CHINESE, null);
        Company company = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", contact, company, null, PartyType.BUYER, null, null);
        PendingUser pendingUser = new PendingUser(10L, username, password, 1L, confirmationToken);

        when(partyDao.loadById(1L)).thenReturn(party);
        when(pendingUserDao.loadPendingUserByEmailAddress(toAddress)).thenReturn(pendingUser);
        when(emailUrlBuilder.buildBuyerSiteUrl()).thenReturn("emailConfirmationUrl");
        when(emailUrlBuilder.buildMailUrl(Language.CHINESE)).thenReturn("emailUrl");
        Object[] expectedBodyTemplateArgs = new Object[] { username, confirmationTokenURL, confirmationTokenURL };
        when(buyerRegistrationConfirmationEmail.createEmail(toAddress, expectedBodyTemplateArgs)).thenReturn(email);

        registrationConfirmationMailSender.resend(1L);

        verify(buyerRegistrationConfirmationEmail).createEmail(toAddress, expectedBodyTemplateArgs);
        verify(restTemplate).postForObject("emailUrl", email, Email.class);
    }

    @Test
    public void sendEmailToBuyerCreatesTokenURLAndPosts() {
        String toAddress = "testToAddress";
        String username = "testUsername";
        String confirmationToken = "testConfirmationToken";
        String confirmationTokenURL = "emailConfirmationUrl/sessions/first/" + confirmationToken;

        when(emailUrlBuilder.buildBuyerSiteUrl()).thenReturn("emailConfirmationUrl");
        when(emailUrlBuilder.buildMailUrl(Language.CHINESE)).thenReturn("emailUrl");
        Object[] expectedBodyTemplateArgs = new Object[] { username, confirmationTokenURL, confirmationTokenURL };
        when(buyerRegistrationConfirmationEmail.createEmail(toAddress, expectedBodyTemplateArgs)).thenReturn(email);

        registrationConfirmationMailSender.send(toAddress, username, confirmationToken, Language.CHINESE);

        verify(buyerRegistrationConfirmationEmail).createEmail(toAddress, expectedBodyTemplateArgs);
        verify(restTemplate).postForObject("emailUrl", email, Email.class);
    }

    @Test
    public void sendEmailToEnglishSuppierCreatesTokenURLAndPosts() {
        String toAddress = "testToAddress";
        String username = "testUsername";
        String confirmationToken = "testConfirmationToken";
        String confirmationTokenURL = "emailConfirmationUrl/sessions/first/" + confirmationToken;

        when(emailUrlBuilder.buildGlobalSiteUrl()).thenReturn("emailConfirmationUrl");
        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenReturn("emailUrl");
        Object[] expectedBodyTemplateArgs = new Object[] { username, confirmationTokenURL, confirmationTokenURL };
        when(englishSupplierRegistrationConfirmationEmail.createEmail(toAddress, expectedBodyTemplateArgs)).thenReturn(
                email);

        registrationConfirmationMailSender.send(toAddress, username, confirmationToken, Language.ENGLISH);

        verify(englishSupplierRegistrationConfirmationEmail).createEmail(toAddress, expectedBodyTemplateArgs);
        verify(restTemplate).postForObject("emailUrl", email, Email.class);
    }

    @Test
    public void sendEmailToSpanishSuppierCreatesTokenURLAndPosts() {
        String toAddress = "testToAddress";
        String username = "testUsername";
        String confirmationToken = "testConfirmationToken";
        String confirmationTokenURL = "emailConfirmationUrl/sessions/first/" + confirmationToken;

        when(emailUrlBuilder.buildGlobalSiteUrl()).thenReturn("emailConfirmationUrl");
        when(emailUrlBuilder.buildMailUrl(Language.SPANISH)).thenReturn("emailUrl");
        Object[] expectedBodyTemplateArgs = new Object[] { username, confirmationTokenURL, confirmationTokenURL };
        when(spanishSupplierRegistrationConfirmationEmail.createEmail(toAddress, expectedBodyTemplateArgs)).thenReturn(
                email);

        registrationConfirmationMailSender.send(toAddress, username, confirmationToken, Language.SPANISH);

        verify(spanishSupplierRegistrationConfirmationEmail).createEmail(toAddress, expectedBodyTemplateArgs);
        verify(restTemplate).postForObject("emailUrl", email, Email.class);
    }
}
