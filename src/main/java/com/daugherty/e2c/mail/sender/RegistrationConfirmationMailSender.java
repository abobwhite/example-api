package com.daugherty.e2c.mail.sender;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPendingUserDao;

/**
 * Sends registration confirmation emails to new users.
 */
@Component
public class RegistrationConfirmationMailSender {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private EmailFactory buyerRegistrationConfirmationEmail;
    @Inject
    private EmailFactory englishSupplierRegistrationConfirmationEmail;
    @Inject
    private EmailFactory spanishSupplierRegistrationConfirmationEmail;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private RestTemplate restTemplate;
    @Inject
    private JdbcPartyDao partyDao;
    @Inject
    private JdbcPendingUserDao pendingUserDao;

    public void resend(Long partyId) {
        Party party = partyDao.loadById(partyId);
        PendingUser pendingUser = pendingUserDao.loadPendingUserByEmailAddress(party.getContact().getEmailAddress());

        this.send(party.getContact().getEmailAddress(), pendingUser.getUsername(), pendingUser.getConfirmationToken(),
                party.getContact().getLanguage());
    }

    public void send(String emailAddress, String username, String confirmationToken, Language language) {
        Email email = buildEmailForLanguage(emailAddress, username, confirmationToken, language);
        String mailUrl = emailUrlBuilder.buildMailUrl(language);
        restTemplate.postForObject(mailUrl, email, Email.class);
        LOG.info("Sent registration confirmation email for username " + username + " to email address " + emailAddress
                + " using mail URL " + mailUrl);
    }

    private Email buildEmailForLanguage(String emailAddress, String username, String confirmationToken,
            Language language) {
        String confirmationTokenUrl = buildConfirmationTokenURL(confirmationToken, language);
        if (Language.CHINESE.equals(language)) {
            return buyerRegistrationConfirmationEmail.createEmail(emailAddress, new Object[] { username,
                    confirmationTokenUrl, confirmationTokenUrl });
        } else if (Language.SPANISH.equals(language)) {
            return spanishSupplierRegistrationConfirmationEmail.createEmail(emailAddress, new Object[] { username,
                    confirmationTokenUrl, confirmationTokenUrl });
        } else {
            return englishSupplierRegistrationConfirmationEmail.createEmail(emailAddress, new Object[] { username,
                    confirmationTokenUrl, confirmationTokenUrl });
        }
    }

    private String buildConfirmationTokenURL(String confirmationToken, Language language) {
        return getSiteUrl(language) + "/sessions/first/" + confirmationToken;
    }

    private String getSiteUrl(Language language) {
        return Language.CHINESE.equals(language) ? emailUrlBuilder.buildBuyerSiteUrl() : emailUrlBuilder
                .buildGlobalSiteUrl();
    }

}
