package com.daugherty.e2c.service.json;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageAttachment;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Marshalls/unmarshalls a Message domain object to/from JSON.
 */
@JsonRootName("message")
public class JsonMessage {

    @JsonProperty("id")
    private String id;
    @JsonProperty(Message.SUBJECT_SERIAL_PROPERTY)
    private String subject;
    @JsonProperty(Message.OTHER_PARTY_SERIAL_PROPERTY)
    private JsonParty otherParty;
    @JsonProperty("flagged")
    private Boolean flagged;
    @JsonProperty(Message.LAST_INTERACTION_SENT_TIME_SERIAL_PROPERTY)
    private Date mostRecentInteractionTime;
    @JsonProperty("allInteractionsRead")
    private Boolean allInteractionsRead;
    @JsonProperty("interactions")
    private List<JsonInteraction> interactions;
    @JsonProperty(Message.ATTACHMENTS_SERIAL_PROPERTY)
    private List<JsonMessageAttachment> attachments = Lists.newArrayList();
    @JsonProperty("messageType")
    private String messageType;
    @JsonProperty("links")
    private Map<String, String> links;

    public JsonMessage() {
    }

    public JsonMessage(Message message, DocumentUrlFactory documentUrlFactory, Locale locale) {
        setId(message.getPublicId());
        setSubject(message.getSubject());
        setOtherParty(new JsonParty(message.getOtherParty(), documentUrlFactory, locale));
        setFlagged(message.isFlagged());
        setMostRecentInteractionTime(message.getLastInteraction().getSentTime());
        setMessageType(message.getMessageType().getType());
        setAllInteractionsRead(message.isCompletelyRead());
        setLinks();
        setAttachments(message.getAttachements(), documentUrlFactory, locale);
    }

    public Map<String, String> getLinks() {
        return links;
    }

    private void setLinks() {
        links = Maps.newHashMap();
        if (id != null) {
            links.put("interactions", "messages/" + id + "/interactions?_=" + System.currentTimeMillis());
        }
    }

    public List<JsonMessageAttachment> getAttachments() {
        return attachments;
    }

    private void setAttachments(List<MessageAttachment> messageAtachments, DocumentUrlFactory documentUrlFactory,
            Locale locale) {

        this.attachments = Lists.newArrayList();

        for (MessageAttachment messageAttachment : messageAtachments) {
            attachments.add(new JsonMessageAttachment(messageAttachment, documentUrlFactory, locale));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public JsonParty getOtherParty() {
        return otherParty;
    }

    public void setOtherParty(JsonParty otherParty) {
        this.otherParty = otherParty;
    }

    public Boolean getFlagged() {
        return flagged;
    }

    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }

    public Date getMostRecentInteractionTime() {
        return mostRecentInteractionTime;
    }

    public void setMostRecentInteractionTime(Date mostRecentInteractionTime) {
        this.mostRecentInteractionTime = mostRecentInteractionTime;
    }

    public Boolean getAllInteractionsRead() {
        return allInteractionsRead;
    }

    public void setAllInteractionsRead(Boolean allInteractionsRead) {
        this.allInteractionsRead = allInteractionsRead;
    }

    public List<JsonInteraction> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<JsonInteraction> interactions) {
        this.interactions = interactions;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
