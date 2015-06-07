package com.daugherty.e2c.mail.sender;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;

/**
 * Sends Message emails (notifications or full content) to Suppliers.
 */
@Component
public class BuyLeadMessageMailSender {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private JdbcPartyDao partyDao;
    @Inject
    private EmailFactory buyerBuyLeadMessageNotificationEmail;
    @Inject
    private EmailFactory anonymousBuyLeadMessageNotificationEmail;

    public void send(BuyLeadMessage message) throws NonEssentialMailFailureException {
        try {
            sendEmail(message);
        } catch (RuntimeException e) {
            throw new NonEssentialMailFailureException(message.getLastInteraction().getId(), e);
        }
    }

    private void sendEmail(BuyLeadMessage message) throws NonEssentialMailFailureException {
        Party receivingParty = partyDao.loadById(message.getLastInteraction().getReceiver().getId());
        String url = emailUrlBuilder.buildMailUrl(Language.CHINESE);
        Email email = buildEmail(message, receivingParty);

        try {
            restTemplate.postForObject(url, email, Email.class);
            LOG.info("Sent Message email to " + receivingParty.getContact().getEmailAddress());
        } catch (RestClientException e) {
            throw new NonEssentialMailFailureException(message.getLastInteraction().getId(), url, email, e);
        }
    }

    private Email buildEmail(BuyLeadMessage message, Party receivingParty) {
        String toEmailAddress = receivingParty.getEmailWithPersonalName();
        String buyerSiteUrl = emailUrlBuilder.buildBuyerSiteUrl();

        if (PartyType.ANONYMOUS.equals(receivingParty.getPartyType())) {
            return buildNotificationEmail(message, receivingParty, toEmailAddress, buyerSiteUrl,
                    anonymousBuyLeadMessageNotificationEmail, buildBuyerRegistrationUrl(buyerSiteUrl));
        } else {
            return buildNotificationEmail(message, receivingParty, toEmailAddress, buyerSiteUrl,
                    buyerBuyLeadMessageNotificationEmail, buildMessageDetailViewUrl(message.getId(), buyerSiteUrl));
        }

    }

    private Email buildNotificationEmail(Message message, Party receivingParty, String toEmailAddress,
            String buyerSiteUrl, EmailFactory emailFactory, String messageDetailUrl) {

        Object[] bodyTemplateArgs = new Object[] { messageDetailUrl };

        return emailFactory.createEmail(toEmailAddress, bodyTemplateArgs);
    }

    private String buildMessageDetailViewUrl(Long id, String buyerSiteUrl) {
        return buyerSiteUrl + "/dashboard/messages/buyLeads/view/" + id;
    }

    private String buildBuyerRegistrationUrl(String buyerSiteUrl) {
        return buyerSiteUrl + "/registration/buyer";
    }

}
