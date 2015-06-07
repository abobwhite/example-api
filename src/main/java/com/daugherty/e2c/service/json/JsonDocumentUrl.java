package com.daugherty.e2c.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "documentUrl")
public class JsonDocumentUrl {
    @JsonProperty("url")
    private String url;

    public JsonDocumentUrl() {
    }

    public JsonDocumentUrl(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
