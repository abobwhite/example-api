package com.daugherty.e2c.mail.sender;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.persistence.data.AnonymousReadDao;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

/**
 * Sends emails for Message replies.
 */
@Component
public class ProductMessageReplyMailSender {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private AnonymousReadDao anonymousReadDao;
    @Inject
    private BuyerReadDao buyerReadDao;
    @Inject
    private EmailFactory supplierToBuyerReplyNotificationEmail;
    @Inject
    private EmailFactory supplierToEnglishSupplierReplyNotificationEmail;
    @Inject
    private EmailFactory supplierToSpanishSupplierReplyNotificationEmail;
    @Inject
    private EmailFactory buyerToEnglishSupplierReplyNotificationEmail;
    @Inject
    private EmailFactory buyerToSpanishSupplierReplyNotificationEmail;
    @Inject
    private EmailFactory advancedSupplierReplyEmail;

    public void send(Interaction interaction) throws NonEssentialMailFailureException {
        try {
            sendEmail(interaction);
        } catch (RuntimeException e) {
            throw new NonEssentialMailFailureException(interaction.getId(), e);
        }
    }

    private void sendEmail(Interaction interaction) throws NonEssentialMailFailureException {
        Party sendingParty = getParty(interaction.getSender());
        Party receivingParty = getParty(interaction.getReceiver());
        String url = emailUrlBuilder.buildMailUrl(receivingParty.getContact().getLanguage());
        Email email = buildEmail(interaction, sendingParty, receivingParty);

        try {
            restTemplate.postForObject(url, email, Email.class);
            LOG.info("Sent Message reply email to " + receivingParty.getContact().getEmailAddress());
        } catch (RestClientException e) {
            throw new NonEssentialMailFailureException(interaction.getId(), url, email, e);
        }
    }

    private Party getParty(Party messenger) {
        if (PartyType.SUPPLIER.equals(messenger.getPartyType())) {
            return supplierReadDao.loadApproved(messenger.getId(), Locale.ENGLISH);
        } else if (PartyType.BUYER.equals(messenger.getPartyType())) {
            return buyerReadDao.loadLatest(messenger.getId());
        } else {
            return anonymousReadDao.load(messenger.getId());
        }
    }

    private Email buildEmail(Interaction interaction, Party sendingParty, Party receivingParty) {
        Language receiverLanguage = receivingParty.getContact().getLanguage();
        String buyerSiteUrl = emailUrlBuilder.buildBuyerSiteUrl();
        String globalSiteUrl = emailUrlBuilder.buildGlobalSiteUrl();
        String toEmailAddress = receivingParty.getEmailWithPersonalName();

        if (interactionInvolvesSupplierWithAdvancedMailCapability(receivingParty)) {
            return buildSimpleBodyEmail(interaction, sendingParty, receiverLanguage, buyerSiteUrl, globalSiteUrl,
                    toEmailAddress);
        } else {
            return buildNotificationEmail(interaction, sendingParty, receivingParty, receiverLanguage, buyerSiteUrl,
                    globalSiteUrl, toEmailAddress);
        }
    }

    private boolean interactionInvolvesSupplierWithAdvancedMailCapability(Party receivingParty) {
        return receivingParty.isAdvancedWebAndMailCapabilityEnabled();
    }

    private Email buildSimpleBodyEmail(Interaction interaction, Party sendingParty, Language receiverLanguage,
            String buyerSiteUrl, String globalSiteUrl, String toEmailAddress) {
        String fromEmailAddress = sendingParty.getEmailWithPersonalName();
        Object[] bodyTemplateArgs = new Object[] { interaction.getBody() };
        return advancedSupplierReplyEmail.createEmail(fromEmailAddress, toEmailAddress, bodyTemplateArgs);
    }

    private Email buildNotificationEmail(Interaction interaction, Party sendingParty, Party receivingParty,
            Language receiverLanguage, String buyerSiteUrl, String globalSiteUrl, String toEmailAddress) {
        if (Language.CHINESE.equals(receiverLanguage)) {
            return buildBuyerNotificationEmail(interaction, buyerSiteUrl, toEmailAddress);
        } else {
            return buildSupplierNotificationEmail(interaction, sendingParty, receivingParty, receiverLanguage,
                    globalSiteUrl, toEmailAddress);
        }
    }

    private String buildMessageDetailViewUrl(boolean isAnonymous, String publicId, String siteUrl) {
        return isAnonymous ? siteUrl + "/messages/view/" + publicId : siteUrl + "/dashboard/messages/view/" + publicId;
    }

    private Email buildBuyerNotificationEmail(Interaction interaction, String buyerSiteUrl, String toEmailAddress) {
        String messageDetailViewUrl = buildMessageDetailViewUrl(interaction.getReceiver().isAnonymous(), interaction
                .getMessage().getPublicId(), buyerSiteUrl);
        Object[] bodyTemplateArgs = new Object[] { messageDetailViewUrl };
        return supplierToBuyerReplyNotificationEmail.createEmail(toEmailAddress, bodyTemplateArgs);
    }

    private Email buildSupplierNotificationEmail(Interaction interaction, Party sendingParty, Party receivingParty,
            Language receiverLanguage, String globalSiteUrl, String toEmailAddress) {
        String supplierFirstName = receivingParty.getContact().getFirstName();
        String messageDetailViewUrl = buildMessageDetailViewUrl(interaction.getReceiver().isAnonymous(), interaction
                .getMessage().getPublicId(), globalSiteUrl);
        Object[] bodyTemplateArgs = new Object[] { supplierFirstName, messageDetailViewUrl };

        if (Language.CHINESE.equals(sendingParty.getContact().getLanguage())) {
            return buildBuyerToSupplierNotificationEmail(receiverLanguage, toEmailAddress, bodyTemplateArgs);
        } else {
            return buildSupplierToSupplierNotificationEmail(receiverLanguage, toEmailAddress, bodyTemplateArgs);
        }
    }

    private Email buildBuyerToSupplierNotificationEmail(Language receiverLanguage, String toEmailAddress,
            Object[] bodyTemplateArgs) {
        if (Language.SPANISH.equals(receiverLanguage)) {
            return buyerToSpanishSupplierReplyNotificationEmail.createEmail(toEmailAddress, bodyTemplateArgs);
        } else {
            return buyerToEnglishSupplierReplyNotificationEmail.createEmail(toEmailAddress, bodyTemplateArgs);
        }
    }

    private Email buildSupplierToSupplierNotificationEmail(Language receiverLanguage, String toEmailAddress,
            Object[] bodyTemplateArgs) {
        if (Language.SPANISH.equals(receiverLanguage)) {
            return supplierToSpanishSupplierReplyNotificationEmail.createEmail(toEmailAddress, bodyTemplateArgs);
        } else {
            return supplierToEnglishSupplierReplyNotificationEmail.createEmail(toEmailAddress, bodyTemplateArgs);
        }
    }

}
