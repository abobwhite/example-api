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
 * Sends Email to Buyer or Supplier to reset their password.
 */
@Component
public class ForgotPasswordMailSender {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory buyerForgotPasswordEmail;
    @Inject
    private EmailFactory englishSupplierForgotPasswordEmail;
    @Inject
    private EmailFactory spanishSupplierForgotPasswordEmail;

    public void send(String username, String emailAddress, String confirmationToken, Locale locale) {
        Email email = buildEmailForLocale(username, emailAddress, confirmationToken, locale);
        restTemplate.postForObject(emailUrlBuilder.buildMailUrl(locale), email, Email.class);
        LOG.info("Sent forgot password email to email address " + emailAddress);
    }

    private Email buildEmailForLocale(String username, String emailAddress, String confirmationToken, Locale locale) {
        String confirmationTokenUrl = buildForgotPasswordUrl(confirmationToken, locale);
        if (Language.CHINESE.matches(locale)) {
            return buyerForgotPasswordEmail.createEmail(emailAddress, new Object[] { username, confirmationTokenUrl,
                    confirmationTokenUrl });
        } else if (Language.SPANISH.matches(locale)) {
            return spanishSupplierForgotPasswordEmail.createEmail(emailAddress, new Object[] { username,
                    confirmationTokenUrl, confirmationTokenUrl });
        } else {
            return englishSupplierForgotPasswordEmail.createEmail(emailAddress, new Object[] { username,
                    confirmationTokenUrl, confirmationTokenUrl });
        }
    }

    private String buildForgotPasswordUrl(String confirmationToken, Locale locale) {
        return getSiteUrl(locale) + "/userCredentials/reset/" + confirmationToken;
    }

    private String getSiteUrl(Locale locale) {
        return Locale.CHINESE.equals(locale) ? emailUrlBuilder.buildBuyerSiteUrl() : emailUrlBuilder
                .buildGlobalSiteUrl();
    }
}
