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
public class MembershipRenewalMailSender {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory englishMembershipLevelOneRenewalEmail;
    @Inject
    private EmailFactory spanishMembershipLevelOneRenewalEmail;
    @Inject
    private EmailFactory englishMembershipLevelTwoRenewalEmail;
    @Inject
    private EmailFactory spanishMembershipLevelTwoRenewalEmail;
    @Inject
    private EmailFactory englishMembershipLevelThreeRenewalEmail;
    @Inject
    private EmailFactory spanishMembershipLevelThreeRenewalEmail;
    @Inject
    private EmailFactory englishMembershipLevelFourRenewalEmail;
    @Inject
    private EmailFactory spanishMembershipLevelFourRenewalEmail;

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
            email = englishMembershipLevelOneRenewalEmail.createEmail(supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount() });
        } else {
            email = spanishMembershipLevelOneRenewalEmail.createEmail(supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount() });
        }
        return email;
    }

    private Email levelTwoEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelTwoRenewalEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelTwoRenewalEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }

    private Email levelThreeEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelThreeRenewalEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelThreeRenewalEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }

    private Email levelFourEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelFourRenewalEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelFourRenewalEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }
}
