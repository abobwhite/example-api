package com.daugherty.e2c.service.json;

import com.daugherty.e2c.domain.MembershipProductSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of Summary of Membership Products.
 */
@JsonRootName("productSummary")
public class JsonMembershipProductSummary {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("published")
    private Integer published;
    @JsonProperty("unpublished")
    private Integer unpublished;
    @JsonProperty("pendingApproval")
    private Integer pendingApproval;
    @JsonProperty("pendingTranslation")
    private Integer pendingTranslation;
    @JsonProperty("waitingForInformation")
    private Integer waitingForInformation;
    @JsonProperty("approved")
    private Integer approved;
    @JsonProperty("disapproved")
    private Integer disapproved;
    @JsonProperty("hotProducts")
    private Integer hotProducts;
    @JsonProperty("productsRemaining")
    private Integer productsRemaining;
    @JsonProperty("draft")
    private Integer draft;
    @JsonProperty("publishRemaining")
    private Integer publishRemaining;
    @JsonProperty("hotProductsRemaining")
    private Integer hotProductsRemaining;

    public JsonMembershipProductSummary(MembershipProductSummary productSummary) {
        id = productSummary.getId();
        published = productSummary.getPublished();
        unpublished = productSummary.getUnpublished();
        pendingApproval = productSummary.getPendingApproval();
        pendingTranslation = productSummary.getPendingTranslation();
        waitingForInformation = productSummary.getWaitingForInformation();
        approved = productSummary.getApproved();
        disapproved = productSummary.getDisapproved();
        hotProducts = productSummary.getHotProducts();
        productsRemaining = productSummary.getProductsRemaining();
        draft = productSummary.getDraft();
        publishRemaining = productSummary.getPublishRemaining();
        hotProductsRemaining = productSummary.getHotProductsRemaining();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public final Integer getUnpublished() {
        return unpublished;
    }

    public final void setUnpublished(Integer unpublished) {
        this.unpublished = unpublished;
    }

    public final Integer getApproved() {
        return approved;
    }

    public final void setApproved(Integer approved) {
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

}
