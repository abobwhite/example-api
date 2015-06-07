package com.daugherty.e2c.mail.sender;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.Email;
import com.daugherty.e2c.mail.EmailFactory;
import com.daugherty.e2c.mail.EmailUrlBuilder;
import com.daugherty.e2c.mail.NonEssentialMailFailureException;
import com.daugherty.e2c.persistence.data.MembershipReadDao;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;

/**
 * Sends Message emails (notifications or full content) to Suppliers.
 */
@Component
public class ProductMessageMailSender {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private RestTemplate restTemplate;
    @Inject
    private EmailUrlBuilder emailUrlBuilder;
    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private JdbcPartyDao partyDao;
    @Inject
    private ProductReadDao productReadDao;
    @Inject
    private EmailFactory englishMessageNotificationEmail;
    @Inject
    private EmailFactory spanishMessageNotificationEmail;
    @Inject
    private EmailFactory advancedSupplierMessageEmail;
    @Inject
    private MembershipReadDao membershipReadDao;

    public void send(ProductMessage message) throws NonEssentialMailFailureException {
        try {
            sendEmail(message);
        } catch (RuntimeException e) {
            throw new NonEssentialMailFailureException(message.getLastInteraction().getId(), e);
        }
    }

    private void sendEmail(ProductMessage message) throws NonEssentialMailFailureException {
        Supplier receivingSupplier = supplierReadDao.loadApproved(message.getLastInteraction().getReceiver().getId(),
                Locale.ENGLISH);
        String url = emailUrlBuilder.buildMailUrl(receivingSupplier.getContact().getLanguage());
        Email email = buildEmail(message, receivingSupplier);

        try {
            restTemplate.postForObject(url, email, Email.class);
            LOG.info("Sent Message email to " + receivingSupplier.getContact().getEmailAddress());
        } catch (RestClientException e) {
            throw new NonEssentialMailFailureException(message.getLastInteraction().getId(), url, email, e);
        }
    }

    private Email buildEmail(ProductMessage message, Supplier receivingSupplier) {
        String toEmailAddress = receivingSupplier.getEmailWithPersonalName();
        String globalSiteUrl = emailUrlBuilder.buildGlobalSiteUrl();
        String supplierFirstName = receivingSupplier.getContact().getFirstName();
        String productHtmlListItems = buildProductHtmlListItems(message.getProductIds(), globalSiteUrl);

        Membership receivingSupplierMembership = membershipReadDao.loadBySupplierId(receivingSupplier.getId());
        if (!receivingSupplierMembership.isExpired() && receivingSupplier.isAdvancedWebAndMailCapabilityEnabled()) {
            return buildRichContentEmail(message, toEmailAddress, globalSiteUrl, supplierFirstName,
                    productHtmlListItems);
        } else {
            return buildNotificationEmail(message, receivingSupplier, toEmailAddress, globalSiteUrl, supplierFirstName,
                    productHtmlListItems);
        }
    }

    private Email buildRichContentEmail(ProductMessage message, String toEmailAddress, String globalSiteUrl,
            String supplierFirstName, String productHtmlListItems) {
        String fromEmailAddress = getSenderEmailAddress(message.getLastInteraction().getSender());
        String tagHtmlListItems = buildMessageTagHtmlListItems(message.getMessageTags());
        Object[] bodyTemplateArgs = new Object[] { supplierFirstName, productHtmlListItems, tagHtmlListItems,
                message.getLastInteraction().getBody() };
        return advancedSupplierMessageEmail.createEmail(fromEmailAddress, toEmailAddress, bodyTemplateArgs);
    }

    private Email buildNotificationEmail(Message message, Supplier receivingSupplier, String toEmailAddress,
            String globalSiteUrl, String supplierFirstName, String productHtmlListItems) {
        String messageDetailViewUrl = buildMessageDetailViewUrl(message.getPublicId(), globalSiteUrl);
        Object[] bodyTemplateArgs = new Object[] { supplierFirstName, productHtmlListItems, messageDetailViewUrl };
        if (Language.SPANISH.equals(receivingSupplier.getContact().getLanguage())) {
            return spanishMessageNotificationEmail.createEmail(toEmailAddress, bodyTemplateArgs);
        } else {
            return englishMessageNotificationEmail.createEmail(toEmailAddress, bodyTemplateArgs);
        }
    }

    private String buildProductHtmlListItems(List<Long> productIds, String globalSiteUrl) {
        List<Product> products = productReadDao.loadPublishedByProductIds(productIds, Locale.ENGLISH);

        StringBuilder listItems = new StringBuilder();
        for (Product product : products) {
            String name = product.getInformation().getName();
            listItems.append("<li>" + name + "</li>");
        }
        return listItems.toString();
    }

    private String buildMessageTagHtmlListItems(List<MessageTag> messageTags) {
        StringBuilder listItems = new StringBuilder();
        for (MessageTag messageTag : messageTags) {
            listItems.append("<li>" + messageTag.getName() + "</li>");
        }
        return listItems.toString();
    }

    private String buildMessageDetailViewUrl(String id, String globalSiteUrl) {
        return globalSiteUrl + "/dashboard/messages/view/" + id;
    }

    private String getSenderEmailAddress(Party sender) {
        if (PartyType.SUPPLIER.equals(sender.getPartyType())) {
            Supplier sendingSupplier = supplierReadDao.loadApproved(sender.getId(), Locale.ENGLISH);
            return sendingSupplier.getEmailWithPersonalName();

        } else {
            Party party = partyDao.loadById(sender.getId());
            return party.getEmailWithPersonalName();
        }
    }

}
