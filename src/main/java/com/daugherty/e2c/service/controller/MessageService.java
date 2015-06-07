package com.daugherty.e2c.service.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.MessageFlagger;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.accessor.MessageAccessor;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.InteractionFilter;
import com.daugherty.e2c.business.accessor.filter.MessageFilter;
import com.daugherty.e2c.business.mapper.InteractionMapper;
import com.daugherty.e2c.business.uploader.MessageAttachementUploader;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageSummary;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonDocumentUrl;
import com.daugherty.e2c.service.json.JsonInteraction;
import com.daugherty.e2c.service.json.JsonInteractions;
import com.daugherty.e2c.service.json.JsonMessage;
import com.daugherty.e2c.service.json.JsonMessageSummary;
import com.daugherty.e2c.service.json.JsonMessages;
import com.daugherty.e2c.service.json.JsonParty;
import com.daugherty.e2c.service.json.JsonPublicId;
import com.daugherty.e2c.service.json.RootWrappingObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST resource for Messages.
 */
@Controller
public class MessageService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private MessageAccessor messageAccessor;
    @Inject
    private Accessor<MessageSummary> messageSummaryAccessor;
    @Inject
    private MessageFlagger messageFlagger;
    @Inject
    private Accessor<Interaction> interactionAccessor;
    @Inject
    private Mutator<Interaction> interactionMutator;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private InteractionMapper interactionMapper;
    @Inject
    private MessageAttachementUploader messageAttachementUploader;

    @RequestMapping(value = "/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.ADMIN })
    @ResponseBody
    public JsonMessages retrieveMessages(
            @RequestParam(value = MessageFilter.RECEIVER_ID, required = false) String receiverId, @RequestParam(
                    value = MessageFilter.SENDER_ID, required = false) String senderId, @RequestParam(
                    value = MessageFilter.FLAGGED, required = false) Boolean flagged, @RequestParam(
                    value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Locale locale) {

        MessageFilter filter = new MessageFilter(receiverId, flagged, senderId, sortBy, sortDesc, startItem, count);
        List<Message> messages = messageAccessor.find(filter);

        JsonMessages jsonMessages = new JsonMessages();
        for (Message message : messages) {
            jsonMessages.add(new JsonMessage(message, documentUrlFactory, locale));
        }
        return jsonMessages;
    }

    @RequestMapping(value = "/buyers/{id}/messageSummary", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER })
    @ResponseBody
    public JsonMessageSummary retrieveBuyerMessageSummary(@PathVariable String id) {
        return new JsonMessageSummary(messageSummaryAccessor.load(id, Locale.ENGLISH));
    }

    @RequestMapping(value = "/suppliers/{id}/messageSummary", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public JsonMessageSummary retrieveSupplierMessageSummary(@PathVariable String id) {
        return new JsonMessageSummary(messageSummaryAccessor.load(id, Locale.ENGLISH));
    }

    @RequestMapping(value = "/messages/{id}/flags", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER })
    @ResponseBody
    public void flagMessage(@PathVariable String id, @RequestBody JsonPublicId publicPartyId) {
        messageFlagger.flagMessageForParty(id, publicPartyId.getStringValue());
    }

    @RequestMapping(value = "/messages/{id}/flags/{publicPartyId}", method = RequestMethod.DELETE)
    @Secured({ Role.BUYER, Role.SUPPLIER })
    @ResponseBody
    public void unflagMessage(@PathVariable String id, @PathVariable String publicPartyId) {
        messageFlagger.unflagMessageForParty(id, publicPartyId);
    }

    @RequestMapping(value = "/messages/{id}/interactions", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.ADMIN, Role.TRANSLATOR, Role.BUYER_MODERATOR, Role.SUPPLIER_MODERATOR,
            Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonInteractions retrieveInteractions(@PathVariable String id, @RequestParam(value = "token",
            required = false) BigInteger token,
            @RequestParam(value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Principal principal, Locale locale) {

        InteractionFilter filter = new InteractionFilter(id, sortBy, sortDesc, startItem, count);

        List<Interaction> interactions = interactionAccessor.find(filter);

        JsonInteractions jsonInteractions = new JsonInteractions();
        for (Interaction interaction : interactions) {
            jsonInteractions.add(new JsonInteraction(interaction, documentUrlFactory, locale));
        }
        return jsonInteractions;
    }

    @RequestMapping(value = "/messages/{id}/interactions", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonInteraction replyToMessage(@PathVariable String id,
            @RequestParam(value = "token", required = false) BigInteger token,
            @RequestBody JsonInteraction jsonInteraction, Locale locale) {

        if (jsonInteraction.getSender() == null) {
            Message message = messageAccessor.load(id, locale, jsonInteraction.getReceiver().getId());
            JsonParty sender = new JsonParty(message.getOtherParty(), documentUrlFactory, locale);
            jsonInteraction.setSender(sender);
        }

        Interaction interaction = interactionMutator.create(interactionMapper.toNewDomainObject(id, jsonInteraction,
                locale));
        return new JsonInteraction(interaction, documentUrlFactory, locale);
    }

    @RequestMapping(value = "/interactions/{id}/reads", method = RequestMethod.POST)
    @Secured({ Role.BUYER, Role.SUPPLIER })
    @ResponseBody
    public void markInteractionAsRead(@PathVariable Long id) {
        Interaction interaction = interactionAccessor.load(id, Locale.ENGLISH);
        interaction.markAsRead();
        interactionMutator.update(interaction);
    }

    @RequestMapping(value = "/messages/{id}/attachment", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER, Role.BUYER, Role.ADMIN, Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void addAttachemnt(@PathVariable String id,
            @RequestParam(value = "attachmentName", required = true) String attachmentName,
            MultipartHttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpStatus.OK.value());

        PrintWriter out = response.getWriter();
        out.write(addDocument(id, attachmentName, request, messageAttachementUploader, locale));
        out.flush();
        out.close();
    }

    private String addDocument(String id, String attachmentName, MultipartHttpServletRequest request,
            Uploader uploader, Locale locale) {
        try {
            Iterator<String> itr = request.getFileNames();

            MultipartFile mpf = request.getFile(itr.next());
            LOG.debug(mpf.getOriginalFilename() + " uploaded for message id " + id + " with content type "
                    + mpf.getContentType());

            ObjectMapper objectMapper = new RootWrappingObjectMapper();
            String uploadedDocumentUri = uploader.uploadDocument(id, attachmentName, mpf.getContentType(),
                    mpf.getInputStream());
            return objectMapper.writeValueAsString(new JsonDocumentUrl(documentUrlFactory.createDocumentUrl(
                    uploadedDocumentUri, locale)));
        } catch (IOException e) {
            throw new UploaderException(e);
        }
    }

}
