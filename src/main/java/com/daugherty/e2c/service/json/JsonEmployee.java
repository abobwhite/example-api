package com.daugherty.e2c.service.json;

import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("employee")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonEmployee extends JsonParty {

    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("locked")
    private Boolean locked;
    @JsonProperty("blocked")
    private Boolean blocked;
    @JsonProperty("roles")
    private List<String> roles;

    public JsonEmployee() {
        super();
    }

    public JsonEmployee(Employee employee, DocumentUrlFactory documentUrlFactory, Locale locale) {
        super(employee, documentUrlFactory, locale);
        this.userId = employee.getUserId();
        this.locked = employee.getLocked();
        this.blocked = employee.getBlocked();
        this.roles = employee.getRoles();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getBlocked() {
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

}
