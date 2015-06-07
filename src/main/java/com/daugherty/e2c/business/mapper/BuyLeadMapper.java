package com.daugherty.e2c.business.mapper;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.service.json.JsonBuyLead;

@Component
public class BuyLeadMapper {
    @Inject
    private Hashids hashids;
    
    public BuyLead toNewDomainObject(JsonBuyLead json) {
        Long partyId = hashids.decode(json.getRequester().getId())[0];
        return new BuyLead(json.getId(), new Party(partyId), new ProductCategory(json.getCategory()), json.getEffectiveDate(), json.getExpirationDate());
    }
}
