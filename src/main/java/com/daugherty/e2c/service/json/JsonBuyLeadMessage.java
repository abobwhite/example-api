package com.daugherty.e2c.service.json;

import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.BuyLeadMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a Message domain object to/from JSON.
 */
@JsonRootName("buyLeadMessage")
public class JsonBuyLeadMessage extends JsonMessage {

    @JsonProperty(BuyLeadMessage.BUY_LEAD_SERIAL_PROPERTY)
    private JsonBuyLead lead;

    public JsonBuyLeadMessage() {
    }

    public JsonBuyLeadMessage(BuyLeadMessage message, DocumentUrlFactory documentUrlFactory, Locale locale) {
        super(message, documentUrlFactory, locale);
        this.lead = new JsonBuyLead(message.getBuyLead(), documentUrlFactory, locale);
    }

    public JsonBuyLead getLead() {
        return lead;
    }

    public void setLead(JsonBuyLead buyLead) {
        this.lead = buyLead;
    }
}
