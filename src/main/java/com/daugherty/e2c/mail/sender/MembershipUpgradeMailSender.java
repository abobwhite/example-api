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
public class MembershipUpgradeMailSender {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory englishMembershipLevelTwoUpgradeEmail;
    @Inject
    private EmailFactory spanishMembershipLevelTwoUpgradeEmail;
    @Inject
    private EmailFactory englishMembershipLevelThreeUpgradeEmail;
    @Inject
    private EmailFactory spanishMembershipLevelThreeUpgradeEmail;
    @Inject
    private EmailFactory englishMembershipLevelFourUpgradeEmail;
    @Inject
    private EmailFactory spanishMembershipLevelFourUpgradeEmail;

    public void sendEmail(Supplier supplier, MembershipLevel level) {
        Email email = getEmail(supplier, level);

        restTemplate.postForObject(emailUrlBuilder.buildMailUrl(supplier.getContact().getLanguage()), email,
                Email.class);
    }

    private Email getEmail(Supplier supplier, MembershipLevel level) {
        if (isLevelTwo(level)) {
            return levelTwoEmail(supplier, level);
        } else if (isLevelThree(level)) {
            return levelThreeEmail(supplier, level);
        } else {
            return levelFourEmail(supplier, level);
        }
    }

    private boolean isLevelTwo(MembershipLevel level) {
        return Integer.valueOf(2).equals(level.getValue());
    }

    private boolean isLevelThree(MembershipLevel level) {
        return Integer.valueOf(3).equals(level.getValue());
    }

    private Email levelTwoEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelTwoUpgradeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelTwoUpgradeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }

    private Email levelThreeEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelThreeUpgradeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelThreeUpgradeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }

    private Email levelFourEmail(Supplier supplier, MembershipLevel level) {
        Email email;
        if (Language.ENGLISH.equals(supplier.getContact().getLanguage())) {
            email = englishMembershipLevelFourUpgradeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        } else {
            email = spanishMembershipLevelFourUpgradeEmail.createEmail(
                    supplier.getEmailWithPersonalName(),
                    new Object[] { supplier.getContact().getFirstName(), level.getProductCount(),
                            level.getHotProductCount() });
        }
        return email;
    }
}
