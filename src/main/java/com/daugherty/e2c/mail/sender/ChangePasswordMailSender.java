package com.daugherty.e2c.mail.sender;

import java.util.Locale;

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
 * Sends Email to Buyer or Supplier after password Change.
 */
@Component
public class ChangePasswordMailSender {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory buyerChangePasswordEmail;
    @Inject
    private EmailFactory englishSupplierChangePasswordEmail;
    @Inject
    private EmailFactory spanishSupplierChangePasswordEmail;

    public void send(String emailAddress, String username, Locale locale) {
        Email email = buildEmailForLocale(emailAddress, username, locale);
        restTemplate.postForObject(emailUrlBuilder.buildMailUrl(locale), email, Email.class);
        LOG.info("Sent change password email to email address " + emailAddress);
    }

    private Email buildEmailForLocale(String emailAddress, String username, Locale locale) {
        if (Language.CHINESE.matches(locale)) {
            return buyerChangePasswordEmail.createEmail(emailAddress, new Object[] {});
        } else if (Language.SPANISH.matches(locale)) {
            return spanishSupplierChangePasswordEmail.createEmail(emailAddress, new Object[] { emailAddress });
        } else {
            return englishSupplierChangePasswordEmail.createEmail(emailAddress, new Object[] { emailAddress });
        }
    }

}
