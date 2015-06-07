package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls an Inquiry domain object to/from JSON.
 */
@JsonRootName("inquiry")
public class JsonInquiry {

    @JsonProperty("id")
    private Long id;
    @JsonProperty(Inquiry.ORIGINATOR_SERIAL_PROPERTY)
    private JsonParty originator;
    @JsonProperty(Inquiry.SUBJECT_SERIAL_PROPERTY)
    private String subject;
    @JsonProperty(Inquiry.BODY_SERIAL_PROPERTY)
    private String body;
    @JsonProperty(Inquiry.PRODUCT_IDS_SERIAL_PROPERTY)
    private List<Long> productIds;
    @JsonProperty("messageTags")
    private List<String> messageTags;
    @JsonProperty(Inquiry.SUBMISSION_TIME_SERIAL_PROPERTY)
    private Date submissionTime;

    public JsonInquiry() {
    }

    public JsonInquiry(Inquiry inquiry, DocumentUrlFactory documentUrlFactory, Locale locale) {
        setId(inquiry.getId());
        setProductIds(inquiry.getProductIds());
        if (inquiry.getOriginator() != null) {
            setOriginator(new JsonParty(inquiry.getOriginator(), documentUrlFactory, locale));
        }
        setSubject(inquiry.getSubject());
        setBody(inquiry.getBody());
        setMessageTags(serializeMessageTags(inquiry.getMessageTags()));
        setSubmissionTime(inquiry.getSubmissionTime());
    }

    private List<String> serializeMessageTags(List<MessageTag> messageTags) {
        List<String> jsonMessageTags = new ArrayList<String>();
        for (MessageTag messageTag : messageTags) {
            jsonMessageTags.add(messageTag.getName());
        }
        return jsonMessageTags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JsonParty getOriginator() {
        return originator;
    }

    public void setOriginator(JsonParty originator) {
        this.originator = originator;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<String> getMessageTags() {
        return messageTags;
    }

    public void setMessageTags(List<String> messageTags) {
        this.messageTags = messageTags;
    }

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Date submissionTime) {
        this.submissionTime = submissionTime;
    }
}
