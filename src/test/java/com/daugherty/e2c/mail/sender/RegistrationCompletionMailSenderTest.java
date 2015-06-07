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
public class RegistrationCompletionMailSenderTest {

    private static final String RESTFUL_EMAIL_URL = "emailUrl";

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private EmailUrlBuilder emailUrlBuilder;
    @Mock
    private Email email;
    @Mock
    private EmailFactory buyerRegistrationCompletionEmail;
    @Mock
    private EmailFactory englishSupplierRegistrationCompletionEmail;
    @Mock
    private EmailFactory spanishSupplierRegistrationCompletionEmail;

    @InjectMocks
    private final RegistrationCompletionMailSender mailSender = new RegistrationCompletionMailSender();

    @Test
    public void sendToBuyerGeneratesEmailAndSendsToRestfulMailAPI() {
        when(emailUrlBuilder.buildMailUrl(Language.CHINESE)).thenReturn(RESTFUL_EMAIL_URL);

        String emailAddress = "me@my.com";
        when(buyerRegistrationCompletionEmail.createEmail(emailAddress, new Object[] {})).thenReturn(email);

        mailSender.sendToBuyer(emailAddress);

        verify(restTemplate).postForObject(RESTFUL_EMAIL_URL, email, Email.class);
    }

}
