package com.daugherty.e2c.service.json;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "membershipDiscount")
public class JsonMembershipDiscount {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("membershipId")
    private Long membershipId;
    @JsonProperty("discount")
    private JsonDiscount discount;
    
    public JsonMembershipDiscount() {
    }
    
    public JsonMembershipDiscount(Discount discount, Long membershipId) {
        this.discount = new JsonDiscount(discount);
        this.membershipId = membershipId;
    }
    
    public JsonMembershipDiscount(Long id, Long discountId, String code, String description, String type, BigDecimal discountAmount,
            BigDecimal amount, Boolean ongoing, Boolean special, Date effectiveDate,
            Date expirationDate, List<Integer> membershipLevels, List<SubscriptionType> subscriptionTypes, Long membershipId) {
        this.id = id;
        this.discount = new JsonDiscount(discountId, code, description, type, discountAmount, amount, ongoing, special, effectiveDate, expirationDate, membershipLevels, subscriptionTypes);
        this.membershipId = membershipId;
    }
    
    public void setId(Long id){
        this.id = id;
    }
    
    public Long getId(){
        return id;
    }

    public Long getMembershipId() {
        return membershipId;
    }
    
    public JsonDiscount getDiscount() {
        return discount;
    }
}
