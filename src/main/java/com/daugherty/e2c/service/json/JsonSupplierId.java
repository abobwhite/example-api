package com.daugherty.e2c.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "supplierId")
public class JsonSupplierId {
    @JsonProperty("stringValue")
    private String stringValue;

    public JsonSupplierId() {
    }

    public JsonSupplierId(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
