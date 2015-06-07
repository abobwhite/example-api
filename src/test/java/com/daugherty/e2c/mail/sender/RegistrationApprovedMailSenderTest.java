package com.daugherty.e2c.mail.sender;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationApprovedMailSenderTest {
    private static final String RESTFUL_EMAIL_URL = "emailUrl";

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private EmailFactory buyerRegistrationApprovedEmail;
    @Mock
    private EmailFactory englishSupplierRegistrationApprovedEmail;
    @Mock
    private EmailFactory spanishSupplierRegistrationApprovedEmail;
    @Mock
    private EmailFactory englishSupplierRegistrationApprovedWithProductsEmail;
    @Mock
    private EmailFactory spanishSupplierRegistrationApprovedWithProductsEmail;
    @Mock
    private Email email;

    @InjectMocks
    private final RegistrationApprovedMailSender mailSender = new RegistrationApprovedMailSender();

    @Test
    public void sendToBuyerGeneratesEmailAndSendsToRestfulMailAPI() {
        when(emailUrlBuilder.buildMailUrl(Language.CHINESE)).thenReturn(RESTFUL_EMAIL_URL);

        String emailAddress = "me@my.com";
        when(buyerRegistrationApprovedEmail.createEmail(emailAddress, new Object[] {})).thenReturn(email);

        mailSender.sendToBuyer(emailAddress);

        verify(restTemplate).postForObject(RESTFUL_EMAIL_URL, email, Email.class);
    }

    @Test
    public void sendToEnglishSupplierGeneratesEmailAndSendsToRestfulMailAPI() {
        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenReturn(RESTFUL_EMAIL_URL);

        String firstName = "firstName";
        String emailAddress = "me@my.com";
        when(englishSupplierRegistrationApprovedEmail.createEmail(emailAddress, new Object[] { firstName }))
                .thenReturn(email);

        mailSender.sendToSupplier(firstName, emailAddress, Language.ENGLISH);

        verify(restTemplate).postForObject(RESTFUL_EMAIL_URL, email, Email.class);
    }

    @Test
    public void sendToSpanishSupplierGeneratesEmailAndSendsToRestfulMailAPI() {
        when(emailUrlBuilder.buildMailUrl(Language.SPANISH)).thenReturn(RESTFUL_EMAIL_URL);

        String firstName = "firstName";
        String emailAddress = "me@my.com";
        when(spanishSupplierRegistrationApprovedEmail.createEmail(emailAddress, new Object[] { firstName }))
                .thenReturn(email);

        mailSender.sendToSupplier(firstName, emailAddress, Language.SPANISH);

        verify(restTemplate).postForObject(RESTFUL_EMAIL_URL, email, Email.class);
    }

    @Test
    public void sendWithProductsToEnglishSupplierGeneratesWithProdcutsEmailAndSendsToRestfulMailAPI() {
        when(emailUrlBuilder.buildMailUrl(Language.ENGLISH)).thenReturn(RESTFUL_EMAIL_URL);

        String firstName = "firstName";
        String emailAddress = "me@my.com";
        when(englishSupplierRegistrationApprovedWithProductsEmail.createEmail(emailAddress, new Object[] { firstName }))
                .thenReturn(email);

        mailSender.sendToSupplierWithProducts(firstName, emailAddress, Language.ENGLISH);

        verify(restTemplate).postForObject(RESTFUL_EMAIL_URL, email, Email.class);
    }

    @Test
    public void sendWithProductsToSpanishSupplierGeneratesWithProdcutsEmailAndSendsToRestfulMailAPI() {
        when(emailUrlBuilder.buildMailUrl(Language.SPANISH)).thenReturn(RESTFUL_EMAIL_URL);

        String firstName = "firstName";
        String emailAddress = "me@my.com";
        when(spanishSupplierRegistrationApprovedWithProductsEmail.createEmail(emailAddress, new Object[] { firstName }))
                .thenReturn(email);

        mailSender.sendToSupplierWithProducts(firstName, emailAddress, Language.SPANISH);

        verify(restTemplate).postForObject(RESTFUL_EMAIL_URL, email, Email.class);
    }

}
