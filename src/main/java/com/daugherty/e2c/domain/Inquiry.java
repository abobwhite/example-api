package com.daugherty.e2c.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * A pending Message (actually set of Messages) from a Buyer, built up from progressive addition of Products (to an
 * "Inquiry Basket").
 */
public class Inquiry extends Entity implements Validatable {

    private static final long serialVersionUID = 1L;

    public static final String PRODUCT_IDS_SERIAL_PROPERTY = "productIds";
    public static final String ORIGINATOR_SERIAL_PROPERTY = "originator";
    public static final String SUBJECT_SERIAL_PROPERTY = "subject";
    public static final String BODY_SERIAL_PROPERTY = "body";
    public static final String SUBMISSION_TIME_SERIAL_PROPERTY = "submissionTime";

    private List<Long> productIds = Lists.newArrayList();
    private Party originator;
    private String subject;
    private String body;
    private List<MessageTag> messageTags = Lists.newArrayList();
    private Date submissionTime;

    // Constructor for unsubmitted Inquiries (Inquiry Basket)
    public Inquiry(Long partyId, List<Long> productIds) {
        super(partyId);
        this.productIds = productIds;
    }

    // Constructor for submitted Inquiries
    public Inquiry(Long id, Party originator, List<Long> productIds, String subject, String body,
            List<MessageTag> messageTags) {
        super(id);
        this.originator = originator;
        this.productIds = productIds;
        this.subject = subject;
        this.body = body;
        this.messageTags = messageTags;
    }

    // Constructor for pending Inquiries
    public Inquiry(Long id, Party originator, String subject, String body, Date submissionTime) {
        super(id);
        this.originator = originator;
        this.subject = subject;
        this.body = body;
        this.submissionTime = submissionTime;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void addProductIds(Collection<Long> productIds) {
        this.productIds.addAll(productIds);
    }

    public Party getOriginator() {
        return originator;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public List<MessageTag> getMessageTags() {
        return messageTags;
    }

    public void addMessageTags(Collection<MessageTag> messageTags) {
        this.messageTags.addAll(messageTags);
    }

    public Date getSubmissionTime() {
        return submissionTime;
    }

    public ProductMessage createMessage(Party receiver, Collection<Long> receiverProductIds) {
    	ProductMessage message = new ProductMessage(subject, originator, null, new Interaction(new Message(null), body, originator, receiver));
        message.addProductIds(receiverProductIds);
        message.addMessageTags(messageTags);
        return message;
    }

    @Override
    public ValidationError validate() {
        ValidationError errors = new ValidationError();
        validateProductIds(errors);
        validateOriginator(errors);
        validateSubject(errors);
        validateBody(errors);
        return errors;
    }

    private void validateProductIds(ValidationError errors) {
        if (productIds == null || productIds.isEmpty()) {
            errors.add(PRODUCT_IDS_SERIAL_PROPERTY, MESSAGE_PRODUCT_IDS_REQUIRED);
        }
    }

    private void validateOriginator(ValidationError errors) {
        if (originator == null) {
            errors.add(ORIGINATOR_SERIAL_PROPERTY, MESSAGE_ORIGINATOR_REQUIRED);
        }
    }

    private void validateSubject(ValidationError errors) {
        if (StringUtils.isBlank(subject)) {
            errors.add(SUBJECT_SERIAL_PROPERTY, MESSAGE_SUBJECT_REQUIRED);
        }
        if (StringUtils.isNotBlank(subject) && subject.length() > 150) {
            errors.add(SUBJECT_SERIAL_PROPERTY, MESSAGE_SUBJECT_LENGTH);
        }
    }

    private void validateBody(ValidationError errors) {
        if (StringUtils.isBlank(body)) {
            errors.add(BODY_SERIAL_PROPERTY, INTERACTION_BODY_REQUIRED);
        }
        if (StringUtils.isNotBlank(body) && body.length() > 4000) {
            errors.add(BODY_SERIAL_PROPERTY, INTERACTION_BODY_LENGTH);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
    }

    @Override
    protected int hashCodeMultiplier() {
        return 65;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("productIds", productIds).append("subject", subject).append("body", body)
                .append("messageTags", messageTags).append("submissionTime", submissionTime);
    }

}
