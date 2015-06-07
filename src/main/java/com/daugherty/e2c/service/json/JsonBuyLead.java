package com.daugherty.e2c.service.json;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.BuyLead;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a Buy Lead domain object to/from JSON.
 */
@JsonRootName(value = "buyLead")
public class JsonBuyLead {

    @JsonProperty("id")
    private Long id;
    @JsonProperty(BuyLead.PARTY_SERIAL_PROPERTY)
    private JsonParty requester;
    @JsonProperty(BuyLead.PRODUCT_CATEGORY_SERIAL_PROPERTY)
    private Long category;
    @JsonProperty(BuyLead.EFFECTIVE_DATE_SERIAL_PROPERTY)
    private Date effectiveDate;
    @JsonProperty(BuyLead.EXPIRATION_DATE_SERIAL_PROPERTY)
    private Date expirationDate;
    @JsonProperty("respondingSuppliers")
    private List<Long> respondingSuppliers;

    public JsonBuyLead() {

    }

    public JsonBuyLead(BuyLead buyLead, DocumentUrlFactory documentUrlFactory, Locale locale) {
        this.id = buyLead.getId();
        this.requester = new JsonParty(buyLead.getRequester(), documentUrlFactory, locale);
        this.category = buyLead.getProductCategory().getId();
        this.effectiveDate = buyLead.getEffectiveDate();
        this.expirationDate = buyLead.getExpirationDate();
        this.respondingSuppliers = buyLead.getRespondingSuppliers();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JsonParty getRequester() {
        return requester;
    }

    public void setRequester(JsonParty requester) {
        this.requester = requester;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Long> getRespondingSuppliers() {
        return respondingSuppliers;
    }

    public void setRespondingSuppliers(List<Long> respondingSuppliers) {
        this.respondingSuppliers = respondingSuppliers;
    }
}
