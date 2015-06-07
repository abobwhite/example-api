package com.daugherty.e2c.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class BuyLeadMessage extends Message {
    public static final String BUY_LEAD_SERIAL_PROPERTY = "lead";

    private final BuyLead buyLead;

    // Constructor for new Buy Lead Message
    public BuyLeadMessage(String subject, Party originator, BuyLead buyLead, Interaction interaction) {
        super(subject, originator, interaction, MessageType.BUYLEAD);
        this.buyLead = buyLead;
    }

    // Constructor for existing Buy Lead Message
    public BuyLeadMessage(Long id, String publicId, String subject, Party otherParty, boolean flagged, Date lastInteractionSentTime,
            boolean completelyRead, BuyLead buyLead) {

        super(id, publicId, subject, otherParty, flagged, lastInteractionSentTime, completelyRead, MessageType.BUYLEAD);
        this.buyLead = buyLead;
    }

    @Override
    public ValidationError validate() {
        ValidationError errors = super.validate();

        validateBuyLead(errors);

        return errors;
    }

    public BuyLead getBuyLead() {
        return buyLead;
    }

    private void validateBuyLead(ValidationError errors) {
        if (buyLead == null) {
            errors.add(BUY_LEAD_SERIAL_PROPERTY, MESSAGE_BUY_LEAD_REQUIRED);
        }
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        super.addEntityFieldsToEqualsBuilder(builder, obj);
        BuyLeadMessage rhs = (BuyLeadMessage) obj;
        builder.append(buyLead, rhs.buyLead);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        super.addEntityFieldsToHashCodeBuilder(builder);
        builder.append(buyLead);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        super.addEntityFieldsToToStringBuilder(builder);
        builder.append("buyLead", buyLead);
    }
}
