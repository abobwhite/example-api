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
public class ChangePasswordMailSenderTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private EmailFactory buyerChangePasswordEmail;
    @Mock
    private EmailFactory englishSupplierChangePasswordEmail;
    @Mock
    private EmailFactory spanishSupplierChangePasswordEmail;

    @InjectMocks
    private final ChangePasswordMailSender changePasswordMailSender = new ChangePasswordMailSender();

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
        Email email = new Email();
        when(buyerChangePasswordEmail.createEmail("emailAddress", new Object[] {})).thenReturn(email);

        changePasswordMailSender.send("emailAddress", "username", Locale.CHINESE);

        verify(restTemplate).postForObject("chinesePostUrl", email, Email.class);
    }

    @Test
    public void sendEnglishSupplierEmail() {
        Email email = new Email();
        when(englishSupplierChangePasswordEmail.createEmail("emailAddress", new Object[] { "emailAddress" }))
                .thenReturn(email);

        changePasswordMailSender.send("emailAddress", "username", Locale.ENGLISH);

        verify(restTemplate).postForObject("englishPostUrl", email, Email.class);
    }

    @Test
    public void sendSpanishSupplierEmail() {
        Email email = new Email();
        when(spanishSupplierChangePasswordEmail.createEmail("emailAddress", new Object[] { "emailAddress" }))
                .thenReturn(email);

        changePasswordMailSender.send("emailAddress", "username", new Locale("es"));

        verify(restTemplate).postForObject("spanishPostUrl", email, Email.class);
    }
}
