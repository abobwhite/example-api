package com.daugherty.e2c.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Unmarshalls the JSON representation of a unique identifier.
 */
@JsonRootName(value = "id")
public class JsonId {

    @JsonProperty("longValue")
    private Long longValue;

    public JsonId() {
    }

    public JsonId(Long longValue) {
        this.longValue = longValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

}
