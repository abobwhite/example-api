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
 * Sends an email to a Buyer/Supplier once their initial registration has been approved.
 */
@Component
public class RegistrationApprovedMailSender {

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory buyerRegistrationApprovedEmail;
    @Inject
    private EmailFactory englishSupplierRegistrationApprovedEmail;
    @Inject
    private EmailFactory spanishSupplierRegistrationApprovedEmail;
    @Inject
    private EmailFactory englishSupplierRegistrationApprovedWithProductsEmail;
    @Inject
    private EmailFactory spanishSupplierRegistrationApprovedWithProductsEmail;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public void sendToBuyer(String emailAddress) {
        Email email = buyerRegistrationApprovedEmail.createEmail(emailAddress, new Object[] {});
        restTemplate.postForObject(emailUrlBuilder.buildMailUrl(Language.CHINESE), email, Email.class);
        LOG.info("Sent Buyer registration approval email to email address " + emailAddress);
    }

    public void sendToSupplier(String firstName, String emailAddress, Language language) {
        Email email = getNoProductsEmailForLanguage(firstName, emailAddress, language);
        restTemplate.postForObject(emailUrlBuilder.buildMailUrl(language), email, Email.class);
        LOG.info("Sent Supplier registration approval email to email address " + emailAddress);
    }

    private Email getNoProductsEmailForLanguage(String firstName, String emailAddress, Language language) {
        if (Language.SPANISH.equals(language)) {
            return spanishSupplierRegistrationApprovedEmail.createEmail(emailAddress, new Object[] { firstName });
        } else {
            return englishSupplierRegistrationApprovedEmail.createEmail(emailAddress, new Object[] { firstName });
        }
    }

    public void sendToSupplierWithProducts(String firstName, String emailAddress, Language language) {
        Email email = getHasProductsEmailForLanguage(firstName, emailAddress, language);
        restTemplate.postForObject(emailUrlBuilder.buildMailUrl(language), email, Email.class);
        LOG.info("Sent Supplier (with Products) registration approval confirmation to email address " + emailAddress);
    }

    private Email getHasProductsEmailForLanguage(String firstName, String emailAddress, Language language) {
        if (Language.SPANISH.equals(language)) {
            return spanishSupplierRegistrationApprovedWithProductsEmail.createEmail(emailAddress,
                    new Object[] { firstName });
        } else {
            return englishSupplierRegistrationApprovedWithProductsEmail.createEmail(emailAddress,
                    new Object[] { firstName });
        }
    }

}
