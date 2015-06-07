package com.daugherty.e2c.service.json;

import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.MessageAttachment;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("messageAttachment")
public class JsonMessageAttachment {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("attachmentName")
    private String attachmentName;
    @JsonProperty("attachmentLink")
    private String attachmentLink;
    @JsonProperty("messageId")
    private Long messageId;

    public JsonMessageAttachment(MessageAttachment messageAttachment, DocumentUrlFactory documentUrlFactory,
            Locale locale) {
        this.id = messageAttachment.getId();
        this.attachmentName = messageAttachment.getAttachmentName();
        this.attachmentLink = documentUrlFactory.createDocumentUrl(messageAttachment.getAttachmentLink(), locale);
        this.messageId = messageAttachment.getMessageId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentLink() {
        return attachmentLink;
    }

    public void setAttachmentLink(String attachmentLink) {
        this.attachmentLink = attachmentLink;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

}
