package com.daugherty.e2c.mail.sender;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;

/**
 * Sends an email to a Buyer/Supplier once they submit their initial registration for approval.
 */
@Component
public class RegistrationCompletionMailSender {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory buyerRegistrationCompletionEmail;

    public void sendToBuyer(String emailAddress) {
        Email email = buyerRegistrationCompletionEmail.createEmail(emailAddress, new Object[] {});
        restTemplate.postForObject(emailUrlBuilder.buildMailUrl(Language.CHINESE), email, Email.class);
        LOG.info("Sent Buyer registration completion email to email address " + emailAddress);
    }

}
