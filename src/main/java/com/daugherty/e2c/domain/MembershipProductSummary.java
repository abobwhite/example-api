package com.daugherty.e2c.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a Membership Product Summary.
 */
public class MembershipProductSummary extends Entity {

    private static final long serialVersionUID = 1L;

    private Integer total = 0;
    private Integer published = 0;
    private Integer pendingApproval = 0;
    private Integer pendingTranslation = 0;
    private Integer waitingForInformation = 0;
    private Integer unpublished = 0;
    private Integer hotProducts = 0;
    private Integer productsRemaining = 0;
    private Integer approved = 0;
    private Integer disapproved = 0;
    private Integer draft = 0;
    private Integer publishRemaining = 0;
    private Integer hotProductsRemaining = 0;

    public MembershipProductSummary(Long id) {
        super(id);
    }

    public MembershipProductSummary(Long id, Integer total, Integer published, Integer pendingApproval,
            Integer pendingTranslation, Integer waitingForInformation, Integer unpublished, Integer hotProducts,
            Integer productsRemaining, Integer approved, Integer disapproved, Integer draft) {
        super(id);
        this.total = total;
        this.published = published;
        this.pendingApproval = pendingApproval;
        this.pendingTranslation = pendingTranslation;
        this.waitingForInformation = waitingForInformation;
        this.unpublished = unpublished;
        this.hotProducts = hotProducts;
        this.productsRemaining = productsRemaining;
        this.approved = approved;
        this.disapproved = disapproved;
        this.draft = draft;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPublished() {
        return published;
    }

    public void setPublished(Integer online) {
        published = online;
    }

    public Integer getPendingApproval() {
        return pendingApproval;
    }

    public void setPendingApproval(Integer pendingApproval) {
        this.pendingApproval = pendingApproval;
    }

    public Integer getPendingTranslation() {
        return pendingTranslation;
    }

    public void setPendingTranslation(Integer pendingTranslation) {
        this.pendingTranslation = pendingTranslation;
    }

    public Integer getWaitingForInformation() {
        return waitingForInformation;
    }

    public void setWaitingForInformation(Integer waitingForInformation) {
        this.waitingForInformation = waitingForInformation;
    }

    public Integer getUnpublished() {
        return unpublished;
    }

    public void setUnpublished(Integer offline) {
        unpublished = offline;
    }

    public Integer getHotProducts() {
        return hotProducts;
    }

    public void setHotProducts(Integer hotProducts) {
        this.hotProducts = hotProducts;
    }

    public Integer getProductsRemaining() {
        return productsRemaining;
    }

    public void setProductsRemaining(Integer productsRemaining) {
        this.productsRemaining = productsRemaining;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getDisapproved() {
        return disapproved;
    }

    public void setDisapproved(Integer disapproved) {
        this.disapproved = disapproved;
    }

    public final Integer getDraft() {
        return draft;
    }

    public final void setDraft(Integer draft) {
        this.draft = draft;
    }

    public final Integer getPublishRemaining() {
        return publishRemaining;
    }

    public final void setPublishRemaining(Integer publishRemaining) {
        this.publishRemaining = publishRemaining;
    }

    public final Integer getHotProductsRemaining() {
        return hotProductsRemaining;
    }

    public final void setHotProductsRemaining(Integer hotProductsRemaining) {
        this.hotProductsRemaining = hotProductsRemaining;
    }

    @Override
    protected void addEntityFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
    }

    @Override
    protected int hashCodeMultiplier() {
        return 23;
    }

    @Override
    protected void addEntityFieldsToHashCodeBuilder(HashCodeBuilder builder) {
    }

    @Override
    protected void addEntityFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("total", total).append("published", published).append("pendingApproval", pendingApproval)
                .append("pendingTranslation", pendingTranslation)
                .append("waitingForInformation", waitingForInformation).append("unpublished", unpublished)
                .append("hotProducts", hotProducts).append("productsRemaining", productsRemaining)
                .append("approved", approved).append("disapproved", disapproved).append("draft", draft)
                .append("publishRemaining", publishRemaining).append("hotProductsRemaining", hotProductsRemaining);

    }

}
