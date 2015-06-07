package com.daugherty.e2c.service.json;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonUser {

    @JsonProperty("id")
    private Long id;
    @JsonProperty(User.USERNAME_SERIAL_PROPERTY)
    private String username;
    @JsonProperty(User.ROLE_SERIAL_PROPERTY)
    private List<String> roles;
    @JsonProperty(User.LOCKED_SERIAL_PROPERTY)
    private Boolean locked;
    @JsonProperty(User.BLOCKED_SERIAL_PROPERTY)
    private Boolean blocked;
    @JsonProperty(User.LAST_UPDATED_SERIAL_PROPERTY)
    private Date lastUpdated;
    @JsonProperty("party")
    private JsonParty party;

    public JsonUser() {

    }

    public JsonUser(User user, DocumentUrlFactory documentUrlFactory, Locale locale) {
        id = user.getId();
        username = user.getUsername();
        roles = user.getRoles();
        locked = user.isLocked();
        blocked = user.isBlocked();
        party = new JsonParty(user.getParty(), documentUrlFactory, locale);
        lastUpdated = user.getLastUpdated();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public JsonParty getParty() {
        return party;
    }

    public void setParty(JsonParty party) {
        this.party = party;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
