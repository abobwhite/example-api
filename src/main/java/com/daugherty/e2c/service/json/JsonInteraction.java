package com.daugherty.e2c.service.json;

import java.util.Date;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Interaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls an Interaction domain object to/from JSON.
 */
@JsonRootName("interaction")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonInteraction {

    @JsonProperty("id")
    private Long id;
    @JsonProperty(Interaction.BODY_SERIAL_PROPERTY)
    private String body;
    @JsonProperty(Interaction.SENDER_SERIAL_PROPERTY)
    private JsonParty sender;
    @JsonProperty(Interaction.RECEIVER_SERIAL_PROPERTY)
    private JsonParty receiver;
    @JsonProperty("sentTime")
    private Date sentTime;
    @JsonProperty("read")
    private Boolean read;

    public JsonInteraction() {
    }

    public JsonInteraction(Interaction interaction, DocumentUrlFactory documentUrlFactory, Locale locale) {
        setId(interaction.getId());
        setBody(interaction.getBody());
        setSender(new JsonParty(interaction.getSender(), documentUrlFactory, locale));
        setReceiver(new JsonParty(interaction.getReceiver(), documentUrlFactory, locale));
        setSentTime(interaction.getSentTime());
        setRead(interaction.isRead());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public JsonParty getSender() {
        return sender;
    }

    public void setSender(JsonParty sender) {
        this.sender = sender;
    }

    public JsonParty getReceiver() {
        return receiver;
    }

    public void setReceiver(JsonParty receiver) {
        this.receiver = receiver;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
