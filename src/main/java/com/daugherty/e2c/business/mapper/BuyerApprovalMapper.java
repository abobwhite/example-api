package com.daugherty.e2c.business.mapper;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.BuyerApproval;
import com.daugherty.e2c.service.json.JsonBuyerApproval;

@Component
public class BuyerApprovalMapper {
    public JsonBuyerApproval fromExistinDomainObject(BuyerApproval approval ) {
        JsonBuyerApproval json = new JsonBuyerApproval();
        json.setId(approval.getPublicId());
        json.setApprovalStatus(approval.getApprovalStatus().getName());
        json.setTitle(approval.getTitle());
        json.setEmail(approval.getEmail());
        json.setVersion(approval.getVersion());
        json.setLastUpdatedAt(approval.getLastUpdatedAt());
        json.setLastUpdatedBy(approval.getLastUpdatedBy());
        
        return json;
    }
}
