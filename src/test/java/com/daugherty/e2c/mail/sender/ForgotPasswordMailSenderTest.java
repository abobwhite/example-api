package com.daugherty.e2c.mail.sender;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ForgotPasswordMailSenderTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private EmailFactory buyerForgotPasswordEmail;
    @Mock
    private EmailFactory englishSupplierForgotPasswordEmail;
    @Mock
    private EmailFactory spanishSupplierForgotPasswordEmail;

    @InjectMocks
    private final ForgotPasswordMailSender forgotPasswordMailSender = new ForgotPasswordMailSender();

    @Before
    public void setup() {
        when(emailUrlBuilder.buildGlobalSiteUrl()).thenReturn("supplierUrl");
        when(emailUrlBuilder.buildBuyerSiteUrl()).thenReturn("buyerUrl");
        when(emailUrlBuilder.buildMailUrl(Locale.CHINESE)).thenReturn("chinesePostUrl");
        when(emailUrlBuilder.buildMailUrl(Locale.ENGLISH)).thenReturn("englishPostUrl");
        when(emailUrlBuilder.buildMailUrl(new Locale("es"))).thenReturn("spanishPostUrl");
    }

    @Test
    public void sendBuyerEmail() {
        String confirmationTokenUrl = "buyerUrl/userCredentials/reset/confirmationToken";
        String username = "username";
        Email email = new Email();
        when(
                buyerForgotPasswordEmail.createEmail("emailAddress", new Object[] { username, confirmationTokenUrl,
                        confirmationTokenUrl })).thenReturn(email);

        forgotPasswordMailSender.send(username, "emailAddress", "confirmationToken", Locale.CHINESE);

        verify(restTemplate).postForObject("chinesePostUrl", email, Email.class);
    }

    @Test
    public void sendEnglishSupplierEmail() {
        String confirmationTokenUrl = "supplierUrl/userCredentials/reset/confirmationToken";
        String username = "username";
        Email email = new Email();
        when(
                englishSupplierForgotPasswordEmail.createEmail("emailAddress", new Object[] { username,
                        confirmationTokenUrl, confirmationTokenUrl })).thenReturn(email);

        forgotPasswordMailSender.send(username, "emailAddress", "confirmationToken", Locale.ENGLISH);

        verify(restTemplate).postForObject("englishPostUrl", email, Email.class);
    }

    @Test
    public void sendSpanishSupplierEmail() {
        String confirmationTokenUrl = "supplierUrl/userCredentials/reset/confirmationToken";
        String username = "username";
        Email email = new Email();
        when(
                spanishSupplierForgotPasswordEmail.createEmail("emailAddress", new Object[] { username,
                        confirmationTokenUrl, confirmationTokenUrl })).thenReturn(email);

        forgotPasswordMailSender.send(username, "emailAddress", "confirmationToken", new Locale("es"));

        verify(restTemplate).postForObject("spanishPostUrl", email, Email.class);
    }
}
