package com.daugherty.e2c.service.json;

import com.daugherty.e2c.domain.MessageSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a MessageSummary domain object to/from JSON.
 */
@JsonRootName("messageSummary")
public class JsonMessageSummary {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("unread")
    private Integer unread;
    @JsonProperty("sentMessageCount")
    private Integer sentMessageCount;

    public JsonMessageSummary(MessageSummary summary) {
        setId(summary.getId());
        setUnread(summary.getUnread());
        setSentMessageCount(summary.getSentMessageCount());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUnread() {
        return unread;
    }

    public void setUnread(Integer unread) {
        this.unread = unread;
    }

    public Integer getSentMessageCount() {
        return sentMessageCount;
    }

    public void setSentMessageCount(Integer sentMessageCount) {
        this.sentMessageCount = sentMessageCount;
    }

}
