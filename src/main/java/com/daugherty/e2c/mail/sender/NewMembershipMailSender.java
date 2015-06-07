package com.daugherty.e2c.mail.sender;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;

/**
 * Sends an email to a Supplier once their initial membership has been paid
 */
@Component
public class NewMembershipMailSender {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory englishMembershipLevelOneWelcomeEmail;
    @Inject
    private EmailFactory spanishMembershipLevelOneWelcomeEmail;
    @Inject
    private EmailFactory englishMembershipLevelTwoWelcomeEmail;
    @Inject
    private EmailFactory spanishMembershipLevelTwoWelcomeEmail;
    @Inject
    private EmailFactory englishMembershipLevelThreeWelcomeEmail;
    @Inject
    private EmailFactory spanishMembershipLevelThreeWelcomeEmail;
    @Inject
    private EmailFactory englishMembershipLevelFourWelcomeEmail;
    @Inject
    private EmailFactory spanishMembershipLevelFourWelcomeEmail;

    public void sendEmail(Supplier supplier, MembershipLevel level) {
        Email email = getEmail(supplier, level);

        restTemplate.postForObject(emailUrlBuilder.buildMailUrl(supplier.getContact().getLanguage()), email,
                Email.class);
    }

    private Email getEmail(Supplier supplier, MembershipLevel level) {
        if (isLevelOne(level)) {
            return levelOneEmail(supplier, level);
        } else if (isLevelTwo(level)) {
            return levelTwoEmail(supplier, level);
        } else if (isLevelThree(level)) {
            return levelThreeEmail(supplier, level);
        } else {
            return levelFourEmail(supplier, level);
        }
    }

    private boolean isLevelOne(MembershipLevel level) {
        return Integer.valueOf(1).equals(level.getValue());
    }

    private boolean isLevelTwo(MembershipLevel level) {
        return Integer.valueOf(2).equals(level.getValue());
    }

    private boolean isLevelThree(MembershipLevel level) {
        return Integer.valueOf(3).equals(level.getValue());
    }

    private Email levelOneEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelOneWelcomeEmail.createEmail(supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount() });
        } else {
            email = spanishMembershipLevelOneWelcomeEmail.createEmail(supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount() });
        }
        return email;
    }

    private Email levelTwoEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelTwoWelcomeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelTwoWelcomeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }

    private Email levelThreeEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelThreeWelcomeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelThreeWelcomeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }

    private Email levelFourEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelFourWelcomeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelFourWelcomeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }
}
