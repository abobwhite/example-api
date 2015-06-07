package com.daugherty.e2c.service.json;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ProductMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a Message domain object to/from JSON.
 */
@JsonRootName("productMessage")
public class JsonProductMessage extends JsonMessage {

    @JsonProperty(ProductMessage.PRODUCT_IDS_SERIAL_PROPERTY)
    private List<Long> productIds;

    public JsonProductMessage() {
    }

    public JsonProductMessage(ProductMessage message, DocumentUrlFactory documentUrlFactory, Locale locale) {
    	super(message, documentUrlFactory, locale);
        setLinks(message.getProductIds());
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    private final void setLinks(List<Long> productIds) {
        if (productIds != null) {
            getLinks().put("products", "products?productIds=" + stringifyWithoutBracketsAndSpaces(productIds));
        }
    }

    private String stringifyWithoutBracketsAndSpaces(List<Long> ids) {
        String stringifiedIds = ids.toString();
        return stringifiedIds.substring(1, stringifiedIds.length() - 1).replaceAll(" ", "");
    }
}
