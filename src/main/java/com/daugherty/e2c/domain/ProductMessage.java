package com.daugherty.e2c.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class ProductMessage extends Message {
    public static final String PRODUCT_IDS_SERIAL_PROPERTY = "products";
	
    private final List<Long> productIds = Lists.newArrayList();
    private final List<MessageTag> messageTags = Lists.newArrayList();
    
    // Constructor for new single-Product Message
    public ProductMessage(String subject, Party originator, Long productId, Interaction interaction) {
    	super(subject, originator, interaction, MessageType.PRODUCT);
        if (productId != null) {
            productIds.add(productId);
        }
    }
    
    // Constructor for existing Product Message
    public ProductMessage(Long id, String publicId, String subject, Party otherParty, boolean flagged, Date lastInteractionSentTime,
            boolean completelyRead) {
    	
    	super(id, publicId, subject, otherParty, flagged, lastInteractionSentTime, completelyRead, MessageType.PRODUCT);
    }
    
    public List<Long> getProductIds() {
        return productIds;
    }

    public void addProductIds(Collection<Long> productIds) {
        this.productIds.addAll(productIds);
    }

    public List<MessageTag> getMessageTags() {
        return messageTags;
    }

    public void addMessageTags(Collection<MessageTag> messageTags) {
        this.messageTags.addAll(messageTags);
    }
    
	@Override
    public ValidationError validate() {
        ValidationError errors = super.validate();
        
        validateProductIds(errors);
        
        return errors;
    }
	
    private void validateProductIds(ValidationError errors) {
        if (productIds == null || productIds.isEmpty()) {
            errors.add(PRODUCT_IDS_SERIAL_PROPERTY, MESSAGE_PRODUCT_IDS_REQUIRED);
        }
    }
    
    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        super.addEntityFieldsToEqualsBuilder(builder, obj);
        ProductMessage rhs = (ProductMessage) obj;
        builder.append(productIds, rhs.productIds).append(messageTags, rhs.messageTags);
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        super.addEntityFieldsToHashCodeBuilder(builder);
        builder.append(productIds).append(messageTags);
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        super.addEntityFieldsToToStringBuilder(builder);
        builder.append("productIds", productIds).append("messageTags", messageTags);
    }

    
    
}
