package com.daugherty.e2c.mail.sender;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;

/**
 * Sends Email to Anonymous Parties Creating Buy Leads.
 */
@Component
public class BuyLeadAnonymousPartyMessageMailSender {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private EmailFactory buyLeadAnonymousPartyMessageNotificationEmail;

    public void send(BuyLead buyLead) throws NonEssentialMailFailureException {
        try {
            sendEmail(buyLead);
        } catch (RuntimeException e) {
            throw new NonEssentialMailFailureException(buyLead.getRequester().getId(), e);
        }
    }

    private void sendEmail(BuyLead buyLead) throws NonEssentialMailFailureException {
        Party party = buyLead.getRequester();
        String url = emailUrlBuilder.buildMailUrl(Language.CHINESE);
        if (PartyType.ANONYMOUS.equals(party.getPartyType())) {
            Email email = buildEmail(party);

            try {
                restTemplate.postForObject(url, email, Email.class);
                LOG.info("Sent Message email to " + party.getContact().getEmailAddress());
            } catch (RestClientException e) {
                throw new NonEssentialMailFailureException(party.getId(), url, email, e);
            }
        }

    }

    private Email buildEmail(Party party) {
        String toEmailAddress = party.getEmailWithPersonalName();
        String buyerSiteUrl = emailUrlBuilder.buildBuyerSiteUrl();

        Object[] bodyTemplateArgs = new Object[] { buildBuyerRegistrationUrl(buyerSiteUrl) };

        return buyLeadAnonymousPartyMessageNotificationEmail.createEmail(toEmailAddress, bodyTemplateArgs);
    }

    private String buildBuyerRegistrationUrl(String buyerSiteUrl) {
        return buyerSiteUrl + "/registration/buyer";
    }

}
