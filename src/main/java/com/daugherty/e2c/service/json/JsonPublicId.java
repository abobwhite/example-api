package com.daugherty.e2c.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Unmarshalls the JSON representation of a unique identifier.
 */
@JsonRootName(value = "id")
public class JsonPublicId {

    @JsonProperty("stringValue")
    private String stringValue;

    public JsonPublicId() {
    }

    public JsonPublicId(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

}
